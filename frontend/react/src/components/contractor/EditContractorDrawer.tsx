import {useTranslation} from "react-i18next";
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
import {Box, Button, DrawerContext, Heading} from "@chakra-ui/react";
import {useThemeColors} from "@/theme/theme-colors.ts";
import {FaTimes} from "react-icons/fa";
import React from "react";
import EditContractorForm from "@/components/contractor/EditContractorForm.tsx";


interface EditContractorDrawerProps {
    fetchContractors: () => void;
    contractorId: number;
}

const EditContractorDrawer: React.FC<EditContractorDrawerProps> = ({fetchContractors, contractorId}) => {
    const {t} = useTranslation(['common', 'contractors']);
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
                                        {t("contractors:edit")}
                                    </Heading>
                                </DrawerHeader>
                                <DrawerBody>
                                    <EditContractorForm
                                        onSuccess={() => {
                                            fetchContractors();
                                            store.setOpen(false);
                                        }}
                                        contractorId={contractorId}
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

export default EditContractorDrawer;