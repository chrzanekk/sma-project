import {useEffect, useRef, useState} from "react";
import {useLocation, useNavigate} from "react-router-dom";
import {Box, Button, Flex, Heading, Link, Stack, Text} from "@chakra-ui/react";
import {confirmAccount} from "@/services/auth-service";
import {successNotification} from "@/notifications/notifications.ts";
import AppBanner from "@/auth/common/AppBanner.tsx";
import {useTranslation} from "react-i18next";
import {ThemeToggle} from "@/layout/ThemeToggle.tsx";
import {themeColors} from "@/theme/theme-colors.ts";

const ConfirmAccount = () => {
    const navigate = useNavigate();
    const location = useLocation();
    const [confirmationStatus, setConfirmationStatus] = useState<"success" | "error" | "loading">("loading");
    const {t} = useTranslation(['auth', 'common']);
    const searchParams = new URLSearchParams(location.search);
    const token = searchParams.get("token");
    const isCalled = useRef(false);

    useEffect(() => {

        if (token && !isCalled.current) {
            isCalled.current = true;
            confirmAccount(token)
                .then(() => {
                    successNotification(t('success', {ns: "common"}), t('notifications.confirmSuccess'))
                    setConfirmationStatus("success");
                })
                .catch(() => {
                });
        }
    }, [t, token]);

    return (
        <Box backgroundColor={themeColors.bgColorSecondary()} p={3} minH={'100vh'}>
            <ThemeToggle/>
            <Stack direction={{base: 'column', md: 'row'}}>
                <Flex p={8} flex={1} align={'center'} justify={'center'}>
                    <Stack gap={4} w={'full'} maxW={'md'}>
                        {confirmationStatus === "loading" && (
                            <Heading textAlign="center" size="lg">{t('common.processing')}</Heading>
                        )}
                        {confirmationStatus === "success" && (
                            <>
                                <Heading textAlign="center" size="lg">{t('confirm.header')}</Heading>
                                <Text textAlign="center">{t('confirm.info')}</Text>
                                <Button
                                    colorScheme="teal"
                                    onClick={() => navigate("/")}
                                    alignSelf="center"
                                >
                                    {t('shared.goToLogin')}
                                </Button>
                            </>
                        )}
                        {confirmationStatus === "error" && (
                            <>
                                <Heading textAlign="center" size="lg"
                                         color="red.500">{t('confirm.headerFailed')}</Heading>
                                <Text textAlign="center">{t('confirm.infoFailed')}</Text>
                                <Link color="purple.600" href="/" alignSelf="center">
                                    {t('shared.goToLogin')}
                                </Link>
                            </>
                        )}
                    </Stack>
                </Flex>
                <AppBanner/>
            </Stack>
        </Box>

    );
};

export default ConfirmAccount;
