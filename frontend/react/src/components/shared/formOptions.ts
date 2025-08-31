import {StylesConfig} from "react-select";
import {themeVars} from "@/theme/theme-colors";

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
        return {
            control: (provided) => ({
                ...provided,
                backgroundColor: themeVars.bgColorPrimary,
                color: themeVars.fontColor,
                borderRadius: "5px",
                boxShadow: "none",
                minHeight: "2rem",
                height: "36px",
                fontSize: "0.875rem",
                minWidth: "150px"
            }),
            placeholder: (provided) => ({
                ...provided,
                color: themeVars.fontColor
            }),
            singleValue: (provided) => ({
                ...provided,
                color: themeVars.fontColor
            }),
            menuList: (provided) => ({
                ...provided,
                padding: 0
            }),
            option: (provided, state) => ({
                ...provided,
                backgroundColor: state.isSelected
                    ? themeVars.bgColorPrimary
                    : themeVars.bgColorSecondary,
                color: state.isSelected
                    ? themeVars.fontColor
                    : themeVars.fontColor,
                cursor: "pointer",
                "&:hover": {
                    backgroundColor: themeVars.highlightBgColor,
                    color: themeVars.fontColorHover
                }
            })
        }
    }
;
