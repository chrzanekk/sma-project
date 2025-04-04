import {StrictMode} from 'react'
import {createRoot} from 'react-dom/client'
import {Toaster} from "@/components/ui/toaster"
import './i18n/i18n';
import {AuthProvider} from "@/context/AuthContext.tsx";
import {RouterProvider} from "react-router-dom";
import router from "@/routes/Router.tsx";
import {ChakraProvider, defaultSystem} from "@chakra-ui/react";
import {ThemeProvider} from "next-themes";
import {CompanyProvider} from "@/context/CompanyContext.tsx";


createRoot(document.getElementById('root')!)
    .render(
        <StrictMode>
            <ThemeProvider>
                <ChakraProvider value={defaultSystem}>
                    <CompanyProvider>
                        <AuthProvider>
                            <RouterProvider router={router}/>
                            <Toaster/>
                        </AuthProvider>
                    </CompanyProvider>
                </ChakraProvider>
            </ThemeProvider>
        </StrictMode>
    );
