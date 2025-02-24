import React from "react";
import {Button, DialogBackdrop, Text} from "@chakra-ui/react";
import {
    DialogActionTrigger,
    DialogBody,
    DialogCloseTrigger,
    DialogContent,
    DialogFooter,
    DialogHeader,
    DialogRoot,
    DialogTitle,
} from "@/components/ui/dialog";
import {themeColors} from "@/theme/theme-colors.ts";

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
    return (
        <DialogRoot
            open={isOpen}
            onOpenChange={(open) => !open && onClose()}
            role="alertdialog"
        >
            <DialogBackdrop/>
            <DialogContent backgroundColor={themeColors.bgColorSecondary()}>
                <DialogCloseTrigger/>
                <DialogHeader color={themeColors.fontColor()}>
                    <DialogTitle>{title}</DialogTitle>
                </DialogHeader>
                <DialogBody asChild>
                    <Text color={themeColors.fontColor()}>{message}</Text>
                </DialogBody>
                <DialogFooter>
                    <DialogActionTrigger asChild>
                        <Button variant="outline" onClick={onClose}>
                            {cancelText}
                        </Button>
                    </DialogActionTrigger>
                    <Button colorPalette="red" onClick={onConfirm}>
                        {confirmText}
                    </Button>
                </DialogFooter>
            </DialogContent>
        </DialogRoot>
    );
};

export default ConfirmModal;
