// src/notifications/notifications.ts
import { createStandaloneToast } from '@chakra-ui/react';

const { toast } = createStandaloneToast();

const notifications = (title: string, description: string, status: "success" | "error" | "info" | "warning") => {
    toast({
        title,
        description,
        status,
        isClosable: true,
        duration: 8000,
    });
};

export const successNotification = (title: string, description: string) => {
    notifications(title, description, "success");
};

export const errorNotification = (title: string, description: string) => {
    notifications(title, description, "error");
};
