import React, {useState} from 'react';
import {Button, Flex, Input, Select, Text} from '@chakra-ui/react';
import {themeColors} from "@/theme/theme-colors.ts";
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
              bg={themeColors.bgColor()}
              borderRadius={"md"}
              fontSize={"sm"}
        >
            {/* Wybór liczby wierszy */}
            <Select
                width="110px"
                size="xs"
                borderRadius={"md"}
                bg={themeColors.bgColorLight()}
                value={rowsPerPage}
                isDisabled={currentPage + 1 === totalPages}
                onChange={(e) => onRowsPerPageChange(Number(e.target.value))}
            >
                <option value={5}>5</option>
                <option value={10}>10</option>
                <option value={20}>20</option>
                <option value={50}>50</option>
                <option value={100}>100</option>
            </Select>

            {/* Nawigacja między stronami */}
            <Button
                size={"xs"}
                onClick={() => onPageChange(currentPage - 1)}
                isDisabled={currentPage === 0} // Poprawka: Blokuj tylko, jeśli to pierwsza strona
                mr={2}
            >
                {t('previous', { ns: 'common' })}
            </Button>
            <Text>
                {t('page', { ns: 'common' })} {currentPage + 1} {t('of', { ns: 'common' })} {totalPages}
            </Text>
            <Button
                size={"xs"}
                onClick={() => onPageChange(currentPage + 1)}
                isDisabled={currentPage + 1 === totalPages} // Poprawka: Blokuj tylko, jeśli to ostatnia strona
                ml={2}
            >
                {t('next', { ns: 'common' })}
            </Button>

            {/* Skok do konkretnej strony */}
                <Input
                    width="110px"
                    size={"xs"}
                    borderRadius={"md"}
                    bg={themeColors.bgColorLight()}
                    mr={2}
                    value={gotoPage}
                    isDisabled={currentPage + 1 === totalPages}
                    onChange={(e) => setGotoPage(e.target.value)}
                    placeholder={t('goToPage', {ns:'common'})}
                />
                <Button size={"xs"}
                        onClick={handleGotoPage}
                        isDisabled={currentPage + 1 === totalPages}
                >
                    {t('go', {ns:'common'})}
                </Button>
            {/*</Flex>*/}
        </Flex>
    );
};

export default Pagination;
