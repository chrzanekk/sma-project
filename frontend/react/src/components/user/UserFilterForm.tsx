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
            {({handleSubmit}) => (
                <Form onSubmit={handleSubmit}>
                    <Flex gap={4} mb={4}>
                        <Field name="emailStartsWith" as={Input} placeholder="Email"/>
                        <Field name="loginStartsWith" as={Input} placeholder="Login"/>
                        <Field name="firstNameStartsWith" as={Input} placeholder="First Name"/>
                        <Field name="lastNameStartsWith" as={Input} placeholder="Last Name"/>
                        <Field name="positionStartsWith" as={Input} placeholder="Position"/>
                        <Field name="isLocked" as={Select} placeholder="Is Locked?">
                            <option value="true">Yes</option>
                            <option value="false">No</option>
                        </Field>
                        <Field name="isEnabled" as={Select} placeholder="Is Enabled?">
                            <option value="true">Yes</option>
                            <option value="false">No</option>
                        </Field>
                    </Flex>
                    <Button type="submit" colorScheme="blue">
                        Search
                    </Button>
                </Form>
            )}
        </Formik>
    );
};

export default UserFilterForm;
