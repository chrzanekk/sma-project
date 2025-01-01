import {StrictMode} from 'react'
import {createRoot} from 'react-dom/client'
import {Toaster} from "@/components/ui/toaster"
import {Provider} from "@/components/ui/provider"
import './i18n/i18n';
import {AuthProvider} from "@/context/AuthContext.tsx";
import {RouterProvider} from "react-router-dom";
import router from "@/routes/router.tsx";
import {ColorModeProvider} from "@/components/ui/color-mode.tsx";

createRoot(document.getElementById('root')!)
    .render(
        <StrictMode>
            <Provider>
                <ColorModeProvider>
                    <AuthProvider>
                        <RouterProvider router={router}/>
                        <Toaster/>
                    </AuthProvider>
                </ColorModeProvider>
            </Provider>
        </StrictMode>
    );
