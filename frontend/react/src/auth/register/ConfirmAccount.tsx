import {useEffect, useState} from "react";
import {useLocation, useNavigate} from "react-router-dom";
import {Box, Button, Flex, Heading, Link, Stack, Text} from "@chakra-ui/react";
import {confirmAccount} from "@/services/auth-service";
import {errorNotification} from "@/notifications/notifications.ts";
import AppBanner from "@/auth/common/AppBanner.tsx";

const ConfirmAccount = () => {
    const navigate = useNavigate();
    const location = useLocation();
    const [confirmationStatus, setConfirmationStatus] = useState<"success" | "error" | "loading">("loading");

    // Pobieranie tokenu z URL
    const searchParams = new URLSearchParams(location.search);
    const token = searchParams.get("token");

    useEffect(() => {
        let isCalled = false;
        if (token && !isCalled) {
            isCalled = true;
            confirmAccount(token)
                .then(() => {
                    console.log("Account confirmed successfully.");
                    setConfirmationStatus("success");
                })
                .catch((err) => {
                    errorNotification("Error", err.response?.data?.message || "Account confirmation failed.");
                    setConfirmationStatus("error");
                });
        }

        return () => {
            isCalled = true;
        };
    }, [token]);

    return (
        <Box>
            <Stack minH={'100vh'} direction={{base: 'column', md: 'row'}}>
                <Flex p={8} flex={1} align={'center'} justify={'center'}>
                    <Stack spacing={4} w={'full'} maxW={'md'}>
                        {confirmationStatus === "loading" && (
                            <Heading textAlign="center" size="lg">Processing your request...</Heading>
                        )}
                        {confirmationStatus === "success" && (
                            <>
                                <Heading textAlign="center" size="lg">Account Confirmed!</Heading>
                                <Text textAlign="center">Your account has been successfully activated.</Text>
                                <Button
                                    colorScheme="teal"
                                    onClick={() => navigate("/")}
                                    alignSelf="center"
                                >
                                    Go to Login
                                </Button>
                            </>
                        )}
                        {confirmationStatus === "error" && (
                            <>
                                <Heading textAlign="center" size="lg" color="red.500">Confirmation Failed</Heading>
                                <Text textAlign="center">Invalid or expired token. Please try again.</Text>
                                <Link color="purple.600" href="/" alignSelf="center">
                                    Back to Home
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
