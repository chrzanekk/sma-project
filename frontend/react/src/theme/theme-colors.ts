import {useColorModeValue} from "@/components/ui/color-mode";

export function useThemeColors() {
    const bgColorPrimary = useColorModeValue('gray.300', 'gray.700');
    const bgColorSecondary = useColorModeValue('gray.100', 'gray.600');
    const buttonBgColor = useColorModeValue('gray.300', 'gray.700');
    const popoverBgColor = useColorModeValue('white', 'gray.200');
    const highlightBgColor = useColorModeValue('green.300', 'green.700');
    const borderColor = useColorModeValue('gray.200', 'gray.600');
    const fontColor = useColorModeValue('gray.600', 'gray.300');
    const fontColorHover = useColorModeValue('gray.500', "gray.400");
    const fontColorChildMenu = useColorModeValue('gray.500', 'gray.200');
    return {
        bgColorPrimary,
        bgColorSecondary,
        buttonBgColor,
        popoverBgColor,
        highlightBgColor,
        borderColor,
        fontColor,
        fontColorHover,
        fontColorChildMenu,
    }
}

export function useThemeColorsHex() {
    const bgColorPrimary = useColorModeValue('#fafafa', '#3f3f46');
    const bgColorSecondary = useColorModeValue('#f4f4f5', '#52525b');
    const popoverBgColor = useColorModeValue('#FFFFFF', '#e4e4e7');
    const fontColor = useColorModeValue('#52525b', '#d4d4d8');
    const fontColorHover = useColorModeValue('#a1a1aa', '#a1a1aa');
    const highlightBgColor = useColorModeValue('#86efac', '#116932');
    const borderColor = useColorModeValue('#e4e4e7', '#52525b');

    return {
        bgColorPrimary,
        bgColorSecondary,
        popoverBgColor,
        fontColor,
        fontColorHover,
        highlightBgColor,
        borderColor,
    };
}