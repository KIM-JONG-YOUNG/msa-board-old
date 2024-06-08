import { ERROR_CODE, GROUP } from "msa-board-view-common/src/constants/constants"
import useFetchData, { FetchErrorResponse, FetchErrorDetails } from "msa-board-view-common/src/hooks/useFetchData"
import sessionUtils from "msa-board-view-common/src/utils/sessionUtils"
import { useState, useCallback, useEffect } from "react"
import { useErrorBoundary } from "react-error-boundary"
import { useForm } from "react-hook-form"
import { useNavigate, useParams } from "react-router-dom"

export type PostWriteFormInputs = {
	title: string
	content: string
};

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
	writer: PostWriter
};

export default function PostWriteForm() {

	const endpointURL = process.env.REACT_APP_ENDPOINT_URL;

	const navigate = useNavigate();
	const { id } = useParams();
	const { showBoundary } = useErrorBoundary();
	const { fetchData } = useFetchData();
	const [ post, setPost ] = useState<PostDetails>();
	const [ accountId, setAccountId ] = useState<string>();
	const {
		register,
		setError,
		setValue,
		getValues,
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

	const modifyErrorHandler = useCallback((errorResponse: FetchErrorResponse) => {

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
			case ERROR_CODE.NOT_FOUND_POST:
			case ERROR_CODE.NOT_ADMIN_GROUP_POST:
			case ERROR_CODE.NOT_FOUND_POST_WRITER:
			case ERROR_CODE.DEACTIVE_POST: 
				alert(errorMessage);
				navigate("/post/list");
				break;
			default:
				showBoundary(errorResponse);
				break;
		}
	}, [setError, navigate, showBoundary]);

	const modify = useCallback(() => {

		const formData = getValues();

		if (!!id) {

			fetchData({
				url: `${endpointURL}/apis/users/posts/${id}`,
				refreshURL: `${endpointURL}/apis/users/refresh`,
				method: "PATCH",
				headers: { "Content-Type": "application/json" },
				body: JSON.stringify(formData)
			}).then((response: Response) => {
			
				alert("게시글 정보가 수정되었습니다.");
				navigate(`/post/${id}/details`);

			}).catch(modifyErrorHandler);
	
		} else {

			fetchData({
				url: `${endpointURL}/apis/users/posts`,
				refreshURL: `${endpointURL}/apis/users/refresh`,
				method: "POST",
				headers: { "Content-Type": "application/json" },
				body: JSON.stringify(formData)
			}).then((response: Response) => {

				const location = response.headers.get("Location");
				const newPostId = location?.substring(location.lastIndexOf("/") + 1);

				alert("게시글이 작성되었습니다.");
				navigate(`/post/${newPostId}/details`);

			}).catch(modifyErrorHandler);
		}

	}, [id, getValues, fetchData, navigate, modifyErrorHandler]);

	useEffect(() => {

		(!!post) && setValue("title", post?.title);
		(!!post) && setValue("content", post?.content);

	}, [post, setValue]);

	useEffect(() => {

		Promise.all([
			async () => fetchData({
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
			}),
			async () => fetchData({
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
			})
		]).then(() => {

			if (accountId !== post?.writer?.id) {
				alert("게시글 작성자가 아닙니다.");
				navigate(`/post/${id}/details`);
			}

		}).catch(showBoundary);

	}, []);

	return (
		// <!-- Post List -->
		<section className="resume-section">

			<form className="resume-section-content" onSubmit={handleSubmit(() => modify())}>

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
};