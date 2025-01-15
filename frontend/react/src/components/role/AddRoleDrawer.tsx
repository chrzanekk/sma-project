import {Button, useDisclosure} from "@chakra-ui/react";
import {useTranslation} from "react-i18next";
import {FaPlus} from "react-icons/fa";
import {themeColors} from "@/theme/theme-colors.ts";
import AddRoleForm from "@/components/role/AddRoleForm.tsx";
import React from "react";
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

interface AddRoleDrawerProps {
    fetchRoles: () => void;
}

const AddRoleDrawer: React.FC<AddRoleDrawerProps> = ({fetchRoles}) => {
    const {t} = useTranslation('auth');
    const {onClose} = useDisclosure();
    return (
        <>
            <DrawerRoot size={"md"}>
                <DrawerBackdrop/>
                <DrawerTrigger asChild>
                    <Button colorPalette="green" size={"xs"} p={1}>
                        <FaPlus/>
                        {t('shared.addRole')}
                    </Button>
                </DrawerTrigger>
                <DrawerContent bg={themeColors.bgColor()}>
                    <DrawerCloseTrigger/>
                    <DrawerHeader>{t('shared.roleDetails')}</DrawerHeader>
                    <DrawerBody>
                        <AddRoleForm onSuccess={() => {
                            fetchRoles();
                            onClose();
                        }}/>
                    </DrawerBody>
                    <DrawerFooter>
                        <DrawerActionTrigger asChild>
                            <Button colorPalette="green">
                                {t('close', {ns: "common"})}
                            </Button>
                        </DrawerActionTrigger>
                    </DrawerFooter>
                </DrawerContent>
            </DrawerRoot>
        </>
    )
}

export default AddRoleDrawer;