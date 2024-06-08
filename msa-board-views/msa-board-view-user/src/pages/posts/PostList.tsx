import { format } from 'date-fns';
import { STATE, POST_SORT, ORDER, GROUP, DATE_TIME_FORMAT, ERROR_CODE } from 'msa-board-view-common/src/constants/constants';
import useFetchData, { FetchErrorResponse, FetchErrorDetails } from 'msa-board-view-common/src/hooks/useFetchData';
import usePaging from 'msa-board-view-common/src/hooks/usePaging';
import constantsUtils from 'msa-board-view-common/src/utils/constantsUtils';
import sessionUtils from 'msa-board-view-common/src/utils/sessionUtils';
import { useState, useCallback, useEffect } from 'react';
import { Modal } from 'react-bootstrap';
import ReactDatePicker from 'react-datepicker';
import { useErrorBoundary } from 'react-error-boundary';
import { useForm, useController } from 'react-hook-form';
import { useNavigate } from 'react-router-dom';

import 'react-datepicker/dist/react-datepicker.css';
import Pagination from 'msa-board-view-common/src/components/Pagination';

export type PostListSearchFormInputs = {
	title: string
	content: string
	writerUsername: string
	createdDateFrom: Date
	createdDateTo: Date
	updatedDateFrom: Date
	updatedDateTo: Date
	offset: number
	limit: number
	sort: keyof typeof POST_SORT
	order: keyof typeof ORDER
};

export type PostWriter = {
	id: string
	username: string
	name: string
	group: keyof typeof GROUP
};

export type PostInfo = {
	id: string
	title: string
	views: number
	writer: PostWriter
	createdDateTime: string
	updatedDateTime: string
}

export default function PostList() {

	const endpointURL = process.env.REACT_APP_ENDPOINT_URL;

	const navigate = useNavigate();
	const { showBoundary } = useErrorBoundary();
	const { fetchData } = useFetchData();
	const [ isModalOpen, setModalOpen ] = useState(false);
	const [postList, setPostList] = useState<PostInfo[]>([]);
	const {
		register,
		setError,
		setValue,
		getValues,
		handleSubmit,
		control,
		formState: { errors }
	} = useForm<PostListSearchFormInputs>({ mode: "onBlur" });
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
	const sortRegister = register("sort");
	const orderRegister = register("order");

	const { field: createdDateFromField } = useController({ control: control, name: "createdDateFrom" });
	const { field: createdDateToField } = useController({ control: control, name: "createdDateTo" });
	const { field: updatedDateFromField } = useController({ control: control, name: "updatedDateFrom" });
	const { field: updatedDateToField } = useController({ control: control, name: "updatedDateTo" });

	const search = useCallback(() => {

		const formData = getValues();
		const urlParams = new URLSearchParams();

		Object.entries(formData)
			.filter(([, value]) => (!!value))
			.forEach(([key, value]) => {
				switch (key) {
					case "createdDateFrom":
					case "createdDateTo":
					case "updatedDateFrom":
					case "updatedDateTo":
						urlParams.append(key, format(value, DATE_TIME_FORMAT.DATE));
						break;
					default:
						urlParams.append(key, String(value));
						break;
				}
			});

		sessionUtils.setQuery("postQuery", urlParams);

		fetchData({
			url: `${endpointURL}/apis/users/posts`,
			refreshURL: `${endpointURL}/apis/users/refresh`,
			method: "GET",
			headers: { "Accept": "application/json" },
			query: urlParams
		}).then(async (response: Response) => {

			const listResponse = await response.json();
			const totalCount = listResponse?.totalCount || 0;
			const list = listResponse?.list || [];

			setTotalCount(totalCount);
			setPostList(list);

		}).catch((errorResponse: FetchErrorResponse) => {

			const errorCode = errorResponse.errorCode;
			const errorDetailsList = errorResponse.errorDetailsList || [];
			  
			switch (errorCode) {
				case ERROR_CODE.INVALID_PARAMETER:
					errorDetailsList.forEach((error: FetchErrorDetails) => {
						(error.field === "title") && setError(error.field, { message: error.message });
						(error.field === "content") && setError(error.field, { message: error.message });
						(error.field === "writerUsername") && setError(error.field, { message: error.message });
						(error.field === "createdDateFrom") && setError(error.field, { message: error.message });
						(error.field === "createdDateTo") && setError(error.field, { message: error.message });
						(error.field === "updatedDateFrom") && setError(error.field, { message: error.message });
						(error.field === "updatedDateTo") && setError(error.field, { message: error.message });
						(error.field === "offset") && setError(error.field, { message: error.message });
						(error.field === "limit") && setError(error.field, { message: error.message });
						(error.field === "sort") && setError(error.field, { message: error.message });
						(error.field === "order") && setError(error.field, { message: error.message });
					});
					break;
				default:
					showBoundary(errorResponse);
					break;
			}	
		}).finally(() => setModalOpen(false));

	}, [getValues, fetchData, setTotalCount, setPostList, setError, showBoundary, setModalOpen]);

	const paging = useCallback((page: number) => {

		setPage(page);
		setValue("offset", (page > 1) ? (page - 1) * pageRows : 0);
		setValue("limit", pageRows);
		search();

	}, [setPage, setValue, search]);

	useEffect(() => {

		const urlParam = new URLSearchParams(sessionStorage.getItem("postQuery") || undefined);
		
		urlParam.forEach((value, key) => {
			(key === "title") && setValue(key, value);
			(key === "content") && setValue(key, value);
			(key === "writerUsername") && setValue(key, value);
			(key === "createdDateFrom" && constantsUtils.isDateString(value)) && setValue(key, new Date(value));
			(key === "createdDateTo" && constantsUtils.isDateString(value)) && setValue(key, new Date(value));
			(key === "updatedDateFrom" && constantsUtils.isDateString(value)) && setValue(key, new Date(value));
			(key === "updatedDateTo" && constantsUtils.isDateString(value)) && setValue(key, new Date(value));
			(key === "sort" && constantsUtils.isPostSortKey(value)) && setValue(key, value);
			(key === "order" && constantsUtils.isOrderKey(value)) && setValue(key, value);
			(key === "offset" && !!Number(value)) && setValue(key, Number(value));
			(key === "limit" && !!Number(value)) && setValue(key, Number(value));
		});

		const offset = Number(urlParam.get("offset") || 0);
		paging((offset > 0) ? Math.floor(offset / pageRows) + 1 : 1);

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
							<colgroup>
								<col width="10%" />
								<col width="%" />
								<col width="15%" />
								<col width="10%" />
								<col width="15%" />
								<col width="15%" />
							</colgroup>
							<thead className="bg-primary text-white text-center">
								<tr>
									<th scope="col">#</th>
									<th scope="col">Title</th>
									<th scope="col">Writer</th>
									<th scope="col">Views</th>
									<th scope="col">Created Date</th>
									<th scope="col">Updated Date</th>
								</tr>
							</thead>
							<tbody>
								{
									(!!postList) && postList.map((post, i) =>
										<tr key={post.id} onClick={() => navigate(`/post/${post.id}/details`)}>
											<th className='text-center' scope="row">{i + 1 + (getValues("offset") || 0)}</th>
											<td className='text-start'>{post.title}</td>
											<td className='text-center'>{post.writer.username}</td>
											<td className='text-center'>{post.views}</td>
											<td className='text-end'>{post?.createdDateTime.substring(0, 10)}</td>
											<td className='text-end'>{post?.updatedDateTime.substring(0, 10)}</td>
										</tr>
									)
								}
							</tbody>
						</table>

						<Pagination 
							page={page}
							startPage={startPage}
							endPage={endPage}
							totalPage={totalPage}
							pageGroupSize={pageGroupSize}
							pagingFn={paging} />

					</div>
				</div>

				{/* <!-- Modal --> */}
				<Modal show={isModalOpen} onHide={() => setModalOpen(false)} dialogClassName="modal-dialog modal-lg modal-dialog-scrollable" >

					<form onSubmit={handleSubmit(() => paging(1))}>

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
										{!!errors.title && <div className="text-danger">{errors.title.message}</div>}
									</div>
								</div>

								<div className="row">
									<div className="col-12 mb-3">
										<label className="form-label subheading">Content</label>
										<input type="text" className="form-control" placeholder="Content..." {...contentRegister} />
										{!!errors.content && <div className="text-danger">{errors.content.message}</div>}
									</div>
								</div>

								<div className="row">
									<div className="col-12 mb-3">
										<label className="form-label subheading">Writer</label>
										<input type="text" className="form-control" placeholder="Writer Username..." {...writerUsernameRegister} />
										{!!errors.writerUsername && <div className="text-danger">{errors.writerUsername.message}</div>}
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
											<ReactDatePicker
												wrapperClassName="form-control"
												customInput={<input type="text" className="form-control" />}
												dateFormat={DATE_TIME_FORMAT.DATE}
												selected={createdDateFromField.value}
												maxDate={createdDateToField.value}
												onChange={(date) => createdDateFromField.onChange(date)}
												disabledKeyboardNavigation={true}
											/>
											<span className="input-group-text">~</span>
											<ReactDatePicker
												wrapperClassName="form-control"
												customInput={<input type="text" className="form-control" />}
												dateFormat={DATE_TIME_FORMAT.DATE}
												minDate={createdDateFromField.value}
												selected={createdDateToField.value}
												onChange={(date) => createdDateToField.onChange(date)}
											/>
										</div>
										{!!errors.createdDateFrom && <div className="text-danger">{errors.createdDateFrom.message}</div>}
										{!!errors.createdDateTo && <div className="text-danger">{errors.createdDateTo.message}</div>}
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
											<ReactDatePicker
												wrapperClassName="form-control"
												customInput={<input type="text" className="form-control" />}
												dateFormat={DATE_TIME_FORMAT.DATE}
												maxDate={updatedDateToField.value}
												selected={updatedDateFromField.value}
												onChange={(date) => updatedDateFromField.onChange(date)}
											/>
											<span className="input-group-text">~</span>
											<ReactDatePicker
												wrapperClassName="form-control"
												customInput={<input type="text" className="form-control" />}
												dateFormat={DATE_TIME_FORMAT.DATE}
												minDate={updatedDateFromField.value}
												selected={updatedDateToField.value}
												onChange={(date) => updatedDateToField.onChange(date)}
											/>
										</div>
										{!!errors.updatedDateFrom && <div className="text-danger">{errors.updatedDateFrom.message}</div>}
										{!!errors.updatedDateTo && <div className="text-danger">{errors.updatedDateTo.message}</div>}
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
										{!!errors.sort && <div className="text-danger">{errors.sort.message}</div>}
										{!!errors.order && <div className="text-danger">{errors.order.message}</div>}
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
};