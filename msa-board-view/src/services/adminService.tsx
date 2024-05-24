import { IFetchResponse, fetchData, fetchDataInAuth } from "../utils/fetchUtils";
import { GENDER, GROUP, MEMBER_SORT, ORDER, POST_SORT, STATE } from '../constants/constants';
import { getRefreshToken } from "../utils/tokenUtils";

const endpointURL = "http://localhost:8000";

export interface IAdminLoginParam {
    username: string,
    password: string
}

export interface IAdminModifyParam {
    name: string,
    gender: keyof typeof GENDER,
    email: string
}

export interface IAdminModifyPasswordParam {
    currentPassword: string,
    newPassword: string
}

export interface IAdminModifyUserParam {
    group: keyof typeof GROUP,
    state: keyof typeof STATE
}

export interface IAdminSearchMemberParam {
    username: string,
    name: string,
    gender: keyof typeof GENDER,
    email: string,
    createdDateTimeFrom: string,
    createdDateTimeTo: string,
    updatedDateTimeFrom: string,
    updatedDateTimeTo: string,
    group: keyof typeof GROUP,
    state: keyof typeof STATE,
    offset: number,
    limit: number,
    sort: keyof typeof MEMBER_SORT,
    order: keyof typeof ORDER
}

export interface IAdminWritePostParam {
    title: string,
    content: string
}

export interface IAdminModifyPostParam {
    title: string,
    content: string,
    state: keyof typeof STATE
}

export interface IAdminSearchPostParam {
    title: string,
    content: string,
    writerUsername: string,
    createdDateTimeFrom: string,
    createdDateTimeTo: string,
    updatedDateTimeFrom: string,
    updatedDateTimeTo: string,
    state: keyof typeof STATE,
    offset: number,
    limit: number,
    sort: keyof typeof POST_SORT,
    order: keyof typeof ORDER
}

export const adminService = {

    loginAdmin: (param: IAdminLoginParam): Promise<IFetchResponse> => {

        return fetchData({
            url: `${endpointURL}/apis/admins/login`,
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(param)
        });
    },
    logoutAdmin: (): Promise<IFetchResponse> => {

        return fetchDataInAuth({
            url: `${endpointURL}/apis/admins/logout`,
            method: "POST"
        });
    },
    refreshAdmin: (): Promise<IFetchResponse> => {

        const refreshToken = getRefreshToken();

        if (!refreshToken) {
            return Promise.reject(new Error("Refresh Token이 존재하지 않습니다."));
        } else {
            return fetchData({
                url: `${endpointURL}/apis/admins/refresh`,
                method: "POST",
                headers: { "Refresh-Token": refreshToken },
            });    
        }
    },
    getAdmin: (): Promise<IFetchResponse> => {

        return fetchDataInAuth({
            url: `${endpointURL}/apis/admins`,
            method: "GET",
            headers: { "Accept": "application/json" },
        });
    },
    modifyAdmin: (param: IAdminModifyParam): Promise<IFetchResponse> => {

        return fetchDataInAuth({
            url: `${endpointURL}/apis/admins`,
            method: "PATCH",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(param)
        });
    },
    modifyAdminPassword: (param: IAdminModifyPasswordParam): Promise<IFetchResponse> => {

        return fetchDataInAuth({
            url: `${endpointURL}/apis/admins/password`,
            method: "PATCH",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(param)
        });
    },
    modifyUser: (userId: string, param: IAdminModifyUserParam): Promise<IFetchResponse> => {

        return fetchDataInAuth({
            url: `${endpointURL}/apis/admins/users/${userId}`,
            method: "PATCH",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(param)
        });
    },
    getMember: (memberId: string): Promise<IFetchResponse> => {

        return fetchDataInAuth({
            url: `${endpointURL}/apis/admins/members/${memberId}`,
            method: "GET",
            headers: { "Accept": "application/json" }
        });
    },
    searchMemberList: (param: IAdminSearchMemberParam): Promise<IFetchResponse> => {

        const urlParam = new URLSearchParams();

        Object.entries(param).forEach(([key, value]) => urlParam.append(key, value))
        
        return fetchDataInAuth({
            url: `${endpointURL}/apis/admins/members`,
            method: "GET",
            headers: { "Accept": "application/json" },
            query: urlParam.toString()
        });
    },
    writePost: (param: IAdminWritePostParam): Promise<IFetchResponse> => {

        return fetchDataInAuth({
            url: `${endpointURL}/apis/admins/posts`,
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(param)
        });
    },
    modifyPost: (postId: string, param: IAdminWritePostParam): Promise<IFetchResponse> => {

        return fetchDataInAuth({
            url: `${endpointURL}/apis/admins/posts${postId}`,
            method: "PATCH",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(param)
        });
    },
    getPost: (postId: string): Promise<IFetchResponse> => {

        return fetchDataInAuth({
            url: `${endpointURL}/apis/admins/posts/${postId}`,
            method: "GET",
            headers: { "Accept": "application/json" }
        });
    },
    searchPostList: (param: IAdminSearchPostParam): Promise<IFetchResponse> => {

        const urlParam = new URLSearchParams();

        Object.entries(param).forEach(([key, value]) => urlParam.append(key, value))
        
        return fetchDataInAuth({
            url: `${endpointURL}/apis/admins/posts`,
            method: "GET",
            headers: { "Accept": "application/json" },
            query: urlParam.toString()
        });
    }
};
