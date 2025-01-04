import { useColorModeValue } from "@/components/ui/color-mode";

export const themeColors = {
    bgColor: () => useColorModeValue('green.400', 'green.700'),
    bgColorLight: () => useColorModeValue('green.400', 'green.600'),
    popoverBgColor: () => useColorModeValue('green.400', 'green.500'),
    highlightBgColor: () => useColorModeValue('green.400', 'green.400'),
    borderColor: () => useColorModeValue('green.400', 'green.600'),
    fontColor: () => useColorModeValue('green.600', 'green.100'),
    fontColorChildMenu: () => useColorModeValue('green.800', 'green.100')
};
