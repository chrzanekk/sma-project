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
import React from "react";
import AddUserForm from "@/components/user/AddUserForm.tsx";
import {FaPlus, FaTimes} from "react-icons/fa";
import {themeColors} from "@/theme/theme-colors.ts";


interface AddUserDrawerProps {
    fetchUsers: () => void;
}

const AddUserDrawer: React.FC<AddUserDrawerProps> = ({fetchUsers}) => {
    const {t} = useTranslation('auth');
    const {isOpen, onOpen, onClose} = useDisclosure();
    return (
        <>
            <Button
                leftIcon={<FaPlus/>}
                colorScheme="green"
                onClick={onOpen}
                size={"xs"}
                p={1}
            >
                {t('shared.addUser')}
            </Button>
            <Drawer isOpen={isOpen} onClose={onClose} size={"md"}>
                <DrawerOverlay/>
                <DrawerContent bg={themeColors.bgColor()}>
                    <DrawerCloseButton/>
                    <DrawerHeader>{t('shared.userDetails')}</DrawerHeader>
                    <DrawerBody>
                        <AddUserForm onSuccess={() => {
                            fetchUsers();
                            onClose();
                        }}/>
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

export default AddUserDrawer;