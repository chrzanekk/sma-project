import React from "react";
import Navbar from "@/layout/Navbar.tsx";
import Footer from "@/layout/Footer.tsx";
import {Box} from "@chakra-ui/react";
import {useThemeColors} from "@/theme/theme-colors.ts";

const Layout = ({children}: { children: React.ReactNode }) => {
    const themeColors = useThemeColors();
        return <Box
        minH="100vh"
        display="flex"
        w={"auto"}
        flexDirection="column"
        overflow={"hidden"}
        bg={themeColors.bgColorSecondary}
    >

        <Box as="header" w="100%" position="sticky" top="0" zIndex="10">
            <Navbar/>
        </Box>

        <Box as="main"
             flex={"1"}
             w="100%"
             overflowY="auto"
             px={2}
        >
            {children}
        </Box>

        <Box as="footer" w="100%" height="50px" position="sticky" bottom="0" zIndex="10">
            <Footer/>
        </Box>
    </Box>
}

export default Layout;