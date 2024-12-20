import React, {useEffect} from "react";
import {useFormik} from "formik";
import * as Yup from "yup";
import {Box, Button, FormLabel, Input, Tab, TabList, TabPanel, TabPanels, Tabs, VStack} from "@chakra-ui/react";
import {useNavigate} from "react-router-dom"; // Import useNavigate
import {changeUserPassword, updateUserAccount,} from "@/services/account-service.ts";
import {useAuth} from "@/context/AuthContext.tsx";
import {successNotification} from "@/notifications/notifications.ts";
import {useTranslation} from "react-i18next";
import {themeColors} from "@/theme/theme-colors.ts";

const UserProfileEdit: React.FC = () => {
    const {user: currentUser, setAuth, logOut} = useAuth();
    const navigate = useNavigate();
    const {t} = useTranslation('auth')

    useEffect(() => {
        if (!currentUser) {
            navigate("/", {replace: true});
        }
    }, [currentUser, navigate]);


    if (!currentUser) {
        return null;
    }


    // Validation schemas
    const accountSchema = Yup.object({
        firstName: Yup.string()
            .min(2, t('verification.minLength', {field: t('shared.firstName'), count: 2}))
            .max(10, t('verification.maxLength', {field: t('shared.firstName'), count: 10}))
            .required(t('verification.required', {field: t('shared.firstName')})),
        lastName: Yup.string()
            .min(2, t('verification.minLength', {field: t('shared.lastName'), count: 2}))
            .max(25, t('verification.maxLength', {field: t('shared.lastName'), count: 25}))
            .required(t('verification.required', {field: t('shared.lastName')})),
        position: Yup.string()
            .min(2, t('verification.minLength', {field: t('shared.position'), count: 2}))
            .max(25, t('verification.maxLength', {field: t('shared.position'), count: 25}))
    });

    const passwordSchema = Yup.object({
        password: Yup.string()
            .min(6, t('verification.minLength', {field: t('shared.password'), count: 6}))
            .max(20, t('verification.maxLength', {field: t('shared.password'), count: 20}))
            .required(t('verification.required', {field: t('shared.password')})),
        newPassword: Yup.string()
            .min(6, t('verification.minLength', {field: t('updateProfile.newPassword'), count: 6}))
            .max(20, t('verification.maxLength', {field: t('updateProfile.newPassword'), count: 20}))
            .required(t('verification.required', {field: t('updateProfile.newPassword')})),
        confirmPassword: Yup.string()
            .oneOf([Yup.ref("newPassword")], t('verification.passwordNotMatch'))
            .required(t('verification.required', {field: t('updateProfile.confirmPassword')})),
    });

    const accountForm = useFormik({
        initialValues: {
            firstName: currentUser.firstName || "",
            lastName: currentUser.lastName || "",
            position: currentUser.position || "",
        },
        validationSchema: accountSchema,
        onSubmit: async (values) => {
            const userUpdateRequest = {
                id: currentUser.id,
                email: currentUser.email,
                login: currentUser.login,
                firstName: values.firstName,
                lastName: values.lastName,
                position: values.position,
                locked: currentUser.locked,
                enabled: currentUser.enabled,
                roles: currentUser.roles || []
            }
            try {
                await updateUserAccount(userUpdateRequest);
                if (userUpdateRequest.id === currentUser.id) {
                    setAuth({
                        ...currentUser,
                        firstName: values.firstName,
                        lastName: values.lastName,
                        position: values.position,
                    });
                }
                successNotification(t('success', {ns: "common"}), t("notifications.accountUpdatedSuccess"));
            } catch (error) {
                console.error(error);
            }
        },
    });

    const passwordForm = useFormik({
        initialValues: {
            password: "",
            newPassword: "",
            confirmPassword: "",
        },
        validationSchema: passwordSchema,
        onSubmit: async (values) => {
            const passwordRequest = {
                userId: currentUser.id!,
                password: values.password,
                newPassword: values.newPassword,
            };
            await changeUserPassword(passwordRequest).then(
                () => {
                    successNotification(t('success', {ns: "common"}), t("notifications.changePasswordSuccess"));
                    logOut();
                }
            );
        },
    });

    return (
        <Box
            minH="100vh"
            display="flex"
            flexDirection="column"
            alignItems="center"
            justifyContent="space-between"
            padding={4}
            bgColor={themeColors.bgColorLight()}
        >
            <Box
                maxWidth="600px"
                width="100%"
                bgColor={themeColors.bgColor()}
                borderRadius={"lg"}
            >
                <Tabs
                    variant="outline"
                    color={themeColors.fontColor()}
                >
                    <TabList>
                        <Tab
                            borderRadius={"md"}
                            color={themeColors.fontColor()}
                            _hover={{
                                border: '1px solid white',
                                textDecoration: 'none',
                                bg: themeColors.highlightBgColor(),
                                color: themeColors.popoverBgColor()
                            }}
                        >{t('updateProfile.accountInfo')}</Tab>
                        <Tab
                            color={themeColors.fontColor()}
                            borderRadius={"md"}
                            _hover={{
                                border: '1px solid white',
                                textDecoration: 'none',
                                bg: themeColors.highlightBgColor(),
                                color: themeColors.popoverBgColor()
                            }}
                        >{t('updateProfile.changePassword')}</Tab>
                    </TabList>

                    <TabPanels>
                        <TabPanel>
                            <form onSubmit={accountForm.handleSubmit}>
                                <VStack spacing={4}>
                                    <FormLabel>{t('shared.firstName')}</FormLabel>
                                    <Input
                                        name="firstName"
                                        value={accountForm.values.firstName}
                                        onChange={accountForm.handleChange}
                                        isInvalid={!!accountForm.errors.firstName}
                                        bgColor={themeColors.bgColorLight()}
                                    />

                                    <FormLabel>{t('shared.lastName')}</FormLabel>
                                    <Input
                                        name="lastName"
                                        value={accountForm.values.lastName}
                                        onChange={accountForm.handleChange}
                                        isInvalid={!!accountForm.errors.lastName}
                                        bgColor={themeColors.bgColorLight()}
                                    />

                                    <FormLabel>{t('shared.position')}</FormLabel>
                                    <Input
                                        name="position"
                                        value={accountForm.values.position}
                                        onChange={accountForm.handleChange}
                                        isInvalid={!!accountForm.errors.position}
                                        bgColor={themeColors.bgColorLight()}
                                    />

                                    <Button type="submit" colorScheme="blue">
                                        {t('updateProfile.updateAccount')}
                                    </Button>
                                </VStack>
                            </form>
                        </TabPanel>

                        {/* Tab 2: Change Password */}
                        <TabPanel>
                            <form onSubmit={passwordForm.handleSubmit}>
                                <VStack spacing={4}>
                                    <FormLabel>{t('updateProfile.currentPassword')}</FormLabel>
                                    <Input
                                        type="password"
                                        name="password"
                                        value={passwordForm.values.password}
                                        onChange={passwordForm.handleChange}
                                        isInvalid={!!passwordForm.errors.password}
                                        bgColor={themeColors.bgColorLight()}
                                    />

                                    <FormLabel>{t('updateProfile.newPassword')}</FormLabel>
                                    <Input
                                        type="password"
                                        name="newPassword"
                                        value={passwordForm.values.newPassword}
                                        onChange={passwordForm.handleChange}
                                        isInvalid={!!passwordForm.errors.newPassword}
                                        bgColor={themeColors.bgColorLight()}
                                    />

                                    <FormLabel>{t('updateProfile.confirmPassword')}</FormLabel>
                                    <Input
                                        type="password"
                                        name="confirmPassword"
                                        value={passwordForm.values.confirmPassword}
                                        onChange={passwordForm.handleChange}
                                        isInvalid={!!passwordForm.errors.confirmPassword}
                                        bgColor={themeColors.bgColorLight()}
                                    />

                                    <Button type="submit" colorScheme="blue">
                                        {t('updateProfile.submitPassword')}
                                    </Button>
                                </VStack>
                            </form>
                        </TabPanel>


                    </TabPanels>
                </Tabs>
            </Box>
        </Box>
    );
};

export default UserProfileEdit;
