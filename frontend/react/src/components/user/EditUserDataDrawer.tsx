import {useTranslation} from "react-i18next";
import {Button, DrawerContext, Heading} from "@chakra-ui/react";
import {FaTimes} from "react-icons/fa";
import React from "react";
import {useThemeColors} from "@/theme/theme-colors.ts";
import EditUserDataForm from './EditUserDataForm.tsx'
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

interface EditUserDataDrawerProps {
    fetchUsers: () => void;
    userId: number;
}

const EditUserDataDrawer: React.FC<EditUserDataDrawerProps> = ({fetchUsers, userId}) => {
    const {t} = useTranslation('auth');
    const themeColors = useThemeColors();
    return (
        <>
            <DrawerRoot size={"md"}>
                <DrawerBackdrop/>
                <DrawerTrigger asChild>
                    <Button
                        colorPalette="green"
                        size={"2xs"}
                        p={1}
                    >
                        {t('data', {ns: "common"})}
                    </Button>
                </DrawerTrigger>
                <DrawerContent bg={themeColors.bgColorSecondary}>
                    <DrawerContext>
                        {(store) => (
                            <>
                                <DrawerCloseTrigger/>
                                <DrawerHeader>
                                    <Heading size={"xl"} color={themeColors.fontColor}>
                                        {t("shared.editUserDetails")}
                                    </Heading>
                                </DrawerHeader>
                                <DrawerBody>
                                    <EditUserDataForm
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

export default EditUserDataDrawer;