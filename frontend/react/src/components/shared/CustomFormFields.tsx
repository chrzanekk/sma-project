import React from "react";
import {Field, FieldProps, useField, useFormikContext} from "formik";
import {Box, Button, Input, Text, Textarea} from "@chakra-ui/react";
import Select, {StylesConfig} from "react-select";
import {useThemeColors, useThemeColorsHex} from "@/theme/theme-colors";
import {getSelectStyles} from "@/components/shared/formOptions.ts";
import {useTranslation} from "react-i18next";
// -----------------------------
// Komponenty formularzowe
// -----------------------------

export interface CustomInputFilterFieldProps {
    name: string;
    placeholder: string;
}

export const CustomInputFilterField: React.FC<CustomInputFilterFieldProps> = ({name, placeholder}) => {
    const themeColors = useThemeColors();
    const themeColorsHex = useThemeColorsHex();
    return <Field
        name={name}
        as={Input}
        color={themeColors.fontColor}
        placeholder={placeholder}
        _placeholder={{color: themeColorsHex.fontColor}}
        size="sm"
        bg={themeColors.bgColorSecondary}
        borderRadius="md"
        width="150px"
    />
};


interface CustomInputFieldProps {
    name: string;
    label?: string;
    placeholder?: string;
    type?: string;
    width?: string;
    disabled?: boolean;
}

const CustomInputField: React.FC<CustomInputFieldProps> = ({
                                                               name,
                                                               label,
                                                               placeholder,
                                                               type = "text",
                                                               width,
                                                               disabled
                                                           }) => {
    const themeColors = useThemeColors();
    const themeColorsHex = useThemeColorsHex();
    return (
        <Field name={name}>
            {({field, meta}: FieldProps) => (
                <Box mb={2}>
                    {label && (
                        <Text fontSize="sm" fontWeight="bold" mb="1" color={themeColors.fontColor}>
                            {label}
                        </Text>
                    )}
                    <Input
                        {...field}
                        placeholder={placeholder}
                        _placeholder={{color: themeColorsHex.fontColor}}
                        type={type}
                        size="sm"
                        color={themeColors.fontColor}
                        bg={themeColors.bgColorPrimary}
                        borderRadius="md"
                        width={width || "100%"}
                        disabled={disabled}
                    />
                    {meta.touched && meta.error && (
                        <Text color="red.500" fontSize="xs" mt="1">
                            {meta.error}
                        </Text>
                    )}
                </Box>
            )}
        </Field>
    );
};


export interface CustomSelectFieldProps {
    name: string;
    label?: string;
    placeholder?: string;
    options: any[];
    isMulti?: boolean;
    width?: string;
    bgColor?: string;
    disabled?: boolean
}

const CustomSelectField: React.FC<CustomSelectFieldProps> = ({
                                                                 name,
                                                                 label,
                                                                 placeholder,
                                                                 options,
                                                                 isMulti = false,
                                                                 width,
                                                                 bgColor,
                                                                 disabled = false,
                                                             }) => {
    const {setFieldValue, setFieldTouched} = useFormikContext<any>();
    const themeColors = useThemeColors();
    const [field, meta] = useField(name);
    const selectStyles = getSelectStyles();

    const selectedValue = isMulti
        ? options.filter((option) => Array.isArray(field.value) && field.value.includes(option.value))
        : field.value === undefined
            ? null
            : options.find((option) => option.value === field.value) || null;

    const customSelectStyles: StylesConfig<any, boolean> = {
        ...selectStyles,
        control: (provided, state) => {
            const baseControl = selectStyles.control
                ? selectStyles.control(provided, state)
                : provided;
            return {
                ...baseControl,
                backgroundColor: bgColor ?? baseControl.backgroundColor,
                width: width ? width : "auto",
            };
        },
    };

    return (
        <Box mb={2}>
            {label && (
                <Text fontSize="sm" fontWeight="bold" mb="1" color={themeColors.fontColor}>
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
                        const values = selectedOption ? selectedOption.map((opt: any) => opt.value) : [];
                        setFieldValue(name, values).catch();
                    } else {
                        setFieldValue(name, selectedOption ? selectedOption.value : "").catch();
                    }
                    setFieldTouched(name, true, false).catch();
                }}
                styles={customSelectStyles}
            />
            {meta.touched && meta.error && (
                <Text color="red.500" fontSize="xs" mt="1">
                    {meta.error}
                </Text>
            )}
        </Box>
    );
};


interface CustomSimpleSelectProps {
    value: number;
    onChange: (value: number) => void;
    options: { value: number; label: string }[];
    width?: string;
    bgColor?: string;
    disabled?: boolean;
    size?: "xs" | "sm" | "md" | "lg";
    hideArrow?: boolean;
}

const CustomSimpleSelect: React.FC<CustomSimpleSelectProps> = ({
                                                                   value,
                                                                   onChange,
                                                                   options,
                                                                   width,
                                                                   bgColor,
                                                                   disabled = false,
                                                                   size = "sm",
                                                                   hideArrow = false,
                                                               }) => {
    const selectStyles = getSelectStyles();
    const selectedValue = options.find((option) => option.value === value) || null;

    const getSizeStyles = (size: string) => {
        switch (size) {
            case "xs":
                return {
                    controlHeight: "24px",
                    fontSize: "12px",
                    padding: "0 6px",
                };
            case "sm":
                return {
                    controlHeight: "30px",
                    fontSize: "14px",
                    padding: "0 8px",
                };
            case "md":
                return {
                    controlHeight: "36px",
                    fontSize: "16px",
                    padding: "0 10px",
                };
            case "lg":
                return {
                    controlHeight: "42px",
                    fontSize: "18px",
                    padding: "0 12px",
                };
            default:
                return {
                    controlHeight: "30px",
                    fontSize: "20px",
                    padding: "0 8px",
                };
        }
    };

    const sizeStyles = getSizeStyles(size);

    const customSelectStyles: StylesConfig<any, false> = {
        ...selectStyles,
        control: (provided, state) => {
            const baseControl = selectStyles.control
                ? selectStyles.control(provided, state)
                : provided;
            return {
                ...baseControl,
                backgroundColor: bgColor ?? baseControl.backgroundColor,
                width: width || "auto", // <- wymusza szerokość całego kontenera
                minWidth: "unset",      // usuwa ewentualne minimalne szerokości
                maxWidth: width,
                minHeight: sizeStyles.controlHeight,
                height: sizeStyles.controlHeight,
                fontSize: sizeStyles.fontSize,
            };
        },
        valueContainer: (provided) => ({
            ...provided,
            height: sizeStyles.controlHeight,
            padding: sizeStyles.padding,
        }),
        indicatorsContainer: (provided) => ({
            ...provided,
            height: sizeStyles.controlHeight,
        }),
        // Możesz dodatkowo zmniejszyć odstępy w menu:
        menu: (provided) => ({
            ...provided,
            fontSize: sizeStyles.fontSize,
            width: "100%",
        }),
    };

    const noArrowComponents = {
        IndicatorSeparator: () => null,
        DropdownIndicator: () => null,
    };

    return (
        <Box>
            <Select
                options={options}
                isDisabled={disabled}
                value={selectedValue}
                onChange={(selectedOption: any) => {
                    onChange(selectedOption.value);
                }}
                styles={customSelectStyles}
                isSearchable={false}  // jeśli nie chcesz pola wyszukiwania
                components={hideArrow ? noArrowComponents : undefined} // usuwamy strzałkę i separator
            />
        </Box>
    );
};

interface CustomTextAreaFieldProps {
    name: string;
    label?: string;
    placeholder?: string;
    width?: string;
    rows?: number;
    disabled?: boolean
}

const CustomTextAreaField: React.FC<CustomTextAreaFieldProps> = ({
                                                                     name,
                                                                     label,
                                                                     placeholder,
                                                                     width,
                                                                     rows = 3,
                                                                     disabled = false
                                                                 }) => {
    const themeColors = useThemeColors();
    const themeColorsHex = useThemeColorsHex();
    return (
        <Field name={name}>
            {({field, meta}: FieldProps) => (
                <Box mb={2}>
                    {label && (
                        <Text fontSize="sm" fontWeight="bold" mb="1" color={themeColors.fontColor}>
                            {label}
                        </Text>
                    )}
                    <Textarea
                        {...field}
                        placeholder={placeholder}
                        _placeholder={{color: themeColorsHex.fontColor}}
                        size="sm"
                        color={themeColors.fontColor}
                        bg={themeColors.bgColorPrimary}
                        borderRadius="md"
                        width={width || "100%"}
                        rows={rows}
                        disabled={disabled}
                    />
                    {meta.touched && meta.error && (
                        <Text color="red.500" fontSize="xs" mt="1">
                            {meta.error}
                        </Text>
                    )}
                </Box>
            )}
        </Field>
    );
};

export interface CustomInputSearchFieldProps {
    name?: string;
    label?: string;
    placeholder: string;
    searchTerm: string;
    setSearchTerm: (value: string) => void;
    handleSearch: () => void;
}

const CustomInputSearchField: React.FC<CustomInputSearchFieldProps> = ({
                                                                           name,
                                                                           label,
                                                                           placeholder,
                                                                           searchTerm,
                                                                           setSearchTerm,
                                                                           handleSearch,
                                                                       }) => {
    const {t} = useTranslation();
    const themeColors = useThemeColors();
    const themeColorsHex = useThemeColorsHex();
    return (
        <Box mb={1}>
            {label && (
                <Text fontSize={"sm"} fontWeight={"bold"} mb={"1"} color={themeColors.fontColor}>
                    {label}
                </Text>
            )}
            <Input
                name={name}
                placeholder={placeholder}
                _placeholder={{color: themeColorsHex.fontColor}}
                value={searchTerm}
                onChange={(e) => setSearchTerm(e.target.value)}
                size={"sm"}
                color={themeColors.fontColor}
                bg={themeColors.bgColorPrimary}
                borderRadius={"md"}
            />
            <Button
                mt={1}
                mb={1}
                onClick={handleSearch}
                colorScheme={"orange"}
                size={"xs"}
            >
                {t("common:search")}
            </Button>
        </Box>
    );
};


export {CustomTextAreaField, CustomInputField, CustomSelectField, CustomInputSearchField, CustomSimpleSelect};
