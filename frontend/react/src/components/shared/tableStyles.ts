import {useThemeColors} from "@/theme/theme-colors.ts";

export const useTableStyles = () => {
    const themeColors = useThemeColors();

    const commonCellProps = {
        textAlign: "center",
        borderColor: "gray.400",
        whiteSpace: "normal",
        // wordBreak: "break-word",
        // overflowWrap: "break-word"
    };

    const commonColumnHeaderProps = {
        cursor: "pointer",
        color: themeColors.fontColor,
        textAlign: "center",
        borderColor: "gray.400",
        fontSize: "x-small",
        whiteSpace: "normal",
        // wordBreak: "break-word",
        // overflowWrap: "break-word"
    };

    return {commonCellProps, commonColumnHeaderProps};
};