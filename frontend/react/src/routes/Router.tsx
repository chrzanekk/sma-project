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
import ContractManagement from "@/components/contract/ContractManagement.tsx";
import PositionManagement from "@/components/position/PositionManagement.tsx";
import ResourcePermissionManagement from "@/components/admin/ResourcePermissionManagement.tsx";
import Unauthorized from "@/components/shared/Unauthorized.tsx";
import {Box, Text} from "@chakra-ui/react";


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
                <ProtectedRoute resourceKey="ACCOUNT_MANAGEMENT">
                    <Layout>
                        <UserProfileEdit/>
                    </Layout>
                </ProtectedRoute>
            )
        },
        {
            path: "users",
            element: (
                <ProtectedRoute resourceKey="USER_MANAGEMENT">
                    <UserManagement/>
                </ProtectedRoute>
            )
        },
        {
            path: "roles",
            element: (
                <ProtectedRoute resourceKey="ROLE_MANAGEMENT">
                    <RoleManagement/>
                </ProtectedRoute>
            )
        },
        {
            path: "companies",
            element: (
                <ProtectedRoute resourceKey="COMPANY_MANAGEMENT">
                    <CompanyManagement/>
                </ProtectedRoute>
            )
        },
        {
            path: "positions",
            element: (
                <ProtectedRoute resourceKey="POSITION_MANAGEMENT">
                    <PositionManagement/>
                </ProtectedRoute>
            )
        },
        {
            path: "resources",
            element: (
                <ProtectedRoute resourceKey="RESOURCE_MANAGEMENT">
                    <Layout>
                        <ResourcePermissionManagement/>
                    </Layout>
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
            element: (
                <ProtectedRoute resourceKey="CONTRACTOR_MANAGEMENT">
                    <Layout>
                        <ContractorManagement/>
                    </Layout>
                </ProtectedRoute>
            )
        },
        {
            path: "contacts",
            element: (
                <ProtectedRoute resourceKey="CONTACT_MANAGEMENT">
                    <Layout>
                        <ContactManagement/>
                    </Layout>
                </ProtectedRoute>
            )
        },
        {
            path: "contracts",
            element: (
                <ProtectedRoute resourceKey="CONTRACT_MANAGEMENT">
                    <Layout>
                        <ContractManagement/>
                    </Layout>
                </ProtectedRoute>
            )
        },
        {
            path: "diaryList",
            element: (
                <ProtectedRoute>
                    <Layout>
                        <UnderConstructionRoute
                            nameKey={'diaryList'}
                            nameSpace={'navbar'}
                        />
                    </Layout>
                </ProtectedRoute>
            )
        },
        {
            path: "diaryAddNew",
            element: (
                <ProtectedRoute>
                    <Layout>
                        <UnderConstructionRoute
                            nameKey={'diaryAddNew'}
                            nameSpace={'navbar'}
                        />
                    </Layout>
                </ProtectedRoute>
            )
        },
        {
            path: "mainWarehouse",
            element: (
                <ProtectedRoute>
                    <Layout>
                        <UnderConstructionRoute
                            nameKey={'mainWarehouse'}
                            nameSpace={'navbar'}
                        />
                    </Layout>
                </ProtectedRoute>
            )
        },
        {
            path: "constructionSites",
            element: (
                <ProtectedRoute resourceKey="CONSTRUCTION_SITE_MANAGEMENT">
                    <Layout>
                        <ConstructionSiteManagement/>
                    </Layout>
                </ProtectedRoute>
            )
        },
        {
            path: "unauthorized",
            element: <Unauthorized/>
        },
        {
            path: "*",
            element: (
                <Layout>
                    <Box p={8} textAlign="center">
                        <Text fontSize="2xl">404 - Page Not Found</Text>
                    </Box>
                </Layout>
            )
        }
    ],
);

export default router;
