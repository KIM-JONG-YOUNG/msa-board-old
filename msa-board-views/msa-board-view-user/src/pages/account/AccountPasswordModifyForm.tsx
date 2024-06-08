import { ERROR_CODE } from "msa-board-view-common/src/constants/constants";
import useFetchData, { FetchErrorResponse, FetchErrorDetails } from "msa-board-view-common/src/hooks/useFetchData";
import sessionUtils from "msa-board-view-common/src/utils/sessionUtils";
import { useCallback } from "react";
import { useErrorBoundary } from "react-error-boundary";
import { useForm } from "react-hook-form";
import { useNavigate } from "react-router-dom";

export type AccountPasswordModifyFormInputs = {
	currentPassword: string
	newPassword: string
};

export default function AccountPasswordModifyForm() {

	const endpointURL = process.env.REACT_APP_ENDPOINT_URL;

	const navigate = useNavigate();
	const { showBoundary } = useErrorBoundary();
	const { fetchData } = useFetchData();	
	const {
		register,
		setError,
		getValues,
		handleSubmit,
		formState: { errors }
	} = useForm<AccountPasswordModifyFormInputs>({ mode: "onBlur" });

	const currentPasswordRegister = register("currentPassword", { 
		required: "현재 비밀번호는 비어있을 수 없습니다." 
	});
	const newPasswordRegister = register("newPassword", {
		required: "새로운 비밀번호는 비어있을 수 없습니다."
	});
	
	const modifyPassword = useCallback(() => {

		const formData = getValues();

		fetchData({
			url: `${endpointURL}/apis/admins/password`,
			refreshURL: `${endpointURL}/apis/admins/refresh`,
			method: "PATCH",
			headers: { "Content-Type": "application/json" },
			body: JSON.stringify(formData)
		}).then(async (response: Response) => {

			sessionUtils.removeAccessToken();
			sessionUtils.removeRefreshToken();
			sessionUtils.removeGroup();
			
			alert("비밀번호가 정상적으로 수정되었습니다.\n다시 로그인해 주십시오.");
			navigate("/account/login/form");

		}).catch((errorResponse: FetchErrorResponse) => {

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
					sessionUtils.removeAccessToken();
					sessionUtils.removeRefreshToken();
					sessionUtils.removeGroup();
					navigate("/account/login/form");
					break;
				case ERROR_CODE.NOT_MATCHED_MEMBER_PASSWORD:
					setError("currentPassword", { message: errorMessage });
					break;
				default:
					showBoundary(errorResponse);
					break;
			}
		});

	}, [getValues, fetchData, navigate, setError, showBoundary]);

	return (
		// <!-- Modify Admin Password -->
		<section className="resume-section">
			<div className="resume-section-content">

				<h1 className="mb-4">
					Modify
					<span className="text-primary">Password</span>
				</h1>

				<form onSubmit={handleSubmit(() => modifyPassword())}>

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
};