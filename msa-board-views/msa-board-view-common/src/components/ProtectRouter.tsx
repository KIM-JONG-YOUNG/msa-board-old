import { Navigate } from "react-router-dom";
import * as sessionUtils from "../utils/sessionUtils";
import { GROUP } from "../constants/constants";

export function MemberRouter(prop: {
    element: React.ReactElement
    group: keyof typeof GROUP
    redirectURL: string
}): React.ReactElement {

    const currentAccessToken = sessionUtils.getAccessToken();
    const currentGroup = sessionUtils.getGroup();

    return (!!currentAccessToken && !!currentGroup 
        && prop.group === currentGroup) ? prop.element : <Navigate to={ prop.redirectURL } />
};

export function AnonymousRouter(prop: {
    element: React.ReactElement
    redirectURL: string
}): React.ReactElement {

    const currentAccessToken = sessionUtils.getAccessToken();
    const currentGroup = sessionUtils.getGroup();

    return (!currentAccessToken && !currentGroup) ? prop.element : <Navigate to={ prop.redirectURL } />
};
