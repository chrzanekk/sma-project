import {useTranslation} from "react-i18next";
import {Box, Button, Dialog, Heading, Portal} from "@chakra-ui/react";
import {useThemeColors} from "@/theme/theme-colors.ts";
import {FaTimes} from "react-icons/fa";
import React from "react";
import EditContractorWithContactForm from "@/components/contractor/EditContractorWithContactForm.tsx";


interface EditContractorDialogProps {
    fetchContractors: () => void;
    contractorId: number;
}

const EditContractorDialog: React.FC<EditContractorDialogProps> = ({fetchContractors, contractorId}) => {
    const {t} = useTranslation(['common', 'contractors']);
    const themeColors = useThemeColors();

    return (
        <Dialog.Root size={"xl"} placement={"top"}>
            <Dialog.Trigger asChild>
                <Button
                    colorPalette="blue"
                    size={"2xs"}
                    p={1}>
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
                                    <Dialog.Header>
                                        <Heading size={"xl"} color={themeColors.fontColor}>
                                            {t("contractors:edit")}
                                        </Heading>
                                    </Dialog.Header>
                                    <Dialog.Body>
                                        <EditContractorWithContactForm
                                            onSuccess={() => {
                                                fetchContractors();
                                                store.setOpen(false);
                                            }}
                                            contractorId={contractorId}
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
    )
}

export default EditContractorDialog;