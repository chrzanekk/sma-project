
import { Flex, Text, Image } from "@chakra-ui/react";
import {useTranslation} from "react-i18next";
import {themeColors} from "@/theme/theme-colors.ts";

const AppBanner = () => {

    const {t} =useTranslation('common')
    return (
        <Flex
            flex={1}
            p={10}
            flexDirection="column"
            alignItems="center"
            justifyContent="center"
            bgGradient={{ sm: "linear(to-r,white,green.500)" }}
        >
            <Text fontSize="6xl" color={themeColors.fontColor()} fontWeight="bold" mb={5}>
                {t('welcome')}
            </Text>
            <Image
                alt="SMA LOGO"
                objectFit="scale-down"
                src="/img/sma-logo-2.png"
                width="600px"
                height="auto"
            />
        </Flex>
    );
};

export default AppBanner;
