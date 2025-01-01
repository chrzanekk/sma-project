import {Button, useDisclosure} from "@chakra-ui/react";
import {useTranslation} from "react-i18next";
import React from "react";
import AddUserForm from "@/components/user/AddUserForm.tsx";
import {FaPlus, FaTimes} from "react-icons/fa";
import {themeColors} from "@/theme/theme-colors.ts";
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

interface AddUserDrawerProps {
    fetchUsers: () => void;
}

const AddUserDrawer: React.FC<AddUserDrawerProps> = ({fetchUsers}) => {
    const {t} = useTranslation('auth');
    const {onClose} = useDisclosure();
    return (
        <>

            <DrawerRoot size={"md"}>
                <DrawerBackdrop/>
                <DrawerTrigger asChild>
                    <Button
                        colorScheme="green"
                        size={"xs"}
                        p={1}
                    ><FaPlus/>
                        {t('shared.addUser')}
                    </Button>
                </DrawerTrigger>
                <DrawerContent bg={themeColors.bgColor()}>
                    <DrawerCloseTrigger/>
                    <DrawerHeader>{t('shared.userDetails')}</DrawerHeader>
                    <DrawerBody>
                        <AddUserForm onSuccess={() => {
                            fetchUsers();
                            onClose();
                        }}/>
                    </DrawerBody>
                    <DrawerFooter>
                        <DrawerActionTrigger asChild>
                            <Button
                                colorScheme="green">
                                {t('close', {ns: "common"})}
                            </Button><FaTimes/>
                        </DrawerActionTrigger>

                    </DrawerFooter>
                </DrawerContent>
            </DrawerRoot>
        </>
    )
}

export default AddUserDrawer;