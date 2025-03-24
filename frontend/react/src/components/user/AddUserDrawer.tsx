import {Button, DrawerContext, Heading} from "@chakra-ui/react";
import {useTranslation} from "react-i18next";
import React from "react";
import AddUserForm from "@/components/user/AddUserForm.tsx";
import {FaPlus} from "react-icons/fa";
import {useThemeColors} from "@/theme/theme-colors.ts";
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
    const themeColors = useThemeColors();
    return (
        <>
            <DrawerRoot size={"md"}>
                <DrawerBackdrop/>
                <DrawerTrigger asChild>
                    <Button
                        colorPalette="green"
                        size={"2xs"}
                        p={1}
                    ><FaPlus/>
                        {t('shared.addUser')}
                    </Button>
                </DrawerTrigger>
                <DrawerContent bg={themeColors.bgColorSecondary}>
                    <DrawerContext>
                        {(store) => (
                            <>
                                <DrawerCloseTrigger/>
                                <DrawerHeader>
                                    <Heading size={"xl"} color={themeColors.fontColor}>
                                        {t("shared.userDetails")}
                                    </Heading>
                                </DrawerHeader>
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