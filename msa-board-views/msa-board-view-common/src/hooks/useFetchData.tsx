import { useContext } from 'react';
import { LoadingContext } from '../contexts/LoadingContext';
import { ERROR_CODE } from '../constants/constants';
import sessionUtils from '../utils/sessionUtils';

export type FetchOptions = {
    url: string
    method: "GET" | "POST" | "PUT" | "PATCH" | "DELETE"
    headers?: HeadersInit
    query?: URLSearchParams
    body?: BodyInit
    refreshURL?: string    
};

export type FetchErrorDetails = {
    field?: string
    message: string
};

export type FetchErrorResponse = {
    errorCode: typeof ERROR_CODE[keyof typeof ERROR_CODE]
    errorMessage: string
    errorDetailsList?: FetchErrorDetails[]
};

export default function useFetchData() {

    const loadingContext = useContext(LoadingContext);

    if (loadingContext === null) {
        throw new Error("useLoading Hook은 LoadingProvider 내부에서 사용해야 합니다.");
    } else {

        const [loading, setLoading] = loadingContext;

        const fetchDataHandler = async (response: Response) => {
        
            if (response.ok) {
                return response;
            } else {
                throw await response.json();
            }
        };

        const refreshFetchDataHandler = (response: Response) => {
        
            sessionUtils.setAccessToken(response.headers.get("Access-Token") || "");
            sessionUtils.setRefreshToken(response.headers.get("Refresh-Token") || "");        
        };

        const fetchData = async (fetchOptions: FetchOptions): Promise<Response> => {

            const refreshURL = fetchOptions.refreshURL;            
            const queryString = fetchOptions.query?.toString();
            const url = (!!queryString) ? `${fetchOptions.url}?${queryString}` : fetchOptions.url;
            const options = {
                method: fetchOptions.method,
                headers: new Headers(fetchOptions.headers),
                body: fetchOptions.body || undefined
            };

            (!!refreshURL) && options.headers.append("Access-Token", sessionUtils.getAccessToken() || "");

            return Promise.resolve()
                .then(() => setLoading(true))
                .then(() => fetch(url, options))
                .then(fetchDataHandler)
                .catch(async (errorResponse: FetchErrorResponse) => {

                    if (!!refreshURL && errorResponse.errorCode === ERROR_CODE.EXPIRED_ACCESS_TOKEN) {

                        const refreshToken = sessionUtils.getRefreshToken() || "";
                        const refreshOpions = {
                            method: "POST",
                            headers: { "Refresh-Token": refreshToken }
                        };

                        sessionUtils.removeAccessToken();
                        sessionUtils.removeRefreshToken();
                        sessionUtils.removeGroup();

                        options.headers.delete("Access-Token");
                        
                        return await fetch(refreshURL, refreshOpions)
                            .then(refreshFetchDataHandler)
                            .then(() => options.headers.append("Access-Token", sessionUtils.getAccessToken() || ""))
                            .then(() => fetch(url, options))
                            .then(fetchDataHandler);

                    } else {
                        throw errorResponse;
                    }
                })
                .finally(() => setLoading(false));
        };
        
        return {
            loading,
            fetchData
        };
    }
};