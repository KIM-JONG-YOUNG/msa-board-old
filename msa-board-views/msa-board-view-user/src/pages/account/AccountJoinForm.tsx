import { EMAIL_DOMAIN, ERROR_CODE, GENDER } from "msa-board-view-common/src/constants/constants";
import useFetchData, { FetchErrorResponse, FetchErrorDetails } from "msa-board-view-common/src/hooks/useFetchData";
import sessionUtils from "msa-board-view-common/src/utils/sessionUtils";
import { useCallback } from "react";
import { useErrorBoundary } from "react-error-boundary";
import { useForm } from "react-hook-form";
import { useNavigate } from "react-router-dom";

export type AccountJoinFormInputs = {
	username: string
	password: string
	confirmPassword: string
	name: string
	gender: keyof typeof GENDER
	emailUsername: string
	emailDomain: typeof EMAIL_DOMAIN[keyof typeof EMAIL_DOMAIN]
};

export default function AccountJoinForm() {

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
	} = useForm<AccountJoinFormInputs>({ mode: "onBlur" });

	const usernameRegister = register("username", {
		required: "계정은 비어있을 수 없습니다.",
		max: { value: 30, message: "계정이 30자를 초과할 수 없습니다." },
		pattern: { value: /^[a-zA-Z0-9]+$/, message: "계정이 형식에 맞지 않습니다." }
	});
	const passwordRegister = register("password", {
		required: "비밀번호는 비어있을 수 없습니다."
	});
	const confirmPasswordRegister = register("confirmPassword", {
		required: "재확인 비밀번호는 비어있을 수 없습니다.",
		validate: {
			notMatched: (confirmPassword) => {
				if (getValues("password") !== confirmPassword) {
					return "비밀번호와 재확인 비밀번호가 일치하지 않습니다.";
				} else {
					return true;
				}
			}
		}
	});
	const nameRegister = register("name", {
		required: "이름은 비어있을 수 없습니다.",
		max: { value: 30, message: "이름이 30자를 초과할 수 없습니다." }
	});
	const genderRegister = register("gender", {
		required: "성별은 비어있을 수 없습니다."
	});
	const emailUsernameRegister = register("emailUsername", {
		required: "이메일 계정은 비어있을 수 없습니다.",
		max: { value: 30, message: "이메일 계정이 30자를 초과할 수 없습니다." },
		pattern: { value: /^[a-zA-Z0-9]+$/, message: "이메일 계정이 형식에 맞지 않습니다." }
	});
	const emailDomainRegister = register("emailDomain", {
		required: "이메일 도메인은 비어있을 수 없습니다."
	});

	const join = useCallback(async () => {

		const formData = getValues();

		fetchData({
			url: `${endpointURL}/apis/users`,
			method: "POST",
			headers: { "Content-Type": "application/json" },
			body: JSON.stringify({
				username: formData.username,
				password: formData.password,
				name: formData.name,
				gender: formData.gender,
				email: `${formData.emailUsername}@${formData.emailDomain}`
			})
		}).then((response: Response) => {

			alert("회원가입이 완료되었습니다.");
			navigate("/account/login/form");

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
				case ERROR_CODE.DUPLICATED_MEMBER_USERNAME:
					setError("username", { message: errorMessage });
					break;
				default:
					showBoundary(errorResponse);
					break;
			}
		});

	}, [getValues, fetchData, navigate, setError, showBoundary]);

	return (
		// <!-- Join User -->
		<section className="resume-section">
			<div className="resume-section-content">

				<h1 className="mb-4">
					Join
					<span className="text-primary">User</span>
				</h1>

				<form onSubmit={handleSubmit(() => join())}>

					<div className="row">
						<div className="col-lg-8 mb-3">
							<label className="form-label subheading">Username</label>
							<input type="text" className="form-control" placeholder="Username..." {...usernameRegister} />
							{!!errors.username && <div className="text-danger">{errors.username.message}</div>}
						</div>
					</div>

					<div className="row">
						<div className="col-lg-8 mb-3">
							<label className="form-label subheading">Password</label>
							<input type="password" className="form-control" placeholder="Password..." {...passwordRegister} />
							{!!errors.password && <div className="text-danger">{errors.password.message}</div>}
						</div>
					</div>

					<div className="row">
						<div className="col-lg-8 mb-3">
							<label className="form-label subheading">Password Confirm</label>
							<input type="password" className="form-control" placeholder="Confirm Password..." {...confirmPasswordRegister} />
							{!!errors.confirmPassword && <div className="text-danger">{errors.confirmPassword.message}</div>}
						</div>
					</div>

					<div className="row">
						<div className="col-lg-4 mb-3">
							<label className="form-label subheading">Name</label>
							<input type="text" className="form-control" placeholder="Name..." {...nameRegister} />
							{!!errors.name && <div className="text-danger">{errors.name.message}</div>}
						</div>
						<div className="col-lg-4 mb-3">
							<label className="form-label subheading">Gender</label>
							<select className="form-select" {...genderRegister} >
								<option value="">Choose...</option>
								{Object.entries(GENDER).map(([key, value]) => <option key={key} value={key}>{value}</option>)}
							</select>
							{!!errors.gender && <div className="text-danger">{errors.gender.message}</div>}
						</div>
					</div>

					<div className="row">
						<div className="col-lg-8 mb-3">
							<label className="form-label subheading">Email</label>
							<div className="input-group">
								<input type="text" className="form-control" placeholder="Email..." {...emailUsernameRegister} />
								<span className="input-group-text">@</span>
								<select className="form-select"  {...emailDomainRegister} >
									<option value="">Choose...</option>
									{Object.entries(EMAIL_DOMAIN).map(([key, value]) => <option key={key} value={key}>{value}</option>)}
								</select>
							</div>
							{!!errors.emailUsername && <div className="text-danger">{errors.emailUsername.message}</div>}
							{!!errors.emailDomain && <div className="text-danger">{errors.emailDomain.message}</div>}
						</div>
					</div>

					<div className="row">
						<div className="col-lg-8 mt-3">
							<button type="submit" className="btn btn-success w-100">Join</button>
						</div>
					</div>

				</form>

			</div>
		</section>
	);
};