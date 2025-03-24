import {useTranslation} from "react-i18next";
import {Box, Button, Dialog, Heading, Portal} from "@chakra-ui/react";
import {FaPlus} from "react-icons/fa";
import React from "react";
import AddContactForm from "@/components/contact/AddContactForm.tsx";
import {useThemeColors} from "@/theme/theme-colors.ts";


interface AddContractorDialogProps {
    fetchContacts: () => void;
}

const AddContactDialog: React.FC<AddContractorDialogProps> = ({fetchContacts}) => {
    const {t} = useTranslation('contacts');
    const themeColors = useThemeColors();
    return (
        <Box>
            <Dialog.Root size={"lg"} placement={"top"}>

                <Dialog.Trigger asChild>
                    <Button
                        colorPalette="green"
                        size={"2xs"}
                        p={1}
                    ><FaPlus/>
                        {t('add')}
                    </Button>
                </Dialog.Trigger>
                <Portal>
                    <Dialog.Backdrop/>
                    <Dialog.Positioner>
                        <Dialog.Content bg={themeColors.bgColorSecondary} offset={"4"} borderRadius={"md"}>
                            <Dialog.Context>
                                {(store) => (
                                    <>
                                        <Dialog.CloseTrigger/>
                                        <Dialog.Header>
                                            <Heading size={"xl"} color={themeColors.fontColor}>
                                                {t("contacts:details")}
                                            </Heading>
                                        </Dialog.Header>
                                        <Dialog.Body>
                                            <AddContactForm
                                                onSuccess={() => {
                                                    fetchContacts();
                                                    store.setOpen(false);
                                                }}
                                            />
                                        </Dialog.Body>
                                        <Dialog.Footer>
                                            <Dialog.ActionTrigger asChild>
                                                <Button
                                                    colorPalette="red"
                                                    onClick={() => store.setOpen(false)} // Zamknięcie drawera po kliknięciu
                                                >
                                                    {t("close", {ns: "common"})}
                                                </Button>
                                            </Dialog.ActionTrigger>
                                        </Dialog.Footer>
                                    </>
                                )}
                            </Dialog.Context>
                        </Dialog.Content>
                    </Dialog.Positioner>
                </Portal>
            </Dialog.Root>
        </Box>
    )
}
export default AddContactDialog;