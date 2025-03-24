import {Button, DrawerContext, Heading} from "@chakra-ui/react";
import {useTranslation} from "react-i18next";
import {FaPlus} from "react-icons/fa";
import {useThemeColors} from "@/theme/theme-colors.ts";
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
    const themeColors = useThemeColors();

    return (
        <>
            <DrawerRoot size={"md"}>
                <DrawerBackdrop/>
                <DrawerTrigger asChild>
                    <Button colorPalette="green" size={"2xs"} p={1}>
                        <FaPlus/>
                        {t('shared.addRole')}
                    </Button>
                </DrawerTrigger>
                <DrawerContent bg={themeColors.bgColorSecondary}>
                    <DrawerContext>
                        {(store) => (
                            <>
                                <DrawerCloseTrigger/>
                                <DrawerHeader>
                                    <Heading size={"xl"} color={themeColors.fontColor}>
                                        {t("shared.roleDetails")}
                                    </Heading>
                                </DrawerHeader>
                                <DrawerBody>
                                    <AddRoleForm
                                        onSuccess={() => {
                                            fetchRoles();
                                            store.setOpen(false); // Zamknięcie drawera po sukcesie
                                        }}
                                    />
                                </DrawerBody>
                                <DrawerFooter>
                                    <DrawerActionTrigger asChild>
                                        <Button
                                            colorPalette="green"
                                            onClick={() => store.setOpen(false)}
                                            width={"300px"}
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

export default AddRoleDrawer;