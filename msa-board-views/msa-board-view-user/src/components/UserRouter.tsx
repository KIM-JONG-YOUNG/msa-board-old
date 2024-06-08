import { ReactElement } from "react";
import { AnonymousRouter, MemberRouter } from 'msa-board-view-common/src/components/ProtectRouter';

export function UserRouter({ element }: { element: ReactElement }) {
    
    return <MemberRouter group="USER" element={(!!element) && element} redirectURL="/account/login/form" />;
}

export function NotUserRouter({ element }: { element: ReactElement }) {

    return <AnonymousRouter element={(!!element) && element} redirectURL="/main" />;
}