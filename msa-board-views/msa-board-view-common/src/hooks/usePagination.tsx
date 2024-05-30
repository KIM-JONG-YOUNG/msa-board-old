import { useEffect, useState } from "react"

export default function usePagination(prop: {
  pageRows: number
  pageGroupSize: number
}) {

  const [page, setPage] = useState(0);
  const [pageRows, setPageRows] = useState(prop.pageRows);
  const [pageGroup, setPageGroup] = useState(0);
  const [pageGroupSize, setPageGroupSize] = useState(prop.pageGroupSize);
  const [totalPage, setTotalPage] = useState(0);
  const [totalCount, setTotalCount] = useState(0);

  const calculate = () => {

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
  }

  const getPageList = (): number[] => {
    
    if (pageGroup > 0 && pageGroupSize > 0 && totalPage > 0) {

      const startPage = (pageGroup - 1) * pageGroupSize;
      const pageCount = ((startPage + pageGroupSize) > totalPage) 
        ? totalPage - startPage 
        : startPage + pageGroupSize; 

      return Array.from({ length: pageCount }, (_, i) => startPage + i + 1);
      
    } else {
      return [];
    }
  }

  useEffect(() => {
    calculate();
  }, [page, pageRows, pageGroupSize, totalCount]);

  return {
    page: page,
    pageRows: pageRows,
    pageGroup: pageGroup,
    pageGroupSize: pageGroupSize,
    totalPage: totalPage,
    totalCount: totalCount,
    setPage: setPage,
    setPageRows: setPageRows,
    setPageGroupSize: setPageGroupSize,
    setTotalCount: setTotalCount,
    getPageList: getPageList
  }
}