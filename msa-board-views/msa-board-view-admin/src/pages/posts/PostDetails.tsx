import { useForm } from "react-hook-form";
import { useNavigate, useParams } from "react-router-dom";
import { useErrorBoundary } from "react-error-boundary";
import { ERROR_CODE, GROUP, STATE } from "msa-board-view-common/src/constants/constants";
import { useEffect, useState } from "react";

import { FetchErrorDetails, FetchErrorResponse } from "msa-board-view-common/src/utils/fetchUtils";
import useLoading from "msa-board-view-common/src/hooks/useLoading";

import * as adminService from "../../services/adminService";
import * as sessionUtils from "msa-board-view-common/src/utils/sessionUtils";

import 'react-datepicker/dist/react-datepicker.css';

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

export default function PostDetails() {

	const { id } = useParams();
	const { showBoundary } = useErrorBoundary();
	const [, setLoading] = useLoading();
	const navigate = useNavigate();

	const [post, setPost] = useState<PostInfos>();

	const changePostState = (state: keyof typeof STATE) => {

		(!!id) && setLoading(true);
		(!!id) && adminService.modifyPostState(id, state)
			.then((response: Response) => {
				setPost(prevState => (!prevState) 
					? undefined 
					: {
						...prevState,
						state: state
					});
			})
			.catch(showBoundary).finally(() => setLoading(false));
	};


	useEffect(() => {

		(!!id) && setLoading(true);
		(!!id) && adminService.getPost(id)
			.then((response: Response) => response.json())
			.then((post: PostInfos) => setPost(post))
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
						<span className="float-end">Views : {post?.views || ""}</span>
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

				<div className="row">
					<div className="col-xl-10">
						{(post?.writer?.group === "ADMIN") &&
							<button type="button" className="btn btn-info" onClick={() => navigate(`/post/${id}/modify/form`)}>Modify</button>}				
						{(post?.state === "ACTIVE") && 
							<button type="button" className="btn btn-danger float-end" onClick={() => changePostState("DEACTIVE")}>Delete</button>}
						{(post?.state === "DEACTIVE") && 
							<button type="button" className="btn btn-success float-end" onClick={() => changePostState("ACTIVE")}>Restore</button>}					
					</div>
				</div>

			</div>
		</section>
	);
}