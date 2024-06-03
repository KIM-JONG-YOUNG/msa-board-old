import React from "react";
import { AnonymousRouter, MemberRouter } from 'msa-board-view-common/src/components/ProtectRouter';

export function AdminRounter({ element }: { element: React.ReactElement }) {

    return <MemberRouter group="ADMIN" element={(!!element) && element} redirectURL="/account/login/form" />;
}

export function NotAdminRounter({ element }: { element: React.ReactElement }) {

    return <AnonymousRouter element={(!!element) && element} redirectURL="/main" />;
}