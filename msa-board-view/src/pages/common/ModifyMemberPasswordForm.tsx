
import { useForm } from "react-hook-form";

interface IModifyMemberPasswordForm {
	currentPassword: string
	newPassword: string
}

export default function ModifyMemberPasswordForm() {

	const { register, handleSubmit, formState: { errors } } = useForm<IModifyMemberPasswordForm>({
		mode: "onBlur"
	});
	const currentPasswordRegister = register("currentPassword", {
		required: "현재 비밀번호는 비어있을 수 없습니다."
	});
	const newPasswordRegister = register("newPassword", {
		required: "현재 비밀번호는 비어있을 수 없습니다."
	});

	const modifyPassword = (modifyPasswordFormData: IModifyMemberPasswordForm) => {
		console.log("Modify Member Password Form Data : ", modifyPasswordFormData);
	}

	return (
		// <!-- Member Modify Form -->
		<section className="resume-section" id="modifyPasswordForm">
			<div className="resume-section-content">

				<h1 className="mb-4">
					Modify
					<span className="text-primary">Password</span>
				</h1>

				<div className="row">
					<div className="col-xl-8 mb-3">
						<label className="form-label subheading">Password</label>
						<input type="password" className="form-control" placeholder="Current Password..." {...currentPasswordRegister} />
						{ errors.currentPassword && <div className='text-danger'>{ errors.currentPassword.message }</div> }
					</div>
				</div>

				<div className="row">
					<div className="col-xl-8 mb-3">
						<label className="form-label subheading">New Password</label>
						<input type="password" className="form-control" placeholder="New Password..." {...newPasswordRegister} />
						{ errors.newPassword && <div className='text-danger'>{ errors.newPassword.message }</div> }
					</div>
				</div>

				<div className="row">
					<div className="col-xl-8 mt-3">
						<button type="button" className="btn btn-warning w-100" onClick={ handleSubmit(modifyPassword) }>Modify Password</button>
					</div>
				</div>
			</div>
		</section>
	);
}
