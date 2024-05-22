import { fetchData, fetchDataInAuth } from "../utils/fetchUtils";
import { GROUP, GENDER, STATE } from './../constants/Constants';

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
}

export const adminService = {

    loginAdmin: (param: IAdminLoginParam) => {

        return fetchData({
            url: `${endpointURL}/apis/admins/login`,
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(param)
        });
    },
    logoutAdmin: () => {

        return fetchDataInAuth({
            url: `${endpointURL}/apis/admins/logout`,
            method: "POST"
        });
    },
    refreshAdmin: () => {

        const refreshToken = sessionStorage.getItem("Refresh-Token");

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
    getAdmin: () => {

        return fetchDataInAuth({
            url: `${endpointURL}/apis/admins`,
            method: "GET",
            headers: { "Accept": "application/json" },
        });
    },
    modifyAdmin: (param: IAdminModifyParam) => {

        return fetchData({
            url: `${endpointURL}/apis/admins`,
            method: "PATCH",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(param)
        });
    },
    modifyAdminPassword: (param: IAdminModifyPasswordParam) => {

        return fetchData({
            url: `${endpointURL}/apis/admins/password`,
            method: "PATCH",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(param)
        });
    },
    modifyUser: (userId: string, param: IAdminModifyUserParam) => {

        return fetchData({
            url: `${endpointURL}/apis/admins/users/${userId}`,
            method: "PATCH",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(param)
        });
    },
    getMember: (memberId: string) => {

        return fetchData({
            url: `${endpointURL}/apis/admins/members/${memberId}`,
            method: "GET",
            headers: { "Accept": "application/json" }
        });
    },
    searchMemberList: (param: IAdminSearchMemberParam) => {

        const urlParam = new URLSearchParams();

        Object.entries(param).forEach(([key, value]) => urlParam.append(key, value))
        
        return fetchData({
            url: `${endpointURL}/apis/admins/members`,
            method: "GET",
            headers: { "Accept": "application/json" },
            query: urlParam.toString()
        });
    },
    writePost: (param: IAdminWritePostParam) => {

        return fetchData({
            url: `${endpointURL}/apis/admins/posts`,
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(param)
        });
    },
    modifyPost: (postId: string, param: IAdminWritePostParam) => {

        return fetchData({
            url: `${endpointURL}/apis/admins/posts${postId}`,
            method: "PATCH",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(param)
        });
    },
    getPost: (postId: string) => {

        return fetchData({
            url: `${endpointURL}/apis/admins/posts/${postId}`,
            method: "GET",
            headers: { "Accept": "application/json" }
        });
    },
    searchPostList: (param: IAdminSearchPostParam) => {

        const urlParam = new URLSearchParams();

        Object.entries(param).forEach(([key, value]) => urlParam.append(key, value))
        
        return fetchData({
            url: `${endpointURL}/apis/admins/posts`,
            method: "GET",
            headers: { "Accept": "application/json" },
            query: urlParam.toString()
        });
    }
};
