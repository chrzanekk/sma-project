import React from "react";
import {useTranslation} from "react-i18next";
import {useThemeColors} from "@/theme/theme-colors.ts";
import {Button, Dialog, Heading, Portal} from "@chakra-ui/react";
import EditScaffoldingLogForm from "@/components/scaffolding/log/EditScaffoldingLogForm.tsx";
import {FaPen} from "react-icons/fa6";

interface EditScaffoldingLogDialogProps {
    fetchLogs: () => void;
    logId: number;
}

const EditScaffoldingLogDialog: React.FC<EditScaffoldingLogDialogProps> = ({
                                                                               fetchLogs,
                                                                               logId
                                                                           }) => {
    const {t} = useTranslation('scaffoldingLogs');
    const themeColors = useThemeColors();

    return (

        <Dialog.Root size={'cover'} placement={"top"}>
            <Dialog.Trigger asChild>
                <Button
                    colorPalette="green"
                    size={"2xs"}
                >
                    {t('edit')}
                    <FaPen/>
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
                                            <EditScaffoldingLogForm
                                                onSuccess={() => {
                                                    fetchLogs();
                                                    store.setOpen(false);
                                                }}
                                                logId={logId}
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
                                    </>
                                )}
                            </Dialog.Context>
                        </Dialog.Content>
                    </Dialog.Positioner>
                </Dialog.Backdrop>
            </Portal>
        </Dialog.Root>
    )
}

export default EditScaffoldingLogDialog;