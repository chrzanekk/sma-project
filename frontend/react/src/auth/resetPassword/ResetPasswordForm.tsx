import {useNavigate} from "react-router-dom";
import {Form, Formik} from "formik";
import * as Yup from "yup";
import {errorNotification, successNotification} from "@/notifications/notifications.ts";
import {Box, Button, Heading, Image, Stack} from "@chakra-ui/react";
import {MyTextInput} from "@/components/form/CustomFields.tsx";
import {requestPasswordReset} from "@/services/auth-service.ts";


const ResetPasswordForm = () => {

    const navigate = useNavigate();

    return (
        <Formik
            validateOnMount={true}
            validationSchema={Yup.object({
                email: Yup.string()
                    .min(4, "Password cannot be less than 4 characters")
                    .max(20, "Password cannot be more than 20 characters")
                    .required("New password is required."),
            })}
            initialValues={{email: ""}}
            onSubmit={(values, {setSubmitting}) => {
                setSubmitting(true);

                const requestData = {
                    email: values.email
                };

                requestPasswordReset(requestData)
                    .then(() => {
                        navigate("/");
                        console.log("Email with password reset link successful sent");
                        successNotification("Success", "Email with password reset link successful sent");
                    })
                    .catch((err) => {
                        errorNotification("Error", err.response?.data?.message || "Failed to reset password.");
                    })
                    .finally(() => {
                        setSubmitting(false);
                    });
            }}
        >
            {({isValid, isSubmitting}) => (
                <Form>
                    <Stack spacing={15}>
                        <Box display="flex" justifyContent="center">
                            <Image
                                alt={'ResetPassword Image'}
                                objectFit={"scale-down"}
                                src={
                                    '/img/sma-logo.png'
                                }
                                width={'200px'}
                                height={'auto'}
                            />
                        </Box>
                        <Box display="flex" justifyContent="center">
                            <Heading fontSize="2xl" mb={15}>
                                Request for password reset
                            </Heading>
                        </Box>
                        <MyTextInput
                            label="Email"
                            name="email"
                            type="email"
                            placeholder="Enter your email"
                        />
                        <Button
                            type="submit"
                            isDisabled={!isValid || isSubmitting}
                        >
                            Send password reset link
                        </Button>
                    </Stack>
                </Form>
            )}
        </Formik>
    )
}

export default ResetPasswordForm;