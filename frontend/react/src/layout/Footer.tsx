'use client'

import {Box, HStack, Image, Stack, Text} from '@chakra-ui/react'
import {themeColors} from "@/theme/theme-colors.ts";
import {useTranslation} from "react-i18next";

export default function SmallWithLogoLeft() {

    const {t} = useTranslation('footer')
    return (
        <Box bg={themeColors.bgColor()}>
            <Stack
                py={5}
                spacing={2}
                align={{base: 'center'}}>
                <HStack spacing={3}>
                    <Image
                        src="/img/author-logo.png"
                        alt="Konrad Chrzanowski"
                        boxSize="45px"
                    />
                    <Text fontWeight={700} color={themeColors.fontColor()}>{t('copyright')}</Text>
                </HStack>
            </Stack>
        </Box>
    )
}