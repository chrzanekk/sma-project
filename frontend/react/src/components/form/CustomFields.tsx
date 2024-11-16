
import {useField} from "formik";
import {Box, Input, FormLabel, Select, Alert, AlertIcon} from "@chakra-ui/react";

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
                    <AlertIcon />
                    {meta.error}
                </Alert>
            ) : null}
        </Box>
    );
};

interface MySelectProps {
    label: string;
    name: string;
    id?: string;
    children: React.ReactNode;
}

export const MySelect = ({label, ...props}: MySelectProps) => {
    const [field, meta] = useField(props);
    return (
        <Box>
            <FormLabel htmlFor={props.id || props.name}>{label}</FormLabel>
            <Select {...field} {...props} />
            {meta.touched && meta.error ? (
                <Alert className="error" status={"error"} mt={2}>
                    <AlertIcon />
                    {meta.error}
                </Alert>
            ) : null}
        </Box>
    );
};
