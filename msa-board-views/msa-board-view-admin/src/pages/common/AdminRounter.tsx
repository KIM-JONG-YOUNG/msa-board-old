import React from "react";
import { AnonymousRouter, MemberRouter } from 'msa-board-view-common/src/components/ProtectRouter';

export function AdminRounter(prop: {
	element: React.ReactElement	
}) {

    return <MemberRouter group="ADMIN" element={ prop.element } redirectURL="/login/form" />;
}

export function NotAdminRounter(prop: {
	element: React.ReactElement	
}) {

    return <AnonymousRouter element={ prop.element } redirectURL="/main" />;
}