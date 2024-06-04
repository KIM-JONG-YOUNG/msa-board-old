import { useCallback, useContext } from 'react';
import { LoadingContext } from '../contexts/LoadingContext';

export default function useLoading() {

    const loadingContext = useContext(LoadingContext);

    useCallback
    if (loadingContext === null) {
        throw new Error("useLoading Hook은 LoadingProvider 내부에서 사용해야 합니다.");
    } else {

        const [loading, setLoading] = loadingContext;
        const loadingCallback = async (callback: Function) => {

            try {
                setLoading(true);
                callback();
            } finally {
                setLoading(false);
            }
        };

        return {
            loading: loading, 
            loadingCallback: loadingCallback
        };
    }
}