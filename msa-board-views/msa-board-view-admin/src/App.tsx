import { BrowserRouter, Route, Routes } from 'react-router-dom';
import Navigation from './pages/common/Navigation';
import { ErrorBoundary } from 'react-error-boundary';
import ErrorPage from './pages/common/ErrorPage';
import AccountLoginForm from './pages/account/AccountLoginForm';
import { NotAdminRounter } from './pages/common/AdminRounter';

export default function App() {

    return (
        <BrowserRouter>
            <div className="container-fluid p-0">
                <Navigation />
                <ErrorBoundary FallbackComponent={ ErrorPage }>
                    <Routes>
                        <Route path="/login/form" element={ <NotAdminRounter element={ <AccountLoginForm /> } /> } />
                    </Routes>
                </ErrorBoundary>
            </div>
        </BrowserRouter>
    );
}