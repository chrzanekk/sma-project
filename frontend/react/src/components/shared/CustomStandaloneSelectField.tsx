// components/shared/CustomStandaloneSelectField.tsx
import React from "react";
import {Box, Text} from "@chakra-ui/react";
import Select, {StylesConfig} from "react-select";
import {useThemeColors} from "@/theme/theme-colors.ts";
import {getSelectStyles} from "@/components/shared/formOptions.ts";
import {themeVars} from "@/theme/theme-colors.ts";

export interface SelectOption {
    value: string;
    label: string;
}

interface CustomStandaloneSelectFieldProps {
    label?: string;
    placeholder?: string;
    options: SelectOption[];
    value: string[];  // Array of selected values for multi-select
    onChange: (selectedValues: string[]) => void;
    isMulti?: boolean;
    width?: string;
    bgColor?: string;
    disabled?: boolean;
}

const CustomStandaloneSelectField: React.FC<CustomStandaloneSelectFieldProps> = ({
                                                                                     label,
                                                                                     placeholder,
                                                                                     options,
                                                                                     value,
                                                                                     onChange,
                                                                                     isMulti = false,
                                                                                     width,
                                                                                     bgColor,
                                                                                     disabled = false,
                                                                                 }) => {
    const themeColors = useThemeColors();
    const selectStyles = getSelectStyles();

    // Convert value array to selected options
    const selectedValue = isMulti
        ? options.filter((option) => value.includes(option.value))
        : options.find((option) => option.value === value[0]) || null;

    const customSelectStyles: StylesConfig<SelectOption, boolean> = {
        ...selectStyles,
        control: (provided, state) => {
            const baseControl = selectStyles.control
                ? selectStyles.control(provided, state)
                : provided;
            return {
                ...baseControl,
                backgroundColor: bgColor ?? baseControl.backgroundColor,
                width: width ? width : "auto",
                minWidth: "auto",
            };
        },
        input: (provided) => ({
            ...provided,
            color: themeColors.fontColor
        }),
        multiValue: (provided) => ({
            ...provided,
            backgroundColor: themeVars.bgColorPrimary,
            borderRadius: "4px",
        }),
        multiValueLabel: (provided) => ({
            ...provided,
            color: themeVars.fontColor,
            padding: "2px 6px",
        }),
        multiValueRemove: (provided) => ({
            ...provided,
            color: themeVars.fontColor,
            cursor: "pointer",
            "&:hover": {
                backgroundColor: themeVars.highlightBgColor,
                color: themeVars.fontColorHover,
            },
        }),
        menu: (provided) => ({
            ...provided,
            backgroundColor: themeVars.bgColorSecondary,
            borderRadius: "4px",
        }),
        menuList: (provided) => ({
            ...provided,
            backgroundColor: themeVars.bgColorSecondary,
            padding: 0,
        }),
        option: (provided, state) => ({
            ...provided,
            borderColor: themeVars.bgColorPrimary,
            backgroundColor: state.isSelected
                ? themeVars.bgColorPrimary
                : state.isFocused
                    ? themeVars.highlightBgColor
                    : themeVars.bgColorPrimary,
            color: themeVars.fontColor,
            cursor: "pointer",
            "&:hover": {
                backgroundColor: themeVars.highlightBgColor,
                color: themeVars.fontColorHover,
            },
        }),
        singleValue: (provided) => ({
            ...provided,
            color: themeVars.fontColor,
        }),
        placeholder: (provided) => ({
            ...provided,
            color: themeVars.fontColor,
        }),
    };

    return (
        <Box mb={2}>
            {label && (
                <Text
                    fontSize="sm"
                    fontWeight="bold"
                    mb="1"
                    color={themeColors.fontColor}
                    textAlign={"center"}
                >
                    {label}
                </Text>
            )}
            <Select
                options={options}
                isDisabled={disabled}
                placeholder={placeholder}
                aria-label={placeholder}
                value={selectedValue}
                isMulti={isMulti}
                onChange={(selectedOption: any) => {
                    if (isMulti) {
                        const values = selectedOption
                            ? selectedOption.map((opt: SelectOption) => opt.value)
                            : [];
                        onChange(values);
                    } else {
                        onChange(selectedOption ? [selectedOption.value] : []);
                    }
                }}
                styles={customSelectStyles}
            />
        </Box>
    );
};

export default CustomStandaloneSelectField;
