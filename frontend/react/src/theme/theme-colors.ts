import { useColorModeValue } from "@/components/ui/color-mode";

export const themeColors = {
    bgColor: () => useColorModeValue('green.300', 'green.800'),
    bgColorLight: () => useColorModeValue('green.100', 'green.600'),
    popoverBgColor: () => useColorModeValue('white', 'green.500'),
    highlightBgColor: () => useColorModeValue('green.400', 'green.100'),
    borderColor: () => useColorModeValue('green.100', 'green.600'),
    fontColor: () => useColorModeValue('green.600', 'green.100'),
    fontColorChildMenu: () => useColorModeValue('green.800', 'green.100')
};
