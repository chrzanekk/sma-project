import React from "react";
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
import {FaPlus} from "react-icons/fa";
import AddUnitForm from "@/components/unit/AddUnitForm.tsx";

interface AddUnitDrawerProps {
    fetchUnits: () => void;
}


const AddUnitDrawer: React.FC<AddUnitDrawerProps> = ({fetchUnits}) => {
    const {t} = useTranslation('units');
    const themeColors = useThemeColors();
    return (
        <Box>
            <DrawerRoot size={"lg"}>
                <DrawerBackdrop/>
                <DrawerTrigger asChild>
                    <Button
                        colorPalette="green"
                        size={"2xs"}
                        p={1}
                    ><FaPlus/>
                        {t('add')}
                    </Button>
                </DrawerTrigger>
                <DrawerContent bg={themeColors.bgColorSecondary} offset={"4"} borderRadius={"md"}>
                    <DrawerContext>
                        {(store) => (
                            <>
                                <DrawerCloseTrigger/>
                                <DrawerHeader>
                                    <Heading size={"xl"} color={themeColors.fontColor}>
                                        {t("units:details")}
                                    </Heading>
                                </DrawerHeader>
                                <DrawerBody>
                                    <AddUnitForm
                                        onSuccess={() => {
                                            fetchUnits();
                                            store.setOpen(false);
                                        }}
                                    />
                                </DrawerBody>
                                <DrawerFooter>
                                    <DrawerActionTrigger asChild>
                                        <Button
                                            colorPalette="red"
                                            onClick={() => store.setOpen(false)}
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
        </Box>
    )
}
export default AddUnitDrawer;