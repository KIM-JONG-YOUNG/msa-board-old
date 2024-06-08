import useFetchData from "msa-board-view-common/src/hooks/useFetchData";
import sessionUtils from "msa-board-view-common/src/utils/sessionUtils";
import { useCallback } from "react";
import { useNavigate } from "react-router-dom";

export default function Navigation() {

    const endpointURL = process.env.REACT_APP_ENDPOINT_URL;

    const navigate = useNavigate();
    const { fetchData } = useFetchData();

    const logout = useCallback(() => {

        fetchData({
            url: `${endpointURL}/apis/users/logout`,
            method: "POST",
            headers: { "Access-Token": sessionUtils.getAccessToken() || "" }
        })
        .then(() => {
            sessionUtils.removeAccessToken();
            sessionUtils.removeRefreshToken();
            sessionUtils.removeGroup();
        })
        .then(() => navigate("/account/login/form"))
        .catch(() => navigate("/account/login/form"));

    }, [fetchData, navigate]);

    return (
        // <!-- Navigation-->
        <nav className="navbar navbar-expand-lg navbar-dark bg-primary fixed-top" id="sideNav">
            <a className="navbar-brand js-scroll-trigger" href="#page-top">
                <span className="d-none d-lg-block"><h2 className="text-white">MSA BOARD</h2></span>
                <span className="d-block d-lg-none"><h2 className="text-white mb-0">MSA BOARD</h2></span>
            </a>
            <button className="navbar-toggler" type="button"
                data-bs-toggle="collapse" data-bs-target="#navbarResponsive"
                aria-controls="navbarResponsive" aria-expanded="false" aria-label="Toggle navigation">
                <span className="navbar-toggler-icon"></span>
            </button>
            <div className="collapse navbar-collapse" id="navbarResponsive">
                {
                    ((!sessionUtils.getAccessToken()) || (!sessionUtils.getGroup())) && <>
                        <ul className="navbar-nav">
                            <li className="nav-item"><a className="nav-link js-scroll-trigger" href="#!" onClick={() => navigate("/account/join/form")}>Join</a></li>
                            <li className="nav-item"><a className="nav-link js-scroll-trigger" href="#!" onClick={() => navigate("/account/login/form")}>Login</a></li>
                        </ul>
                    </>
                }

                {
                    ((!!sessionUtils.getAccessToken()) && (!!sessionUtils.getGroup())) && <>
                        <ul className="navbar-nav">
                            <li className="nav-item"><a className="nav-link js-scroll-trigger" href="#!" onClick={() => logout()}>Logout</a></li>
                            <li className="nav-item"><a className="nav-link js-scroll-trigger" href="#!" onClick={() => navigate("/account/modify/form")}>Account</a></li>
                            <li className="nav-item"><a className="nav-link js-scroll-trigger" href="#!" onClick={() => navigate("/post/list")}>Post</a></li>
                        </ul>
                    </>
                }
            </div>
        </nav>
    );
};