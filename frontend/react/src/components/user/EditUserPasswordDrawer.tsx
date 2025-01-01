import {useTranslation} from "react-i18next";
import {Button, useDisclosure} from "@chakra-ui/react";
import {themeColors} from "@/theme/theme-colors.ts";
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
    const {onClose} = useDisclosure();
    return (
        <>

            <DrawerRoot size={"md"}>
                <DrawerBackdrop/>
                <DrawerTrigger asChild>
                    <Button
                        colorScheme="orange"
                        size={"xs"}
                        p={1}
                        disabled={currentUserId === userId}>
                        {t('shared.password')}
                    </Button>
                </DrawerTrigger>
                <DrawerContent bg={themeColors.bgColor()}>
                    <DrawerCloseTrigger/>
                    <DrawerHeader>{t('newPassword.header')}</DrawerHeader>
                    <DrawerBody>
                        <EditUserPasswordForm
                            onSuccess={() => {
                                fetchUsers();
                                onClose();
                            }}
                            userId={userId}
                            login={login}
                        />
                    </DrawerBody>
                    <DrawerFooter>
                        <DrawerActionTrigger asChild>
                            <Button
                                colorScheme="green"
                            ><FaTimes/>
                                {t('close', {ns: "common"})}
                            </Button>
                        </DrawerActionTrigger>

                    </DrawerFooter>
                </DrawerContent>
            </DrawerRoot>
        </>
    )
}

export default EditUserPasswordDrawer;