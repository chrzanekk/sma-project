import React, {useState} from 'react';
import {Button, Flex, Input, Text} from '@chakra-ui/react';
import {themeVars, useThemeColors} from "@/theme/theme-colors.ts";
import {useTranslation} from "react-i18next";
import {CustomSimpleSelect} from "@/components/shared/CustomFormFields.tsx";

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
    const themeColors = useThemeColors();

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
              mt={1}
              mb={1}
              p={1}
              bg={themeColors.bgColorPrimary}
              borderRadius={"md"}
              fontSize={"sm"}
        >
            {/* Wybór liczby wierszy */}
            <CustomSimpleSelect
                value={rowsPerPage}
                onChange={onRowsPerPageChange}
                options={[
                    {value: 5, label: "5"},
                    {value: 10, label: "10"},
                    {value: 20, label: "20"},
                    {value: 50, label: "50"},
                    {value: 100, label: "100"},
                ]}
                bgColor={themeVars.bgColorSecondary}
                width="100px"
                size="xs"
                hideArrow={true}
            />
            {/* Nawigacja między stronami */}
            <Button
                size={"2xs"}
                colorPalette={"blue"}
                onClick={() => onPageChange(currentPage - 1)}
                disabled={currentPage === 0}
                mr={2}
            >
                {t('previous', {ns: 'common'})}
            </Button>
            <Text color={themeColors.fontColor}>
                {t('page', {ns: 'common'})} {currentPage + 1} {t('of', {ns: 'common'})} {totalPages}
            </Text>
            <Button
                size={"2xs"}
                colorPalette={"blue"}
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
                bg={themeColors.bgColorSecondary}
                color={themeColors.fontColor}
                mr={2}
                value={gotoPage}
                disabled={currentPage + 1 === totalPages}
                onChange={(e) => setGotoPage(e.target.value)}
                placeholder={t('goToPage', {ns: 'common'})}
                _placeholder={{color: themeVars.fontColor}}
            />
            <Button size={"2xs"}
                    onClick={handleGotoPage}
                    colorPalette={"blue"}
                    disabled={currentPage + 1 === totalPages}
            >
                {t('go', {ns: 'common'})}
            </Button>
            {/*</Flex>*/}
        </Flex>
    );
};

export default Pagination;
