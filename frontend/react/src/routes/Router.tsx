import {createBrowserRouter} from "react-router-dom";
import Login from "@/auth/login/Login.tsx";
import Register from "@/auth/register/Register.tsx";
import ResetPassword from "@/auth/resetPassword/ResetPassword.tsx";
import NewPassword from "@/auth/setPassword/NewPassword.tsx";
import ProtectedRoute from "@/routes/ProtectedRoute.tsx";
import App from "@/App.tsx";
import ConfirmAccount from "@/auth/register/ConfirmAccount.tsx";
import UserProfileEdit from "@/auth/profile/UserProfileEdit.tsx";
import UserManagement from "@/components/user/UserManagement.tsx";
import Layout from "@/layout/Layout.tsx";
import RoleManagement from "@/components/role/RoleManagement.tsx";
import AdminPanel from "@/components/admin/AdminPanel.tsx";
import UnderConstructionRoute from "@/routes/UnderConstructionRoute.tsx";
import ContractorManagement from "@/components/contractor/ContractorManagement.tsx";
import ContactManagement from "@/components/contact/ContactManagement.tsx";
import CompanyManagement from "@/components/company/CompanyManagement.tsx";
import ConstructionSiteManagement from "@/components/constructionsite/ConstructionSiteManagement.tsx";


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
        },
        {
            path: "users",
            element: (
                <ProtectedRoute>
                    <UserManagement/>
                </ProtectedRoute>
            )
        },
        {
            path: "roles",
            element: (
                <ProtectedRoute>
                    <RoleManagement/>
                </ProtectedRoute>)
        },
        {
            path: "companies",
            element: (
                <ProtectedRoute>
                    <CompanyManagement/>
                </ProtectedRoute>
            )
        },
        {
            path: "adminPanel",
            element: (
                <ProtectedRoute>
                    <Layout>
                        <AdminPanel/>
                    </Layout>
                </ProtectedRoute>
            )
        },
        {
            path: "contractors",
            element: <ProtectedRoute>
                <Layout>
                    <ContractorManagement/>
                </Layout>
            </ProtectedRoute>
        },
        {
            path: "contacts",
            element: <ProtectedRoute>
                <Layout>
                    <ContactManagement/>
                </Layout>
            </ProtectedRoute>
        },
        {
            path: "diaryList",
            element: <ProtectedRoute>
                <Layout>
                    <UnderConstructionRoute
                        nameKey={'diaryList'}
                        nameSpace={'navbar'}
                    />
                </Layout>
            </ProtectedRoute>
        },
        {
            path: "diaryAddNew",
            element: <ProtectedRoute>
                <Layout>
                    <UnderConstructionRoute
                        nameKey={'diaryAddNew'}
                        nameSpace={'navbar'}
                    />
                </Layout>
            </ProtectedRoute>
        },
        {
            path: "mainWarehouse",
            element: <ProtectedRoute>
                <Layout>
                    <UnderConstructionRoute
                        nameKey={'mainWarehouse'}
                        nameSpace={'navbar'}
                    />
                </Layout>
            </ProtectedRoute>
        },
        {
            path: "constructionSites",
            element: <ProtectedRoute>
                <Layout>
                    <ConstructionSiteManagement/>
                </Layout>
            </ProtectedRoute>
        }
    ],
);

export default router;
