import React from "react";
import {Button, Modal, ModalBody, ModalContent, ModalFooter, ModalHeader, ModalOverlay,} from "@chakra-ui/react";
import {themeColors} from "@/theme/theme-colors.ts";

interface ConfirmModalProps {
    isOpen: boolean;
    onClose: () => void;
    onConfirm: () => void;
    title: string;
    message: string;
    confirmText?: string;
    cancelText?: string;
    isLoading?: boolean;
}

const ConfirmModal: React.FC<ConfirmModalProps> = ({
                                                       isOpen,
                                                       onClose,
                                                       onConfirm,
                                                       title,
                                                       message,
                                                       confirmText = "Confirm",
                                                       cancelText = "Cancel",
                                                       isLoading = false,
                                                   }) => {
    return (
        <Modal isOpen={isOpen} onClose={onClose} isCentered>
            <ModalOverlay/>
            <ModalContent bg={themeColors.bgColor()}>
                <ModalHeader>{title}</ModalHeader>
                <ModalBody>{message}</ModalBody>
                <ModalFooter>
                    <Button colorScheme="red" onClick={onConfirm} mr={3} isLoading={isLoading}>
                        {confirmText}
                    </Button>
                    <Button
                        colorScheme="green"
                        onClick={onClose}>{cancelText}</Button>
                </ModalFooter>
            </ModalContent>
        </Modal>
    );
};

export default ConfirmModal;
