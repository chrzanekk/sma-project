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
import EditContactForm from "@/components/contact/EditContactForm.tsx";


interface EditContractorDialogProps {
    fetchContacts: () => void;
    contactId: number;
}

const EditContactDialog: React.FC<EditContractorDialogProps> = ({fetchContacts, contactId}) => {
    const {t} = useTranslation(['common', 'contacts']);

    return (
        <Box>
            <DialogRoot size={"lg"} placement={"top"}>
                <DialogBackdrop/>
                <DialogTrigger asChild>
                    <Button
                        colorPalette="green"
                        size={"2xs"}
                        p={1}
                    >
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
                                        {t("contacts:edit")}
                                    </Heading>
                                </DialogHeader>
                                <DialogBody>
                                    <EditContactForm
                                        onSuccess={() => {
                                            fetchContacts();
                                            store.setOpen(false);
                                        }}
                                        contactId={contactId}
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

export default EditContactDialog;