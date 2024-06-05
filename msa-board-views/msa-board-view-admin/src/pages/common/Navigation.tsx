import { useCallback } from "react";
import { useNavigate } from "react-router-dom";

import * as adminService from "../../services/adminService";
import * as sessionUtils from "msa-board-view-common/src/utils/sessionUtils";

export default function Navigation() {

    const navigate = useNavigate();
    const logout = useCallback(() => {
        
        adminService.logoutAdmin()
            .then(sessionUtils.initSessionInfo)
            .catch(sessionUtils.initSessionInfo)
            .finally(() => navigate("/account/login/form"));
    }, []);
    
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
                <ul className="navbar-nav">
                    <li className="nav-item"><a className="nav-link js-scroll-trigger" href="#!" onClick={() => logout()}>Logout</a></li>
                    <li className="nav-item"><a className="nav-link js-scroll-trigger" href="#!" onClick={() => navigate("/account/modify/form")}>Account</a></li>
                    <li className="nav-item"><a className="nav-link js-scroll-trigger" href="#!" onClick={() => navigate("/member/list")}>Member</a></li>
                    <li className="nav-item"><a className="nav-link js-scroll-trigger" href="#!" onClick={() => navigate("/post/list")}>Post</a></li>
                </ul>
            </div>
        </nav>
    );
}