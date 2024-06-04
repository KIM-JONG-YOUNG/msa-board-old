import { useForm } from "react-hook-form";
import { useErrorBoundary } from "react-error-boundary";
import { ERROR_CODE, GENDER, GROUP, STATE } from "msa-board-view-common/src/constants/constants";
import { useCallback, useEffect, useState } from "react";

import { FetchErrorDetails, FetchErrorResponse } from "msa-board-view-common/src/utils/fetchUtils";
import useLoading from "msa-board-view-common/src/hooks/useLoading";
import { useParams, useNavigate } from "react-router-dom";

import * as adminService from "../../services/adminService";

export type UserModifyFormInputs = {
	group: keyof typeof GROUP
	state: keyof typeof STATE
}

export type MemberDetails = {
	id: string
	username: string
	name: string
	gender: keyof typeof GENDER
	email: string
	group: keyof typeof GROUP
	state: keyof typeof STATE
}

export default function MemberDetails() {

	const navigate = useNavigate();
	const { id } = useParams();
	const { showBoundary } = useErrorBoundary();
	const { loadingCallback } = useLoading();

	const [member, setMember] = useState<MemberDetails>();

	const {
		register,
		setError,
		setValue,
		handleSubmit
	} = useForm<UserModifyFormInputs>({ mode: "onBlur" });

	const groupRegister = register("group");
	const stateRegister = register("state");

	const onSubmitErrorHandler = useCallback((errorResponse: FetchErrorResponse) => {

		const errorCode = errorResponse.errorCode;
		const errorMessage = errorResponse.errorMessage;
		const errorDetailsList = errorResponse.errorDetailsList || [];

		switch (errorCode) {

			case ERROR_CODE.INVALID_PARAMETER:
				errorDetailsList.forEach((error: FetchErrorDetails) => {
					(error.field === "state") && setError(error.field, { message: error.message });
					(error.field === "group") && setError(error.field, { message: error.message });
				});
				break;

			case ERROR_CODE.NOT_FOUND_MEMBER:
				alert(errorMessage);
				navigate("/member/list");
				break;

			default:
				throw errorResponse;
		}

	}, [setError, navigate]);

	const onSubmit = useCallback((formData: UserModifyFormInputs) => {

		if (!!id && member?.group === "USER") {

			loadingCallback(() => adminService
				.modifyUser(id, {
					group: formData.group,
					state: formData.state
				})
				.catch(onSubmitErrorHandler)
				.catch(showBoundary));

		} else {

			alert((!id) ? "회원 ID가 존재하지 않습니다." : "일반 회원이 아닙니다.");
			navigate("/member/list");
		}

	}, [id, member, loadingCallback, onSubmitErrorHandler, showBoundary, navigate]);

	useEffect(() => {

		(!!member) && setValue("group", member?.group);
		(!!member) && setValue("state", member?.state);

	}, [member, setValue]);

	useEffect(() => {

		if (!!id) {

			loadingCallback(() => adminService
				.getMember(id)
				.then((response: Response) => response.json())
				.then((member: MemberDetails) => setMember(member))
				.catch(onSubmitErrorHandler)
				.catch(showBoundary));

		} else {

			alert("회원 ID가 존재하지 않습니다.");
			navigate("/post/list");
		}

	}, []);

	return (
		// <!-- User Modify Form -->
		<section className="resume-section">
			<div className="resume-section-content">

				<h1 className="mb-4">
					Member
					<span className="text-primary">Details</span>
				</h1>

				<form onSubmit={handleSubmit(onSubmit)}>

					<div className="row">
						<div className="col-xl-8 mb-3">
							<label className="form-label subheading">Username</label>
							<input type="text" className="form-control" disabled value={member?.username || ""} />
						</div>
					</div>

					<div className="row">
						<div className="col-xl-4 mb-3">
							<label className="form-label subheading">Name</label>
							<input type="text" className="form-control" disabled value={member?.name || ""} />
						</div>
						<div className="col-xl-4 mb-3">
							<label className="form-label subheading">Gender</label>
							<input type="text" className="form-control" disabled 
								value={
									(!!member?.gender) ? GENDER[member.gender] : ""
								} />
						</div>
					</div>

					<div className="row">
						<div className="col-xl-8 mb-3">
							<label className="form-label subheading">Email</label>
							<input type="text" className="form-control" disabled value={member?.email || ""} />
						</div>
					</div>

					<div className="row">
						<div className="col-xl-8 mb-3">
							<label className="form-label subheading">Group</label>
							{
								(member?.group === "USER") 
									? <select className="form-select" {...groupRegister}>
										{Object.entries(GROUP).map(([key, value]) => <option key={key} value={key}>{value}</option>)}
									</select>
									: <input type="text" className="form-control" disabled value={(!!member?.group) ? GROUP[member.group] : ""} />
							}
							
						</div>
					</div>

					<div className="row">
						<div className="col-xl-8 mb-3">
							<label className="form-label subheading">State</label>
							{
								(member?.group === "USER") 
									? <select className="form-select" {...stateRegister}>
										{Object.entries(STATE).map(([key, value]) => <option key={key} value={key}>{value}</option>)}
									</select>
									: <input type="text" className="form-control" disabled value={(!!member?.state) ? STATE[member.state] : ""} />
							}
						</div>
					</div>

					{
						(member?.group === "USER") && 
							<div className="row">
								<div className="col-xl-8 mt-3">
									<button type="submit" className="btn btn-info w-100">Modify</button>
								</div>
							</div>
					}

				</form>

			</div>
		</section>
	);
}