import {useTranslation} from "react-i18next";
import {Button, DrawerContext, Heading} from "@chakra-ui/react";
import {FaTimes} from "react-icons/fa";
import React from "react";
import {useThemeColors} from "@/theme/theme-colors.ts";
import {
    DrawerActionTrigger,
    DrawerBackdrop,
    DrawerBody,
    DrawerCloseTrigger,
    DrawerContent,
    DrawerFooter,
    DrawerHeader,
    DrawerRoot,
    DrawerTrigger,
} from "@/components/ui/drawer"
import EditUserCompaniesForm from "@/components/user/EditUserCompaniesForm.tsx";

interface EditUserCompaniesDrawerProps {
    fetchUsers: () => void;
    userId: number;
    currentUserId?: number;
}

const EditUserCompaniesDrawer: React.FC<EditUserCompaniesDrawerProps> = ({fetchUsers, currentUserId, userId}) => {
    const {t} = useTranslation('auth');
    const themeColors = useThemeColors();
    return (
        <>
            <DrawerRoot size={"md"}>
                <DrawerBackdrop/>
                <DrawerTrigger asChild>
                    <Button
                        colorPalette="yellow"
                        size={"2xs"}
                        p={1}
                        disabled={currentUserId != null && currentUserId === userId}
                    >
                        {t('common:companies')}
                    </Button>
                </DrawerTrigger>
                <DrawerContent bg={themeColors.bgColorSecondary}>
                    <DrawerContext>
                        {(store) => (
                            <>
                                <DrawerCloseTrigger/>
                                <DrawerHeader>
                                    <Heading size={"xl"} color={themeColors.fontColor}>
                                        {t("shared.editUserCompanies")}
                                    </Heading>
                                </DrawerHeader>
                                <DrawerBody>
                                    <EditUserCompaniesForm
                                        onSuccess={() => {
                                            fetchUsers();
                                            store.setOpen(false); // Zamknięcie drawera po sukcesie
                                        }}
                                        userId={userId}
                                    />
                                </DrawerBody>
                                <DrawerFooter>
                                    <DrawerActionTrigger asChild>
                                        <Button
                                            colorPalette="red"
                                            onClick={() => store.setOpen(false)} // Zamknięcie drawera po kliknięciu
                                        >
                                            <FaTimes/>
                                            {t("close", {ns: "common"})}
                                        </Button>
                                    </DrawerActionTrigger>
                                </DrawerFooter>
                            </>

                        )}
                    </DrawerContext>
                </DrawerContent>
            </DrawerRoot>
        </>
    )
}

export default EditUserCompaniesDrawer;