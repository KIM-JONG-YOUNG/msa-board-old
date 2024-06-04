import { useForm } from "react-hook-form";
import { useNavigate, useParams } from "react-router-dom";
import { useErrorBoundary } from "react-error-boundary";
import { ERROR_CODE, GROUP, STATE } from "msa-board-view-common/src/constants/constants";
import { useCallback, useEffect, useState } from "react";

import { FetchErrorResponse } from "msa-board-view-common/src/utils/fetchUtils";
import useLoading from "msa-board-view-common/src/hooks/useLoading";

import * as adminService from "../../services/adminService";

import 'react-datepicker/dist/react-datepicker.css';

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

export default function PostDetails() {

	const navigate = useNavigate();
	const { id } = useParams();
	const { showBoundary } = useErrorBoundary();
	const { loadingCallback } = useLoading();

	const [post, setPost] = useState<PostDetails>();
	
	const onChangeStateSuccessHandler = useCallback((state: keyof typeof STATE) => {

		setPost((prev) => (!!prev) ? {
			...prev,
			state: state
		} : prev);
		
	}, [setPost]);

	const onChangeStateErrorHandler = useCallback((errorResponse: FetchErrorResponse) => {

		const errorCode = errorResponse.errorCode;
		const errorMessage = errorResponse.errorMessage;

		switch (errorCode) {

			case ERROR_CODE.NOT_FOUND_POST:
			case ERROR_CODE.NOT_FOUND_POST_WRITER:
				alert(errorMessage);
				navigate("/member/list");
				break;

			default:
				throw errorResponse;
		}

	}, [navigate]);

	const onChangeState = useCallback((state: keyof typeof STATE) => {

		if (!!id) {

			loadingCallback(() => adminService
				.modifyPostState(id, state)
				.then(() => state)
				.then(onChangeStateSuccessHandler)
				.catch(onChangeStateErrorHandler)
				.catch(showBoundary));

		} else {

			alert("게시글 ID가 존재하지 않습니다.");
			navigate("/post/list");
		}
		
	}, [id, navigate, loadingCallback, onChangeStateSuccessHandler, onChangeStateErrorHandler, showBoundary]);

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
		// <!-- Post Details -->
		<section className="resume-section">
			<div className="resume-section-content">

				<h1 className="mb-4">
					Post
					<span className="text-primary">Details</span>
				</h1>

				<div className="row">
					<div className="col-xl-10 mb-3">
						<div className="subheading ">{post?.title || ""}</div>
						<hr className="mb-0" />
						<span>Writer : {post?.writer?.username || ""}</span>
						<span className="float-end">Views : {post?.views || 0}</span>
					</div>
				</div>


				<div className="row">
					<div className="col-xl-10 mb-3">
						<p className="lead mb-4">{post?.content || ""}</p>
						<hr className="mb-0" />
						<span>Created Date : {post?.createdDateTime || ""}</span>
						<span className="float-end">Updated Date : {post?.updatedDateTime || ""}</span>
					</div>
				</div>

				<div className="row" >
					<div className="col-xl-10">
						{(post?.writer?.group === "ADMIN") &&
							<button type="button" className="btn btn-info" onClick={() => navigate(`/post/${id}/modify/form`)}>Modify</button>
						}				
						{(post?.state === "ACTIVE") && 
							<button type="submit" className="btn btn-danger float-end" onClick={() => onChangeState("DEACTIVE")} >Delete</button>
						}
						{(post?.state === "DEACTIVE") &&
							<button type="submit" className="btn btn-success float-end" onClick={() => onChangeState("ACTIVE")} >Restore</button>
						}
					</div>
				</div>

			</div>
		</section>
	);
}