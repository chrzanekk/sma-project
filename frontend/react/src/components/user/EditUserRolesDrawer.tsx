import {useTranslation} from "react-i18next";
import {
    Button,
    Drawer,
    DrawerBody,
    DrawerCloseButton,
    DrawerContent,
    DrawerFooter,
    DrawerHeader,
    DrawerOverlay,
    useDisclosure
} from "@chakra-ui/react";
import {themeColors} from "@/theme/theme-colors.ts";
import {FaTimes} from "react-icons/fa";
import React from "react";
import EditUserRolesForm from "@/components/user/EditUserRolesForm.tsx";


interface EditUserRolesDrawerProps {
    fetchUsers: () => void;
    userId: number;
    currentUserId?: number;
    login: string;
}

const EditUserRolesDrawer: React.FC<EditUserRolesDrawerProps> = ({fetchUsers, userId, currentUserId, login}) => {
    const {t} = useTranslation('auth');
    const {isOpen, onOpen, onClose} = useDisclosure();
    return (
        <>
            <Button
                colorScheme="blue"
                onClick={onOpen}
                size={"xs"}
                p={1}
                isDisabled={currentUserId === userId}>

                {t('shared.roles')}
            </Button>
            <Drawer isOpen={isOpen} onClose={onClose} size={"md"}>
                <DrawerOverlay/>
                <DrawerContent bg={themeColors.bgColor()}>
                    <DrawerCloseButton/>
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
                        <Button
                            leftIcon={<FaTimes/>}
                            colorScheme="green"
                            onClick={onClose}
                        >
                            {t('close', {ns: "common"})}
                        </Button>
                    </DrawerFooter>
                </DrawerContent>
            </Drawer>
        </>
    )
}

export default EditUserRolesDrawer;