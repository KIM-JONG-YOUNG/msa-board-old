import { Navigate } from "react-router-dom";
import * as sessionUtils from "../utils/sessionUtils";
import { GROUP } from "../constants/constants";

export function MemberRouter(  
    element: React.ReactElement,
    group: keyof typeof GROUP,
    redirectURL: string
): React.ReactElement {

    const currentAccessToken = sessionUtils.getAccessToken();
    const currentGroup = sessionUtils.getGroup();

    return (!!currentAccessToken && !!currentGroup && group === currentGroup) ? element : <Navigate to={redirectURL} />
};

export function AnonymousRouter(
    element: React.ReactElement,
    redirectURL: string
): React.ReactElement {

    const currentAccessToken = sessionUtils.getAccessToken();
    const currentGroup = sessionUtils.getGroup();

    return (!!currentAccessToken && !!currentGroup) ? element : <Navigate to={redirectURL} />
};
