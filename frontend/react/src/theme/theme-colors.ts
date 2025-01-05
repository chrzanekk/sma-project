import { useColorModeValue } from "@/components/ui/color-mode";

export const themeColors = {
    bgColor: () => useColorModeValue('green.300', 'green.700'),
    bgColorLight: () => useColorModeValue('green.100', 'green.600'),
    popoverBgColor: () => useColorModeValue('white', 'green.500'),
    highlightBgColor: () => useColorModeValue('green.400', 'green.600'),
    borderColor: () => useColorModeValue('green.100', 'green.600'),
    fontColor: () => useColorModeValue('green.600', 'green.400'),
    fontColorChildMenu: () => useColorModeValue('green.800', 'green.300')
};
