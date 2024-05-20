
import { error } from 'console';
import { LOGIN_TYPE } from '../../constants/Constants';
import { useForm } from "react-hook-form";

interface LoginFormInputs {
  username: string
  password: string
}

export default function LoginForm(prop: {
  loginType: LOGIN_TYPE
}) {

  const { register, handleSubmit, formState: { errors } } = useForm<LoginFormInputs>({ 
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

  const login = (loginFormData: LoginFormInputs) => {
    console.log("Login Form Data: ", loginFormData);
  }

  return (
    // <!-- Login Member -->
    <section className="resume-section">
      <div className="resume-section-content">

        <h1 className="mb-4">
          Login
          <span className="text-primary">{prop.loginType}</span>
        </h1>

        <div className="row">
          <div className="col-xl-8 mb-3">
            <label className="form-label subheading">Username</label>
            <input type="text" className="form-control" placeholder="Username..." {...usernameRegister} />
            {errors.username && <span className='text-danger'>{errors.username.message}</span>}
          </div>
        </div>

        <div className="row">
          <div className="col-xl-8 mb-3">
            <label className="form-label subheading">Password</label>
            <input type="password" className="form-control" placeholder="Password..."  {...passwordRegister} />
            {errors.password && <span className='text-danger'>{errors.password.message}</span>}
          </div>
        </div>

        <div className="row">
          <div className="col-xl-8 mt-3">
            <button type="button" className="btn btn-primary text-white w-100" onClick={handleSubmit(login)}>Login</button>
          </div>
        </div>
      </div>
    </section>
  );
}
