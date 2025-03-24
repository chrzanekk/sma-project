import {useTranslation} from "react-i18next";
import {
    DialogActionTrigger,
    DialogBackdrop,
    DialogBody,
    DialogCloseTrigger,
    DialogContent,
    DialogFooter,
    DialogHeader,
    DialogRoot,
    DialogTrigger,
} from "@/components/ui/dialog.tsx";
import {Box, Button, DialogContext, Heading} from "@chakra-ui/react";
import {FaPlus} from "react-icons/fa";
import {useThemeColors} from "@/theme/theme-colors.ts";
import React from "react";
import AddContractorWithContactForm from "@/components/contractor/AddContractorWithContactForm";

interface AddContractorWithContactDialogProps {
    fetchContractors: () => void;
}

const AddContractorWithContactDialog: React.FC<AddContractorWithContactDialogProps> = ({fetchContractors}) => {
    const {t} = useTranslation("contractors");
    const themeColors = useThemeColors();

    return (
        <Box>
            <DialogRoot size="xl" placement="top">
                <DialogBackdrop/>
                <DialogTrigger asChild>
                    <Button colorPalette="green" size="2xs">
                        <FaPlus/> {t("add")}
                    </Button>
                </DialogTrigger>
                <DialogContent bg={themeColors.bgColorSecondary} offset="4" borderRadius="md">
                    <DialogContext>
                        {(store) => (
                            <Box>
                                <DialogCloseTrigger/>
                                <DialogHeader>
                                    <Heading size="xl" color={themeColors.fontColor}>
                                        {t("contractors:details")}
                                    </Heading>
                                </DialogHeader>
                                <DialogBody>
                                    <AddContractorWithContactForm
                                        onSuccess={() => {
                                            fetchContractors();
                                            store.setOpen(false);
                                        }}
                                    />
                                </DialogBody>
                                <DialogFooter>
                                    <DialogActionTrigger asChild>
                                        <Button colorPalette="red" onClick={() => store.setOpen(false)}>
                                            {t("close", {ns: "common"})}
                                        </Button>
                                    </DialogActionTrigger>
                                </DialogFooter>
                            </Box>
                        )}
                    </DialogContext>
                </DialogContent>
            </DialogRoot>
        </Box>
    );
};

export default AddContractorWithContactDialog;
