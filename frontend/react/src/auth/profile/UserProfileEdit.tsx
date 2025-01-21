import React, {useEffect, useState} from "react";
import {useFormik} from "formik";
import * as Yup from "yup";
import {Box, Button, Input, Tabs, VStack} from "@chakra-ui/react";
import {useNavigate} from "react-router-dom"; // Import useNavigate
import {changeUserPassword, updateUserAccount,} from "@/services/account-service.ts";
import {useAuth} from "@/context/AuthContext.tsx";
import {successNotification} from "@/notifications/notifications.ts";
import {useTranslation} from "react-i18next";
import {themeColors} from "@/theme/theme-colors.ts";
import {Field} from "@/components/ui/field.tsx";

const UserProfileEdit: React.FC = () => {
    const {user: currentUser, setAuth, logOut} = useAuth();
    const navigate = useNavigate();
    const {t} = useTranslation("auth");
    const [isLoading, setIsLoading] = useState(false);


    useEffect(() => {
        if (!currentUser) {
            navigate("/", {replace: true});
        } else {
            setIsLoading(false);
        }
    }, [currentUser, navigate]);
    // useEffect(() => {
    //     if (!currentUser) {
    //         const storedUser = localStorage.getItem("currentUser");
    //         if (storedUser) {
    //             setAuth(JSON.parse(storedUser));
    //         } else {
    //             navigate("/", {replace: true});
    //         }
    //     }
    // }, [currentUser, navigate, setAuth]);


    const accountSchema = Yup.object({
        firstName: Yup.string()
            .min(2, t("verification.minLength", {field: t("shared.firstName"), count: 2}))
            .max(10, t("verification.maxLength", {field: t("shared.firstName"), count: 10}))
            .required(t("verification.required", {field: t("shared.firstName")})),
        lastName: Yup.string()
            .min(2, t("verification.minLength", {field: t("shared.lastName"), count: 2}))
            .max(25, t("verification.maxLength", {field: t("shared.lastName"), count: 25}))
            .required(t("verification.required", {field: t("shared.lastName")})),
        position: Yup.string()
            .min(2, t("verification.minLength", {field: t("shared.position"), count: 2}))
            .max(25, t("verification.maxLength", {field: t("shared.position"), count: 25})),
    });

    const passwordSchema = Yup.object({
        password: Yup.string()
            .min(6, t("verification.minLength", {field: t("shared.password"), count: 6}))
            .max(20, t("verification.maxLength", {field: t("shared.password"), count: 20}))
            .required(t("verification.required", {field: t("shared.password")})),
        newPassword: Yup.string()
            .min(6, t("verification.minLength", {field: t("updateProfile.newPassword"), count: 6}))
            .max(20, t("verification.maxLength", {field: t("updateProfile.newPassword"), count: 20}))
            .required(t("verification.required", {field: t("updateProfile.newPassword")})),
        confirmPassword: Yup.string()
            .oneOf([Yup.ref("newPassword")], t("verification.passwordNotMatch"))
            .required(t("verification.required", {field: t("updateProfile.confirmPassword")})),
    });

    const accountForm = useFormik({
        initialValues: {
            firstName: currentUser!.firstName || "",
            lastName: currentUser!.lastName || "",
            position: currentUser!.position || "",
        },
        validationSchema: accountSchema,
        onSubmit: async (values) => {
            if (!currentUser || !currentUser.email || !currentUser.id) {
                console.error("Brak wymaganych danych użytkownika!");
                return;
            }
            const userUpdateRequest = {
                id: currentUser?.id!,
                email: currentUser?.email,
                login: currentUser?.login,
                firstName: values.firstName,
                lastName: values.lastName,
                position: values.position,
                locked: currentUser?.locked,
                enabled: currentUser?.enabled,
                roles: currentUser?.roles || [],
            };
            try {
                await updateUserAccount(userUpdateRequest);
                if (userUpdateRequest.id === currentUser!.id) {
                    setAuth({
                        ...currentUser,
                        firstName: values.firstName,
                        lastName: values.lastName,
                        position: values.position,
                    });
                }
                successNotification(t("success", {ns: "common"}), t("notifications.accountUpdatedSuccess"));
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
            if (!currentUser?.id) {
                console.error("Brak ID użytkownika! Nie można zmienić hasła.");
                return;
            }
            const passwordRequest = {
                userId: currentUser.id,
                password: values.password,
                newPassword: values.newPassword,
            };
            try {
                await changeUserPassword(passwordRequest);
                successNotification(t("success", {ns: "common"}), t("notifications.changePasswordSuccess"));
                logOut();
            } catch (error) {
                console.error(error);
            }
        },
    });

    if (isLoading) {
        return <Box>{t("loading", { ns: "common" })}...</Box>;
    }

    return (
        <Box
            minH="100vh"
            display="flex"
            flexDirection="column"
            alignItems="center"
            justifyContent="space-between"
            padding={4}
            // bgColor={themeColors.bgColorLight()}
        >
            <Box
                maxWidth="600px"
                width="100%"
                // bgColor={themeColors.bgColor()}
                borderRadius={"lg"}
            >
                <Tabs.Root
                    variant="enclosed"
                    color={themeColors.fontColor()}
                    defaultValue={"account"}
                    fitted
                    background={themeColors.bgColorPrimary()}
                >
                    <Tabs.List>
                        <Tabs.Trigger
                            value="account"
                            borderRadius={"md"}
                            color={themeColors.fontColor()}
                            _hover={{
                                border: '1px solid white',
                                textDecoration: 'none',
                                bg: themeColors.highlightBgColor(),
                                color: themeColors.fontColor()
                            }}
                            background={themeColors.bgColorPrimary()}
                        >{t('updateProfile.accountInfo')}
                        </Tabs.Trigger>
                        <Tabs.Trigger
                            value="passwordReset"
                            color={themeColors.fontColor()}
                            borderRadius={"md"}
                            _hover={{
                                border: '1px solid white',
                                textDecoration: 'none',
                                bg: themeColors.highlightBgColor(),
                                color: themeColors.popoverBgColor()
                            }}
                        >{t('updateProfile.changePassword')}
                        </Tabs.Trigger>
                    </Tabs.List>
                    <Tabs.Content value="account">
                        <form onSubmit={accountForm.handleSubmit}>
                            <VStack p={4}>
                                <Field
                                    label={t('shared.firstName')}
                                    invalid={!!accountForm.errors.firstName && accountForm.touched.firstName}
                                    errorText={accountForm.errors.firstName}
                                >
                                    <Input
                                        name="firstName"
                                        value={accountForm.values.firstName}
                                        onChange={accountForm.handleChange}
                                        bgColor={themeColors.bgColorSecondary()}
                                        color={themeColors.bgColorPrimary()}
                                    />
                                </Field>

                                <Field
                                    label={t('shared.lastName')}
                                    invalid={!!accountForm.errors.lastName && accountForm.touched.lastName}
                                    errorText={accountForm.errors.lastName}
                                >
                                    <Input
                                        name="lastName"
                                        value={accountForm.values.lastName}
                                        onChange={accountForm.handleChange}
                                        bgColor={themeColors.bgColorSecondary()}
                                        color={themeColors.bgColorPrimary()}
                                    />
                                </Field>

                                <Field
                                    label={t('shared.position')}
                                    invalid={!!accountForm.errors.position && accountForm.touched.position}
                                    errorText={accountForm.errors.position}
                                > <Input
                                    name="position"
                                    value={accountForm.values.position}
                                    onChange={accountForm.handleChange}
                                    bgColor={themeColors.bgColorSecondary()}
                                    color={themeColors.bgColorPrimary()}
                                />
                                </Field>

                                <Button type="submit" colorScheme="blue">
                                    {t('updateProfile.updateAccount')}
                                </Button>
                            </VStack>
                        </form>
                    </Tabs.Content>

                    {/* Tab 2: Change Password */}
                    <Tabs.Content value="passwordReset">
                        <form onSubmit={passwordForm.handleSubmit}>
                            <VStack p={4}>
                                <Field
                                    label={t('updateProfile.currentPassword')}
                                    invalid={!!passwordForm.errors.password && passwordForm.touched.password}
                                    errorText={passwordForm.errors.password}
                                > <Input
                                    type="password"
                                    name="password"
                                    value={passwordForm.values.password}
                                    onChange={passwordForm.handleChange}
                                    bgColor={themeColors.bgColorSecondary()}
                                />
                                </Field>

                                <Field
                                    label={t('updateProfile.newPassword')}
                                    invalid={!!passwordForm.errors.newPassword && passwordForm.touched.newPassword}
                                    errorText={passwordForm.errors.newPassword}
                                >
                                    <Input
                                        type="password"
                                        name="newPassword"
                                        value={passwordForm.values.newPassword}
                                        onChange={passwordForm.handleChange}
                                        bgColor={themeColors.bgColorSecondary()}
                                    />
                                </Field>

                                <Field
                                    label={t('updateProfile.confirmPassword')}
                                    invalid={!!passwordForm.errors.confirmPassword && passwordForm.touched.confirmPassword}
                                    errorText={passwordForm.errors.confirmPassword}
                                >
                                    <Input
                                        type="password"
                                        name="confirmPassword"
                                        value={passwordForm.values.confirmPassword}
                                        onChange={passwordForm.handleChange}
                                        bgColor={themeColors.bgColorSecondary()}
                                    />
                                </Field>

                                <Button type="submit" colorScheme="blue">
                                    {t('updateProfile.submitPassword')}
                                </Button>
                            </VStack>
                        </form>
                    </Tabs.Content>

                </Tabs.Root>
            </Box>
        </Box>
    );
};

export default UserProfileEdit;
