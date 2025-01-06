import { useColorModeValue } from "@/components/ui/color-mode";

export const themeColors = {
    bgColor: () => useColorModeValue('green.400', 'green.700'),
    bgColorLight: () => useColorModeValue('green.300', 'green.500'),
    popoverBgColor: () => useColorModeValue('white', 'green.500'),
    highlightBgColor: () => useColorModeValue('green.400', 'green.600'),
    borderColor: () => useColorModeValue('green.100', 'green.600'),
    fontColor: () => useColorModeValue('green.600', 'green.500'),
    fontColorChildMenu: () => useColorModeValue('green.800', 'green.300')
};


export const themeColorsHex = {
    bgColor: () => useColorModeValue('#4ade80', '#116932'), // green.300 (jasny zielony) | green.700 (ciemny zielony)
    bgColorLight: () => useColorModeValue('#86efac', '#22c55e'), // green.100 (bardzo jasny zielony) | green.600
    popoverBgColor: () => useColorModeValue('#FFFFFF', '#22c55e'), // white | green.500
    highlightBgColor: () => useColorModeValue('#4ade80', '#116932'), // green.400 | green.700
    borderColor: () => useColorModeValue('#dcfce7', '#16a34a'), // green.100 | green.600
    fontColor: () => useColorModeValue('#16a34a', '#86efac'), // green.600 | green.300
    fontColorChildMenu: () => useColorModeValue('#124a28', '#86efac'), // green.800 | green.300
};