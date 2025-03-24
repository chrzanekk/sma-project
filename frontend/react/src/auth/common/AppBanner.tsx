import {Flex, Image, Text} from "@chakra-ui/react";
import {useTranslation} from "react-i18next";
import {useThemeColors} from "@/theme/theme-colors.ts";

const AppBanner = () => {
    const {t} = useTranslation('common')
    const themeColors = useThemeColors();

    return (
        <Flex
            flex={1}
            p={3}
            flexDirection="column"
            alignItems="center"
            justifyContent="center"
        >
            <Text fontSize="5xl" color={themeColors.fontColor} fontWeight="bold" mb={1}>
                {t('welcome')}
            </Text>
            <Image
                alt="SMA LOGO"
                objectFit="scale-down"
                src="/img/sma-logo-2.png"
                width="500px"
                height="auto"
            />
        </Flex>
    );
};

export default AppBanner;
