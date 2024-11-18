import {createBrowserRouter} from "react-router-dom";
import Login from "@/auth/login/Login.tsx";
import Register from "@/auth/register/Register.tsx";
import ResetPassword from "@/auth/resetPassword/ResetPassword.tsx";
import NewPassword from "@/auth/setPassword/NewPassword.tsx";
import ProtectedRoute from "@/routes/ProtectedRoute.tsx";
import App from "@/App.tsx";
import ConfirmAccount from "@/auth/register/ConfirmAccount.tsx";


const router = createBrowserRouter([
    {
        path: "/",
        element: <Login/>
    },
    {
        path: "register",
        element: <Register/>,
    },
    {
        path: "confirm",
        element: <ConfirmAccount/>
    },
    {
        path: "reset-password",
        element: <ResetPassword/>
    },
    {
        path: "set-new-password",
        element: <NewPassword/>
    },
    {
        path: "dashboard",
        element: <ProtectedRoute>
            <App/>
        </ProtectedRoute>
    }

]);

export default router;
