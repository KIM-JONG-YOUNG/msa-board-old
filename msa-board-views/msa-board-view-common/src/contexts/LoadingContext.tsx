import { CSSProperties, ReactNode, createContext, useState } from "react";

export type LoadingContextProps = [boolean, React.Dispatch<React.SetStateAction<boolean>>] | null;

export const LoadingContext = createContext<LoadingContextProps>(null);

export function LoadingProvider({ children }: { children: ReactNode }) {

    const [loading, setLoading] = useState(false);
    const loadingOeverlayStyle: CSSProperties = {
        position: "absolute",
        left: "0px",
        zIndex: 2000,
        opacity: 0.5,
        width: "100%",
        height: "100%",
        backgroundColor: "black" 
    };
    const loadingStyle: CSSProperties = {
        position: "absolute",
        top: "40%",
        left: "50%"
    };

    return (
        <LoadingContext.Provider value={[loading, setLoading]}>
            {
                loading && <>
                    <div className="loading-overlay" style={loadingOeverlayStyle}>
                        <div className="loading" style={loadingStyle}>
                            <div className="spinner-border text-primary" role="status">
                                <span className="sr-only"></span>
                            </div>
                        </div>
                    </div>
                </>
            }
            {children}
        </LoadingContext.Provider>
    );
}
