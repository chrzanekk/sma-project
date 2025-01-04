import {StrictMode} from 'react'
import {createRoot} from 'react-dom/client'
import {Toaster} from "@/components/ui/toaster"
import {Provider} from "@/components/ui/provider"
import './i18n/i18n';
import {AuthProvider} from "@/context/AuthContext.tsx";
import {RouterProvider} from "react-router-dom";
import router from "@/routes/router.tsx";

createRoot(document.getElementById('root')!)
    .render(
        <StrictMode>
            <Provider>
                <AuthProvider>
                    <RouterProvider router={router}/>
                    <Toaster/>
                </AuthProvider>
            </Provider>
        </StrictMode>
    );
