import { BrowserRouter, Navigate, Route, Routes } from 'react-router-dom';
import Navigation from './pages/common/Navigation';
import { ErrorBoundary } from 'react-error-boundary';
import ErrorPage from './pages/common/ErrorPage';
import AccountLoginForm from './pages/account/AccountLoginForm';
import { AdminRounter, NotAdminRounter } from './components/AdminRounter';
import AccountModifyForm from './pages/account/AccountModifyForm';
import { LoadingProvider } from 'msa-board-view-common/src/contexts/LoadingContext';
import MemberList from './pages/member/MemberList';
import MemberDetails from './pages/member/MemberDetails';
import PostList from './pages/posts/PostList';
import PostWriteForm from './pages/posts/PostWriteForm';
import PostDetails from './pages/posts/PostDetails';
import { useLocation } from 'react-router-dom';

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
                            <Route path="/main" element={<Navigate to="/member/list" />} />
                            <Route path="/account/login/form" element={<NotAdminRounter element={<AccountLoginForm />} />} />
                            <Route path="/account/modify/form" element={<AdminRounter element={<AccountModifyForm />} />} />
                            <Route path="/member/list" element={<AdminRounter element={<MemberList />} />} />
                            <Route path="/member/:id/details" element={<AdminRounter element={<MemberDetails />} />} />
                            <Route path="/post/list" element={<AdminRounter element={<PostList />} />} />
                            <Route path="/post/write/form" element={<AdminRounter element={<PostWriteForm />} />} />
                            <Route path="/post/:id/details" element={<AdminRounter element={<PostDetails />} />} />
                        </Routes>
                    </ErrorBoundary>
                </div>
            </LoadingProvider>
        </BrowserRouter>
    );
}