import { ERROR_CODE } from "msa-board-view-common/src/constants/constants";
import sessionUtils from "msa-board-view-common/src/utils/sessionUtils";
import { useEffect, useRef } from "react";
import { FallbackProps } from "react-error-boundary";
import { Navigate, useLocation } from "react-router-dom";

export default function ErrorPage({ error, resetErrorBoundary }: FallbackProps) {

	const errorCode = error?.errorCode;
	const errorMessage = error?.errorMessage;

	const { pathname } = useLocation();
	const originalPathname = useRef(pathname);

	useEffect(() => {
		
		(pathname !== originalPathname.current) && resetErrorBoundary();
		
	}, [pathname, originalPathname, resetErrorBoundary])

	useEffect(() => {
		switch (errorCode) {
			case ERROR_CODE.EXPIRED_ACCESS_TOKEN:
			case ERROR_CODE.EXPIRED_REFRESH_TOKEN:
			case ERROR_CODE.REVOKED_ACCESS_TOKEN:
			case ERROR_CODE.REVOKED_REFRESH_TOKEN:
			case ERROR_CODE.INVALID_ACCESS_TOKEN:
			case ERROR_CODE.INVALID_REFRESH_TOKEN:
				sessionUtils.removeAccessToken();
				sessionUtils.removeRefreshToken();
				sessionUtils.removeGroup();
				alert(errorMessage);
				break;
			default:
				break;
		}
	}, [errorCode]);

	return (
		(	errorCode === ERROR_CODE.EXPIRED_ACCESS_TOKEN
			|| errorCode === ERROR_CODE.EXPIRED_REFRESH_TOKEN
			|| errorCode === ERROR_CODE.REVOKED_ACCESS_TOKEN
			|| errorCode === ERROR_CODE.REVOKED_ACCESS_TOKEN
			|| errorCode === ERROR_CODE.INVALID_ACCESS_TOKEN
			|| errorCode === ERROR_CODE.INVALID_REFRESH_TOKEN	) 
		? <Navigate to={"/account/login/form"} />
		: <section className="resume-section">
			<div className="resume-section-content">
				<h1 className="mb-4">
					Error
					<span className="text-primary">Page</span>
					{(!!errorMessage) && <div className="subheading mb-5">{errorMessage}</div>}
				</h1>
			</div>
		</section>
	);
};