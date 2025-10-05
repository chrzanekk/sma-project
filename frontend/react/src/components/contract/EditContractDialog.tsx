import {useTranslation} from "react-i18next";
import {Box, Button, Dialog, Heading, Portal} from "@chakra-ui/react";
import React from "react";
import {useThemeColors} from "@/theme/theme-colors.ts";
import EditContractForm from "@/components/contract/EditContractForm.tsx";


interface EditContractDialogProps {
    fetchContracts: () => void;
    contractId: number;
    contractorId: number;
    constructionSiteId: number;
}

const EditContractDialog: React.FC<EditContractDialogProps> = ({
                                                                   fetchContracts,
                                                                   contractId,
                                                                   contractorId,
                                                                   constructionSiteId
                                                               }) => {
    const {t} = useTranslation(['contracts', 'contractors', 'constructionSites']);
    const themeColors = useThemeColors();
    return (
        <Box>
            <Dialog.Root size={"cover"} placement={"top"}>
                <Dialog.Trigger asChild>
                    <Button
                        colorPalette="green"
                        size={"2xs"}
                        p={1}
                    >
                        {t('data', {ns: "common"})}
                    </Button>
                </Dialog.Trigger>
                <Portal>
                    <Dialog.Backdrop/>
                    <Dialog.Positioner>
                        <Dialog.Content bg={themeColors.bgColorSecondary} offset={"4"} borderRadius={"md"}>
                            <Dialog.Context>
                                {(store) => (
                                    <Box>
                                        <Dialog.CloseTrigger/>
                                        <Dialog.Header justifyContent={"center"}>
                                            <div>
                                                <Heading size={"2xl"} color={themeColors.fontColor}>
                                                    {t("common:edit")}
                                                </Heading>
                                            </div>
                                        </Dialog.Header>
                                        <Dialog.Body>
                                            <EditContractForm
                                                onSuccess={() => {
                                                    fetchContracts();
                                                    store.setOpen(false);
                                                }}
                                                contractId={contractId}
                                            />
                                        </Dialog.Body>
                                        <Dialog.Footer>
                                            <Dialog.ActionTrigger asChild>
                                                <Button
                                                    colorPalette="red"
                                                    onClick={() => store.setOpen(false)}
                                                >
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
export default EditContractDialog;