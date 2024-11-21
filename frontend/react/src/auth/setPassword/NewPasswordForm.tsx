import {useLocation, useNavigate} from "react-router-dom";
import {Form, Formik} from "formik";
import * as Yup from "yup";
import {errorNotification, successNotification} from "@/notifications/notifications.ts";
import {Box, Button, Heading, Image, Stack} from "@chakra-ui/react";
import {MyTextInput} from "@/components/form/CustomFields.tsx";
import {resetPassword} from "@/services/auth-service.ts";


const NewPasswordForm = () => {

    const navigate = useNavigate();
    const location = useLocation();
    const queryParams = new URLSearchParams(location.search);
    const token = queryParams.get('token');

    if (!token) {
        errorNotification("Error", "Invalid or missing token")
        navigate("/")
        return null;
    }
    return (
        <Formik
            validateOnMount={true}
            validationSchema={Yup.object({
                password: Yup.string()
                    .min(4, "Password cannot be less than 4 characters")
                    .max(20, "Password cannot be more than 20 characters")
                    .required("New password is required."),
                confirmPassword: Yup.string()
                    .oneOf([Yup.ref("password")], "Passwords must match")
                    .required("Please confirm your new password."),
            })}
            initialValues={{password: "", confirmPassword: ""}}
            onSubmit={(values, {setSubmitting}) => {
                setSubmitting(true);

                const requestData = {
                    password: values.password,
                    confirmPassword: values.confirmPassword,
                    token: token,
                };

                resetPassword(requestData)
                    .then(() => {
                        navigate("/");
                        console.log("Password successfully reset");
                        successNotification("Success", "Password successfully reset")
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
                                Set New Password
                            </Heading>
                        </Box>
                        <MyTextInput
                            label="New Password"
                            name="password"
                            type="password"
                            placeholder="Enter your new password"
                        />
                        <MyTextInput
                            label="Confirm New Password"
                            name="confirmPassword"
                            type="password"
                            placeholder="Confirm your new password"
                        />
                        <Button
                            type="submit"
                            isDisabled={!isValid || isSubmitting}
                        >
                            Set New Password
                        </Button>
                    </Stack>
                </Form>
            )}
        </Formik>
    )
}

export default NewPasswordForm;