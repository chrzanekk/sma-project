import { useColorModeValue } from "@/components/ui/color-mode";

export const themeColors = {
    bgColor: () => useColorModeValue('gray.50', 'gray.800'),
    bgColorLight: () => useColorModeValue('gray.200', 'gray.700'),
    popoverBgColor: () => useColorModeValue('white', 'gray.200'),
    highlightBgColor: () => useColorModeValue('gray.100', 'gray.850'),
    borderColor: () => useColorModeValue('gray.100', 'gray.600'),
    fontColor: () => useColorModeValue('gray.600', 'gray.300'),
    fontColorChildMenu: () => useColorModeValue('gray.500', 'gray.200')
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