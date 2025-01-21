import React from "react";
import {
    Button,
    DialogRoot,
    DialogBackdrop,
    DialogContent,
    DialogHeader,
    DialogBody,
    DialogFooter,
    DialogCloseTrigger,
} from "@chakra-ui/react";
import { themeColors } from "@/theme/theme-colors.ts";

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
                                                       cancelText = "Cancel"}) => {
    return (
        <DialogRoot open={isOpen} onOpenChange={(open) => !open && onClose()}>
            <DialogBackdrop />
            <DialogContent backgroundColor={themeColors.bgColorPrimary()}>
                <DialogHeader>{title}</DialogHeader>
                <DialogBody>{message}</DialogBody>
                <DialogFooter>
                    <Button
                        colorScheme="red"
                        onClick={onConfirm}
                        mr={3}
                    >
                        {confirmText}
                    </Button>
                    <DialogCloseTrigger>
                        <Button colorScheme="green">{cancelText}</Button>
                    </DialogCloseTrigger>
                </DialogFooter>
            </DialogContent>
        </DialogRoot>
    );
};

export default ConfirmModal;
