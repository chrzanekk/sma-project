import React from "react";
import {Field, FieldProps, useField, useFormikContext} from "formik";
import {Box, Input, Text} from "@chakra-ui/react";
import Select, {StylesConfig} from "react-select";
import {themeColors, themeColorsHex} from "@/theme/theme-colors";
import {selectStyles} from "@/components/shared/formOptions.ts";
// -----------------------------
// Komponenty formularzowe
// -----------------------------

export interface CustomInputFilterFieldProps {
    name: string;
    placeholder: string;
}

export const CustomInputFilterField: React.FC<CustomInputFilterFieldProps> = ({name, placeholder}) => (
    <Field
        name={name}
        as={Input}
        color={themeColors.fontColor()}
        placeholder={placeholder}
        _placeholder={{color: themeColorsHex.fontColor()}}
        size="sm"
        bg={themeColors.bgColorSecondary()}
        borderRadius="md"
        width="150px"
    />
);


interface CustomInputFieldProps {
    name: string;
    label?: string;
    placeholder?: string;
    type?: string;
    width?: string;
}

const CustomInputField: React.FC<CustomInputFieldProps> = ({
                                                               name,
                                                               label,
                                                               placeholder,
                                                               type = "text",
                                                               width
                                                           }) => {
    return (
        <Field name={name}>
            {({field, meta}: FieldProps) => (
                <Box mb={2}>
                    {label && (
                        <Text fontSize="sm" fontWeight="bold" mb="1" color={themeColors.fontColor()}>
                            {label}
                        </Text>
                    )}
                    <Input
                        {...field}
                        placeholder={placeholder}
                        type={type}
                        size="sm"
                        color={themeColors.fontColor()}
                        bg={themeColors.bgColorSecondary()}
                        borderRadius="md"
                        width={width || "100%"}
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

export default CustomInputField;

export interface CustomSelectFieldProps {
    name: string;
    label?: string;
    placeholder?: string;
    options: any[];
    isMulti?: boolean;
    width?: string;
}

export const CustomSelectField: React.FC<CustomSelectFieldProps> = ({
                                                                        name,
                                                                        label,
                                                                        placeholder,
                                                                        options,
                                                                        isMulti = false,
                                                                        width
                                                                    }) => {
    const {setFieldValue, setFieldTouched} = useFormikContext<any>();
    const [field, meta] = useField(name);

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
                width: width ? width : "auto",
            };
        },
    };

    return (
        <Box mb={2}>
            {label && (
                <Text fontSize="sm" fontWeight="bold" mb="1" color={themeColors.fontColor()}>
                    {label}
                </Text>
            )}
            <Select
                options={options}
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