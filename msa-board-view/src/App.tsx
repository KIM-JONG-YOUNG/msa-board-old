import './App.css';
import { BrowserRouter, Route, Routes } from 'react-router-dom';
import Navigator from './pages/common/Navigator';
import LoginForm from './pages/common/LoginForm';
import ModifyMemberForm from './pages/common/ModifyMemberForm';
import ModifyMemberPasswordForm from './pages/common/ModifyMemberPasswordForm';
import { PrivateAnonymousRouter, PrivateMemberRouter } from './components/PrivateRoutes';

export default function App() {
  return (
    <BrowserRouter>
      <Navigator />
      <Routes>
        <Route  path='/admin/login/form' 
                element={ <PrivateAnonymousRouter 
                            element={ <LoginForm loginType="ADMIN" /> } 
                            redirectURL='/member/modify/form' /> } />
        <Route  path='/user/login/form' 
                element={ <PrivateAnonymousRouter 
                            element={ <LoginForm loginType="USER" /> } 
                            redirectURL='/member/modify/form' /> } />
        <Route  path='/member/modify/form' 
                element={ <PrivateMemberRouter 
                            element={ <ModifyMemberForm /> } 
                            redirectURL='/login/error' /> } />
        <Route  path='/member/password/modify/form' 
                element={ <PrivateMemberRouter 
                            element={ <ModifyMemberPasswordForm /> }
                            redirectURL='/login/error' /> } />
      </Routes>
    </BrowserRouter>
  );
}