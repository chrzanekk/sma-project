import {themeColors} from "@/theme/theme-colors.ts";

export const useTableStyles = () => {
    // Przykład: zwróć kolor w zależności od trybu (jasny/ciemny)
    const fontColor = themeColors.fontColor()

    const commonCellProps = {
        textAlign: "center",
        borderColor: "gray.400"
    };

    const commonColumnHeaderProps = {
        cursor: "pointer",
        color: fontColor,
        textAlign: "center",
        borderColor: "gray.400",
        fontSize: "x-small"
    };

    return {commonCellProps, commonColumnHeaderProps};
};