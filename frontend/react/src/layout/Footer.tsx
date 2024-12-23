'use client'

import {Box, HStack, Image, Stack, Text} from '@chakra-ui/react'
import {themeColors} from "@/theme/theme-colors.ts";
import {useTranslation} from "react-i18next";

export default function SmallWithLogoLeft() {

    const {t} = useTranslation('footer')
    return (
        <Box
            as="footer"
            bg={themeColors.bgColor()}
            color={themeColors.fontColor()}
            py={2}
            width="100%"
            textAlign="center" // Minimalna wysokość stopki
        >
            <Stack
                align="center"
                justify="center">
                <HStack spacing={2}>
                    <Image
                        src="/img/author-logo.png"
                        alt="Konrad Chrzanowski"
                        boxSize="40px"
                    />
                    <Text fontWeight={700} color={themeColors.fontColor()}>{t('copyright')}</Text>
                </HStack>
            </Stack>
        </Box>
    )
}