import {useField} from "formik";
import {Box, Input, Text as ChakraText} from "@chakra-ui/react";
import {Field} from "@/components/ui/field.tsx";

interface MyTextInputProps {
    label: string;
    name: string;
    type?: string;
    placeholder?: string;
    id?: string;
    styleProps?: { [key: string]: any };
}

export const MyTextInput = ({label, styleProps = {}, ...props}: MyTextInputProps) => {
    const [field, meta] = useField(props);

    return (
        <Field label={label}>
            <Input {...field} {...props} {...styleProps} />
            {meta.touched && meta.error ? (
                <Box mt={2} display="flex" alignItems="center" color="red.500">
                    <ChakraText>{meta.error}</ChakraText>
                </Box>
            ) : null}
        </Field>
    );
};