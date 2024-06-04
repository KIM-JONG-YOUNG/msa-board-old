import { useForm } from "react-hook-form";
import { useErrorBoundary } from "react-error-boundary";

import { useNavigate } from "react-router-dom";
import { ERROR_CODE } from "msa-board-view-common/src/constants/constants";
import { FetchErrorDetails, FetchErrorResponse } from "msa-board-view-common/src/utils/fetchUtils";
import useLoading from "msa-board-view-common/src/hooks/useLoading";

import * as adminService from "../../services/adminService";
import * as sessionUtils from "msa-board-view-common/src/utils/sessionUtils";
import { useCallback } from "react";

export type AccountPasswordModifyFormInputs = {
	readonly currentPassword: string
	readonly newPassword: string
}

export default function AccountPasswordModifyForm() {

	const navigate = useNavigate();
	const { showBoundary } = useErrorBoundary();
	const { loadingCallback } = useLoading();
	
	const {
		register,
		setError,
		handleSubmit,
		formState: { errors }
	} = useForm<AccountPasswordModifyFormInputs>({ mode: "onBlur" });

	const currentPasswordRegister = register("currentPassword", { 
		required: "현재 비밀번호는 비어있을 수 없습니다." 
	});
	const newPasswordRegister = register("newPassword", {
		required: "새로운 비밀번호는 비어있을 수 없습니다."
	});

	const onSubmitSuccessHandler = useCallback(() => {

		sessionUtils.initSessionInfo();
		navigate("/account/login/form");

	}, [navigate]);
	
	const onSubmitErrorHandler = useCallback((errorResponse: FetchErrorResponse) => {

		const errorCode = errorResponse.errorCode;
		const errorMessage = errorResponse.errorMessage;
		const errorDetailsList = errorResponse.errorDetailsList || [];

		switch (errorCode) {

			case ERROR_CODE.INVALID_PARAMETER:
				errorDetailsList.forEach((error: FetchErrorDetails) => {
					(error.field === "currentPassword") && setError(error.field, { message: error.message });
					(error.field === "newPassword") && setError(error.field, { message: error.message });
				});
				break;

			case ERROR_CODE.NOT_FOUND_MEMBER:
				sessionUtils.initSessionInfo();
				navigate("/account/login/form");
				break;

			case ERROR_CODE.NOT_MATCHED_MEMBER_PASSWORD:
				setError("currentPassword", { message: errorMessage });
				break;

			default:
				throw errorResponse;
		}

	}, [navigate, setError]);
	
	const onSubmit = useCallback((formData: AccountPasswordModifyFormInputs) => {

		loadingCallback(() => adminService
			.modifyAdminPassword({
				currentPassword: formData.currentPassword,
				newPassword: formData.newPassword
			})
			.then(onSubmitSuccessHandler)
			.catch(onSubmitErrorHandler)
			.catch(showBoundary));

	}, [loadingCallback, onSubmitSuccessHandler, onSubmitErrorHandler, showBoundary]);

	return (
		// <!-- Modify Admin Password -->
		<section className="resume-section">
			<div className="resume-section-content">

				<h1 className="mb-4">
					Modify
					<span className="text-primary">Password</span>
				</h1>

				<form onSubmit={handleSubmit(onSubmit)}>

					<div className="row">
						<div className="col-xl-8 mb-3">
							<label className="form-label subheading">Password</label>
							<input type="password" className="form-control" placeholder="Current Password..." {...currentPasswordRegister} />
							{!!errors.currentPassword && <div className="text-danger">{errors.currentPassword.message}</div>}
						</div>
					</div>

					<div className="row">
						<div className="col-xl-8 mb-3">
							<label className="form-label subheading">New Password</label>
							<input type="password" className="form-control" placeholder="New Password..." {...newPasswordRegister} />
							{!!errors.newPassword && <div className="text-danger">{errors.newPassword.message}</div>}
						</div>
					</div>

					<div className="row">
						<div className="col-xl-8 mt-3">
							<button type="submit" className="btn btn-warning w-100">Modify Password</button>
						</div>
					</div>

				</form>

			</div>
		</section>
	);
}