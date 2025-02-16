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
import {Button, DrawerContext, Heading} from "@chakra-ui/react";
import {FaPlus} from "react-icons/fa";
import {themeColors} from "@/theme/theme-colors.ts";
import React from "react";
import AddContractorForm from "@/components/contractor/AddContractorForm.tsx";


interface AddContractorDrawerProps {
    fetchContractors: () => void;
}

const AddContractorDrawer: React.FC<AddContractorDrawerProps> = ({fetchContractors}) => {
    const {t} = useTranslation('contractors');
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
                        {t('add')}
                    </Button>
                </DrawerTrigger>
                <DrawerContent bg={themeColors.bgColorPrimary()}>
                    <DrawerContext>
                        {(store) => (
                            <>
                                <DrawerCloseTrigger/>
                                <DrawerHeader>
                                    <Heading size={"xl"} color={themeColors.fontColor()}>
                                    {t("contractors:details")}
                                    </Heading>
                                </DrawerHeader>
                                <DrawerBody>
                                    <AddContractorForm
                                        onSuccess={() => {
                                            fetchContractors();
                                            store.setOpen(false);
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
export default AddContractorDrawer;