import {StrictMode} from 'react'
import {createRoot} from 'react-dom/client'
import App from './App.tsx'
import {ChakraProvider, createStandaloneToast} from "@chakra-ui/react";
import './i18n/i18n';
import {AuthProvider} from "@/contexts/AuthContext.tsx";

const {ToastContainer} = createStandaloneToast();

createRoot(document.getElementById('root')!)
    .render(
        <StrictMode>
            <ChakraProvider>
                <AuthProvider>
                    <App/>
                </AuthProvider>
                <ToastContainer/>
            </ChakraProvider>
        </StrictMode>
    );
