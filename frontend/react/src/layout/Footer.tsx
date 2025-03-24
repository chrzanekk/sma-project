'use client'

import {Box, HStack, Image, Stack, Text} from '@chakra-ui/react'
import {useThemeColors} from "@/theme/theme-colors.ts";
import {useTranslation} from "react-i18next";

export default function SmallWithLogoLeft() {

    const {t} = useTranslation('footer')
    const themeColors = useThemeColors();

    return (
        <Box
            as="footer"
            bg={themeColors.bgColorPrimary}
            color={themeColors.fontColor}
            width="100%"
            textAlign="center"
        >
            <Stack
                align="center"
                justify="center">
                <HStack gap={2}>
                    <Image
                        src="/img/author-logo.png"
                        alt="Konrad Chrzanowski"
                        boxSize="50px"
                    />
                    <Text fontWeight={700} color={themeColors.fontColor}>{t('copyright')}</Text>
                </HStack>
            </Stack>
        </Box>
    )
}