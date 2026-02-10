import React from "react";
import {useTranslation} from "react-i18next";
import {useThemeColors} from "@/theme/theme-colors.ts";
import {Box, Button, Dialog, Portal} from "@chakra-ui/react";
import {FaPlus} from "react-icons/fa";
import {BaseScaffoldingLogPositionFormValues} from "@/types/scaffolding-log-position-types.ts";
import TechnicalProtocolForm from "@/components/scaffolding/protocol/TechnicalProtocolForm.tsx";

interface TechnicalProtocolDialogProps {
    position: BaseScaffoldingLogPositionFormValues;
    triggerLabel?: string;
    triggerIcon?: React.ReactElement;
    triggerColorPalette?: string;
}

const TechnicalProtocolDialog: React.FC<TechnicalProtocolDialogProps> = ({
                                                                             position,
                                                                             triggerLabel,
                                                                             triggerIcon = <FaPlus/>,
                                                                             triggerColorPalette = "green"
                                                                         }) => {
    const {t} = useTranslation('scaffoldingLogPositions');
    const themeColors = useThemeColors();

    return (
        <Box onClick={(e) => e.stopPropagation()}>
            <Dialog.Root size={"lg"} scrollBehavior={"inside"}>
                <Dialog.Trigger asChild>
                    <Button
                        colorPalette={triggerColorPalette}
                        size={"2xs"}
                        p={1}
                    >{triggerIcon}
                        {triggerLabel || t('generateProtocol', {ns: 'scaffoldingLogPositions'})}
                    </Button>
                </Dialog.Trigger>
                <Portal>
                    <Dialog.Backdrop/>
                    <Dialog.Positioner>
                        <Dialog.Content bg={themeColors.bgColorSecondary} offset={"4"} borderRadius={"md"} maxH="85vh">


                            <Dialog.CloseTrigger/>
                            <Dialog.Body>
                                <TechnicalProtocolForm
                                    position={
                                        position}
                                />
                            </Dialog.Body>
                            <Dialog.Footer>
                                <Dialog.ActionTrigger asChild>
                                    <Button
                                        colorPalette="red"
                                    >
                                        {t("close", {ns: "common"})}
                                    </Button>
                                </Dialog.ActionTrigger>
                            </Dialog.Footer>
                        </Dialog.Content>
                    </Dialog.Positioner>
                </Portal>
            </Dialog.Root>
        </Box>
    )
}

export default TechnicalProtocolDialog;