import {createBrowserRouter} from "react-router-dom";
import Login from "@/auth/login/Login.tsx";
import Register from "@/auth/register/Register.tsx";
import ResetPassword from "@/auth/resetPassword/ResetPassword.tsx";
import NewPassword from "@/auth/setPassword/NewPassword.tsx";
import ProtectedRoute from "@/routes/ProtectedRoute.tsx";
import App from "@/App.tsx";
import ConfirmAccount from "@/auth/register/ConfirmAccount.tsx";
import UserProfileEdit from "@/auth/profile/UserProfileEdit.tsx";
import Navbar from "@/layout/Navbar.tsx";
import React from "react";

const Layout = ({children}: { children: React.ReactNode }) => (
    <>
        <Navbar/>
        {children}
    </>
)


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
        path: "request-reset-password",
        element: <ResetPassword/>
    },
    {
        path: "reset-password",
        element: <NewPassword/>
    },
    {
        path: "dashboard",
        element: <ProtectedRoute>
            <Layout>
                <App/>
            </Layout>
        </ProtectedRoute>
    },
    {
        path: "profile",
        element: (
            <ProtectedRoute>
                <Layout>
                    <UserProfileEdit/>
                </Layout>
            </ProtectedRoute>
        )
    }

],

    );

export default router;
