"use client";

import React, {useEffect, useState} from "react";
import {Box, HStack} from "@chakra-ui/react";
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
        <HStack>
            <Box as={FaSun} color="green.500"/>
            <Switch
                colorScheme="green"
                checked={theme === "dark"}
                onChange={() => setTheme(theme === "light" ? "dark" : "light")}
                size="sm"
            />
            <Box as={FaMoon} color="green.500"/>
        </HStack>
    );
};
