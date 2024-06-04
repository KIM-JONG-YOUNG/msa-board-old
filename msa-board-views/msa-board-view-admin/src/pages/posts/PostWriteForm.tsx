import { useForm } from "react-hook-form";
import { useErrorBoundary } from "react-error-boundary";
import { ERROR_CODE, GROUP, STATE } from "msa-board-view-common/src/constants/constants";
import { useEffect, useState } from "react";

import * as adminService from "../../services/adminService";
import { FetchErrorDetails, FetchErrorResponse } from "msa-board-view-common/src/utils/fetchUtils";
import useLoading from "msa-board-view-common/src/hooks/useLoading";
import { useNavigate, useParams } from "react-router-dom";

import 'react-datepicker/dist/react-datepicker.css';

export type PostWriteFormInputs = {
	readonly title: string
	readonly content: string
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
	readonly content: string
	readonly views: number
	readonly writer: PostWriterInfos
	readonly createdDateTime: string
	readonly updatedDateTime: string
	readonly state: keyof typeof STATE
}


export default function PostWriteForm() {

	const { id } = useParams();
	const {
		register,
		setError,
		setValue,
		handleSubmit,
		formState: { errors }
	} = useForm<PostWriteFormInputs>({ mode: "onBlur" });
	const { showBoundary } = useErrorBoundary();
	const [, setLoading] = useLoading();
	const navigate = useNavigate();
	const [post, setPost] = useState<PostInfos>();

	const titleRegister = register("title", {
		required: "제목은 비어있을 수 없습니다.",
		max: { value: 300, message: "제목은 300자를 넘을 수 없습니다." }
	});
	const contentRegister = register("content", {
		required: "내용은 비어있을 수 없습니다."
	});

	const onSubmit = (formData: PostWriteFormInputs) => {

		setLoading(true);

		if (!!id && !!post) {

			adminService.modifyPost(id, {
				title: formData.title,
				content: formData.content
			}).catch((errorResponse: FetchErrorResponse) => {

				switch (errorResponse.errorCode) {

					case ERROR_CODE.NOT_FOUND_POST:
					case ERROR_CODE.NOT_FOUND_POST_WRITER:
					case ERROR_CODE.NOT_ADMIN_GROUP_POST:
						alert(errorResponse.errorMessage);
						navigate("/post/list");
						break;

					case ERROR_CODE.INVALID_PARAMETER:
						(!!errorResponse.errorDetailsList) && errorResponse.errorDetailsList
							.forEach((error: FetchErrorDetails) => {
								(error.field === "title") && setError(error.field, { message: error.message });
								(error.field === "content") && setError(error.field, { message: error.message });
							});
						break;

					default:
						throw errorResponse;
				}

			}).catch(showBoundary).finally(() => setLoading(false));
	
		} else {
			adminService.writePost({
				title: formData.title,
				content: formData.content
			}).catch((errorResponse: FetchErrorResponse) => {

				switch (errorResponse.errorCode) {

					case ERROR_CODE.NOT_FOUND_POST:
					case ERROR_CODE.NOT_FOUND_POST_WRITER:
						alert(errorResponse.errorMessage);
						navigate("/post/list");
						break;

					case ERROR_CODE.INVALID_PARAMETER:
						(!!errorResponse.errorDetailsList) && errorResponse.errorDetailsList
							.forEach((error: FetchErrorDetails) => {
								(error.field === "title") && setError(error.field, { message: error.message });
								(error.field === "content") && setError(error.field, { message: error.message });
							});
						break;

					default:
						throw errorResponse;
				}

			}).catch(showBoundary).finally(() => setLoading(false));
		}

	};

	useEffect(() => {
		(!!post) && setValue("title", post?.title);
		(!!post) && setValue("content", post?.content);
	}, [post]);

	useEffect(() => {

		(!!id) && adminService.getPost(id)
			.then((response: Response) => response.json())
			.then((post: PostInfos) => setPost(post))
			.then(() => {
				if (post?.writer?.group === "ADMIN") {
					alert("관리자 게시글이 아닙니다.");
					navigate(`/post/${id}/details`);
				}
			})
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
			.catch(showBoundary).finally(() => setLoading(false));
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