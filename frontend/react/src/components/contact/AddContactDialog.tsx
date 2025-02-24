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
import AddContactForm from "@/components/contact/AddContactForm.tsx";


interface AddContractorDialogProps {
    fetchContacts: () => void;
}

const AddContactDialog: React.FC<AddContractorDialogProps> = ({fetchContacts}) => {
    const {t} = useTranslation('contacts');
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
                            <>
                                <DialogCloseTrigger/>
                                <DialogHeader>
                                    <Heading size={"xl"} color={themeColors.fontColor()}>
                                        {t("contacts:details")}
                                    </Heading>
                                </DialogHeader>
                                <DialogBody>
                                    <AddContactForm
                                        onSuccess={() => {
                                            fetchContacts();
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
                            </>
                        )}
                    </DialogContext>
                </DialogContent>
            </DialogRoot>
        </Box>
    )
}
export default AddContactDialog;