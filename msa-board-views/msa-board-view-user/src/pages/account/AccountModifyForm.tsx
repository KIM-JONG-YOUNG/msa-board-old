import { GENDER, EMAIL_DOMAIN, ERROR_CODE } from "msa-board-view-common/src/constants/constants"
import useFetchData, { FetchErrorResponse, FetchErrorDetails } from "msa-board-view-common/src/hooks/useFetchData"
import sessionUtils from "msa-board-view-common/src/utils/sessionUtils"
import { useState, useCallback, useEffect } from "react"
import { useErrorBoundary } from "react-error-boundary"
import { useForm } from "react-hook-form"
import { useNavigate } from "react-router-dom"

export type AccountModifyFormInputs = {
	name: string
	gender: keyof typeof GENDER
	emailUsername: string
	emailDomain: typeof EMAIL_DOMAIN[keyof typeof EMAIL_DOMAIN]
};

export type AccountDetails = {
	username: string
	name: string
	gender: keyof typeof GENDER
	email: string
};

export default function AccountModifyForm() {

	const endpointURL = process.env.REACT_APP_ENDPOINT_URL;

	const navigate = useNavigate();
	const { showBoundary } = useErrorBoundary();
	const { fetchData } = useFetchData();
	const [ account, setAccount ] = useState<AccountDetails>();
	const {
		register,
		setValue,
		setError,
		getValues,
		handleSubmit,
		formState: { errors }
	} = useForm<AccountModifyFormInputs>({ mode: "onBlur" });

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

	const modify = useCallback(() => {

		const formData = getValues();

		fetchData({
			url: `${endpointURL}/apis/users`,
			refreshURL: `${endpointURL}/apis/users/refresh`,
			method: "PATCH",
			headers: { "Content-Type": "application/json" },
			body: JSON.stringify({
				name: formData.name,
				gender: formData.gender,
				email: `${formData.emailUsername}@${formData.emailDomain}`
			})
		}).catch((errorResponse: FetchErrorResponse) => {

			const errorCode = errorResponse.errorCode;
			const errorDetailsList = errorResponse.errorDetailsList || [];
	
			switch (errorCode) {
				case ERROR_CODE.INVALID_PARAMETER:
					errorDetailsList.forEach((error: FetchErrorDetails) => {
						(error.field === "name") && setError(error.field, { message: error.message });
						(error.field === "gender") && setError(error.field, { message: error.message });
						(error.field === "email") && setError("emailUsername", { message: error.message });
					});
					break;
				case ERROR_CODE.NOT_FOUND_MEMBER:
					sessionUtils.removeAccessToken();
					sessionUtils.removeRefreshToken();
					sessionUtils.removeGroup();
					navigate("/account/login/form");
					break;
				default:
					throw errorResponse;
			}
		});

	}, [getValues, fetchData, setError, navigate, showBoundary]);

	useEffect(() => {

		if (!!account) {

			const emailAddress = account.email?.split("@");

			setValue("name", account.name);
			setValue("gender", account.gender);
			setValue("emailUsername", emailAddress?.[0]);

			switch (emailAddress[1]) {
				case EMAIL_DOMAIN.EXAMPLE_DOMAIN:
				case EMAIL_DOMAIN.NAVER_DOMAIN:
				case EMAIL_DOMAIN.GMAIL_DOMAIN:
					setValue("emailDomain", emailAddress?.[1]);
					break;
			}			
		}

	}, [account, setValue]);

	useEffect(() => {

		fetchData({
			url: `${endpointURL}/apis/users`,
			refreshURL: `${endpointURL}/apis/users/refresh`,
			method: "GET",
			headers: { "Accept": "application/json" }
		}).then(async (response: Response) => {

			setAccount(await response.json());

		}).catch((errorResponse: FetchErrorResponse) => {

			const errorCode = errorResponse.errorCode;

			switch (errorCode) {
				case ERROR_CODE.NOT_FOUND_MEMBER:
					sessionUtils.removeAccessToken();
					sessionUtils.removeRefreshToken();
					sessionUtils.removeGroup();	
					navigate("/account/login/form");
					break;
				default:
					showBoundary(errorResponse);
					break;
			}
		});

	}, []);

	return (
		// <!-- Modify Admin -->
		<section className="resume-section">
			<div className="resume-section-content">

				<h1 className="mb-4">
					Modify
					<span className="text-primary">User</span>
				</h1>

				<div className="row">
					<div className="col-xl-8 mb-3">
						<label className="form-label subheading">Username</label>
						<input type="text" className="form-control" disabled value={account?.username || ""} />
					</div>
				</div>

				<div className="row">
					<label className="form-label subheading">Password</label>
					<div className="col-xl-8 mb-3">
						<button type="button" className="btn btn-warning w-100" onClick={() => navigate("/account/password/modify/form")}>Modify Password</button>
					</div>
				</div>

				<form onSubmit={handleSubmit(() => modify())}>

					<div className="row">
						<div className="col-xl-4 mb-3">
							<label className="form-label subheading">Name</label>
							<input type="text" className="form-control" placeholder="Name..." {...nameRegister} />
							{!!errors.name && <div className="text-danger">{errors.name.message}</div>}
						</div>
						<div className="col-xl-4 mb-3">
							<label className="form-label subheading">Gender</label>
							<select className="form-select" {...genderRegister}>
								{Object.entries(GENDER).map(([key, value]) => <option key={key} value={key}>{value}</option>)}
							</select>
							{!!errors.gender && <div className="text-danger">{errors.gender.message}</div>}
						</div>
					</div>

					<div className="row">
						<div className="col-xl-8 mb-3">
							<label className="form-label subheading">Email</label>
							<div className="input-group">
								<input type="text" className="form-control" placeholder="Email..." {...emailUsernameRegister} />
								<span className="input-group-text">@</span>
								<select className="form-select" {...emailDomainRegister}>
									{Object.entries(EMAIL_DOMAIN).map(([key, value]) => <option key={key} value={value}>{value}</option>)}
								</select>
							</div>
							{!!errors.emailUsername && <div className="text-danger">{errors.emailUsername.message}</div>}
							{!!errors.emailDomain && <div className="text-danger">{errors.emailDomain.message}</div>}
						</div>
					</div>

					<div className="row">
						<div className="col-xl-8 mt-3">
							<button type="submit" className="btn btn-info w-100">Modify</button>
						</div>
					</div>

				</form>

			</div>
		</section>
	);
};