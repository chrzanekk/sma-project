import React from "react";
import {toaster} from "@/components/ui/toaster.tsx"

const notifications = (title: string, description: React.ReactNode, status: "success" | "error" | "info" | "warning") => {
    toaster.create({
        title,
        description,
        type: status,
        duration: 2000,
    });
};

export const successNotification = (title: string, description: React.ReactNode) => {
    notifications(title, description, "success");
};

export const errorNotification = (title: string, description: React.ReactNode) => {
    notifications(title, description, "error");
};
