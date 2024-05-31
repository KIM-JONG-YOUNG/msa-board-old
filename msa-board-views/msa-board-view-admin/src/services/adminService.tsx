import { GENDER, GROUP, MEMBER_SORT, ORDER, POST_SORT, STATE } from 'msa-board-view-common/src/constants/constants';
import { IFetchOption } from 'msa-board-view-common/src/utils/fetchUtils';
import * as fetchUtils from 'msa-board-view-common/src/utils/fetchUtils';
import * as sessionUtils from 'msa-board-view-common/src/utils/sessionUtils';

export interface IAdminLoginParam {
    readonly username: string
    readonly password: string
}

export interface IAdminModifyParam {
    readonly name: string
    readonly gender: keyof typeof GENDER
    readonly email: string
}

export interface IAdminModifyPasswordParam {
    readonly currentPassword: string
    readonly newPassword: string
}

export interface IAdminModifyUserParam {
    readonly group: keyof typeof GROUP
    readonly state: keyof typeof STATE
}

export interface IAdminSearchMemberParam {
    readonly username?: string
    readonly name?: string
    readonly gender?: keyof typeof GENDER
    readonly email?: string
    readonly createdDateTimeFrom?: string
    readonly createdDateTimeTo?: string
    readonly updatedDateTimeFrom?: string
    readonly updatedDateTimeTo?: string
    readonly group?: keyof typeof GROUP
    readonly state?: keyof typeof STATE
    readonly offset?: number
    readonly limit?: number
    readonly sort?: keyof typeof MEMBER_SORT
    readonly order?: keyof typeof ORDER
}

export interface IAdminWritePostParam {
    readonly title: string
    readonly content: string
}

export interface IAdminModifyPostParam {
    readonly title: string
    readonly content: string
    readonly state: keyof typeof STATE
}

export interface IAdminSearchPostParam {
    readonly title?: string
    readonly content?: string
    readonly writerUsername?: string
    readonly createdDateTimeFrom?: string
    readonly createdDateTimeTo?: string
    readonly updatedDateTimeFrom?: string
    readonly updatedDateTimeTo?: string
    readonly state?: keyof typeof STATE
    readonly offset?: number
    readonly limit?: number
    readonly sort?: keyof typeof POST_SORT
    readonly order?: keyof typeof ORDER
}

const endpointURL = "http://localhost:8000";

export function fetchDataInAdmin(fetchOption: IFetchOption): Promise<Response> {

    return fetchUtils.fetchDataInAuth(fetchOption, `${endpointURL}/apis/admins/refresh`);
}

export function loginAdmin(param: IAdminLoginParam): Promise<Response> {

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

export function refreshAdmin(): Promise<Response> {

    return fetchUtils.fetchData({
        url: `${endpointURL}/apis/admins/refresh`,
        method: "POST",
        headers: { "Refresh-Token": sessionUtils.getRefreshToken() }
    });
}

export function getAdmin(): Promise<Response> {

    return fetchDataInAdmin({
        url: `${endpointURL}/apis/admins`,
        method: "GET",
        headers: { "Accept": "application/json" }
    });
}

export function modifyAdmin(param: IAdminModifyParam): Promise<Response> {

    return fetchDataInAdmin({
        url: `${endpointURL}/apis/admins`,
        method: "PATCH",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(param)
    });
}

export function modifyAdminPassword(param: IAdminModifyPasswordParam): Promise<Response> {

    return fetchDataInAdmin({
        url: `${endpointURL}/apis/admins/password`,
        method: "PATCH",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(param)
    });
}

export function modifyUser(userId: string, param: IAdminModifyUserParam): Promise<Response> {

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

export function searchMemberList(param: IAdminSearchMemberParam): Promise<Response> {

    const urlParam = new URLSearchParams();

    Object.entries(param)
        .filter(([key, value]) => (!!value))
        .forEach(([key, value]) => urlParam.append(key, value))

    return fetchDataInAdmin({
        url: `${endpointURL}/apis/admins/members`,
        method: "GET",
        headers: { "Accept": "application/json" },
        param: urlParam
    });
}

export function writePost(param: IAdminWritePostParam): Promise<Response> {

    return fetchDataInAdmin({
        url: `${endpointURL}/apis/admins/posts`,
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(param)
    });
}

export function modifyPost(postId: string, param: IAdminWritePostParam): Promise<Response> {

    return fetchDataInAdmin({
        url: `${endpointURL}/apis/admins/posts${postId}`,
        method: "PATCH",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(param)
    });
}

export function getPost(postId: string): Promise<Response> {

    return fetchDataInAdmin({
        url: `${endpointURL}/apis/admins/posts/${postId}`,
        method: "GET",
        headers: { "Accept": "application/json" }
    });
}

export function searchPostList(param: IAdminSearchPostParam): Promise<Response> {

    const urlParam = new URLSearchParams();

    Object.entries(param)
        .filter(([key, value]) => (!!value))
        .forEach(([key, value]) => urlParam.append(key, value))

    return fetchDataInAdmin({
        url: `${endpointURL}/apis/admins/posts`,
        method: "GET",
        headers: { "Accept": "application/json" },
        param: urlParam
    });
}
