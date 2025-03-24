import React from "react";
import {Box, Button, Flex, Heading, Text} from "@chakra-ui/react";
import {FaTools} from "react-icons/fa";
import {useThemeColors} from "@/theme/theme-colors.ts";
import {useTranslation} from "react-i18next";

interface UnderConstructionProps {
    title?: string;
    description?: string;
    icon?: React.ReactNode;
    onBackToHome?: () => void;
}

const UnderConstruction: React.FC<UnderConstructionProps> = ({
                                                                 title = "Under Construction",
                                                                 description = "This feature is currently under development. Please check back later!",
                                                                 icon = <FaTools size="5rem"/>,
                                                                 onBackToHome
                                                             }) => {
    const {t} = useTranslation();
    const themeColors = useThemeColors();
    return (
        <Flex
            direction="column"
            align="center"
            justify="center"
            height="100vh"
            bg={themeColors.bgColorSecondary}
            px={4}
            textAlign="center"
        >
            <Box mb={6}>{icon}</Box>
            <Heading size="lg" mb={2}>
                {title}
            </Heading>
            <Text fontSize="md" color={themeColors.fontColor} mb={6}>
                {description}
            </Text>
            {onBackToHome && (
                <Button colorScheme="blue" onClick={onBackToHome}>
                    {t("main")}
                </Button>
            )}
        </Flex>
    );
};

export default UnderConstruction;
