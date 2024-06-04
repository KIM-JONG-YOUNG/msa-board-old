import { Control, Controller, useController, useForm } from "react-hook-form";
import { useErrorBoundary } from "react-error-boundary";
import { DATE_TIME_FORMAT, ERROR_CODE, GENDER, GROUP, MEMBER_SORT, ORDER, POST_SORT, STATE } from "msa-board-view-common/src/constants/constants";
import { useEffect, useRef, useState } from "react";
import { Modal } from "react-bootstrap";
import usePaging from "msa-board-view-common/src/hooks/usePaging";

import * as adminService from "../../services/adminService";
import { FetchErrorDetails, FetchErrorResponse } from "msa-board-view-common/src/utils/fetchUtils";
import useLoading from "msa-board-view-common/src/hooks/useLoading";
import { useNavigate } from 'react-router-dom';
import DatePicker from 'react-datepicker';
import { format } from 'date-fns';

import 'react-datepicker/dist/react-datepicker.css';
import { title } from "process";

export type PostListSearchFormInputs = {
	readonly title?: string
	readonly content?: string
	readonly writerUsername?: string
	readonly createdDateFrom?: Date
	readonly createdDateTo?: Date
	readonly updatedDateFrom?: Date
	readonly updatedDateTo?: Date
	readonly state?: keyof typeof STATE
	readonly offset?: number
	readonly limit?: number
	readonly sort?: keyof typeof POST_SORT
	readonly order?: keyof typeof ORDER
}

export type PostWriterInfos = {
	readonly id: string
	readonly username: string
	readonly name: string
	readonly group: keyof typeof GROUP
}

export type PostInfos = {
	readonly id: string
	readonly title: string
	readonly views: number
	readonly writer: PostWriterInfos
	readonly createdDateTime: string
	readonly updatedDateTime: string
	readonly state: keyof typeof STATE
}

export default function PostList() {

	const {
		register,
		setError,
		getValues,
		formState: { errors },
		control
	} = useForm<PostListSearchFormInputs>({ mode: "onBlur" });
	const { showBoundary } = useErrorBoundary();
	const [, setLoading] = useLoading();
	const navigate = useNavigate();
	const [isModalOpen, setModalOpen] = useState(false);
	const [postList, setPostList] = useState<PostInfos[]>([]);
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

	const titleRegister = register("title");
	const contentRegister = register("content");
	const writerUsernameRegister = register("writerUsername");
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


	const search = () => {

		const formData = getValues();

		setLoading(true);

		adminService.searchPostList({
			...formData,
			createdDateFrom: (!!formData.createdDateFrom) ? format(formData.createdDateFrom, DATE_TIME_FORMAT.DATE) : "",
			createdDateTo: (!!formData.createdDateTo) ? format(formData.createdDateTo, DATE_TIME_FORMAT.DATE) : "",
			updatedDateFrom: (!!formData.updatedDateFrom) ? format(formData.updatedDateFrom, DATE_TIME_FORMAT.DATE) : "",
			updatedDateTo: (!!formData.updatedDateTo) ? format(formData.updatedDateTo, DATE_TIME_FORMAT.DATE) : "",
			offset: (page > 0) ? (page - 1) * pageRows : 0,
			limit: (page > 0) ? page * pageRows : pageRows
		}).then(async (response: Response) => {

			const responseBody = await response.json();
			const totalCount = responseBody?.totalCount;
			const list = responseBody?.list;

			(!!totalCount) ? setTotalCount(totalCount) : setTotalCount(0);
			(!!list) ? setPostList(list) : setPostList([]);

		}).catch((errorResponse: FetchErrorResponse) => {

			switch (errorResponse.errorCode) {

				case ERROR_CODE.INVALID_PARAMETER:
					(!!errorResponse.errorDetailsList) && errorResponse.errorDetailsList
						.forEach((error: FetchErrorDetails) => {
							(error.field === "title") && setError(error.field, { message: error.message });
							(error.field === "content") && setError(error.field, { message: error.message });
							(error.field === "writerUsername") && setError(error.field, { message: error.message });
							(error.field === "createdDateFrom") && setError(error.field, { message: error.message });
							(error.field === "createdDateTo") && setError(error.field, { message: error.message });
							(error.field === "updatedDateFrom") && setError(error.field, { message: error.message });
							(error.field === "updatedDateTo") && setError(error.field, { message: error.message });
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

		}).catch(showBoundary).finally(() => setLoading(false));
	};

	useEffect(() => {
		setPage(1);
		search();
	}, []);

	return (
		// <!-- Post List -->
		<section className="resume-section">
			<div className="resume-section-content">

				<h1 className="mb-4">
					Post
					<span className="text-primary">List</span>
				</h1>

				<div className="row">
					<div className="col-xl-10 mb-3">

						<div className="mb-3 ">
							<button type="button" className="btn btn-secondary " onClick={() => setModalOpen(true)}>Search</button>
							<button type="button" className="btn btn-success float-end" onClick={() => navigate("/post/write/form")}>Write</button>
						</div>

						<table className="table table-hover">
							<thead className="bg-primary text-white">
								<tr>
									<th scope="col">#</th>
									<th scope="col">Title</th>
									<th scope="col">Writer</th>
									<th scope="col">Views</th>
									<th scope="col">Created Date</th>
									<th scope="col">State</th>
								</tr>
							</thead>
							<tbody>
								{
									(!!postList) && postList.map((post, i) =>
										<tr key={post.id} onClick={() => navigate(`/post/${post.id}/details`)}>
											<th scope="row">{i + 1 + (getValues("offset") || 0)}</th>
											<td>{post.title}</td>
											<td>{post.writer.username}</td>
											<td>{post.views}</td>
											<td>{post.createdDateTime}</td>
											<td>{post.state}</td>
										</tr>
									)
								}
							</tbody>
						</table>

						<ul className="pagination justify-content-center">
							{
								(startPage > pageGroupSize) &&
								<li className="page-item" onClick={() => {
									setPage(startPage - 1);
									search();
								}}>
									<a className="page-link" href="#!" aria-label="Previous">
										<span aria-hidden="true">&laquo;</span>
									</a>
								</li>
							}
							{
								(startPage > 0 && endPage > 0) &&
								Array.from({ length: endPage - startPage + 1 }, (_, i) => startPage + i)
									.map(i => (page === i)
										? <li key={i} className="page-item active"><a className="page-link" href="#">{i}</a></li>
										: <li key={i} className="page-item" onClick={() => {
											setPage(i);
											search();
										}}><a className="page-link" href="#">{i}</a></li>)
							}
							{
								(totalPage > endPage) &&
								<li className="page-item" onClick={() => {
									setPage(endPage + 1);
									search();
								}}>
									<a className="page-link" href="#" aria-label="Next">
										<span aria-hidden="true">&raquo;</span>
									</a>
								</li>
							}
						</ul>

					</div>
				</div>

				{/* <!-- Modal --> */}
				<Modal show={isModalOpen} onHide={() => setModalOpen(false)} dialogClassName="modal-dialog modal-lg modal-dialog-scrollable" >

					<div className="modal-header">
						<h5 className="modal-title">Post Search Form</h5>
						<button type="button" className="btn-close" onClick={() => setModalOpen(false)}></button>
					</div>

					<div className="modal-body">
						<div className="container-fluid p-0">

							<div className="row">
								<div className="col-12 mb-3">
									<label className="form-label subheading">Title</label>
									<input type="text" className="form-control" placeholder="Title..." {...titleRegister} />
								</div>
							</div>

							<div className="row">
								<div className="col-12 mb-3">
									<label className="form-label subheading">Content</label>
									<input type="text" className="form-control" placeholder="Content..." {...contentRegister} />
								</div>
							</div>

							<div className="row">
								<div className="col-12 mb-3">
									<label className="form-label subheading">Writer</label>
									<input type="text" className="form-control" placeholder="Writer Username..." {...writerUsernameRegister} />
								</div>
							</div>

							<div className="row">
								<div className="col-12 mb-3">
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
											{Object.entries(POST_SORT).map(([key, value]) => <option key={key} value={key}>{value}</option>)}
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
						<button type="button" className="btn btn-secondary w-100" onClick={search}>Search</button>
					</div>

				</Modal>

			</div>
		</section>
	);
}