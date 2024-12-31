import {useField} from "formik";
import {Alert, AlertIcon, Box, FormLabel, Input, Select, SelectProps} from "@chakra-ui/react";
import React from "react";

interface MyTextInputProps {
    label: string;
    name: string;
    type?: string;
    placeholder?: string;
    id?: string;
    styleProps?: { [key: string]: any }; // Obiekt z dowolnymi stylami
}

export const MyTextInput = ({label, styleProps = {}, ...props}: MyTextInputProps) => {
    const [field, meta] = useField(props);
    return (
        <Box>
            <FormLabel htmlFor={props.id || props.name}>{label}</FormLabel>
            <Input className="text-input" {...field} {...props} {...styleProps}/>
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

export const MySelect = ({label, children, multiple, ...props}: MySelectProps) => {
    const [field, meta] = useField(props.name); // Pobierz dane z Formik

    return (
        <Box>
            <FormLabel htmlFor={props.id || props.name}>{label}</FormLabel>
            <Select
                {...field}
                {...props}
                multiple={multiple}
            >
                {children}
            </Select>
            {meta.touched && meta.error ? (
                <Alert className="error" status={"error"} mt={2}>
                    <AlertIcon/>
                    {meta.error}
                </Alert>
            ) : null}
        </Box>
    );
};
