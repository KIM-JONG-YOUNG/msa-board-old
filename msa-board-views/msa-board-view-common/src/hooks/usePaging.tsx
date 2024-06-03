import { useEffect, useState } from "react"

export type UsePagingProps = {
    initPageRows: number
    initPageGroupSize: number
}

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

    const getPageList = (): number[] => {

        if (pageGroup > 0 && pageGroupSize > 0 && totalPage > 0) {

            const startPage = (pageGroup - 1) * pageGroupSize;
            const pageCount = ((startPage + pageGroupSize) > totalPage)
                ? totalPage - startPage
                : pageGroupSize;

            return Array.from({ length: pageCount }, (_, i) => startPage + i + 1);

        } else {
            return [];
        }
    }

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

    }, [page, pageRows, pageGroup, pageGroupSize, totalPage, totalCount]);

    return {
        page: page,
        pageRows: pageRows,
        pageGroup: pageGroup,
        pageGroupSize: pageGroupSize,
        totalPage: totalPage,
        totalCount: totalCount,
        startPage: startPage,
        endPage: endPage,
        setPage: setPage,
        setPageRows: setPageRows,
        setPageGroupSize: setPageGroupSize,
        setTotalCount: setTotalCount,
        getPageList: getPageList
    }
}