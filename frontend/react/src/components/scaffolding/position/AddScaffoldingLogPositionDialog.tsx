import React from "react";
import {useTranslation} from "react-i18next";
import {useThemeColors} from "@/theme/theme-colors.ts";
import {Box, Button, Dialog, Heading, Portal, Text} from "@chakra-ui/react";
import {FaPlus} from "react-icons/fa";
import AddScaffoldingLogPositionForm from "@/components/scaffolding/position/AddScaffoldingLogPositionForm.tsx";

interface AddLogPositionDialogProps {
    fetchPositions: () => void;
}

const AddScaffoldingLogPositionDialog: React.FC<AddLogPositionDialogProps> = ({fetchPositions}) => {
    const {t} = useTranslation('scaffoldingLogPositions');
    const themeColors = useThemeColors();
    return (
        <Box>
            <Dialog.Root size={'full'} placement={"top"}>
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
                                        <Box>
                                            <Dialog.CloseTrigger/>
                                            <Dialog.Header>
                                                {/*<Heading size={"xl"} color={themeColors.fontColor}>*/}
                                                    <Text textAlign={"center"} color={themeColors.fontColor} textStyle={"3xl"}>
                                                        {t("scaffoldingLogPositions:details")}
                                                    </Text>
                                                {/*</Heading>*/}
                                            </Dialog.Header>
                                            <Dialog.Body>
                                                <AddScaffoldingLogPositionForm onSuccess={
                                                    () => {
                                                        fetchPositions();
                                                        store.setOpen(false)
                                                    }
                                                }/>
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