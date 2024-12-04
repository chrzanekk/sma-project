import React from 'react';
import {Field, Form, Formik} from 'formik';
import * as Yup from 'yup';
import {Button, Flex, Input, Select} from '@chakra-ui/react';

interface FilterValues {
    emailStartsWith?: string;
    loginStartsWith?: string;
    firstNameStartsWith?: string;
    lastNameStartsWith?: string;
    positionStartsWith?: string;
    isLocked?: boolean;
    isEnabled?: boolean;
}

const validationSchema = Yup.object({
    emailStartsWith: Yup.string(),
    loginStartsWith: Yup.string(),
    firstNameStartsWith: Yup.string(),
    lastNameStartsWith: Yup.string(),
    positionStartsWith: Yup.string(),
    isLocked: Yup.boolean(),
    isEnabled: Yup.boolean(),
});

interface Props {
    onSubmit: (values: FilterValues) => void;
}

const UserFilterForm: React.FC<Props> = ({onSubmit}) => {
    return (
        <Formik
            initialValues={{
                emailStartsWith: '',
                loginStartsWith: '',
                firstNameStartsWith: '',
                lastNameStartsWith: '',
                positionStartsWith: '',
                isLocked: undefined,
                isEnabled: undefined,
            }}
            validationSchema={validationSchema}
            onSubmit={onSubmit}
        >
            {({handleSubmit, resetForm}) => (
                <Form onSubmit={handleSubmit}>
                    <Flex gap={2} mb={1} justifyContent={"center"}>
                        <Field name="emailStartsWith" as={Input} placeholder="Email" size="sm"
                               width="150px"/>
                        <Field name="loginStartsWith" as={Input} placeholder="Login" size="sm"
                               width="150px"/>
                        <Field name="firstNameStartsWith" as={Input} placeholder="First Name" size="sm"
                               width="150px"/>
                        <Field name="lastNameStartsWith" as={Input} placeholder="Last Name" size="sm"
                               width="150px"/>
                        <Field name="positionStartsWith" as={Input} placeholder="Position" size="sm"
                               width="150px"/>
                        <Field name="isLocked" as={Select} placeholder="Is Locked?" size="sm"
                               width="150px">
                            <option value="true">Yes</option>
                            <option value="false">No</option>
                        </Field>
                        <Field name="isEnabled" as={Select} placeholder="Is Enabled?" size="sm"
                               width="150px">
                            <option value="true">Yes</option>
                            <option value="false">No</option>
                        </Field>
                    </Flex>
                    <Flex gap={2} justifyContent={"center"}>
                        <Button type="submit" colorScheme="blue" size={"sm"}>
                            Search
                        </Button>
                        <Button
                            type="button"
                            colorScheme="gray"
                            size="sm"
                            onClick={() => {
                                resetForm(); // Resetuje wszystkie wartości pól
                                onSubmit({}); // Wywołuje `onSubmit` z pustym obiektem
                            }}
                        >
                            Clear Filters
                        </Button>
                    </Flex>

                </Form>
            )}
        </Formik>
    );
};

export default UserFilterForm;
