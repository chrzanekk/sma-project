import React from "react";
import {Button, Flex, Heading, Text} from "@chakra-ui/react";
import {useTranslation} from "react-i18next";
import {themeColors} from "@/theme/theme-colors.ts";

interface Error404Props {
    title?: string;
    description?: string;
    onNavigateHome?: () => void;
}

const Error404: React.FC<Error404Props> = ({
                                               title = "404 - Page Not Found",
                                               description = "The page you are looking for does not exist.",
                                               onNavigateHome,
                                           }) => {
    const {t} = useTranslation();
    return (
        <Flex
            direction="column"
            align="center"
            justify="center"
            height="100vh"
            bg={themeColors.bgColorPrimary()}
            px={4}
            textAlign="center"
        >
            <Heading size="lg" mb={2}>
                {title}
            </Heading>
            <Text fontSize="md" color={themeColors.fontColor()} mb={6}>
                {description}
            </Text>
            {onNavigateHome && (
                <Button colorScheme="blue" onClick={onNavigateHome}>
                    {t("main")}
                </Button>
            )}
        </Flex>
    );
};

export default Error404;
