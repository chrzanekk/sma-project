import {useTranslation} from "react-i18next";
import {useThemeColors} from "@/theme/theme-colors.ts";
import {Box, Button, DrawerContext, Heading} from "@chakra-ui/react";
import {
    DrawerActionTrigger,
    DrawerBackdrop,
    DrawerBody,
    DrawerCloseTrigger,
    DrawerContent,
    DrawerFooter,
    DrawerHeader,
    DrawerRoot,
    DrawerTrigger
} from "@/components/ui/drawer.tsx";
import {FaTimes} from "react-icons/fa";
import React from "react";
import EditUnitForm from "@/components/unit/EditUnitForm.tsx";

interface EditUnitDrawerProps {
    fetchUnits: () => void;
    unitId: number;
    disabled?: boolean;
}

const EditUnitDrawer: React.FC<EditUnitDrawerProps> = ({fetchUnits, unitId, disabled = false}) => {
    const {t} = useTranslation(['common', 'units']);
    const themeColors = useThemeColors();

    return (
        <Box>
            <DrawerRoot size={"lg"} placement={"end"}>
                <DrawerBackdrop/>
                <DrawerTrigger asChild>
                    <Button
                        colorPalette="green"
                        size={"2xs"}
                        p={1}
                        disabled={disabled}
                    >
                        {t('data', {ns: "common"})}
                    </Button>
                </DrawerTrigger>
                <DrawerContent bg={themeColors.bgColorSecondary}
                               offset={"2"}
                               borderRadius={"md"}>
                    <DrawerContext>
                        {(store) => (
                            <Box>
                                <DrawerCloseTrigger/>
                                <DrawerHeader>
                                    <Heading size={"xl"} color={themeColors.fontColor}>
                                        {t("units:edit")}
                                    </Heading>
                                </DrawerHeader>
                                <DrawerBody>
                                    <EditUnitForm
                                        onSuccess={() => {
                                            fetchUnits();
                                            store.setOpen(false);
                                        }}
                                        unitId={unitId}
                                    />
                                </DrawerBody>
                                <DrawerFooter>
                                    <DrawerActionTrigger asChild>
                                        <Button
                                            colorPalette="red"
                                            onClick={() => store.setOpen(false)}
                                        >
                                            <FaTimes/>
                                            {t("close", {ns: "common"})}
                                        </Button>
                                    </DrawerActionTrigger>
                                </DrawerFooter>
                            </Box>

                        )}
                    </DrawerContext>
                </DrawerContent>
            </DrawerRoot>
        </Box>
    )
}

export default EditUnitDrawer;