import { useForm } from "react-hook-form";
import { useErrorBoundary } from "react-error-boundary";

import { useNavigate } from "react-router-dom";
import { EMAIL_DOMAIN, ERROR_CODE, GENDER } from "msa-board-view-common/src/constants/constants";
import { IFetchErrorDetails, IFetchErrorResponse } from "msa-board-view-common/src/utils/fetchUtils";
import * as adminService from "../../services/adminService";
import * as sessionUtils from "msa-board-view-common/src/utils/sessionUtils";
import { useEffect, useState } from "react";

export interface IAccountPasswordModifyForm {
	readonly currentPassword: string
	readonly newPassword: string
}

export default function AccountPasswordModifyForm() {

	const {
		register,
		setError,
		handleSubmit,
		formState: { errors }
	} = useForm<IAccountPasswordModifyForm>({ mode: "onBlur" });
	const { showBoundary } = useErrorBoundary();
	const navigate = useNavigate();

	const currentPasswordRegister = register("currentPassword", {
		required: "현재 비밀번호는 비어있을 수 없습니다."
	});
	const newPasswordRegister = register("newPassword", {
		required: "새로운 비밀번호는 비어있을 수 없습니다."
	});

	const onSubmit = (formData: IAccountPasswordModifyForm) => {

		adminService.modifyAdminPassword({
			currentPassword: formData.currentPassword,
			newPassword: formData.newPassword
		}).then((response: Response) => {

			sessionUtils.initSessionInfo();
			navigate("/account/login/form");

		}).catch((errorResponse: IFetchErrorResponse) => {

			switch (errorResponse.errorCode) {

				case ERROR_CODE.INVALID_PARAMETER:
					return (!!errorResponse.errorDetailsList) && errorResponse.errorDetailsList
						.forEach((error: IFetchErrorDetails) => {
							(error.field === "currentPassword") && setError(error.field, { message: error.message });
							(error.field === "newPassword") && setError(error.field, { message: error.message });
						});

				case ERROR_CODE.NOT_FOUND_MEMBER:
					sessionUtils.initSessionInfo();
					return navigate("/account/login/form");

				case ERROR_CODE.NOT_MATCHED_MEMBER_PASSWORD:
					return setError("currentPassword", { message: errorResponse.errorMessage });

				default:
					throw errorResponse;
			}

		}).catch(showBoundary);
	};

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