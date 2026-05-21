import React from "react";
import {useTranslation} from "react-i18next";
import {useThemeColors} from "@/theme/theme-colors.ts";
import {Box, Button, Dialog, Portal, Text} from "@chakra-ui/react";
import {FaPlus} from "react-icons/fa";
import AddScaffoldingLogPositionForm from "@/components/scaffolding/position/AddScaffoldingLogPositionForm.tsx";
import {ScaffoldingLogPositionBaseDTO} from "@/types/scaffolding-log-position-types.ts";

interface AddLogPositionDialogProps {
    fetchPositions: () => void;
    scaffoldingLogId?: number;
    parentPosition?: ScaffoldingLogPositionBaseDTO;
    triggerLabel?: string;
    triggerIcon?: React.ReactElement;
    triggerColorPalette?: string;
}

const AddScaffoldingLogPositionDialog: React.FC<AddLogPositionDialogProps> = ({
                                                                                  fetchPositions,
                                                                                  scaffoldingLogId,
                                                                                  parentPosition,
                                                                                  triggerLabel,
                                                                                  triggerIcon = <FaPlus/>,
                                                                                  triggerColorPalette = "green"
                                                                              }) => {
    const {t} = useTranslation('scaffoldingLogPositions');
    const themeColors = useThemeColors();

    return (
        <Box onClick={(e) => e.stopPropagation()}>
            <Dialog.Root size={{mdDown: 'full', md: "full"}} placement={"bottom"} scrollBehavior={"inside"}>
                <Dialog.Trigger asChild>
                    <Button
                        colorPalette={triggerColorPalette}
                        size={"2xs"}
                        p={1}
                    >{triggerIcon}
                        {triggerLabel || t('add', {ns: 'common'})}
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
                                                    {t("scaffoldingLogPositions:details")}
                                                </Text>
                                            </Dialog.Header>
                                            <Dialog.Body>
                                                <Box>
                                                    <AddScaffoldingLogPositionForm
                                                        onSuccess={
                                                            () => {
                                                                fetchPositions();
                                                                store.setOpen(false)
                                                            }}
                                                        scaffoldingLogId={scaffoldingLogId}
                                                        parentPosition={parentPosition}
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

export default AddScaffoldingLogPositionDialog;