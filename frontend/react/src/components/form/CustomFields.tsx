import {useField} from "formik";
import {Alert, AlertIcon, Box, FormLabel, Input, Select, SelectProps} from "@chakra-ui/react";
import React from "react";

interface MyTextInputProps {
    label: string;
    name: string;
    type?: string;
    placeholder?: string;
    id?: string;
}

export const MyTextInput = ({label, ...props}: MyTextInputProps) => {
    const [field, meta] = useField(props);
    return (
        <Box>
            <FormLabel htmlFor={props.id || props.name}>{label}</FormLabel>
            <Input className="text-input" {...field} {...props} />
            {meta.touched && meta.error ? (
                <Alert className="error" status={"error"} mt={2}>
                    <AlertIcon/>
                    {meta.error}
                </Alert>
            ) : null}
        </Box>
    );
};

interface MySelectProps extends SelectProps {
    label: string;
    name: string;
    id?: string;
    children: React.ReactNode;
}

export const MySelect = ({ label, children, ...props }: MySelectProps) => {
    const [field, meta, helpers] = useField(props.name); // Pobierz dane z Formik
    const { setValue } = helpers;

    return (
        <Box>
            <FormLabel htmlFor={props.id || props.name}>{label}</FormLabel>
            <Select
                {...field}
                {...props}
                value={field.value || []} // Ustaw aktualną wartość
                onChange={(e) => {
                    const selectedOptions = Array.from(
                        e.target.selectedOptions,
                        (option) => option.value
                    );
                    setValue(selectedOptions); // Ręczna aktualizacja w Formik
                }}
                multiple={props.multiple}
            >
                {children}
            </Select>
            {meta.touched && meta.error ? (
                <Alert className="error" status={"error"} mt={2}>
                    <AlertIcon />
                    {meta.error}
                </Alert>
            ) : null}
        </Box>
    );
};
