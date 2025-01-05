"use client";

import React from "react";
import {Button} from "@chakra-ui/react";
import {MenuContent, MenuItem, MenuRoot, MenuTrigger} from "@/components/ui/menu.tsx";
import {useTheme} from "next-themes";
import {FaMoon, FaSun} from "react-icons/fa";
import {themeColors} from "@/theme/theme-colors.ts";

export const ThemeToggle: React.FC = () => {
    const {setTheme} = useTheme();

    return (
        <MenuRoot>
            <MenuTrigger asChild>
                <Button
                    bg={themeColors.bgColor()}
                    ml={1}
                    color={themeColors.fontColor()}
                    _hover={{
                        textDecoration: 'none',
                        bg: themeColors.highlightBgColor(),
                        color: themeColors.popoverBgColor()
                    }}
                    as={Button}
                    variant="outline"
                    size="sm"
                >
                    <FaSun/>
                </Button>
            </MenuTrigger>
            <MenuContent>
                <MenuItem
                    value="light-theme"
                    onClick={() => setTheme("light")}
                >
                    <FaSun/>
                    Jasny motyw
                </MenuItem>
                <MenuItem
                    value="dark-theme"
                    onClick={() => setTheme("dark")}
                >
                    <FaMoon/>
                    Ciemny motyw
                </MenuItem>
            </MenuContent>

        </MenuRoot>
    );
};
