import { useColorModeValue } from "@/components/ui/color-mode";

export const themeColors = {
    bgColorPrimary: () => useColorModeValue('gray.300', 'gray.700'),
    bgColorSecondary: () => useColorModeValue('gray.100', 'gray.600'),
    buttonBgColor: () => useColorModeValue('gray.300', 'gray.700'),
    popoverBgColor: () => useColorModeValue('white', 'gray.200'),
    highlightBgColor: () => useColorModeValue('green.300', 'green.700'),
    borderColor: () => useColorModeValue('gray.200', 'gray.600'),
    fontColor: () => useColorModeValue('gray.600', 'gray.300'),
    fontColorHover: () => useColorModeValue('gray.100',"gray.400"),
    fontColorChildMenu: () => useColorModeValue('gray.500', 'gray.200')
};


export const themeColorsHex = {
    bgColorPrimary: () => useColorModeValue('#fafafa', '#3f3f46'), // 'gray.200', 'gray.600'
    bgColorSecondary: () => useColorModeValue('#f4f4f5', '#52525b'), // 'gray.100', 'gray.600'
    popoverBgColor: () => useColorModeValue('#FFFFFF', '#e4e4e7'), // white | gray.200
    fontColor: () => useColorModeValue('#52525b', '#d4d4d8'), // gray.600 | gray.200
    fontColorHover: () => useColorModeValue('#3f3f46', '#e4e4e7'),// gray.500 | gray.200
    highlightBgColor: () => useColorModeValue('#86efac', '#116932'),
    borderColor: () => useColorModeValue('#e4e4e7', '#52525b'), // gray.200 | gray.600
};