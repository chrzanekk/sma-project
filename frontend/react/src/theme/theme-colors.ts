import {useColorModeValue} from "@/components/ui/color-mode.tsx";

export function useThemeColors() {
    return {
        bgColorPrimary: "bgColorPrimary",
        bgColorSecondary: "bgColorSecondary",
        buttonBgColor: "buttonBgColor",
        popoverBgColor: "popoverBgColor",
        highlightBgColor: "highlightBgColor",
        borderColor: "borderColor",
        fontColor: "fontColor",
        fontColorHover: "fontColorHover",
        fontColorChildMenu: "fontColorChildMenu",
    }
}

const tokenVar = (name: string) => `var(--colors-${name})`;

export const themeVars = {
    bgColorPrimary: tokenVar("bgColorPrimary"),
    bgColorSecondary: tokenVar("bgColorSecondary"),
    buttonBgColor: tokenVar("buttonBgColor"),
    popoverBgColor: tokenVar("popoverBgColor"),
    highlightBgColor: tokenVar("highlightBgColor"),
    borderColor: tokenVar("borderColor"),
    fontColor: tokenVar("fontColor"),
    fontColorHover: tokenVar("fontColorHover"),
    fontColorChildMenu: tokenVar("fontColorChildMenu"),
};

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
