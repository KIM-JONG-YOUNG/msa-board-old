import { ReactElement } from "react";
import { Navigate } from "react-router-dom";
import { GROUP } from "../constants/constants";
import sessionUtils from "../utils/sessionUtils";

export type MemberRouterProps = {
    group: keyof typeof GROUP
    element: ReactElement
    redirectURL: string
}

export type AnonymousRouterProps = {
    element: ReactElement
    redirectURL: string
}

export function MemberRouter({
    group,
    element,
    redirectURL
}: MemberRouterProps) {

    const currentAccessToken = sessionUtils.getAccessToken();
    const currentGroup = sessionUtils.getGroup();
    const isExistsAuth = (!!currentAccessToken && currentGroup === group);

    if (!isExistsAuth) {
        sessionUtils.removeAccessToken();
        sessionUtils.removeRefreshToken();        
        sessionUtils.removeGroup();
    }

    return (isExistsAuth) ? element : <Navigate to={redirectURL} />
};

export function AnonymousRouter({
    element,
    redirectURL
}: AnonymousRouterProps) {

    const currentAccessToken = sessionUtils.getAccessToken();
    const currentGroup = sessionUtils.getGroup();

    return (!currentAccessToken && !currentGroup) ? element : <Navigate to={redirectURL} />
};
