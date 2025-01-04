import { useColorModeValue } from "@/components/ui/color-mode";

export const themeColors = {
    bgColor: () => useColorModeValue('green.100', 'green.700'),
    bgColorLight: () => useColorModeValue('green.100', 'green.600'),
    popoverBgColor: () => useColorModeValue('green.100', 'green.500'),
    highlightBgColor: () => useColorModeValue('green.100', 'green.600'),
    borderColor: () => useColorModeValue('green.100', 'green.600'),
    fontColor: () => useColorModeValue('green.100', 'green.400'),
    fontColorChildMenu: () => useColorModeValue('green.100', 'green.300')
};
