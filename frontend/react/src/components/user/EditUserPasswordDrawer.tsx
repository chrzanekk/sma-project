import {useTranslation} from "react-i18next";
import {Button, DrawerContext, Heading} from "@chakra-ui/react";
import {useThemeColors} from "@/theme/theme-colors.ts";
import {FaTimes} from "react-icons/fa";
import React from "react";
import EditUserPasswordForm from "@/components/user/EditUserPasswordForm.tsx";
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

interface EditUserPasswordDrawerProps {
    fetchUsers: () => void;
    userId: number;
    currentUserId?: number;
    login: string;
}

const EditUserPasswordDrawer: React.FC<EditUserPasswordDrawerProps> = ({fetchUsers, userId, currentUserId, login}) => {
    const {t} = useTranslation('auth');
    const themeColors = useThemeColors();

    return (
        <>

            <DrawerRoot size={"md"}>
                <DrawerBackdrop/>
                <DrawerTrigger asChild>
                    <Button
                        colorPalette="orange"
                        size={"2xs"}
                        p={1}
                        disabled={currentUserId === userId}>
                        {t('shared.password')}
                    </Button>
                </DrawerTrigger>
                <DrawerContent bg={themeColors.bgColorSecondary}>
                    <DrawerContext>
                        {(store) => (
                            <>
                                <DrawerCloseTrigger/>
                                <DrawerHeader>
                                    <Heading size={"xl"} color={themeColors.fontColor}>
                                        {t("newPassword.header")}
                                    </Heading>
                                </DrawerHeader>
                                <DrawerBody>
                                    <EditUserPasswordForm
                                        onSuccess={() => {
                                            fetchUsers();
                                            store.setOpen(false); // Zamknięcie drawera po sukcesie
                                        }}
                                        userId={userId}
                                        login={login}
                                    />
                                </DrawerBody>
                                <DrawerFooter>
                                    <DrawerActionTrigger asChild>
                                        <Button
                                            colorPalette="green"
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

export default EditUserPasswordDrawer;