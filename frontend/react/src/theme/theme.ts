// src/theme/theme.ts
import { createSystem, defaultConfig, defineConfig } from "@chakra-ui/react";

/**
 * Mapujemy TWOJE nazwy na semantic tokens.
 * Ważne: w v3 wartości warunkowe muszą być w formacie:
 * value: { base: "{colors.xxx}", _dark: "{colors.yyy}" }
 */
const config = defineConfig({
    theme: {
        semanticTokens: {
            colors: {
                bgColorPrimary: {
                    value: { base: "{colors.gray.300}", _dark: "{colors.gray.700}" },
                },
                bgColorSecondary: {
                    value: { base: "{colors.gray.100}", _dark: "{colors.gray.600}" },
                },
                buttonBgColor: {
                    value: { base: "{colors.gray.300}", _dark: "{colors.gray.700}" },
                },
                popoverBgColor: {
                    value: { base: "{colors.white}", _dark: "{colors.gray.200}" },
                },
                highlightBgColor: {
                    value: { base: "{colors.green.300}", _dark: "{colors.green.700}" },
                },
                borderColor: {
                    value: { base: "{colors.gray.200}", _dark: "{colors.gray.600}" },
                },
                fontColor: {
                    value: { base: "{colors.gray.600}", _dark: "{colors.gray.300}" },
                },
                fontColorHover: {
                    value: { base: "{colors.gray.500}", _dark: "{colors.gray.400}" },
                },
                fontColorChildMenu: {
                    value: { base: "{colors.gray.500}", _dark: "{colors.gray.200}" },
                },
            },
        },
    },
});

export const system = createSystem(defaultConfig, config);
