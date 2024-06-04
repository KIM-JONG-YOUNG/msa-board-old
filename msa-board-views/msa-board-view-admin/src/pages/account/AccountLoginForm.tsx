import { useCallback } from "react";
import { useForm } from "react-hook-form";
import { useErrorBoundary } from "react-error-boundary";
import { useNavigate } from "react-router-dom";

import { FetchErrorDetails, FetchErrorResponse } from "msa-board-view-common/src/utils/fetchUtils";
import { ERROR_CODE } from "msa-board-view-common/src/constants/constants";
import useLoading from 'msa-board-view-common/src/hooks/useLoading';

import * as adminService from "../../services/adminService";
import * as sessionUtils from "msa-board-view-common/src/utils/sessionUtils";

export type AccountLoginFormInputs = {
	readonly username: string
	readonly password: string
}

export default function AccountLoginForm() {

	const navigate = useNavigate();
	const { showBoundary } = useErrorBoundary();
	const { loadingCallback } = useLoading();
	
	const { 
		register, 
		setError, 
		handleSubmit, 
		formState: { errors } 
	} = useForm<AccountLoginFormInputs>({ mode: "onBlur" });
	
	const usernameRegister = register("username", {
		required: "계정은 비어있을 수 없습니다.",
		max: { value: 30, message: "계정이 30자를 초과할 수 없습니다." },
		pattern: { value: /^[a-zA-Z0-9]+$/, message: "계정이 형식에 맞지 않습니다." }
	});
	const passwordRegister = register("password", {
		required: "비밀번호는 비어있을 수 없습니다."
	});

	const onSubmitSuccessHandler = useCallback((response: Response) => {
	
		sessionUtils.setAccessToken(response.headers.get("Access-Token") || "");
		sessionUtils.setRefreshToken(response.headers.get("Refresh-Token") || "");
		sessionUtils.setGroup("ADMIN");

		navigate("/main");

	}, [navigate]);

	const onSubmitErrorHandler = useCallback((errorResponse: FetchErrorResponse) => {

		const errorCode = errorResponse.errorCode;
		const errorMessage = errorResponse.errorMessage;
		const errorDetailsList = errorResponse.errorDetailsList || [];

		switch (errorCode) {

			case ERROR_CODE.INVALID_PARAMETER:
				errorDetailsList.forEach((error: FetchErrorDetails) => {
					(error.field === "username") && setError(error.field, { message: error.message });
					(error.field === "password") && setError(error.field, { message: error.message });
				});
				break;

			case ERROR_CODE.NOT_FOUND_MEMBER_USERNAME:
			case ERROR_CODE.NOT_ADMIN_GROUP_MEMBER_USERNAME:
				setError("username", { message: errorMessage });
				break;

			case ERROR_CODE.NOT_MATCHED_MEMBER_PASSWORD:
				setError("password", { message: errorMessage });
				break;

			default:
				throw errorResponse;
		}
		
	}, [setError]);

	const onSubmit = useCallback((formData: AccountLoginFormInputs) => {

		loadingCallback(() => adminService
			.loginAdmin(formData)
			.then(onSubmitSuccessHandler)
			.catch(onSubmitErrorHandler)
			.catch(showBoundary));
			
	}, [loadingCallback, onSubmitSuccessHandler, onSubmitErrorHandler, showBoundary]);

	return (
		// <!-- Login Admin -->
		<section className="resume-section">
			<div className="resume-section-content">

				<form onSubmit={handleSubmit(onSubmit)}>

					<h1 className="mb-4">
						Login
						<span className="text-primary">Admin</span>
					</h1>

					<div className="row">
						<div className="col-xl-8 mb-3">
							<label className="form-label subheading">Username</label>
							<input type="text" className="form-control" placeholder="Username..." {...usernameRegister} />
							{!!errors.username && <div className="text-danger">{errors.username.message}</div>}
						</div>
					</div>

					<div className="row">
						<div className="col-xl-8 mb-3">
							<label className="form-label subheading">Password</label>
							<input type="password" className="form-control" placeholder="Password..." {...passwordRegister} />
							{!!errors.password && <div className="text-danger">{errors.password.message}</div>}
						</div>
					</div>

					<div className="row">
						<div className="col-xl-8 mt-3">
							<button type="submit" className="btn btn-primary text-white w-100">Login</button>
						</div>
					</div>

				</form>

			</div>
		</section>
	);
}