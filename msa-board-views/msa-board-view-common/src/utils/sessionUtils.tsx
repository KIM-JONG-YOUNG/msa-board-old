import { GROUP } from "../constants/constants";

function setAccessToken(accessToken: string): void {

    (!!accessToken) && sessionStorage.setItem("accessToken", accessToken);
}

function setRefreshToken(refreshToken: string): void {

    (!!refreshToken) && sessionStorage.setItem("refreshToken", refreshToken);
}

function setGroup(group: keyof typeof GROUP): void {

    (!!group) && sessionStorage.setItem("group", group);
}

function setQuery(queryKey: string, query: URLSearchParams): void {

    query?.forEach((value, key) => (!value) && query.delete(key));

    (!!query.toString()) && sessionStorage.setItem(queryKey, query.toString());
}

function getAccessToken(): string | null {

    return sessionStorage.getItem("accessToken")
}

function getRefreshToken(): string | null {

    return sessionStorage.getItem("refreshToken")
}

function getGroup(): keyof typeof GROUP | null {

    const group = sessionStorage.getItem("group");

    return (group !== null) ? group as keyof typeof GROUP : null;
}

function getQuery(queryKey: string): URLSearchParams | null {

    const query = sessionStorage.getItem(queryKey);

    return (query !== null) ? new URLSearchParams(query) : null;
}

function removeAccessToken(): void {

    sessionStorage.removeItem("accessToken");
}

function removeRefreshToken(): void {
    
    sessionStorage.removeItem("refreshToken");
}

function removeGroup(): void {

    sessionStorage.removeItem("group");
}

function removeQuery(queryKey: string): void {

    sessionStorage.removeItem(queryKey);
}

export default {
    setAccessToken,
    setRefreshToken,
    setGroup,
    setQuery,
    getAccessToken,
    getRefreshToken,
    getGroup,
    getQuery,
    removeAccessToken,
    removeRefreshToken,
    removeGroup,
    removeQuery
};