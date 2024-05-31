import { useForm } from "react-hook-form";
import { useErrorBoundary } from "react-error-boundary";

import { useNavigate } from "react-router-dom";
import { EMAIL_DOMAIN, ERROR_CODE, GENDER } from "msa-board-view-common/src/constants/constants";
import { IFetchErrorDetails, IFetchErrorResponse } from "msa-board-view-common/src/utils/fetchUtils";
import * as adminService from "../../services/adminService";
import * as sessionUtils from "msa-board-view-common/src/utils/sessionUtils";
import { useEffect, useState } from "react";

export interface IAccountModifyForm {
	readonly name: string
	readonly gender: keyof typeof GENDER
	readonly emailUsername: string
	readonly emailDomain: keyof typeof EMAIL_DOMAIN
}

export default function AccountModifyForm() {

	const {
		register,
		setValue,
		setError,
		handleSubmit,
		formState: { errors }
	} = useForm<IAccountModifyForm>({ mode: "onBlur" });
	const { showBoundary } = useErrorBoundary();
	const navigate = useNavigate();

	const [username, setUsername] = useState("");
	const nameRegister = register("name", {
		required: "이름은 비어있을 수 없습니다.",
		max: {
			value: 30,
			message: "이름이 30자를 초과할 수 없습니다."
		}
	});
	const genderRegister = {
		...register("gender", {
			required: "성별은 비어있을 수 없습니다."
		}),
		defaultValue: undefined
	};
	const emailUsernameRegister = register("emailUsername", {
		required: "이메일 계정은 비어있을 수 없습니다.",
		max: {
			value: 30,
			message: "이메일 계정이 30자를 초과할 수 없습니다."
		},
		pattern: {
			value: /^[a-zA-Z0-9]+$/,
			message: "이메일 계정이 형식에 맞지 않습니다."
		}
	});
	const emailDomainRegister = {
		...register("emailDomain", {
			required: "이메일 도메인은 비어있을 수 없습니다.",
		}),
		defaultValue: undefined
	};

	const onSubmit = (formData: IAccountModifyForm) => {

		adminService.modifyAdmin({
			name: formData.name,
			gender: formData.gender,
			email: `${formData.emailUsername}@${formData.emailDomain}`
		}).catch((errorResponse: IFetchErrorResponse) => {

			switch (errorResponse.errorCode) {

				case ERROR_CODE.INVALID_PARAMETER:
					return (!!errorResponse.errorDetailsList) && errorResponse.errorDetailsList
						.forEach((error: IFetchErrorDetails) => {
							(error.field === "name") && setError(error.field, { message: error.message });
							(error.field === "gender") && setError(error.field, { message: error.message });
							(error.field === "email") && setError("emailUsername", { message: error.message });
						});

				case ERROR_CODE.NOT_FOUND_MEMBER:
					sessionUtils.initSessionInfo();
					return navigate("/account/login/form");

				default:
					throw errorResponse;
			}

		}).catch(showBoundary);
	};

	useEffect(() => {
		
		adminService.getAdmin().then(async (response: Response) => {

			const admin = await response.json();

			("username" in admin) && setUsername(admin?.username);
			("name" in admin) && setValue("name", admin?.name);
			("gender" in admin) && setValue("gender", admin?.gender);
			
			if ("email" in admin) {

				const email = admin?.email;

				setValue("emailUsername", (!!email) ? email.split("@")[0] : "");
				setValue("emailDomain", (!!email) ? email.split("@")[1] : "");
			}

		}).catch(showBoundary);

	}, [setValue, showBoundary]);

	return (
		// <!-- Modify Admin -->
		<section className="resume-section">
			<div className="resume-section-content">

				<h1 className="mb-4">
					Modify
					<span className="text-primary">Admin</span>
				</h1>

				<div className="row">
					<div className="col-xl-8 mb-3">
						<label className="form-label subheading">Username</label>
						<input type="text" className="form-control" disabled value={username} />
					</div>
				</div>

				<div className="row">
					<label className="form-label subheading">Password</label>
					<div className="col-xl-8 mb-3">
						<button type="button" className="btn btn-warning w-100">Modify Password</button>
					</div>
				</div>

				<form onSubmit={handleSubmit(onSubmit)}>

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
									{Object.entries(EMAIL_DOMAIN).map(([key, value]) => <option key={key} value={key}>{value}</option>)}
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
}