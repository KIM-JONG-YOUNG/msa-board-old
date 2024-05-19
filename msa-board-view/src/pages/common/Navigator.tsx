
export default function Navigator() {
  return (
    // <!-- Navigation-->
    <nav className="navbar navbar-expand-lg navbar-dark bg-primary fixed-top" id="sideNav">
      <a className="navbar-brand js-scroll-trigger" href="#page-top">
        <span className="d-block d-lg-none fw-bold ">MSA BOARD</span>
        <span className="d-none d-lg-block fw-bold fs-3">MSA BOARD</span>
      </a>
      <button className="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarResponsive"
        aria-controls="navbarResponsive" aria-expanded="false" aria-label="Toggle navigation"><span
          className="navbar-toggler-icon"></span></button>
      <div className="collapse navbar-collapse" id="navbarResponsive">
        <ul className="navbar-nav">
          <li className="nav-item"><a className="nav-link js-scroll-trigger" href="#menu1">Menu#1</a></li>
          <li className="nav-item"><a className="nav-link js-scroll-trigger" href="#menu2">Menu#2</a></li>
          <li className="nav-item"><a className="nav-link js-scroll-trigger" href="#menu3">Menu#3</a></li>
          <li className="nav-item"><a className="nav-link js-scroll-trigger" href="#menu4">Menu#4</a></li>
        </ul>
      </div>
    </nav>
  );
}
