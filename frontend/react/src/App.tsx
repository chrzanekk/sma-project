import React from "react";
import Navbar from "@/layout/Navbar.tsx";
import Footer from "@/layout/Footer.tsx"
import {Box} from "@chakra-ui/react";


const App: React.FC = () => {
    return (
        <Box minHeight="100vh" display="flex" flexDirection="column">
            <Navbar/>
            <Box flex="1">
                {/* Tutaj wstaw główną treść aplikacji */}
            </Box>
            <Footer/>
        </Box>
    )
}

export default App;
