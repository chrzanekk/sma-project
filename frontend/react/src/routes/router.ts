import {createBrowserRouter, RouteObject} from "react-router-dom";
import Login from "../components/login/Login";
import Register from "../components/register/Register";
import ProtectedRoute from "@/routes/ProtectedRoute.tsx";
import App from "@/App.tsx";


const router: RouteObject[] = [
    {
        path: "/",
        element: <Login / >,
    },
    {
        path: "/register",
        element: <Register / >,
    },
    {
        path: "/dashboard",
        element: (
            <ProtectedRoute>
                <App / >
            </ProtectedRoute>
        ),
    }
}
]
;

export default createBrowserRouter(router);
