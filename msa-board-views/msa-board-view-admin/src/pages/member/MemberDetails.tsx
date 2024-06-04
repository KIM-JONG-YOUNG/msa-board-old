import { useForm } from "react-hook-form";
import { useErrorBoundary } from "react-error-boundary";
import { ERROR_CODE, GENDER, GROUP, STATE } from "msa-board-view-common/src/constants/constants";
import { useEffect, useState } from "react";

import * as adminService from "../../services/adminService";
import { FetchErrorDetails, FetchErrorResponse } from "msa-board-view-common/src/utils/fetchUtils";
import useLoading from "msa-board-view-common/src/hooks/useLoading";
import { useParams, useNavigate } from "react-router-dom";

export type UserModifyFormInputs = {
	group: keyof typeof GROUP
	state: keyof typeof STATE
}

export type MemberInfos = {
	id: string
	username: string
	name: string
	gender: keyof typeof GENDER
	email: string
	group: keyof typeof GROUP
	state: keyof typeof STATE
}

export default function MemberDetails() {

	const {
		register,
		setError,
		setValue,
		handleSubmit
	} = useForm<UserModifyFormInputs>({ mode: "onBlur" });
	const { id } = useParams();
	const { showBoundary } = useErrorBoundary();
	const navigate = useNavigate();
	const [, setLoading] = useLoading();
	const [memberInfos, setMemberInfos] = useState<MemberInfos>();

	const groupRegister = register("group");
	const stateRegister = register("state");

	const onSubmit = (formData: UserModifyFormInputs) => {

		(!!id) && setLoading(true);
		(!!id) && adminService.modifyUser(id, {
			group: formData.group,
			state: formData.state
		}).catch((errorResponse: FetchErrorResponse) => {

			switch (errorResponse.errorCode) {

				case ERROR_CODE.INVALID_PARAMETER:
					(!!errorResponse.errorDetailsList) && errorResponse.errorDetailsList
						.forEach((error: FetchErrorDetails) => {
							(error.field === "state") && setError(error.field, { message: error.message });
							(error.field === "group") && setError(error.field, { message: error.message });
						});
					break;

				case ERROR_CODE.NOT_FOUND_MEMBER:
					alert(errorResponse.errorMessage);
					navigate(-1);
					break;

				default:
					throw errorResponse;
			}


		}).catch(showBoundary).finally(() => setLoading(false));

	};

	useEffect(() => {

		(!!memberInfos) && setValue("group", memberInfos.group);
		(!!memberInfos) && setValue("state", memberInfos.state);

	}, [memberInfos]);

	useEffect(() => {

		setLoading(true);

		(!!id) && setLoading(true);
		(!!id) && adminService.getMember(id).then(async (response: Response) => {

			const member = await response.json();

			setMemberInfos(member);

		}).catch((errorResponse: FetchErrorResponse) => {

			switch (errorResponse.errorCode) {

				case ERROR_CODE.NOT_FOUND_MEMBER:
					alert(errorResponse.errorMessage);
					navigate(-1);
					break;

				default:
					throw errorResponse;
			}

		}).catch(showBoundary).finally(() => setLoading(false));

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
							<input type="text" className="form-control" disabled value={memberInfos?.username || ""} />
						</div>
					</div>

					<div className="row">
						<div className="col-xl-4 mb-3">
							<label className="form-label subheading">Name</label>
							<input type="text" className="form-control" disabled value={memberInfos?.name || ""} />
						</div>
						<div className="col-xl-4 mb-3">
							<label className="form-label subheading">Gender</label>
							<input type="text" className="form-control" disabled value={
								(!!memberInfos?.gender) ? GENDER[memberInfos.gender] : ""
							} />
						</div>
					</div>

					<div className="row">
						<div className="col-xl-8 mb-3">
							<label className="form-label subheading">Email</label>
							<input type="text" className="form-control" disabled value={memberInfos?.email || ""} />
						</div>
					</div>

					<div className="row">
						<div className="col-xl-8 mb-3">
							<label className="form-label subheading">Group</label>
							{
								(memberInfos?.group === "USER") 
									? <select className="form-select" {...groupRegister}>
										{Object.entries(GROUP).map(([key, value]) => <option key={key} value={key}>{value}</option>)}
									</select>
									: <input type="text" className="form-control" disabled value={
										(!!memberInfos?.group) ? GROUP[memberInfos.group] : ""
									} />
							}
							
						</div>
					</div>

					<div className="row">
						<div className="col-xl-8 mb-3">
							<label className="form-label subheading">State</label>
							{
								(memberInfos?.group === "USER") 
									? <select className="form-select" {...stateRegister}>
										{Object.entries(STATE).map(([key, value]) => <option key={key} value={key}>{value}</option>)}
									</select>
									: <input type="text" className="form-control" disabled value={
										(!!memberInfos?.state) ? STATE[memberInfos.state] : ""	
									} />
							}
						</div>
					</div>

					{
						(memberInfos?.group === "USER") && 
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