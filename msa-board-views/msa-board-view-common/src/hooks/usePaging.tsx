import { useEffect, useState } from "react"

export type UsePagingProps = {
    initPageRows: number
    initPageGroupSize: number
};

export default function usePaging({
    initPageRows,
    initPageGroupSize
}: UsePagingProps) {

    const [page, setPage] = useState(0);
    const [pageRows, setPageRows] = useState(initPageRows);
    const [pageGroupSize, setPageGroupSize] = useState(initPageGroupSize);    
    const [pageGroup, setPageGroup] = useState(0);
    const [totalCount, setTotalCount] = useState(0);
    const [totalPage, setTotalPage] = useState(0);
    const [startPage, setStartPage] = useState(0);
    const [endPage, setEndPage] = useState(0);

    useEffect(() => {

        if (totalCount > 0 && pageRows > 0) {
            setTotalPage(Math.ceil(totalCount / pageRows));
        } else {
            setTotalPage(0);
        }

        if (page > 0 && pageGroupSize > 0) {
            setPageGroup(Math.ceil(page / pageGroupSize));
        } else {
            setPageGroup(0);
        }

        if (pageGroup > 0 && totalPage > 0) {
            setStartPage((pageGroup - 1) * pageGroupSize + 1);
            setEndPage(Math.min(totalPage, pageGroup * pageGroupSize))
        } else {
            setStartPage(0); 
            setEndPage(0); 
        }

    }, [totalCount, pageRows, page, pageGroupSize, pageGroup, totalPage]);

    return {
        page,
        pageRows,
        pageGroup,
        pageGroupSize,
        totalPage,
        totalCount,
        startPage,
        endPage,
        setPage,
        setPageRows,
        setPageGroupSize,
        setTotalCount
    };
};