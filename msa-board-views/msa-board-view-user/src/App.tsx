import { LoadingProvider } from 'msa-board-view-common/src/contexts/LoadingContext';
import { ErrorBoundary } from 'react-error-boundary';
import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import { UserRouter, NotUserRouter } from "./components/UserRouter";
import AccountLoginForm from './pages/account/AccountLoginForm';
import AccountModifyForm from './pages/account/AccountModifyForm';
import AccountPasswordModifyForm from './pages/account/AccountPasswordModifyForm';
import ErrorPage from './pages/common/ErrorPage';
import PostDetails from './pages/posts/PostDetails';
import PostList from './pages/posts/PostList';
import PostWriteForm from './pages/posts/PostWriteForm';
import Navigation from './pages/common/Navigation';
import AccountJoinForm from './pages/account/AccountJoinForm';
import NotFound from './pages/common/NotFound';

export default function App() {

  return (
      <BrowserRouter>
          <LoadingProvider>
              <div className="container-fluid p-0">
                  <Navigation />
                  <ErrorBoundary
                      fallbackRender={fallbackProps => <ErrorPage {...fallbackProps} />} 
                      onError={(error) => console.error(error)} >
                      <Routes>
                          <Route path="/" element={<Navigate to="/main" />} />
                          <Route path="/main" element={<Navigate to="/post/list" />} />
                          <Route path="/account/join/form" element={<NotUserRouter element={<AccountJoinForm />} />} />
                          <Route path="/account/login/form" element={<NotUserRouter element={<AccountLoginForm />} />} />
                          <Route path="/account/modify/form" element={<UserRouter element={<AccountModifyForm />} />} />
                          <Route path="/account/password/modify/form" element={<UserRouter element={<AccountPasswordModifyForm />} />} />
                          <Route path="/post/list" element={<UserRouter element={<PostList />} />} />
                          <Route path="/post/write/form" element={<UserRouter element={<PostWriteForm />} />} />
                          <Route path="/post/:id/write/form" element={<UserRouter element={<PostWriteForm />} />} />
                          <Route path="/post/:id/details" element={<UserRouter element={<PostDetails />} />} />
                          <Route path="*" element={<NotFound />} />
                      </Routes>
                  </ErrorBoundary>
              </div>
          </LoadingProvider>
      </BrowserRouter>
  );
}