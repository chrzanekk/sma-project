import {useTranslation} from "react-i18next";
import {Button, useDisclosure} from "@chakra-ui/react";
import {themeColors} from "@/theme/theme-colors.ts";
import {FaTimes} from "react-icons/fa";
import React from "react";
import EditUserRolesForm from "@/components/user/EditUserRolesForm.tsx";
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

interface EditUserRolesDrawerProps {
    fetchUsers: () => void;
    userId: number;
    currentUserId?: number;
    login: string;
}

const EditUserRolesDrawer: React.FC<EditUserRolesDrawerProps> = ({fetchUsers, userId, currentUserId, login}) => {
    const {t} = useTranslation('auth');
    const {onClose} = useDisclosure();
    return (
        <>
            <DrawerRoot size={"md"}>
                <DrawerBackdrop/>
                <DrawerTrigger asChild>
                    <Button
                        colorPalette="blue"
                        size={"xs"}
                        p={1}
                        disabled={currentUserId === userId}>

                        {t('shared.roles')}
                    </Button>
                </DrawerTrigger>
                <DrawerContent bg={themeColors.bgColor()}>
                    <DrawerCloseTrigger/>
                    <DrawerHeader>{t('shared.userRoles', {login: login})}</DrawerHeader>
                    <DrawerBody>
                        <EditUserRolesForm
                            onSuccess={() => {
                                fetchUsers();
                                onClose();
                            }}
                            userId={userId}
                            login={login}
                        />
                    </DrawerBody>
                    <DrawerFooter>
                        <DrawerActionTrigger>
                            <Button colorPalette="red"><FaTimes/>
                                {t('close', {ns: "common"})}
                            </Button>
                        </DrawerActionTrigger>
                    </DrawerFooter>
                </DrawerContent>
            </DrawerRoot>
        </>
    )
}

export default EditUserRolesDrawer;