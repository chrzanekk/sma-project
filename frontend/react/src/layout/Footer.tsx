'use client'

import {Box, HStack, Image, Stack, Text} from '@chakra-ui/react'
import {useThemeColors} from "@/theme/theme-colors.ts";
import {useTranslation} from "react-i18next";
import {formatMessage} from "@/notifications/FormatMessage.tsx";

export default function SmallWithLogoLeft() {

    const {t} = useTranslation('footer')
    const themeColors = useThemeColors();
    const currentYear = new Date().getFullYear();

    return (
        <Box
            as={"footer"}
            bg={themeColors.bgColorPrimary}
            color={themeColors.fontColor}
            width={"100%"}
            textAlign={"center"}
            minH={"40px"}
        >
            <Stack
                align="center"
                justify="center">
                <HStack gap={2}>
                    <Image
                        src="/img/author-logo.png"
                        alt="Konrad Chrzanowski"
                        boxSize="40px"
                    />
                    <Text fontWeight={700}
                          color={themeColors.fontColor}>{t(formatMessage('copyright', {presentYear: currentYear}, "footer"))}</Text>
                </HStack>
            </Stack>
        </Box>
    )
}