import './App.css';
import { BrowserRouter, Route, Routes } from 'react-router-dom';
import Navigator from './pages/common/Navigator';
import LoginForm from './pages/common/LoginForm';

export default function App() {
  return (
    <BrowserRouter>
      <Navigator />
      <Routes>
        <Route path='/admin/login/form' element={<LoginForm loginType='ADMIN' />} />
        <Route path='/user/login/form' element={<LoginForm loginType='USER' />} />
      </Routes>
    </BrowserRouter>
  );
}