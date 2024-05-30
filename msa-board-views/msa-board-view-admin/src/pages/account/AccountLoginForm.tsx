import { useForm } from "react-hook-form";
import { useErrorBoundary } from "react-error-boundary";
import { IFetchErrorDetails, IFetchErrorResponse } from 'msa-board-view-common/src/utils/fetchUtils';

import * as adminService from "../../services/adminService";
import * as sessionUtils from "msa-board-view-common/src/utils/sessionUtils";
import { useNavigate } from "react-router-dom";
import { ERROR_CODE } from "msa-board-view-common/src/constants/constants";

export interface IAccountLoginForm {
	username: string
	password: string
}

export default function AccountLoginForm() {

	const {
		register,
		setError,
		handleSubmit,
		formState: { errors }
	} = useForm<IAccountLoginForm>({ mode: "onBlur" });
	const { showBoundary } = useErrorBoundary();
	const navigate = useNavigate();

	const usernameRegister = register("username", {
		required: "계정은 비어있을 수 없습니다.",
		max: {
			value: 30,
			message: "계정이 30자를 초과할 수 없습니다."
		},
		pattern: {
			value: /^[a-zA-Z0-9]+$/,
			message: "계정이 형식에 맞지 않습니다."
		}
	});
	const passwordRegister = register("password", {
		required: "비밀번호는 비어있을 수 없습니다."
	});

	const onSubmit = (formData: IAccountLoginForm) => {

		adminService.loginAdmin({
			username: formData.username,
			password: formData.password
		}).then(response => {

			const accessToken = response.headers.get("Access-Token");
			const refreshToken = response.headers.get("Refresh-Token");

			(!!accessToken) && sessionUtils.setAccessToken(accessToken);
			(!!refreshToken) && sessionUtils.setRefreshToken(refreshToken);

			navigate("/main");

		}).catch((errorResponse: IFetchErrorResponse) => {

			switch (errorResponse.errorCode) {

				case ERROR_CODE.INVALID_PARAMETER:
					
					return (!!errorResponse.errorDetailsList) && errorResponse.errorDetailsList
						.forEach((error: IFetchErrorDetails) => {
							(error.field === "username") && setError(error.field, { message: error.message });
							(error.field === "password") && setError(error.field, { message: error.message });
						});

				case ERROR_CODE.NOT_FOUND_MEMBER_USERNAME:
				case ERROR_CODE.NOT_ADMIN_GROUP_MEMBER_USERNAME:
					
					return setError("username", { message: errorResponse.errorMessage });

				case ERROR_CODE.NOT_MATCHED_MEMBER_PASSWORD:
					
					return setError("password", { message: errorResponse.errorMessage });

				default:
					throw errorResponse;
			}

		}).catch(showBoundary);
	}; 

	return (
		// <!-- Login Member -->
		<section className="resume-section">
			<div className="resume-section-content">

				<form onSubmit={ handleSubmit(onSubmit) }>

					<h1 className="mb-4">
						Login
						<span className="text-primary">Admin</span>
					</h1>

					<div className="row">
						<div className="col-xl-8 mb-3">
							<label className="form-label subheading">Username</label>
							<input type="text" className="form-control" placeholder="Example input placeholder" { ...usernameRegister } />
							{!!errors.username && <div className="text-danger">{ errors.username.message }</div>}
						</div>
					</div>

					<div className="row">
						<div className="col-xl-8 mb-3">
							<label className="form-label subheading">Password</label>
							<input type="password" className="form-control" placeholder="Another input placeholder" { ...passwordRegister } />
							{!!errors.password && <div className="text-danger">{ errors.password.message }</div>}
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