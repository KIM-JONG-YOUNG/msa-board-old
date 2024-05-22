
import { error } from 'console';
import { GROUP } from '../../constants/Constants';
import { useForm } from "react-hook-form";
import { useEffect } from 'react';
import { fetchData } from '../../utils/fetchUtils';
import { adminService } from '../../services/adminService';
 
interface ILoginForm {
  username: string
  password: string
}

export default function LoginForm(prop: {
  loginType: keyof typeof GROUP
}) {

  const { register, handleSubmit, formState: { errors } } = useForm<ILoginForm>({ 
    mode: "onBlur" 
  });
  const usernameRegister = register("username", {
    required: "계정은 비어있을 수 없습니다.",
    max: {
      value: 30,
      message: "계정은 30자를 넘을 수 없습니다."
    }
  });
  const passwordRegister = register("password", {
    required: "비밀번호는 비어있을 수 없습니다."
  });

  const login = async (loginFormData: ILoginForm) => {

    const result = await adminService.loginAdmin(loginFormData)
      .catch(error => console.log("Error : ", error))

    console.log('loginResult : ', result);
  }

  return (
    // <!-- Login Member -->
    <section className="resume-section">
      <div className="resume-section-content">

        <h1 className="mb-4">
          Login
          <span className="text-primary">{ prop.loginType }</span>
        </h1>

        <div className="row">
          <div className="col-xl-8 mb-3">
            <label className="form-label subheading">Username</label>
            <input type="text" className="form-control" placeholder="Username..." { ...usernameRegister } />
            { errors.username && <div className='text-danger'>{ errors.username.message }</div> }
          </div>
        </div>

        <div className="row">
          <div className="col-xl-8 mb-3">
            <label className="form-label subheading">Password</label>
            <input type="password" className="form-control" placeholder="Password..."  { ...passwordRegister } />
            { errors.password && <div className='text-danger'>{ errors.password.message }</div> }
          </div>
        </div>

        <div className="row">
          <div className="col-xl-8 mt-3">
            <button type="button" className="btn btn-primary text-white w-100" onClick={ handleSubmit(login) }>Login</button>
          </div>
        </div>
      </div>
    </section>
  );
}
