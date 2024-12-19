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
import {FaTimes} from "react-icons/fa";
import React from "react";
import {themeColors} from "@/theme/theme-colors.ts";
import EditUserDataForm from './EditUserDataForm.tsx'

interface EditUserDrawerProps {
    fetchUsers: () => void;
    userId: number;
}

const EditUserDataDrawer: React.FC<EditUserDrawerProps> = ({fetchUsers, userId}) => {
    const {t} = useTranslation('auth');
    const {isOpen, onOpen, onClose} = useDisclosure();

    return (
        <>
            <Button
                colorScheme="green"
                onClick={onOpen}
                size={"xs"}
                p={1}
            >
                {t('data', {ns: "common"})}
            </Button>
            <Drawer isOpen={isOpen} onClose={onClose} size={"md"}>
                <DrawerOverlay/>
                <DrawerContent bg={themeColors.bgColor()}>
                    <DrawerCloseButton/>
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

export default EditUserDataDrawer;