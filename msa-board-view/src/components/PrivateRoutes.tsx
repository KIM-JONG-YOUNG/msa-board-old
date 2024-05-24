import { Navigate } from "react-router-dom";
import { GROUP } from "../constants/constants";

export function PrivateAuthRouter(prop: {
  permitGroups: Array<keyof typeof GROUP>
  redirectURL: string
  element: React.ReactElement,
}): React.ReactElement {

  const group = sessionStorage.getItem("Group")
  let isAuth = false;

  if (prop.permitGroups.length === 0) {
    // 로그인이 안되어 있을 경우에만 체크  
    isAuth = (!group);
  } else {
    // 로그인이 되어 있을 경우 
    for (let i = 0; i < prop.permitGroups.length; i++) {
      isAuth = prop.permitGroups[i] === group
      break;
    }
    prop.permitGroups.forEach(x => isAuth = (!isAuth && x === group));
  }

  return (
    isAuth ? prop.element : <Navigate to={prop.redirectURL} />
  );
};

export function PrivateAdminRouter(prop: {
  element: React.ReactElement,
}): React.ReactElement {

  return (
    <PrivateAuthRouter 
      permitGroups={["ADMIN"]} 
      element={prop.element} 
      redirectURL="/admin/login/form" />
  );
};

export function PrivateUserRouter(prop: {
  element: React.ReactElement,
}): React.ReactElement {

  return (
    <PrivateAuthRouter 
      permitGroups={["USER"]} 
      element={prop.element} 
      redirectURL="/user/login/form" />
  );
};

export function PrivateMemberRouter(prop: {
  element: React.ReactElement,
  redirectURL: string
}): React.ReactElement {

  return (
    <PrivateAuthRouter 
      permitGroups={["ADMIN", "USER"]} 
      element={prop.element} 
      redirectURL={prop.redirectURL} />
  );
};

export function PrivateAnonymousRouter(prop: {
  element: React.ReactElement,
  redirectURL: string
}): React.ReactElement {

  return (
    <PrivateAuthRouter 
      permitGroups={[]} 
      element={prop.element} 
      redirectURL={prop.redirectURL} />
  );
};
