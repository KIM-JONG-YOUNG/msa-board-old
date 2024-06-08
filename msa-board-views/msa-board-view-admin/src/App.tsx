import { LoadingProvider } from "msa-board-view-common/src/contexts/LoadingContext";
import { ErrorBoundary } from "react-error-boundary";
import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";
import { AdminRounter, NotAdminRounter } from "./components/AdminRounter";
import AccountLoginForm from "./pages/account/AccountLoginForm";
import ErrorPage from "./pages/common/ErrorPage";
import Navigation from "./pages/common/Navigation";
import AccountModifyForm from "./pages/account/AccountModifyForm";
import MemberDetails from "./pages/member/MemberDetails";
import MemberList from "./pages/member/MemberList";
import PostDetails from "./pages/posts/PostDetails";
import PostList from "./pages/posts/PostList";
import PostWriteForm from "./pages/posts/PostWriteForm";
import AccountPasswordModifyForm from "./pages/account/AccountPasswordModifyForm";

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
                            <Route path="/account/password/modify/form" element={<AdminRounter element={<AccountPasswordModifyForm />} />} />
                            <Route path="/member/list" element={<AdminRounter element={<MemberList />} />} />
                            <Route path="/member/:id/details" element={<AdminRounter element={<MemberDetails />} />} />
                            <Route path="/post/list" element={<AdminRounter element={<PostList />} />} />
                            <Route path="/post/write/form" element={<AdminRounter element={<PostWriteForm />} />} />
                            <Route path="/post/:id/write/form" element={<AdminRounter element={<PostWriteForm />} />} />
                            <Route path="/post/:id/details" element={<AdminRounter element={<PostDetails />} />} />
                            <Route path="*" element={<NotFound />} />
                        </Routes>
                    </ErrorBoundary>
                </div>
            </LoadingProvider>
        </BrowserRouter>
    );
}