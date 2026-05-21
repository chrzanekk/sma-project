import React from "react";
import {useTranslation} from "react-i18next";
import {useThemeColors} from "@/theme/theme-colors.ts";
import {Box, Button, Dialog, Portal, Text} from "@chakra-ui/react";
import {FaPen} from "react-icons/fa6";
import EditScaffoldingLogPositionForm from "@/components/scaffolding/position/EditScaffoldingLogPositionForm.tsx";

interface EditLogPositionDialogProps {
    positionId: number;
    fetchPositions: () => void;
    triggerLabel?: string;
    triggerIcon?: React.ReactElement;
    triggerColorPalette?: string;
}

const EditScaffoldingLogPositionDialog: React.FC<EditLogPositionDialogProps> = ({
                                                                                    positionId,
                                                                                    fetchPositions,
                                                                                    triggerLabel,
                                                                                    triggerIcon = <FaPen/>,
                                                                                    triggerColorPalette = "blue"
                                                                                }) => {
    const {t} = useTranslation(['scaffoldingLogPositions', 'common']);
    const themeColors = useThemeColors();

    return (
        <Box onClick={(e) => e.stopPropagation()}>
            <Dialog.Root size={{mdDown: 'full', md: "full"}} placement={"bottom"} scrollBehavior={"inside"}>
                <Dialog.Trigger asChild>
                    <Button
                        colorPalette={triggerColorPalette}
                        size={"2xs"}
                        p={1}
                    >
                        {triggerIcon}
                        {triggerLabel || t('edit', {ns: 'scaffoldingLogPositions'})}
                    </Button>
                </Dialog.Trigger>
                <Portal>
                    <Dialog.Backdrop>
                        <Dialog.Positioner>
                            <Dialog.Content bg={themeColors.bgColorSecondary} offset={"4"} borderRadius={"md"}>
                                <Dialog.Context>
                                    {(store) => (
                                        <Box>
                                            <Dialog.CloseTrigger/>
                                            <Dialog.Header>
                                                <Text textAlign={"center"} color={themeColors.fontColor}
                                                      textStyle={"3xl"}>
                                                    {t("scaffoldingLogPositions:editDetails")}
                                                </Text>
                                            </Dialog.Header>
                                            <Dialog.Body>
                                                <Box>
                                                    <EditScaffoldingLogPositionForm
                                                        positionId={positionId}
                                                        onSuccess={() => {
                                                            fetchPositions();
                                                            store.setOpen(false);
                                                        }}
                                                        onClose={() => store.setOpen(false)}
                                                    />
                                                </Box>
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
                    </Dialog.Backdrop>
                </Portal>
            </Dialog.Root>
        </Box>
    )
}

export default EditScaffoldingLogPositionDialog;
