"use client";

import React, {useEffect, useState} from "react";
import {Box, Icon} from "@chakra-ui/react";
import {FaMoon, FaSun} from "react-icons/fa6";
import {Switch} from "@/components/ui/switch.tsx";

export const ThemeToggle: React.FC = () => {
    const [theme, setTheme] = useState<"light" | "dark">("light");

    useEffect(() => {
        const stored = localStorage.getItem("chakra-ui-color-mode") as "light" | "dark" | null;
        const prefersDark = window.matchMedia("(prefers-color-scheme: dark)").matches;
        const initial = stored ?? (prefersDark ? "dark" : "light");
        setTheme(initial);
    }, []);

    const toggleTheme = () => {
        const newTheme = theme === "light" ? "dark" : "light";
        setTheme(newTheme);
        localStorage.setItem("chakra-ui-color-mode", newTheme);
        document.documentElement.classList.toggle("dark", newTheme === "dark");
    };

    return (
        <Switch
            colorPalette="teal"
            checked={theme === "dark"}
            onChange={toggleTheme}
            size="lg"
            trackLabel={{
                on: (
                    <Icon color={"green.500"}>
                        <Box>
                            <FaMoon />
                        </Box>
                    </Icon>
                ),
                off: (
                    <Icon color={"yellow.500"}>
                        <Box>
                            <FaSun />
                        </Box>
                    </Icon>
                ),
            }}
        />
    );
};
