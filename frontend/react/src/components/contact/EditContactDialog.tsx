import {useTranslation} from "react-i18next";
import {Box, Button, Dialog, Heading, Portal} from "@chakra-ui/react";
import {useThemeColors} from "@/theme/theme-colors.ts";
import {FaTimes} from "react-icons/fa";
import React from "react";
import EditContactForm from "@/components/contact/EditContactForm.tsx";


interface EditContactDialogProps {
    fetchContacts: () => void;
    contactId: number;
}

const EditContactDialog: React.FC<EditContactDialogProps> = ({fetchContacts, contactId}) => {
    const {t} = useTranslation(['common', 'contacts']);
    const themeColors = useThemeColors();

    return (
        <Box>
            <Dialog.Root size={"lg"} placement={"top"}>
                <Dialog.Trigger asChild>
                    <Button
                        colorPalette="blue"
                        size={"2xs"}
                        p={1}
                    >
                        {t('data', {ns: "common"})}
                    </Button>
                </Dialog.Trigger>
                <Portal>
                    <Dialog.Backdrop/>
                    <Dialog.Positioner>
                        <Dialog.Content bg={themeColors.bgColorSecondary}
                                        offset={"2"}
                                        borderRadius={"md"}>
                            <Dialog.Context>
                                {(store) => (
                                    <Box>
                                        <Dialog.CloseTrigger/>
                                        <Dialog.Header asChild>
                                            <Heading size={"xl"} color={themeColors.fontColor}>
                                                {t("contacts:edit")}
                                            </Heading>
                                        </Dialog.Header>
                                        <Dialog.Body>
                                            <EditContactForm
                                                onSuccess={() => {
                                                    fetchContacts();
                                                    store.setOpen(false);
                                                }}
                                                contactId={contactId}
                                            />
                                        </Dialog.Body>
                                        <Dialog.Footer>
                                            <Dialog.ActionTrigger asChild>
                                                <Button
                                                    colorPalette="red"
                                                    onClick={() => store.setOpen(false)}
                                                >
                                                    <FaTimes/>
                                                    {t("close", {ns: "common"})}
                                                </Button>
                                            </Dialog.ActionTrigger>
                                        </Dialog.Footer>
                                    </Box>
                                )}
                            </Dialog.Context>
                        </Dialog.Content>
                    </Dialog.Positioner>
                </Portal>
            </Dialog.Root>
        </Box>
    )
}

export default EditContactDialog;