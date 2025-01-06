import React from "react";
import Navbar from "@/layout/Navbar.tsx";
import Footer from "@/layout/Footer.tsx";
import {Box} from "@chakra-ui/react";
import {themeColors} from "@/theme/theme-colors.ts";

const Layout = ({children}: { children: React.ReactNode }) => (
    <Box
        // direction="column"
        h={"100vh"}
        overflow={"hidden"}
        bg={themeColors.bgColorLight()}
    >

        <Box as="header" w="100%" position="sticky" top="0" zIndex="10">
            <Navbar/>
        </Box>

        <Box as="main"  w="100%"
             overflowY="auto"
             height="calc(100vh-100px)"
        >
            {children}
        </Box>

        <Box as="footer" w="100%" height="50px" position="sticky" bottom="0" zIndex="10">
            <Footer/>
        </Box>
    </Box>
)

export default Layout;