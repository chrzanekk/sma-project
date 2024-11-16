// src/components/form/RegisterForm.tsx

import {Form, Formik} from 'formik';
import * as Yup from 'yup';
import {Box, Button, Heading, Image, Stack} from "@chakra-ui/react";
import {errorNotification, successNotification} from "@/notifications/notifications.ts";
import {MyTextInput} from "@/components/form/CustomFields";
import {registerUser} from "@/services/auth-service.ts";
import {RegisterRequest} from "@/types/user-types.ts";

const RegisterForm = () => {
    return (
        <Formik
            initialValues={{
                username: '',
                email: '',
                password: ''
            }}
            validationSchema={Yup.object({
                userName: Yup.string()
                    .max(15, 'Must be 15 characters or less')
                    .required('Required'),
                email: Yup.string()
                    .email('Invalid email address')
                    .required('Required'),
                password: Yup.string()
                    .min(4, 'Must be 4 characters or more')
                    .max(10, 'Must be 10 characters or less')
                    .required('Required')
            })}
            onSubmit={async (register: RegisterRequest, {setSubmitting}) => {
                setSubmitting(true);
                try {
                    await registerUser(register);
                    successNotification(
                        "User registered successful.",
                        `${register.username} was successfully saved. Please check your email to activate your account.`
                    );
                } catch (err: any) {
                    console.error(err);
                    errorNotification(
                        "Registration failed",
                        err.response?.data?.message || "An error occurred during registration."
                    );
                } finally {
                    setSubmitting(false)
                }
            }}
        >
            {({isValid, isSubmitting}) => (
                <Form>
                    <Box display="flex" justifyContent="center">
                        <Image
                            alt="Login Image"
                            objectFit="scale-down"
                            src="/img/sma-logo.png"
                            width="200px"
                            height="auto"
                        />
                    </Box>
                    <Box display="flex" justifyContent="center">
                        <Heading fontSize="2xl" mb={15}>Register new account</Heading>
                    </Box>
                    <Stack spacing="24px">
                        <MyTextInput
                            label="Login"
                            name="username"
                            type="text"
                            placeholder="login"
                        />
                        <MyTextInput
                            label="Email Address"
                            name="email"
                            type="email"
                            placeholder="example@example.com"
                        />
                        <MyTextInput
                            label="Password"
                            name="password"
                            type="password"
                            placeholder="Pick a secure password"
                        />
                        <Button isDisabled={!isValid || isSubmitting} type="submit">
                            Submit
                        </Button>
                    </Stack>
                </Form>
            )}
        </Formik>
    );
};

export default RegisterForm;
