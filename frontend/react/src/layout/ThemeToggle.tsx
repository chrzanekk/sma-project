"use client";

import React, {useEffect, useState} from "react";
import {Icon} from "@chakra-ui/react";
import {useTheme} from "next-themes";
import {FaMoon, FaSun} from "react-icons/fa6";
import {Switch} from "@/components/ui/switch.tsx";

export const ThemeToggle: React.FC = () => {
    const {theme, setTheme} = useTheme();
    const [mounted, setMounted] = useState(false);

    useEffect(() => {
        setMounted(true);
    }, []);

    if (!mounted) return null;

    return (

        <Switch
            colorPalette="teal"
            checked={theme === "dark"}
            onChange={() => setTheme(theme === "light" ? "dark" : "light")}
            size="lg"
            trackLabel={{
                on: (
                    <Icon color={"green.500"}>
                        <FaMoon/>
                    </Icon>
                ),
                off: (
                    <Icon color={"yellow.500"}>
                        <FaSun/>
                    </Icon>
                ),
            }}
        />
    );
};
