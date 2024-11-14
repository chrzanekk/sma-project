import { useColorModeValue } from '@chakra-ui/react';

export const themeColors = {
    bgColor: () => useColorModeValue('green.300', 'green.900'),
    popoverBgColor: () => useColorModeValue('white', 'green.300'),
    highlightBgColor: () => useColorModeValue('green.400', 'green.900'),
    borderColor: () => useColorModeValue('green.100','green.100'),
    fontColor: () => useColorModeValue('green.600','green.100'),
    fontColorChildMenu: () => useColorModeValue('green.800','green.100')
};
