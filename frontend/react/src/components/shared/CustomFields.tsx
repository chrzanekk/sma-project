import {useField} from "formik";
import {Box, Icon, Input, Text as ChakraText} from "@chakra-ui/react";
import {Field} from "@/components/ui/field.tsx";
import {WarningIcon} from "@chakra-ui/icons";

interface MyTextInputProps {
    label: string;
    name: string;
    type?: string;
    placeholder?: string;
    id?: string;
    styleProps?: { [key: string]: any }; // Obiekt z dowolnymi stylami
}

// export const MyTextInput = ({label, styleProps = {}, ...props}: MyTextInputProps) => {
//     const [field, meta] = useField(props);
//     return (
//         <Box>
//             <FormLabel htmlFor={props.id || props.name}>{label}</FormLabel>
//             <Input className="text-input" {...field} {...props} {...styleProps}/>
//             {meta.touched && meta.error ? (
//                 <Alert className="error" status={"error"} mt={2}>
//                     <AlertIcon/>
//                     {meta.error}
//                 </Alert>
//             ) : null}
//         </Box>
//     );
// };
//

export const MyTextInput = ({label, styleProps = {}, ...props}: MyTextInputProps) => {
    const [field, meta] = useField(props);

    return (
        <Field label={label}>
            <Input {...field} {...props} {...styleProps} />
            {meta.touched && meta.error ? (
                <Box mt={2} display="flex" alignItems="center" color="red.500">
                    <Icon as={WarningIcon} mr={2}/>
                    <ChakraText>{meta.error}</ChakraText>
                </Box>
            ) : null}
        </Field>
    );
};