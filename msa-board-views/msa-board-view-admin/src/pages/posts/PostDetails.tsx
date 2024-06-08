import { STATE, GROUP, ERROR_CODE } from "msa-board-view-common/src/constants/constants"
import useFetchData, { FetchErrorResponse, FetchErrorDetails } from "msa-board-view-common/src/hooks/useFetchData"
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
	state: keyof typeof STATE
};

export default function PostDetails() {

	const endpointURL = process.env.REACT_APP_ENDPOINT_URL;

	const navigate = useNavigate();
	const { id } = useParams();
	const { showBoundary } = useErrorBoundary();
	const { fetchData } = useFetchData();
	const [ post, setPost ] = useState<PostDetails>();

	const modifyState = useCallback((state: keyof typeof STATE) => {

		fetchData({
			url: `${endpointURL}/apis/admins/posts/${id || "empty"}/state`,
			refreshURL: `${endpointURL}/apis/admins/refresh`,
			method: "PATCH",
			headers: { "Content-Type": "application/json" },
			body: `"${state}"`
		}).then((response: Response) => {
		
			alert("게시글 정보가 수정되었습니다.");
			setPost((prevState: PostDetails | undefined) => (
				(!prevState) ? prevState : {
					...prevState,
					state: state
				}));

		}).catch((errorResponse: FetchErrorResponse) => {
		
			const errorCode = errorResponse.errorCode;
			const errorMessage = errorResponse.errorMessage;
			const errorDetailsList = errorResponse.errorDetailsList || [];
	
			switch (errorCode) {
				case ERROR_CODE.INVALID_PARAMETER:
					errorDetailsList
						.filter((error: FetchErrorDetails) => (error.field === "state"))
						.forEach((error: FetchErrorDetails) => alert(error.message));
					break;
				case ERROR_CODE.NOT_FOUND_POST:
				case ERROR_CODE.NOT_FOUND_POST_WRITER:
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

		fetchData({
			url: `${endpointURL}/apis/admins/posts/${id || "empty"}`,
			refreshURL: `${endpointURL}/apis/admins/refresh`,
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
					alert(errorMessage);
					navigate("/post/list");
					break;
				default:
					showBoundary(errorResponse);
					break;
			}
		});

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
						{(post?.writer?.group === "ADMIN") &&
							<button type="button" className="btn btn-info" onClick={() => navigate(`/post/${id}/write/form`)}>Modify</button>
						}				
						{(post?.state === "ACTIVE") && 
							<button type="button" className="btn btn-danger float-end" onClick={() => modifyState("DEACTIVE")} >Delete</button>
						}
						{(post?.state === "DEACTIVE") && 
							<button type="button" className="btn btn-success float-end" onClick={() => modifyState("ACTIVE")} >Restore</button>
						}
					</div>
				</div>

			</div>
		</section>
	);
};