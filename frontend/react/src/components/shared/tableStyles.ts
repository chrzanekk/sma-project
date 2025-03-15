import {themeColors} from "@/theme/theme-colors.ts";

export const useTableStyles = () => {
    // Przykład: zwróć kolor w zależności od trybu (jasny/ciemny)
    const fontColor = themeColors.fontColor()

    const commonCellProps = {
        textAlign: "center",
        borderColor: "gray",
    };

    const commonColumnHeaderProps = {
        cursor: "pointer",
        color: fontColor,
        textAlign: "center",
        borderColor: "gray",
    };

    return {commonCellProps, commonColumnHeaderProps};
};