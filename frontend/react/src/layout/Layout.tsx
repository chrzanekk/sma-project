import React, {useEffect, useState} from "react";
import Navbar from "@/layout/Navbar.tsx";
import Footer from "@/layout/Footer.tsx";
import {Box} from "@chakra-ui/react";
import {useThemeColors} from "@/theme/theme-colors.ts";
import {useCompany} from "@/hooks/useCompany";
import {CompanyBaseDTO} from "@/types/company-types.ts";
import {useAuth} from "@/context/AuthContext.tsx";
import SelectCompanyModal from "@/components/shared/SelectCompanyModal.tsx";

const Layout = ({children}: { children: React.ReactNode }) => {
    const themeColors = useThemeColors();
    const {user} = useAuth();
    const {selectedCompany, setSelectedCompany} = useCompany();
    const [showModal, setShowModal] = useState(false);

    useEffect(() => {
        const companyAlreadySelected = localStorage.getItem("companySelected") === "true";

        if (user && !selectedCompany && !companyAlreadySelected) {
            setShowModal(true);
        }
    }, [user, selectedCompany]);

    const handleConfirmCompany = (company: CompanyBaseDTO | null) => {
        setSelectedCompany(company);
        localStorage.setItem("companySelected", "true");
        setShowModal(false);
    };

    return (
        <>
            <SelectCompanyModal
                isOpen={showModal}
                onClose={() => {
                }}
                onConfirm={handleConfirmCompany}
                companies={user?.companies || []}
            />
            <Box
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
        </>
    )
}

export default Layout;