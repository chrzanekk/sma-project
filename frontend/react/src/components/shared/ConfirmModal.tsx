import React from "react";
import {Button, Dialog, Portal, Text} from "@chakra-ui/react";
import {useThemeColors} from "@/theme/theme-colors.ts";

interface ConfirmModalProps {
    isOpen: boolean;
    onClose: () => void;
    onConfirm: () => void;
    title: string;
    message: string;
    confirmText?: string;
    cancelText?: string;
}

const ConfirmModal: React.FC<ConfirmModalProps> = ({
                                                       isOpen,
                                                       onClose,
                                                       onConfirm,
                                                       title,
                                                       message,
                                                       confirmText = "Confirm",
                                                       cancelText = "Cancel",
                                                   }) => {
    const themeColors = useThemeColors();
    return (
        <Dialog.Root
            open={isOpen}
            onOpenChange={(open) => !open && onClose()}
            role="alertdialog"
        >
            <Portal>
                <Dialog.Backdrop/>
                <Dialog.Positioner>
                    <Dialog.Content backgroundColor={themeColors.bgColorSecondary}>
                        <Dialog.CloseTrigger/>
                        <Dialog.Header color={themeColors.fontColor}>
                            <Dialog.Title>{title}</Dialog.Title>
                        </Dialog.Header>
                        <Dialog.Body asChild>
                            <Text color={themeColors.fontColor}>{message}</Text>
                        </Dialog.Body>
                        <Dialog.Footer>
                            <Dialog.ActionTrigger asChild>
                                <Button variant="outline" onClick={onClose}>
                                    {cancelText}
                                </Button>
                            </Dialog.ActionTrigger>
                            <Button colorPalette="red" onClick={onConfirm}>
                                {confirmText}
                            </Button>
                        </Dialog.Footer>
                    </Dialog.Content>
                </Dialog.Positioner>
            </Portal>
        </Dialog.Root>
    );
};

export default ConfirmModal;
