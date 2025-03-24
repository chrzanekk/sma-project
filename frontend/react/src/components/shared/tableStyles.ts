import {useThemeColors} from "@/theme/theme-colors.ts";

export const useTableStyles = () => {
    const themeColors = useThemeColors();

    const commonCellProps = {
        textAlign: "center",
        borderColor: "gray.400"
    };

    const commonColumnHeaderProps = {
        cursor: "pointer",
        color: themeColors.fontColor,
        textAlign: "center",
        borderColor: "gray.400",
        fontSize: "x-small"
    };

    return {commonCellProps, commonColumnHeaderProps};
};