import {useTranslation} from "react-i18next";
import {Box, Button, Dialog, Heading, Portal} from "@chakra-ui/react";
import {useThemeColors} from "@/theme/theme-colors.ts";
import {FaTimes} from "react-icons/fa";
import React from "react";
import EditCompanyForm from "@/components/company/EditCompanyForm.tsx";


interface EditCompanyDialogProps {
    fetchCompanies: () => void;
    companyId: number;
}

const EditCompanyDialog: React.FC<EditCompanyDialogProps> = ({fetchCompanies, companyId}) => {
    const {t} = useTranslation(['common', 'companies']);
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
                                            <EditCompanyForm
                                                onSuccess={() => {
                                                    fetchCompanies();
                                                    store.setOpen(false);
                                                }}
                                                companyId={companyId}
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

export default EditCompanyDialog;