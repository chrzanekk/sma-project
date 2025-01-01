import React from "react";
import {createToaster} from "@chakra-ui/react";

const toaster = createToaster({
    max: 3,
    duration: 3000,
    placement: "bottom",
    overlap: true
});

const notifications = (title: string, description: React.ReactNode, status: "success" | "error" | "info" | "warning") => {
    toaster.create(({
        title,
        description,
        type: status
    }));
};

export const successNotification = (title: string, description: React.ReactNode) => {
    notifications(title, description, "success");
};

export const errorNotification = (title: string, description: React.ReactNode) => {
    notifications(title, description, "error");
};
