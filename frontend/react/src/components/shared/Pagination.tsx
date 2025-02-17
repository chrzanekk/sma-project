import React, {useState} from 'react';
import {Button, Flex, Input, NativeSelectField, NativeSelectRoot, Text} from '@chakra-ui/react';
import {themeColors, themeColorsHex} from "@/theme/theme-colors.ts";
import {useTranslation} from "react-i18next";

interface PaginationProps {
    currentPage: number;
    totalPages: number;
    rowsPerPage: number;
    onPageChange: (page: number) => void;
    onRowsPerPageChange: (size: number) => void;
}

const Pagination: React.FC<PaginationProps> = ({
                                                   currentPage,
                                                   totalPages,
                                                   rowsPerPage,
                                                   onPageChange,
                                                   onRowsPerPageChange,
                                               }) => {

    const [gotoPage, setGotoPage] = useState("");
    const {t} = useTranslation('auth')

    const handleGotoPage = () => {
        const page = parseInt(gotoPage, 10) - 1;
        if (page >= 0 && page < totalPages) {
            onPageChange(page);
        }
    };
    return (
        <Flex justify="center"
              align="center"
              gap={4}
              mt={2}
              p={1}
              bg={themeColors.bgColorPrimary()}
              borderRadius={"md"}
              fontSize={"sm"}
        >
            {/* Wybór liczby wierszy */}
            <NativeSelectRoot
                width="110px"
                size="xs"
                borderRadius={"md"}
                bg={themeColors.bgColorPrimary()}
                color={themeColors.fontColor()}
                disabled={currentPage + 1 === totalPages}
            >
                <NativeSelectField
                    value={rowsPerPage}
                    onChange={(e) => onRowsPerPageChange(Number(e.target.value))}
                >
                    <option value={5}>5</option>
                    <option value={10}>10</option>
                    <option value={20}>20</option>
                    <option value={50}>50</option>
                    <option value={100}>100</option>
                </NativeSelectField>
            </NativeSelectRoot>

            {/* Nawigacja między stronami */}
            <Button
                size={"2xs"}
                onClick={() => onPageChange(currentPage - 1)}
                disabled={currentPage === 0}
                mr={2}
            >
                {t('previous', {ns: 'common'})}
            </Button>
            <Text color={themeColors.fontColor()}>
                {t('page', {ns: 'common'})} {currentPage + 1} {t('of', {ns: 'common'})} {totalPages}
            </Text>
            <Button
                size={"2xs"}
                onClick={() => onPageChange(currentPage + 1)}
                disabled={currentPage + 1 === totalPages}
                ml={2}
            >
                {t('next', {ns: 'common'})}
            </Button>

            {/* Skok do konkretnej strony */}
            <Input
                width="110px"
                size={"2xs"}
                borderRadius={"md"}
                bg={themeColors.bgColorPrimary()}
                color={themeColors.fontColor()}
                mr={2}
                value={gotoPage}
                disabled={currentPage + 1 === totalPages}
                onChange={(e) => setGotoPage(e.target.value)}
                placeholder={t('goToPage', {ns: 'common'})}
                _placeholder={{color: themeColorsHex.fontColor()}}
            />
            <Button size={"2xs"}
                    onClick={handleGotoPage}
                    disabled={currentPage + 1 === totalPages}
            >
                {t('go', {ns: 'common'})}
            </Button>
            {/*</Flex>*/}
        </Flex>
    );
};

export default Pagination;
