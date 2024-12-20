import {createStandaloneToast} from '@chakra-ui/react';
import React from "react";

const {toast} = createStandaloneToast();

const notifications = (title: string, description: React.ReactNode, status: "success" | "error" | "info" | "warning") => {
    toast({
        title,
        description,
        status,
        isClosable: true,
        duration: 8000,
    });
};

export const successNotification = (title: string, description: React.ReactNode) => {
    notifications(title, description, "success");
};

export const errorNotification = (title: string, description: React.ReactNode) => {
    notifications(title, description, "error");
};
