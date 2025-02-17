import React from "react";
import {
    Button,
    DialogBackdrop,
    DialogBody,
    DialogContent,
    DialogFooter,
    DialogHeader,
    DialogRoot,
    Heading,
} from "@chakra-ui/react";
import {themeColors} from "@/theme/theme-colors.ts";
import {DialogActionTrigger} from "@/components/ui/dialog.tsx";

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
                                                       cancelText = "Cancel"
                                                   }) => {
    return (
        <DialogRoot open={isOpen} onOpenChange={(open) => !open && onClose()}>
            <DialogBackdrop/>
            <DialogContent backgroundColor={themeColors.bgColorSecondary()}>
                <DialogHeader>
                    <Heading size={"xl"}>
                        {title}
                    </Heading>
                </DialogHeader>
                <DialogBody>{message}</DialogBody>
                <DialogFooter>
                    <Button
                        colorPalette="red"
                        onClick={onConfirm}
                        mr={3}
                    >
                        {confirmText}
                    </Button>
                    <DialogActionTrigger asChild>
                        <Button colorPalette="green"
                                onClick={onClose}
                        >{cancelText}</Button>
                    </DialogActionTrigger>
                </DialogFooter>
            </DialogContent>
        </DialogRoot>
    );
};

export default ConfirmModal;
