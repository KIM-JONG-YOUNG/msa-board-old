import { useController, useForm } from "react-hook-form";
import { useErrorBoundary } from "react-error-boundary";
import { DATE_TIME_FORMAT, ERROR_CODE, GENDER, GROUP, MEMBER_SORT, ORDER, STATE } from "msa-board-view-common/src/constants/constants";
import { useCallback, useEffect, useState } from "react";
import { Modal } from "react-bootstrap";
import usePaging from "msa-board-view-common/src/hooks/usePaging";

import * as adminService from "../../services/adminService";
import { FetchErrorDetails, FetchErrorResponse } from "msa-board-view-common/src/utils/fetchUtils";
import useLoading from "msa-board-view-common/src/hooks/useLoading";
import { useNavigate } from 'react-router-dom';
import DatePicker from 'react-datepicker';
import { format } from 'date-fns';

import 'react-datepicker/dist/react-datepicker.css';

export type MemberListSearchFormInputs = {
	readonly username?: string
	readonly name?: string
	readonly gender?: keyof typeof GENDER
	readonly email?: string
	readonly createdDateFrom?: Date
	readonly createdDateTo?: Date
	readonly updatedDateFrom?: Date
	readonly updatedDateTo?: Date
	readonly group?: keyof typeof GROUP
	readonly state?: keyof typeof STATE
	readonly offset?: number
	readonly limit?: number
	readonly sort?: keyof typeof MEMBER_SORT
	readonly order?: keyof typeof ORDER
}

export type MemberListInfo = {
	readonly id: string
	readonly username: string
	readonly name: string
	readonly gender: keyof typeof GENDER
	readonly email: string
	readonly group: keyof typeof GROUP
	readonly state: keyof typeof STATE
}
export type MemberList = MemberListInfo[];

export default function MemberList() {

	const navigate = useNavigate();
	const { showBoundary } = useErrorBoundary();
	const { loadingCallback } = useLoading();

	const [isModalOpen, setModalOpen] = useState(false);
	const [memberList, setMemberList] = useState<MemberList>([]);
	const {
		page,
		pageRows,
		pageGroupSize,
		startPage,
		endPage,
		totalPage,
		setPage,
		setTotalCount
	} = usePaging({
		initPageRows: 10,
		initPageGroupSize: 10
	});

	const {
		register,
		setError,
		getValues,
		handleSubmit,
		control
	} = useForm<MemberListSearchFormInputs>({ mode: "onBlur" });

	const usernameRegister = register("username");
	const nameRegister = register("name");
	const genderRegister = register("gender");
	const emailRegister = register("email");
	const groupRegister = register("group");
	const stateRegister = register("state");
	const sortRegister = register("sort");
	const orderRegister = register("order");

	const { field: createdDateFromField } = useController({
		control: control,
		name: "createdDateFrom"
	})
	const { field: createdDateToField } = useController({
		control: control,
		name: "createdDateTo"
	})
	const { field: updatedDateFromField } = useController({
		control: control,
		name: "updatedDateFrom"
	})
	const { field: updatedDateToField } = useController({
		control: control,
		name: "updatedDateTo"
	})

	const onSubmitSuccessHandler = useCallback(({ totalCount, list }: {
		totalCount: number
		list: MemberList
	}) => {

		(!!totalCount) ? setTotalCount(totalCount) : setTotalCount(0);
		(!!list) ? setMemberList(list) : setMemberList([]);

	}, [setTotalCount, setMemberList]);

	const onSubmitErrorHandler = useCallback((errorResponse: FetchErrorResponse) => {

		const errorCode = errorResponse.errorCode;
		const errorDetailsList = errorResponse.errorDetailsList || [];

		switch (errorCode) {

			case ERROR_CODE.INVALID_PARAMETER:
				errorDetailsList.forEach((error: FetchErrorDetails) => {
					(error.field === "username") && setError(error.field, { message: error.message });
					(error.field === "name") && setError(error.field, { message: error.message });
					(error.field === "gender") && setError(error.field, { message: error.message });
					(error.field === "email") && setError(error.field, { message: error.message });
					(error.field === "createdDateFrom") && setError(error.field, { message: error.message });
					(error.field === "createdDateTo") && setError(error.field, { message: error.message });
					(error.field === "updatedDateFrom") && setError(error.field, { message: error.message });
					(error.field === "updatedDateTo") && setError(error.field, { message: error.message });
					(error.field === "group") && setError(error.field, { message: error.message });
					(error.field === "state") && setError(error.field, { message: error.message });
					(error.field === "offset") && setError(error.field, { message: error.message });
					(error.field === "limit") && setError(error.field, { message: error.message });
					(error.field === "sort") && setError(error.field, { message: error.message });
					(error.field === "order") && setError(error.field, { message: error.message });
				});
				break;

			default:
				throw errorResponse;
		}

	}, [setError]);

	const onSubmit = useCallback((formData: MemberListSearchFormInputs) => {

		loadingCallback(() => adminService
			.searchMemberList({
				...formData,
				createdDateFrom: (!!formData.createdDateFrom) ? format(formData.createdDateFrom, DATE_TIME_FORMAT.DATE) : "",
				createdDateTo: (!!formData.createdDateTo) ? format(formData.createdDateTo, DATE_TIME_FORMAT.DATE) : "",
				updatedDateFrom: (!!formData.updatedDateFrom) ? format(formData.updatedDateFrom, DATE_TIME_FORMAT.DATE) : "",
				updatedDateTo: (!!formData.updatedDateTo) ? format(formData.updatedDateTo, DATE_TIME_FORMAT.DATE) : "",
				offset: (page > 0) ? (page - 1) * pageRows : 0,
				limit: (page > 0) ? page * pageRows : pageRows
			})
			.then((response: Response) => response.json())
			.then(onSubmitSuccessHandler)
			.catch(onSubmitErrorHandler)
			.catch(showBoundary));

	}, [page, pageRows, loadingCallback, onSubmitSuccessHandler, onSubmitErrorHandler, showBoundary]);

	const paging = useCallback((page: number) => {

		setPage(page);
		onSubmit(getValues());

	}, [setPage, onSubmit, getValues]);

	useEffect(() => {

		paging(1);

	}, []);

	return (
		// <!-- Member List -->
		<section className="resume-section">
			<div className="resume-section-content">

				<h1 className="mb-4">
					Member
					<span className="text-primary">List</span>
				</h1>

				<div className="row">
					<div className="col-xl-10 mb-3">

						<div className="mb-3 ">
							<button type="button" className="btn btn-secondary " onClick={() => setModalOpen(true)}>Search</button>
						</div>

						<table className="table table-hover">
							<thead className="bg-primary text-white">
								<tr>
									<th scope="col">#</th>
									<th scope="col">Username</th>
									<th scope="col">Name</th>
									<th scope="col">Gender</th>
									<th scope="col">Group</th>
									<th scope="col">State</th>
								</tr>
							</thead>
							<tbody>
								{
									(!!memberList) && memberList.map((member, i) =>
										<tr key={member.id} onClick={() => navigate(`/member/${member.id}/details`)}>
											<th scope="row">{i + 1 + (getValues("offset") || 0)}</th>
											<td>{member.username}</td>
											<td>{member.name}</td>
											<td>{member.gender}</td>
											<td>{member.group}</td>
											<td>{member.state}</td>
										</tr>
									)
								}
							</tbody>
						</table>

						<ul className="pagination justify-content-center">
							{
								(startPage > pageGroupSize) &&
								<li className="page-item" onClick={() => paging(startPage - 1)} >
									<a className="page-link" href="#!" aria-label="Previous">
										<span aria-hidden="true">&laquo;</span>
									</a>
								</li>
							}
							{
								(startPage > 0 && endPage > 0) &&
								Array.from({ length: endPage - startPage + 1 }, (_, i) => startPage + i)
									.map(i => (page === i)
										? <li key={i} className="page-item active"><a className="page-link" href="#!">{i}</a></li>
										: <li key={i} className="page-item" onClick={() => paging(i)}><a className="page-link" href="#!">{i}</a></li>)
							}
							{
								(totalPage > endPage) &&
								<li className="page-item" onClick={() => paging(endPage + 1)} >
									<a className="page-link" href="#!" aria-label="Next">
										<span aria-hidden="true">&raquo;</span>
									</a>
								</li>
							}
						</ul>

					</div>
				</div>

				{/* <!-- Modal --> */}
				<Modal show={isModalOpen} onHide={() => setModalOpen(false)} dialogClassName="modal-dialog modal-lg modal-dialog-scrollable" >

					<form onSubmit={handleSubmit(onSubmit)}>

						<div className="modal-header">
							<h5 className="modal-title">Member Search Form</h5>
							<button type="button" className="btn-close" onClick={() => setModalOpen(false)}></button>
						</div>

						<div className="modal-body">
							<div className="container-fluid p-0">

								<div className="row">
									<div className="col-12 mb-3">
										<label className="form-label subheading">Username</label>
										<input type="text" className="form-control" placeholder="Username..." {...usernameRegister} />
									</div>
								</div>

								<div className="row">
									<div className="col-lg-6 mb-3">
										<label className="form-label subheading">Name</label>
										<input type="text" className="form-control" placeholder="Name..." {...nameRegister} />
									</div>
									<div className="col-lg-6 mb-3">
										<label className="form-label subheading">Gender</label>
										<select className="form-select" {...genderRegister}>
											<option value="">Choose...</option>
											{Object.entries(GENDER).map(([key, value]) => <option key={key} value={key}>{value}</option>)}
										</select>
									</div>
								</div>

								<div className="row">
									<div className="col-12 mb-3">
										<label className="form-label subheading">Email</label>
										<input type="text" className="form-control" placeholder="Email..." {...emailRegister} />
									</div>
								</div>

								<div className="row">
									<div className="col-lg-6 mb-3">
										<label className="form-label subheading">Group</label>
										<select className="form-select" {...groupRegister}>
											<option value="">Choose...</option>
											{Object.entries(GROUP).map(([key, value]) => <option key={key} value={key}>{value}</option>)}
										</select>
									</div>
									<div className="col-lg-6 mb-3">
										<label className="form-label subheading">State</label>
										<select className="form-select" {...stateRegister}>
											<option value="">Choose...</option>
											{Object.entries(STATE).map(([key, value]) => <option key={key} value={key}>{value}</option>)}
										</select>
									</div>
								</div>

								<div className="row">
									<div className="col-12 mb-3">
										<label className="form-label subheading">Created Date</label>
										<button className="btn btn-sm btn-primary text-white float-end" onClick={() => {
											createdDateFromField.onChange(undefined);
											createdDateToField.onChange(undefined);
										}}>RESET</button>
										<div className="input-group">
											<DatePicker
												wrapperClassName="form-control"
												customInput={<input type="text" className="form-control" />}
												dateFormat={DATE_TIME_FORMAT.DATE}
												selected={createdDateFromField.value}
												maxDate={createdDateToField.value}
												onChange={(date) => createdDateFromField.onChange(date)}
												disabledKeyboardNavigation={true}
											/>
											<span className="input-group-text">~</span>
											<DatePicker
												wrapperClassName="form-control"
												customInput={<input type="text" className="form-control" readOnly />}
												dateFormat={DATE_TIME_FORMAT.DATE}
												minDate={createdDateFromField.value}
												selected={createdDateToField.value}
												onChange={(date) => createdDateToField.onChange(date)}
											/>
										</div>
									</div>
								</div>

								<div className="row">
									<div className="col-12 mb-3">
										<label className="form-label subheading">Updated Date</label>
										<button className="btn btn-sm btn-primary text-white float-end" onClick={() => {
											updatedDateFromField.onChange(undefined);
											updatedDateToField.onChange(undefined);
										}}>RESET</button>
										<div className="input-group">
											<DatePicker
												wrapperClassName="form-control"
												customInput={<input type="text" className="form-control" readOnly />}
												dateFormat={DATE_TIME_FORMAT.DATE}
												maxDate={updatedDateToField.value}
												selected={updatedDateFromField.value}
												onChange={(date) => updatedDateFromField.onChange(date)}
											/>
											<span className="input-group-text">~</span>
											<DatePicker
												wrapperClassName="form-control"
												customInput={<input type="text" className="form-control" readOnly />}
												dateFormat={DATE_TIME_FORMAT.DATE}
												minDate={updatedDateFromField.value}
												selected={updatedDateToField.value}
												onChange={(date) => updatedDateToField.onChange(date)}
											/>
										</div>
									</div>
								</div>

								<div className="row">
									<div className="col-12 mb-3">
										<label className="form-label subheading">Sort By</label>
										<div className="input-group">
											<select className="form-select" {...sortRegister}>
												<option value="">Choose...</option>
												{Object.entries(MEMBER_SORT).map(([key, value]) => <option key={key} value={key}>{value}</option>)}
											</select>
											<span className="input-group-text">By</span>
											<select className="form-select" {...orderRegister}>
												<option value="">Choose...</option>
												{Object.entries(ORDER).map(([key, value]) => <option key={key} value={key}>{value}</option>)}
											</select>
										</div>
									</div>
								</div>

							</div>
						</div>

						<div className="modal-footer ">
							<button type="submit" className="btn btn-secondary w-100" >Search</button>
						</div>

					</form>

				</Modal>

			</div>
		</section>
	);
}