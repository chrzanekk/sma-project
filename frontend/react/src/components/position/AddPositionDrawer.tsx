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
import AddPositionForm from "@/components/position/AddPositionForm.tsx";

interface AddPositionDrawerProps {
    fetchPositions: () => void;
}


const AddPositionDrawer: React.FC<AddPositionDrawerProps> = ({fetchPositions}) => {
    const {t} = useTranslation('positions');
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
                                        {t("positions:details")}
                                    </Heading>
                                </DrawerHeader>
                                <DrawerBody>
                                    <AddPositionForm
                                        onSuccess={() => {
                                            fetchPositions();
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
export default AddPositionDrawer;