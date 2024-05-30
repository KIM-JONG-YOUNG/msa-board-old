export default function Navigation() {
    
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
                    <li className="nav-item"><a className="nav-link js-scroll-trigger" href="#loginAdminForm">Menu#1</a></li>
                    <li className="nav-item"><a className="nav-link js-scroll-trigger" href="#loginAdminForm">Menu#2</a></li>
                    <li className="nav-item"><a className="nav-link js-scroll-trigger" href="#loginAdminForm">Menu#3</a></li>
                    <li className="nav-item"><a className="nav-link js-scroll-trigger" href="#loginAdminForm">Menu#4</a></li>
                    <li className="nav-item"><a className="nav-link js-scroll-trigger" href="#loginAdminForm">Menu#5</a></li>
                </ul>
            </div>
        </nav>
    );
}