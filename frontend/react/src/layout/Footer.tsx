'use client'

import {Box, HStack, Image, Stack, Text} from '@chakra-ui/react'
import {themeColors} from "@/theme/themeColors.ts";

export default function SmallWithLogoLeft() {
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
                    <Text fontWeight={700} color={themeColors.fontColor()}>© 2024 Konrad Chrzanowski. All rights reserved.</Text>
                </HStack>
            </Stack>
        </Box>
    )
}