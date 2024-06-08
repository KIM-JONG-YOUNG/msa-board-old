import { ERROR_CODE } from "msa-board-view-common/src/constants/constants";
import useFetchData, { FetchErrorResponse, FetchErrorDetails } from "msa-board-view-common/src/hooks/useFetchData";
import sessionUtils from "msa-board-view-common/src/utils/sessionUtils";
import { useCallback } from "react";
import { useErrorBoundary } from "react-error-boundary";
import { useForm } from "react-hook-form";
import { useNavigate } from "react-router-dom";

export type AccountLoginFormInputs = {
	username: string
	password: string
};

export default function AccountLoginForm() {

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
	} = useForm<AccountLoginFormInputs>({ mode: "onBlur" });

	const usernameRegister = register("username", {
		required: "계정은 비어있을 수 없습니다.",
		max: { value: 30, message: "계정이 30자를 초과할 수 없습니다." },
		pattern: { value: /^[a-zA-Z0-9]+$/, message: "계정이 형식에 맞지 않습니다." }
	});
	const passwordRegister = register("password", {
		required: "비밀번호는 비어있을 수 없습니다."
	});

	const login = useCallback(async () => {

		const formData = getValues();

		fetchData({
			url: `${endpointURL}/apis/users/login`,
			method: "POST",
			headers: { "Content-Type": "application/json" },
			body: JSON.stringify(formData)
		}).then((response: Response) => {

			sessionUtils.setAccessToken(response.headers.get("Access-Token") || "");
			sessionUtils.setRefreshToken(response.headers.get("Refresh-Token") || "");
			sessionUtils.setGroup("USER");

			navigate("/main");

		}).catch((errorResponse: FetchErrorResponse) => {

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
				case ERROR_CODE.NOT_USER_GROUP_MEMBER_USERNAME:
					setError("username", { message: errorMessage });
					break;
				case ERROR_CODE.NOT_MATCHED_MEMBER_PASSWORD:
					setError("password", { message: errorMessage });
					break;
				default:
					showBoundary(errorResponse);
					break;
			}
		});
		
	}, [getValues, fetchData, navigate, setError, showBoundary]);

	return (
		// <!-- Login User -->
		<section className="resume-section">
			<div className="resume-section-content">

				<form onSubmit={handleSubmit(() => login())}>

					<h1 className="mb-4">
						Login
						<span className="text-primary">USER</span>
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
};