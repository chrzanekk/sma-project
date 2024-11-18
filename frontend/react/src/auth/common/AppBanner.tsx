
import { Flex, Text, Image } from "@chakra-ui/react";

const AppBanner = () => {
    return (
        <Flex
            flex={1}
            p={10}
            flexDirection="column"
            alignItems="center"
            justifyContent="center"
            bgGradient={{ sm: "linear(to-r,white,green.600)" }}
        >
            <Text fontSize="6xl" color="green.800" fontWeight="bold" mb={5}>
                TESTOWA STRONA STARTOWA
            </Text>
            <Image
                alt="ResetPassword Image"
                objectFit="scale-down"
                src="/img/sma-logo-2.png"
                width="600px"
                height="auto"
            />
        </Flex>
    );
};

export default AppBanner;
