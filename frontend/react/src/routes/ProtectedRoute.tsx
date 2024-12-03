import {ReactNode} from "react";
import {useAuth} from "@/context/AuthContext";
import {Navigate} from "react-router-dom";


interface ProtectedRouteProps {
    children: ReactNode;
}

const ProtectedRoute = ({children}: ProtectedRouteProps) => {
    const {isAuthenticated} = useAuth();

    if (!isAuthenticated()) {
        return <Navigate to="/" replace/>;
    }

    return <>{children}</>;
};

export default ProtectedRoute;
