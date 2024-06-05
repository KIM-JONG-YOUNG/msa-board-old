import { GENDER, GROUP, MEMBER_SORT, ORDER, POST_SORT, STATE } from 'msa-board-view-common/src/constants/constants';

import * as fetchUtils from 'msa-board-view-common/src/utils/fetchUtils';

export type AdminLoginRequest = {
    readonly username: string
    readonly password: string
}

export type AdminModifyRequest = {
    readonly name: string
    readonly gender: keyof typeof GENDER
    readonly email: string
}

export type AdminModifyPasswordRequest = {
    readonly currentPassword: string
    readonly newPassword: string
}

export type AdminModifyUserRequest = {
    readonly group: keyof typeof GROUP
    readonly state: keyof typeof STATE
}

export type AdminSearchMemberRequest = {
    readonly username: string
    readonly name: string
    readonly gender: keyof typeof GENDER
    readonly email: string
    readonly createdDateFrom: string
    readonly createdDateTo: string
    readonly updatedDateFrom: string
    readonly updatedDateTo: string
    readonly group: keyof typeof GROUP
    readonly state: keyof typeof STATE
    readonly offset: number
    readonly limit: number
    readonly sort: keyof typeof MEMBER_SORT
    readonly order: keyof typeof ORDER
}

export type AdminWritePostRequest = {
    readonly title: string
    readonly content: string
}

export type AdminModifyPostRequest = {
    readonly title: string
    readonly content: string
    readonly state: keyof typeof STATE
}

export type AdminSearchPostRequest = {
    readonly title: string
    readonly content: string
    readonly writerUsername: string
    readonly createdDateFrom: string
    readonly createdDateTo: string
    readonly updatedDateFrom: string
    readonly updatedDateTo: string
    readonly state: keyof typeof STATE
    readonly offset: number
    readonly limit: number
    readonly sort: keyof typeof POST_SORT
    readonly order: keyof typeof ORDER
}

const endpointURL = "http://localhost:8000";

export function fetchDataInAdmin(fetchOption: fetchUtils.FetchOption): Promise<Response> {

    return fetchUtils.fetchDataInAuth(fetchOption, `${endpointURL}/apis/admins/refresh`);
}

export function loginAdmin(param: AdminLoginRequest): Promise<Response> {

    return fetchUtils.fetchData({
        url: `${endpointURL}/apis/admins/login`,
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(param)
    });
}

export function logoutAdmin(): Promise<Response> {

    return fetchDataInAdmin({
        url: `${endpointURL}/apis/admins/logout`,
        method: "POST"
    });
}

export function getAdmin(): Promise<Response> {

    return fetchDataInAdmin({
        url: `${endpointURL}/apis/admins`,
        method: "GET",
        headers: { "Accept": "application/json" }
    });
}

export function modifyAdmin(param: AdminModifyRequest): Promise<Response> {

    return fetchDataInAdmin({
        url: `${endpointURL}/apis/admins`,
        method: "PATCH",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(param)
    });
}

export function modifyAdminPassword(param: AdminModifyPasswordRequest): Promise<Response> {

    return fetchDataInAdmin({
        url: `${endpointURL}/apis/admins/password`,
        method: "PATCH",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(param)
    });
}

export function modifyUser(userId: string, param: AdminModifyUserRequest): Promise<Response> {

    return fetchDataInAdmin({
        url: `${endpointURL}/apis/admins/users/${userId}`,
        method: "PATCH",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(param)
    });
}

export function getMember(memberId: string): Promise<Response> {

    return fetchDataInAdmin({
        url: `${endpointURL}/apis/admins/members/${memberId}`,
        method: "GET",
        headers: { "Accept": "application/json" }
    });
}

export function searchMemberList(param: AdminSearchMemberRequest): Promise<Response> {

    const urlParam = new URLSearchParams();

    Object.entries(param)
        .filter(([, value]) => (!!value))
        .map(([key, value]) => ([key, String(value)]))
        .forEach(([key, value]) => urlParam.append(key, value))

    return fetchDataInAdmin({
        url: `${endpointURL}/apis/admins/members`,
        method: "GET",
        headers: { "Accept": "application/json" },
        param: urlParam
    });
}

export function writePost(param: AdminWritePostRequest): Promise<Response> {

    return fetchDataInAdmin({
        url: `${endpointURL}/apis/admins/posts`,
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(param)
    });
}

export function modifyPost(postId: string, param: AdminWritePostRequest): Promise<Response> {

    return fetchDataInAdmin({
        url: `${endpointURL}/apis/admins/posts/${postId}`,
        method: "PATCH",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(param)
    });
}

export function modifyPostState(postId: string, state: keyof typeof STATE): Promise<Response> {

    return fetchDataInAdmin({
        url: `${endpointURL}/apis/admins/posts/${postId}/state`,
        method: "PATCH",
        headers: { "Content-Type": "application/json" },
        body: `"${state}"`
    });
}


export function getPost(postId: string): Promise<Response> {

    return fetchDataInAdmin({
        url: `${endpointURL}/apis/admins/posts/${postId}`,
        method: "GET",
        headers: { "Accept": "application/json" }
    });
}

export function searchPostList(param: AdminSearchPostRequest): Promise<Response> {

    const urlParam = new URLSearchParams();

    Object.entries(param)
        .filter(([, value]) => (!!value))
        .map(([key, value]) => ([key, String(value)]))
        .forEach(([key, value]) => urlParam.append(key, value));

    return fetchDataInAdmin({
        url: `${endpointURL}/apis/admins/posts`,
        method: "GET",
        headers: { "Accept": "application/json" },
        param: urlParam
    });
}
