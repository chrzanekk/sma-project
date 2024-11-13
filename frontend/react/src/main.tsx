import {StrictMode} from 'react'
import {createRoot} from 'react-dom/client'
import App from './App.tsx'
import {ChakraProvider} from "@chakra-ui/react";
import './i18n/i18n';

createRoot(document.getElementById('root')!)
    .render(
        <StrictMode>
            <ChakraProvider>
                <App/>
            </ChakraProvider>
        </StrictMode>
    );
