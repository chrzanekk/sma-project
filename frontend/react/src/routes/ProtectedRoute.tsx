import {ReactNode} from "react";
import {useAuth} from "@/context/AuthContext";
import {Navigate} from "react-router-dom";
import {Box, Spinner} from "@chakra-ui/react";
import {useResourcePermissions} from "@/context/ResourcePermissionContext.tsx";


interface ProtectedRouteProps {
    children: ReactNode;
    resourceKey?: string;
}

const ProtectedRoute = ({children, resourceKey}: ProtectedRouteProps) => {
    const {isAuthenticated} = useAuth();
    const {canAccessResource, loading} = useResourcePermissions();

    if (!isAuthenticated()) {
        return <Navigate to="/" replace/>;
    }

    if (loading) {
        return (
            <Box
                display="flex"
                justifyContent="center"
                alignItems="center"
                minH="100vh"
            >
                <Spinner size="xl" />
            </Box>
        );
    }

    if (resourceKey && !canAccessResource(resourceKey)) {
        return <Navigate to="/unauthorized" replace/>;
    }


    return <>{children}</>;
};

export default ProtectedRoute;
