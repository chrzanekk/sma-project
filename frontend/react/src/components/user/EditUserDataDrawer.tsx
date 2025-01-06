import {useTranslation} from "react-i18next";
import {Button, useDisclosure} from "@chakra-ui/react";
import {FaTimes} from "react-icons/fa";
import React from "react";
import {themeColors} from "@/theme/theme-colors.ts";
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
    const {onClose} = useDisclosure();

    return (
        <>
            <DrawerRoot size={"md"} onOpenChange={onClose}>
                <DrawerBackdrop/>
                <DrawerTrigger asChild>
                    <Button
                        colorPalette="green"
                        size={"xs"}
                        p={1}
                    >
                        {t('data', {ns: "common"})}
                    </Button>
                </DrawerTrigger>
                <DrawerContent bg={themeColors.bgColor()}>
                    <DrawerCloseTrigger/>
                    <DrawerHeader>{t('shared.editUserDetails')}</DrawerHeader>
                    <DrawerBody>
                            <EditUserDataForm
                                onSuccess={() => {
                                    fetchUsers();
                                    onClose();
                                }}
                                userId={userId}
                            />
                    </DrawerBody>
                    <DrawerFooter>
                        <DrawerActionTrigger asChild>
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

export default EditUserDataDrawer;