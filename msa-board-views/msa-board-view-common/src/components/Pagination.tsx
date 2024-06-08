export type PaginationProps = {
    page: number
    startPage: number
    endPage: number
    totalPage: number
    pageGroupSize: number
    pagingFn: (page: number) => void
};

export default function Pagination({
    page,
    startPage,
    endPage,
    totalPage,
    pageGroupSize,
    pagingFn
}: PaginationProps) {

    return <ul className="pagination justify-content-center">
        {
            (startPage > pageGroupSize) &&
            <li className="page-item" onClick={() => pagingFn(startPage - 1)} >
                <a className="page-link" href="#!" aria-label="Previous">
                    <span aria-hidden="true">&laquo;</span>
                </a>
            </li>
        }
        {
            (startPage > 0 && endPage > 0) &&
            Array.from({ length: endPage - startPage + 1 }, (_, i) => startPage + i)
                .map(i => (page === i)
                    ? <li key={i} className="page-item active"><a className="page-link" href="#!">{i}</a></li>
                    : <li key={i} className="page-item" onClick={() => pagingFn(i)}><a className="page-link" href="#!">{i}</a></li>)
        }
        {
            (totalPage > endPage) &&
            <li className="page-item" onClick={() => pagingFn(endPage + 1)} >
                <a className="page-link" href="#!" aria-label="Next">
                    <span aria-hidden="true">&raquo;</span>
                </a>
            </li>
        }
    </ul>
};