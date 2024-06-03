import { Navigate } from "react-router-dom";
import * as sessionUtils from "../utils/sessionUtils";
import { GROUP } from "../constants/constants";

export type MemberRouterProps = {
    group: keyof typeof GROUP
    element: React.ReactElement
    redirectURL: string
}
export type AnonymousRouterProps = {
    element: React.ReactElement
    redirectURL: string
}

export function MemberRouter({
    group,
    element,
    redirectURL
}: MemberRouterProps) {

    const currentAccessToken = sessionUtils.getAccessToken();
    const currentGroup = sessionUtils.getGroup();

    return (!!currentAccessToken && !!currentGroup && group === currentGroup) ? element : <Navigate to={redirectURL} />
};

export function AnonymousRouter({
    element,
    redirectURL
}: AnonymousRouterProps) {

    const currentAccessToken = sessionUtils.getAccessToken();
    const currentGroup = sessionUtils.getGroup();

    return (!currentAccessToken && !currentGroup) ? element : <Navigate to={redirectURL} />
};
