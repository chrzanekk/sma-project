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
import {useTranslation} from "react-i18next";
import {FaPlus, FaTimes} from "react-icons/fa";
import {themeColors} from "@/theme/theme-colors.ts";
import AddRoleForm from "@/components/role/AddRoleForm.tsx";
import React from "react";

interface AddRoleDrawerProps {
    fetchRoles: () => void;
}

const AddRoleDrawer: React.FC<AddRoleDrawerProps> = ({fetchRoles}) => {
    const {t} = useTranslation('auth');
    const {isOpen, onOpen, onClose} = useDisclosure();
    return (
        <>
            <Button leftIcon={<FaPlus/>} colorScheme="green" onClick={onOpen} size={"xs"} p={1}>
                {t('shared.addRole')}
            </Button>
            <Drawer isOpen={isOpen} onClose={onClose} size={"md"}>
                <DrawerOverlay/>
                <DrawerContent bg={themeColors.bgColor()}>
                    <DrawerCloseButton/>
                    <DrawerHeader>{t('shared.roleDetails')}</DrawerHeader>
                    <DrawerBody>
                        <AddRoleForm onSuccess={() => {
                            fetchRoles();
                            onClose();
                        }}/>
                    </DrawerBody>
                    <DrawerFooter>
                        <Button leftIcon={<FaTimes/>} colorScheme="green" onClick={onClose}>
                            {t('close', {ns: "common"})}
                        </Button>
                    </DrawerFooter>
                </DrawerContent>
            </Drawer>
        </>
    )
}

export default AddRoleDrawer;