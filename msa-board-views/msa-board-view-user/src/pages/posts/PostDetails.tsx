import { GROUP, ERROR_CODE } from "msa-board-view-common/src/constants/constants"
import useFetchData, { FetchErrorResponse } from "msa-board-view-common/src/hooks/useFetchData"
import sessionUtils from "msa-board-view-common/src/utils/sessionUtils"
import { useState, useCallback, useEffect } from "react"
import { useErrorBoundary } from "react-error-boundary"
import { useNavigate, useParams } from "react-router-dom"

export type PostWriter = {
	id: string
	username: string
	name: string
	group: keyof typeof GROUP
};

export type PostDetails = {
	id: string
	title: string
	content: string
	views: number
	writer: PostWriter
	createdDateTime: string
	updatedDateTime: string
};

export default function PostDetails() {

	const endpointURL = process.env.REACT_APP_ENDPOINT_URL;

	const navigate = useNavigate();
	const { id } = useParams();
	const { showBoundary } = useErrorBoundary();
	const { fetchData } = useFetchData();
	const [ post, setPost ] = useState<PostDetails>();
	const [ accountId, setAccountId ] = useState<string>();

	const remove = useCallback(() => {

		fetchData({
			url: `${endpointURL}/apis/users/posts/${id || "empty"}`,
			refreshURL: `${endpointURL}/apis/users/refresh`,
			method: "DELETE"
		}).then((response: Response) => {
		
			alert("게시글이 삭제되었습니다.");
			navigate("/post/list");

		}).catch((errorResponse: FetchErrorResponse) => {
		
			const errorCode = errorResponse.errorCode;
			const errorMessage = errorResponse.errorMessage;
			const errorDetailsList = errorResponse.errorDetailsList || [];
	
			switch (errorCode) {
				case ERROR_CODE.NOT_FOUND_POST:
				case ERROR_CODE.NOT_FOUND_POST_WRITER:
				case ERROR_CODE.DEACTIVE_POST:
					alert(errorMessage);
					navigate("/member/list");
					break;
				default:
					showBoundary(errorResponse);
					break;
			}		
		});

	}, [id, fetchData, navigate, showBoundary]);

	useEffect(() => {

		Promise.all([
			(async () => fetchData({
				url: `${endpointURL}/apis/users`,
				refreshURL: `${endpointURL}/apis/users/refresh`,
				method: "GET",
				headers: { "Accept": "application/json" }
			}).then((response: Response) => {
				
				return response.json();
	
			}).then((responseBody) => {
	
				setAccountId(responseBody?.id);
	
			}).catch((errorResponse: FetchErrorResponse) => {
	
				const errorCode = errorResponse.errorCode;
	
				switch (errorCode) {
					case ERROR_CODE.NOT_FOUND_MEMBER:
						sessionUtils.removeAccessToken();
						sessionUtils.removeRefreshToken();
						sessionUtils.removeGroup();	
						navigate("/account/login/form");
						break;
					default:
						showBoundary(errorResponse);
						break;
				}
			}))(),
			(async () => fetchData({
				url: `${endpointURL}/apis/users/posts/${id || "empty"}`,
				refreshURL: `${endpointURL}/apis/users/refresh`,
				method: "GET",
				headers: { "Accept": "application/json" }
			}).then(async (response: Response) => {
	
				setPost(await response.json());
	
			}).catch((errorResponse: FetchErrorResponse) => {
	
				const errorCode = errorResponse.errorCode;
				const errorMessage = errorResponse.errorMessage;
	
				switch (errorCode) {
					case ERROR_CODE.NOT_FOUND_POST:
					case ERROR_CODE.NOT_FOUND_POST_WRITER:
					case ERROR_CODE.DEACTIVE_POST:
						alert(errorMessage);
						navigate("/post/list");
						break;
					default:
						showBoundary(errorResponse);
						break;
				}
			}))()
		]).catch(showBoundary);;

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

				<div className="row">
					<div className="col-xl-10">
						{(!!post?.writer?.id && !!accountId && post.writer.id === accountId) &&
							<button type="button" className="btn btn-info" onClick={() => navigate(`/post/${id}/write/form`)}>Modify</button>
						}				
						{(!!post?.writer?.id && !!accountId && post.writer.id === accountId) && 
							<button type="button" className="btn btn-danger float-end" onClick={() => remove()} >Delete</button>
						}
					</div>
				</div>

			</div>
		</section>
	);
};