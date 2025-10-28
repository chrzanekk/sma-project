// pages/Unauthorized.tsx
import React from "react";
import {Box, Button, Heading, Text} from "@chakra-ui/react";
import {useNavigate} from "react-router-dom";
import {useTranslation} from "react-i18next";
import {useThemeColors} from "@/theme/theme-colors.ts";

const Unauthorized: React.FC = () => {
    const navigate = useNavigate();
    const {t} = useTranslation('common');
    const themeColors = useThemeColors();

    return (
        <Box
            display="flex"
            flexDirection="column"
            alignItems="center"
            justifyContent="center"
            minH="100vh"
            bg={themeColors.bgColorPrimary}
            p={4}
        >
            <Heading mb={4} color={themeColors.fontColor}>403</Heading>
            <Text mb={4} color={themeColors.fontColor}>
                {t('errors.unauthorized', 'You do not have permission to access this page')}
            </Text>
            <Button
                onClick={() => navigate('/')}
                colorPalette="blue"
            >
                {t('goToDashboard', 'Go to Dashboard')}
            </Button>
        </Box>
    );
};

export default Unauthorized;
