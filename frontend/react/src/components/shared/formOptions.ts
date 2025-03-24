import {StylesConfig} from "react-select";
import {useThemeColorsHex} from "@/theme/theme-colors";

// -----------------------------
// Opcje boolean i style dla react-select
// -----------------------------

export interface BooleanOption {
    value?: boolean;
    label: string;
}

export const getBooleanOptions = (
    t: (key: string, options?: any) => string
): BooleanOption[] => [
    {value: undefined, label: t("empty", {ns: "common"})},
    {value: true, label: t("yes", {ns: "common"})},
    {value: false, label: t("no", {ns: "common"})}
];

export const getSelectStyles = (): StylesConfig<any, boolean> => {
        const themeColorsHex = useThemeColorsHex();
        return {
            control: (provided) => ({
                ...provided,
                backgroundColor: themeColorsHex.bgColorPrimary,
                color: themeColorsHex.fontColor,
                borderRadius: "5px",
                boxShadow: "none",
                minHeight: "2rem",
                height: "36px",
                fontSize: "0.875rem",
                minWidth: "150px"
            }),
            placeholder: (provided) => ({
                ...provided,
                color: themeColorsHex.fontColor
            }),
            singleValue: (provided) => ({
                ...provided,
                color: themeColorsHex.fontColor
            }),
            menuList: (provided) => ({
                ...provided,
                padding: 0
            }),
            option: (provided, state) => ({
                ...provided,
                backgroundColor: state.isSelected
                    ? themeColorsHex.bgColorPrimary
                    : themeColorsHex.bgColorSecondary,
                color: state.isSelected
                    ? themeColorsHex.fontColor
                    : themeColorsHex.fontColor,
                cursor: "pointer",
                "&:hover": {
                    backgroundColor: themeColorsHex.highlightBgColor,
                    color: themeColorsHex.fontColorHover
                }
            })
        }
    }
;
