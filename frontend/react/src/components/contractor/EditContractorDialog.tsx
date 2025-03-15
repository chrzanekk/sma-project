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
import {themeColors} from "@/theme/theme-colors.ts";
import {FaTimes} from "react-icons/fa";
import React from "react";
import EditContractorWithContactForm from "@/components/contractor/EditContractorWithContactForm.tsx";


interface EditContractorDialogProps {
    fetchContractors: () => void;
    contractorId: number;
}

const EditContractorDialog: React.FC<EditContractorDialogProps> = ({fetchContractors, contractorId}) => {
    const {t} = useTranslation(['common', 'contractors']);

    return (
        <Box>
            <DialogRoot size={"xl"} placement={"top"}>
                <DialogBackdrop/>
                <DialogTrigger asChild>
                    <Button
                        colorPalette="blue"
                        size={"2xs"}
                        p={1}>
                        {t('data', {ns: "common"})}
                    </Button>
                </DialogTrigger>
                <DialogContent bg={themeColors.bgColorSecondary()}
                               offset={"2"}
                               borderRadius={"md"}>
                    <DialogContext>
                        {(store) => (
                            <Box>
                                <DialogCloseTrigger/>
                                <DialogHeader>
                                    <Heading size={"xl"} color={themeColors.fontColor()}>
                                        {t("contractors:edit")}
                                    </Heading>
                                </DialogHeader>
                                <DialogBody>
                                    <EditContractorWithContactForm
                                        onSuccess={() => {
                                            fetchContractors();
                                            store.setOpen(false);
                                        }}
                                        contractorId={contractorId}
                                    />
                                </DialogBody>
                                <DialogFooter>
                                    <DialogActionTrigger asChild>
                                        <Button
                                            colorPalette="red"
                                            onClick={() => store.setOpen(false)}
                                        >
                                            <FaTimes/>
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

export default EditContractorDialog;