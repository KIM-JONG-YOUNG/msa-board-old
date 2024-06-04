import { useForm } from "react-hook-form";
import { useErrorBoundary } from "react-error-boundary";
import { ERROR_CODE, GROUP, STATE } from "msa-board-view-common/src/constants/constants";
import { useCallback, useEffect, useState } from "react";

import * as adminService from "../../services/adminService";
import { FetchErrorDetails, FetchErrorResponse } from "msa-board-view-common/src/utils/fetchUtils";
import useLoading from "msa-board-view-common/src/hooks/useLoading";
import { useNavigate, useParams } from "react-router-dom";

import 'react-datepicker/dist/react-datepicker.css';

export type PostWriteFormInputs = {
	readonly title: string
	readonly content: string
}

export type PostWriter = {
	readonly id: string
	readonly username: string
	readonly name: string
	readonly group: keyof typeof GROUP
}

export type PostDetails = {
	readonly id: string
	readonly title: string
	readonly content: string
	readonly views: number
	readonly writer: PostWriter
	readonly createdDateTime: string
	readonly updatedDateTime: string
	readonly state: keyof typeof STATE
}


export default function PostWriteForm() {

	const navigate = useNavigate();
	const { id } = useParams();
	const { showBoundary } = useErrorBoundary();
	const { loadingCallback } = useLoading();

	const [post, setPost] = useState<PostDetails>();

	const {
		register,
		setError,
		setValue,
		handleSubmit,
		formState: { errors }
	} = useForm<PostWriteFormInputs>({ mode: "onBlur" });

	const titleRegister = register("title", {
		required: "제목은 비어있을 수 없습니다.",
		max: { value: 300, message: "제목은 300자를 넘을 수 없습니다." }
	});
	const contentRegister = register("content", {
		required: "내용은 비어있을 수 없습니다."
	});

	const onSubmitErrorHandler = useCallback((errorResponse: FetchErrorResponse) => {

		const errorCode = errorResponse.errorCode;
		const errorMessage = errorResponse.errorMessage;
		const errorDetailsList = errorResponse.errorDetailsList || [];

		switch (errorCode) {

			case ERROR_CODE.INVALID_PARAMETER:
				errorDetailsList.forEach((error: FetchErrorDetails) => {
					(error.field === "title") && setError(error.field, { message: error.message });
					(error.field === "content") && setError(error.field, { message: error.message });
				});
				break;

			case ERROR_CODE.NOT_FOUND_POST_WRITER:
				sessionStorage.initSessionInfo();
				alert("현재 회원은 존재하지 않는 회원입니다.");
				navigate("/account/login/form");
				break;

			case ERROR_CODE.NOT_FOUND_POST:
			case ERROR_CODE.NOT_ADMIN_GROUP_POST:
				alert(errorMessage);
				navigate("/post/list");
				break;

			default:
				throw errorResponse;
		}

	}, [setError, navigate]);

	const onSubmit = useCallback((formData: PostWriteFormInputs) => {

		loadingCallback(() => (!!id) 
			? adminService
				.modifyPost(id, {
					title: formData.title,
					content: formData.content
				})
				.catch(onSubmitErrorHandler)
				.catch(showBoundary)
			: adminService
				.writePost({
					title: formData.title,
					content: formData.content
				})
				.catch(onSubmitErrorHandler)
				.catch(showBoundary));		

	}, [id, loadingCallback, onSubmitErrorHandler, showBoundary]);

	useEffect(() => {

		(!!post) && setValue("title", post?.title);
		(!!post) && setValue("content", post?.content);

	}, [post, setValue]);

	useEffect(() => {

		(!!id) && loadingCallback(() => adminService
			.getPost(id)
			.then((response: Response) => response.json())
			.then((post: PostDetails) => setPost(post))
			.catch((errorResponse: FetchErrorResponse) => {

				switch (errorResponse.errorCode) {

					case ERROR_CODE.NOT_FOUND_POST:
					case ERROR_CODE.NOT_FOUND_POST_WRITER:
						alert(errorResponse.errorMessage);
						navigate("/post/list");
						break;

					default:
						throw errorResponse;
				}
			})
			.catch(showBoundary));

	}, []);

	return (
		// <!-- Post List -->
		<section className="resume-section">

			<form className="resume-section-content" onSubmit={handleSubmit(onSubmit)}>

				<h1 className="mb-4">
					Post
					<span className="text-primary">Write</span>
				</h1>

				<div className="row">
					<div className="col-xl-10 mb-3">
						<label className="form-label subheading">Title</label>
						<input type="text" className="form-control" placeholder="Title..." {...titleRegister} />
						{!!errors.title && <div className="text-danger">{errors.title.message}</div>}
					</div>
				</div>

				<div className="row">
					<div className="col-xl-10 mb-3">
						<label className="form-label subheading">Content</label>
						{!!errors.content && <div className="text-danger">{errors.content.message}</div>}
						<textarea className="form-control" placeholder="Content..." rows={15} {...contentRegister}></textarea>
					</div>
				</div>

				<div className="row">
					<div className="col-xl-10 mt-3">
						<button type="submit" className="btn btn-success w-100">Write</button>
					</div>
				</div>

			</form>

		</section>
	);
}