import { ERROR_CODE } from "msa-board-view-common/src/constants/constants";
import * as sessionUtils from "msa-board-view-common/src/utils/sessionUtils";
import { useEffect, useRef } from "react";
import { FallbackProps } from "react-error-boundary";
import { useLocation, useNavigate } from "react-router-dom";

export default function ErrorPage({
	error,
	resetErrorBoundary
}: FallbackProps) {

	const navigate = useNavigate();
	const { pathname } = useLocation();
	const originalPathname = useRef(pathname);

	const errorCode = ("errorCode" in error) ? error.errorCode : null;
	const errorMessage = ("errorCode" in error) ? error.errorMessage : null;

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
				sessionUtils.initSessionInfo();
				alert(errorMessage);
				navigate("/account/login/form");
				resetErrorBoundary();
				break;
			default:
				break;
		}
	}, []);

	return (
		// <!-- Error -->
		<section className="resume-section">
			<div className="resume-section-content">
				<h1 className="mb-4">
					Error
					<span className="text-primary">Page</span>
				</h1>
			</div>
		</section>
	);
}