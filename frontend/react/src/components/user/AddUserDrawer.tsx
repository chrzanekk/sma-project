import {Button, DrawerContext} from "@chakra-ui/react";
import {useTranslation} from "react-i18next";
import React from "react";
import AddUserForm from "@/components/user/AddUserForm.tsx";
import {FaPlus} from "react-icons/fa";
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
    return (
        <>

            <DrawerRoot size={"md"}>
                <DrawerBackdrop/>
                <DrawerTrigger asChild>
                    <Button
                        colorPalette="green"
                        size={"xs"}
                        p={1}
                    ><FaPlus/>
                        {t('shared.addUser')}
                    </Button>
                </DrawerTrigger>
                <DrawerContent bg={themeColors.bgColor()}>
                    <DrawerContext>
                        {(store) => (
                            <>
                                <DrawerCloseTrigger/>
                                <DrawerHeader>{t("shared.userDetails")}</DrawerHeader>
                                <DrawerBody>
                                    <AddUserForm
                                        onSuccess={() => {
                                            fetchUsers();
                                            store.setOpen(false); // Zamknięcie drawera po sukcesie
                                        }}
                                    />
                                </DrawerBody>
                                <DrawerFooter>
                                    <DrawerActionTrigger asChild>
                                        <Button
                                            colorPalette="red"
                                            onClick={() => store.setOpen(false)} // Zamknięcie drawera po kliknięciu
                                        >
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

export default AddUserDrawer;