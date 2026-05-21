import React from "react";
import {useTranslation} from "react-i18next";
import {useThemeColors} from "@/theme/theme-colors.ts";
import {Box, Button, Dialog, Heading, Portal} from "@chakra-ui/react";
import {FaPlus} from "react-icons/fa";
import AddScaffoldingLogForm from "@/components/scaffolding/log/AddScaffoldingLogForm.tsx";

interface AddLogDialogProps {
    fetchLogs: () => void;
}

const AddScaffoldingLogDialog: React.FC<AddLogDialogProps> = ({fetchLogs}) => {
    const {t} = useTranslation('scaffoldingLogs');
    const themeColors = useThemeColors();
    return (
        <Box>
            <Dialog.Root size={'cover'} placement={"top"}>
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
                    <Dialog.Backdrop>
                        <Dialog.Positioner>
                            <Dialog.Content bg={themeColors.bgColorSecondary} offset={"4"} borderRadius={"md"}>
                                <Dialog.Context>
                                    {(store) => (
                                        <>
                                            <Dialog.CloseTrigger/>
                                            <Dialog.Header>
                                                <Heading size={"xl"} color={themeColors.fontColor}>
                                                    {t("scaffoldingLogs:details")}
                                                </Heading>
                                            </Dialog.Header>
                                            <Dialog.Body>
                                                <AddScaffoldingLogForm
                                                    onSuccess={() => {
                                                        fetchLogs();
                                                        store.setOpen(false);
                                                    }}/>
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
                                        </>
                                    )}
                                </Dialog.Context>
                            </Dialog.Content>
                        </Dialog.Positioner>
                    </Dialog.Backdrop>
                </Portal>
            </Dialog.Root>
        </Box>
    )
}

export default AddScaffoldingLogDialog;