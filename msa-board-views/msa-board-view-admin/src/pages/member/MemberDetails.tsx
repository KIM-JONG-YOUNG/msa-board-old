import { GROUP, STATE, GENDER, ERROR_CODE } from "msa-board-view-common/src/constants/constants"
import useFetchData, { FetchErrorResponse, FetchErrorDetails } from "msa-board-view-common/src/hooks/useFetchData"
import { useState, useCallback, useEffect } from "react"
import { useErrorBoundary } from "react-error-boundary"
import { useForm } from "react-hook-form"
import { useNavigate, useParams } from "react-router-dom"

export type UserModifyFormInputs = {
	group: keyof typeof GROUP
	state: keyof typeof STATE
};

export type MemberDetails = {
	id: string
	username: string
	name: string
	gender: keyof typeof GENDER
	email: string
	group: keyof typeof GROUP
	createdDateTime: string
	updatedDateTime: string
	state: keyof typeof STATE
};

export default function MemberDetails() {

	const endpointURL = process.env.REACT_APP_ENDPOINT_URL;

	const navigate = useNavigate();
	const { id } = useParams();
	const { showBoundary } = useErrorBoundary();
	const { fetchData } = useFetchData();
	const [ member, setMember ] = useState<MemberDetails>();
	const {
		register,
		setError,
		setValue,
		getValues,
		handleSubmit,
		formState: { errors }
	} = useForm<UserModifyFormInputs>({ mode: "onBlur" });

	const groupRegister = register("group", { required: "그룹은 비어있을 수 없습니다." });
	const stateRegister = register("state", { required: "상태는 비어있을 수 없습니다." });

	const modify = useCallback(() => {

		const formData = getValues();

		fetchData({
			url: `${endpointURL}/apis/admins/users/${id || "empty"}`,
			refreshURL: `${endpointURL}/apis/admins/refresh`,
			method: "PATCH",
			headers: { "Content-Type": "application/json" },
			body: JSON.stringify(formData)
		}).then((response: Response) => {
		
			alert("회원 정보가 수정되었습니다.");

		}).catch((errorResponse: FetchErrorResponse) => {
		
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
				case ERROR_CODE.NOT_USER_GROUP_MEMBER:
					alert(errorMessage);
					navigate("/member/list");
					break;
				default:
					showBoundary(errorResponse);
					break;
			}		
		});

	}, [id, getValues, fetchData, navigate, setError, showBoundary]);

	useEffect(() => {

		(!!member) && setValue("group", member?.group);
		(!!member) && setValue("state", member?.state);

	}, [member, setValue]);

	useEffect(() => {

		fetchData({
			url: `${endpointURL}/apis/admins/members/${id || "empty"}`,
			refreshURL: `${endpointURL}/apis/admins/refresh`,
			method: "GET",
			headers: { "Accept": "application/json" }
		}).then(async (response: Response) => {

			setMember(await response.json());

		}).catch((errorResponse: FetchErrorResponse) => {

			const errorCode = errorResponse.errorCode;
			const errorMessage = errorResponse.errorMessage;

			switch (errorCode) {
				case ERROR_CODE.NOT_FOUND_MEMBER:
					alert(errorMessage);
					navigate("/member/list");
					break;
				default:
					showBoundary(errorResponse);
					break;
			}
		});

	}, []);

	return (
		// <!-- User Modify Form -->
		<section className="resume-section">
			<div className="resume-section-content">

				<h1 className="mb-4">
					Member
					<span className="text-primary">Details</span>
				</h1>

				<form onSubmit={handleSubmit(() => modify())}>

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
									? <>
										<select className="form-select" {...groupRegister}>
											{Object.entries(GROUP).map(([key, value]) => <option key={key} value={key}>{value}</option>)}
										</select>
										{!!errors.group && <div className="text-danger">{errors.group.message}</div>}
									</>
									: <input type="text" className="form-control" disabled value={(!!member?.group) ? GROUP[member.group] : ""} />
							}

						</div>
					</div>

					<div className="row">
						<div className="col-xl-8 mb-3">
							<label className="form-label subheading">State</label>
							{
								(member?.group === "USER")
									? <>
										<select className="form-select" {...stateRegister}>
											{Object.entries(STATE).map(([key, value]) => <option key={key} value={key}>{value}</option>)}
										</select>
										{!!errors.state && <div className="text-danger">{errors.state.message}</div>}
									</>
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
};