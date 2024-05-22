
import { EMAIL_DOMAIN, GENDER } from '../../constants/Constants';
import { useForm } from "react-hook-form";
import { useState, useEffect } from 'react';

interface IModifyMemberForm {
  name: string
  gender: string
  email: string
  emailDomain: string
}

export default function ModifyMemberForm() {

  const [username, setUsername] = useState("");
  const { register, handleSubmit, setValue, formState: { errors } } = useForm<IModifyMemberForm>({
    mode: "onBlur"
  });
  const nameRegister = register("name", {
    required: "이름은 비어있을 수 없습니다.",
    max: {
      value: 30,
      message: "이름은 30자를 넘을 수 없습니다."
    }
  });
  const genderRegister = register("gender", {
    required: "성별은 비어있을 수 없습니다."
  });
  const emailRegister = register("email", {
    required: "이메일은 비어있을 수 없습니다.",
    max: {
      value: 60,
      message: "이메일은 30자를 넘을 수 없습니다."
    },
    pattern: {
      value: /^[0-9a-zA-Z-_.]+$/,
      message: "이메일이 형식에 맞지 않습니다."
    }
  });
  const emailDomainRegister = register("emailDomain", {
    required: "이메일 도메인은 비어있을 수 없습니다."
  });

  const modify = (modifyFormData: IModifyMemberForm) => {
    console.log("Modify Member Form Data : ", modifyFormData);
  }

  useEffect(() => {

    const emailDomainComboBox = document.getElementById('emailDomain');
    const genderComboBox = document.getElementById('gender');

    // remove all options
    while (!!emailDomainComboBox?.lastElementChild) {
      emailDomainComboBox?.removeChild(emailDomainComboBox?.lastElementChild);
    }

    while (!!genderComboBox?.lastElementChild) {
      genderComboBox?.removeChild(genderComboBox?.lastElementChild);
    }

    Object.entries(EMAIL_DOMAIN).forEach(([key, value]) => {
      const option = document.createElement('option');
      option.value = key;
      option.innerText = value;
      emailDomainComboBox?.appendChild(option);
    });

    Object.entries(GENDER).forEach(([key, value]) => {
      const option = document.createElement('option');
      option.value = key;
      option.innerText = value;
      genderComboBox?.appendChild(option);
    });
  
  }, [])

  return (
    // <!-- Member Modify Form -->
    <section className="resume-section">
      <div className="resume-section-content">

        <h1 className="mb-4">
          Modify
          <span className="text-primary">Member</span>
        </h1>

        <div className="row">
          <div className="col-xl-8 mb-3">
            <label className="form-label subheading">Username</label>
            <input type="text" className="form-control" placeholder="Another input placeholder" disabled readOnly value={ username } />
          </div>
        </div>

        <div className="row">
          <label className="form-label subheading">Password</label>
          <div className="col-xl-8 mb-3">
            <button type="button" className="btn btn-warning w-100">Modify Password</button>
          </div>
        </div>

        <div className="row">
          <div className="col-xl-4 mb-3">
            <label className="form-label subheading">Name</label>
            <input type="text" className="form-control" placeholder="Name..." { ...nameRegister } />
            { errors.name && <div className='text-danger'>{ errors.name.message }</div> }
          </div>
          <div className="col-xl-4 mb-3">
            <label className="form-label subheading">Gender</label>
            <select className="form-select" id="gender" {...genderRegister}></select>
            { errors.gender && <div className='text-danger'>{ errors.gender.message }</div> }
          </div>
        </div>

        <div className="row">
          <div className="col-xl-8 mb-3">
            <label className="form-label subheading">Email</label>
            <div className="input-group">
              <input type="text" className="form-control" placeholder="Email..." { ...emailRegister } />
              <span className="input-group-text">@</span>
              <select className="form-select" id="emailDomain" {...emailDomainRegister}></select>
            </div>
            { errors.email && <div className='text-danger'>{ errors.email.message }</div> }
            { errors.emailDomain && <div className='text-danger'>{ errors.emailDomain.message }</div> }
          </div>
        </div>

        <div className="row">
          <div className="col-xl-8 mt-3">
            <button type="button" className="btn btn-info w-100" onClick={ handleSubmit(modify) }>Modify</button>
          </div>
        </div>
      </div>
    </section>
  );
}
