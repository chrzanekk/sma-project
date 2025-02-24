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
    DialogTrigger
} from "@/components/ui/dialog.tsx";
import {Box, Button, DialogContext, Heading} from "@chakra-ui/react";
import {FaPlus} from "react-icons/fa";
import {themeColors} from "@/theme/theme-colors.ts";
import React from "react";
import AddContractorForm from "@/components/contractor/AddContractorForm.tsx";


interface AddContractorDialogProps {
    fetchContractors: () => void;
}

const AddContractorDialog: React.FC<AddContractorDialogProps> = ({fetchContractors}) => {
    const {t} = useTranslation('contractors');
    return (
        <Box>
            <DialogRoot size={"lg"} placement={"top"}>
                <DialogBackdrop/>
                <DialogTrigger asChild>
                    <Button
                        colorPalette="green"
                        size={"2xs"}
                        p={1}
                    ><FaPlus/>
                        {t('add') + "(DIALOG)"}
                    </Button>
                </DialogTrigger>
                <DialogContent bg={themeColors.bgColorSecondary()} offset={"4"} borderRadius={"md"}>
                    <DialogContext>
                        {(store) => (
                            <Box>
                                <DialogCloseTrigger/>
                                <DialogHeader>
                                    <Heading size={"xl"} color={themeColors.fontColor()}>
                                    {t("contractors:details")}
                                    </Heading>
                                </DialogHeader>
                                <DialogBody>
                                    <AddContractorForm
                                        onSuccess={() => {
                                            fetchContractors();
                                            store.setOpen(false);
                                        }}
                                    />
                                </DialogBody>
                                <DialogFooter>
                                    <DialogActionTrigger asChild>
                                        <Button
                                            colorPalette="red"
                                            onClick={() => store.setOpen(false)} // Zamknięcie drawera po kliknięciu
                                        >
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
    )
}
export default AddContractorDialog;