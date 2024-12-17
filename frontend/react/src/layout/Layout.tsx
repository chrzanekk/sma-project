import React from "react";
import Navbar from "@/layout/Navbar.tsx";
import Footer from "@/layout/Footer.tsx";
import {Box, Flex} from "@chakra-ui/react";

const Layout = ({children}: { children: React.ReactNode }) => (
    <Flex
        direction="column"
        minHeight="100vh" // Pełna wysokość ekranu
        overflow="hidden" // Ukrycie dodatkowego scrolla
    >
        {/* Pasek nawigacyjny */}
        <Box as="header" w="100%">
            <Navbar/>
        </Box>

        {/* Główna zawartość */}
        <Box as="main" flex="1" w="100%" overflowY="auto">
            {children} {/* Dynamiczna treść */}
        </Box>

        {/* Stopka */}
        <Box as="footer" w="100%">
            <Footer/>
        </Box>
    </Flex>
)

export default Layout;