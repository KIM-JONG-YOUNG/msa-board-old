import { BrowserRouter, Route, Routes } from 'react-router-dom';
import Navigation from './pages/common/Navigation';
import { ErrorBoundary } from 'react-error-boundary';
import ErrorPage from './pages/common/ErrorPage';
import AccountLoginForm from './pages/account/AccountLoginForm';
import { AnonymousRouter, MemberRouter } from 'msa-board-view-common/src/components/ProtectRouter';

export default function App() {

    return (
        <BrowserRouter>
            <div className="container-fluid p-0">
                <Navigation />
                <ErrorBoundary FallbackComponent={ ErrorPage }>
                    <Routes>
                        <Route path="/login/form" element={ 
                            <AnonymousRouter 
                                element={ <AccountLoginForm /> }
                                redirectURL="/login/form" />
                        } />
                    </Routes>
                </ErrorBoundary>
            </div>
        </BrowserRouter>
    );
}