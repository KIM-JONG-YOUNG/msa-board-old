import { ERROR_CODE } from "../constants/constants"
import * as sessionUtils from "./sessionUtils"

export interface IFetchOption {
    readonly url: string
    readonly method: "GET" | "POST" | "PUT" | "PATCH" | "DELETE"
    readonly headers?: HeadersInit
    readonly param?: URLSearchParams
    readonly body?: BodyInit
}

export interface IFetchErrorDetails {
    readonly field?: string
    readonly message: string
}

export interface IFetchErrorResponse {
    readonly errorCode: typeof ERROR_CODE[keyof typeof ERROR_CODE]
    readonly errorMessage: string
    readonly errorDetailsList?: IFetchErrorDetails[]
}

export async function fetchData(fetchOption: IFetchOption): Promise<Response> {

    const url = (!!fetchOption.param)
        ? `${fetchOption.url}?${fetchOption.param.toString()}`
        : fetchOption.url

    return fetch(url, {
        method: fetchOption.method,
        headers: fetchOption.headers,
        body: fetchOption.body
    }).then(async (response: Response) => {

        if (response.ok) {
            return response;
        } else {
            throw await response.json();
        }
    });
}

export async function fetchDataInAuth(fetchOption: IFetchOption, refreshURL: string): Promise<Response> {

    return fetchData({
        ...fetchOption,
        headers: {
            ...fetchOption.headers,
            "Access-Token": sessionUtils.getAccessToken()
        }
    }).catch((errorResponse: IFetchErrorResponse) => {

        if (errorResponse.errorCode === ERROR_CODE.EXPIRED_ACCESS_TOKEN) {

            return fetchData({
                url: refreshURL,
                method: "POST",
                headers: { "Refresh-Token": sessionUtils.getRefreshToken() }
            }).then(response => {

                const accessToken = response.headers.get("Access-Token");
                const refreshToken = response.headers.get("Refresh-Token");

                (!!accessToken) && sessionUtils.setAccessToken(accessToken);
                (!!refreshToken) && sessionUtils.setRefreshToken(refreshToken);

            }).catch(() => {
                throw errorResponse;
            });
        } else {
            throw errorResponse;
        }
    }).then(() => {

        return fetchData({
            ...fetchOption,
            headers: {
                ...fetchOption.headers,
                "Access-Token": sessionUtils.getAccessToken()
            }
        });
    });
}
