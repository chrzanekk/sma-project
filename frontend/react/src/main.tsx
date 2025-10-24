import {StrictMode} from 'react'
import {createRoot} from 'react-dom/client'
import {Toaster} from "@/components/ui/toaster"
import './i18n/i18n';
import {AuthProvider} from "@/context/AuthContext.tsx";
import {RouterProvider} from "react-router-dom";
import router from "@/routes/Router.tsx";
import {ChakraProvider} from "@chakra-ui/react";
import {CompanyProvider} from "@/context/CompanyContext.tsx";
import {system} from "@/theme/theme.ts";
import {GlobalStyles} from "@/theme/GlobalStyles.tsx";
import {ResourcePermissionProvider} from "@/context/ResourcePermissionContext.tsx";


function initColorMode() {
    const stored = localStorage.getItem("chakra-ui-color-mode");
    const prefersDark = window.matchMedia("(prefers-color-scheme: dark)").matches;
    const mode = stored ?? (prefersDark ? "dark" : "light");
    document.documentElement.classList.remove("light", "dark");
    document.documentElement.classList.add(mode === "dark" ? "dark" : "light");
}

initColorMode();

createRoot(document.getElementById('root')!)
    .render(
        <StrictMode>
            <ChakraProvider value={system}>
                <GlobalStyles/>
                <CompanyProvider>
                    <AuthProvider>
                        <ResourcePermissionProvider>
                            <RouterProvider router={router}/>
                            <Toaster/>
                        </ResourcePermissionProvider>
                    </AuthProvider>
                </CompanyProvider>
            </ChakraProvider>
        </StrictMode>
    );
