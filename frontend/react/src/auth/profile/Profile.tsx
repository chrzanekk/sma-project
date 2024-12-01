// import {Form, Formik, useField} from 'formik';
// import * as Yup from 'yup';
// import {Alert, AlertIcon, Box, Button, FormLabel, Input, Stack} from "@chakra-ui/react";
// import {useNavigate} from "react-router-dom";
// import {useAuth} from "@/context/AuthContext.tsx";
// import {errorNotification, successNotification} from "@/notifications/notifications.ts";
// import {updateUserAccount} from "@/services/account-service.ts";
//
//
// const UpdateProfileForm = ({initialValues, customerId}) => {
//     const navigate = useNavigate();
//     const {updateUserData} = useAuth();
//
//     return (
//         <>
//             <Formik
//                 initialValues={initialValues}
//                 validationSchema={Yup.object({
//                     name: Yup.string()
//                         .max(25, 'Must be 15 characters or less')
//                         .required('Required'),
//                     age: Yup.number()
//                         .min(16, 'Must be at least 16 years of age')
//                         .max(200, 'Must be less than 200 years of age')
//                         .required(),
//                 })}
//                 onSubmit={(updatedUserData, {setSubmitting}) => {
//                     setSubmitting(true);
//                     updateUserAccount(userId, updatedCustomer)
//                         .then(res => {
//                             successNotification(
//                                 "Profile updated",
//                                 `${updatedCustomer.name} was successfully updated`
//                             )
//                             console.log("Customer to update: ", updatedCustomer);
//                             updateCustomerData(updatedCustomer);
//                             navigate("/dashboard")
//                         })
//                         .catch(err => {
//                             console.log(err);
//                             errorNotification(
//                                 err.code,
//                                 err.response.data.message
//                             )
//                         }).finally(() => {
//                         setSubmitting(false);
//                     })
//                 }}
//             >
//                 {({isValid, isSubmitting, dirty}) => (
//                     <Form>
//                         <Stack spacing={"24px"}>
//                             <MyTextInput
//                                 label="Name"
//                                 name="name"
//                                 type="text"
//                                 placeholder="name"
//                             />
//                             <MyTextInput
//                                 label="Age"
//                                 name="age"
//                                 type="number"
//                                 placeholder="age"
//                             />
//                             <Button isDisabled={!(isValid && dirty) || isSubmitting} type="submit">Update</Button>
//                         </Stack>
//                     </Form>
//                 )}
//             </Formik>
//         </>
//     );
// };
//
// export default UpdateProfileForm;