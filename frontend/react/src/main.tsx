import {StrictMode} from 'react'
import {createRoot} from 'react-dom/client'
import {ChakraProvider, createStandaloneToast} from "@chakra-ui/react";
import './i18n/i18n';
import {AuthProvider} from "@/context/AuthContext.tsx";
import {RouterProvider} from "react-router-dom";

import router from "@/routes/router.tsx";
import Footer from "@/layout/Footer.tsx";

const {ToastContainer} = createStandaloneToast();

createRoot(document.getElementById('root')!)
    .render(
        <StrictMode>
            <ChakraProvider>
                <AuthProvider>
                    <RouterProvider router={router}/>
                </AuthProvider>
                <Footer/>
                <ToastContainer/>
            </ChakraProvider>
        </StrictMode>
    );
