import { useContext } from 'react';
import { LoadingContext } from '../contexts/LoadingContext';

export default function useLoading() {

    const loadingContext = useContext(LoadingContext);

    if (loadingContext === null) {
        throw new Error("useLoading Hook은 LoadingProvider 내부에서 사용해야 합니다.");
    } else {
        return loadingContext;
    }
}