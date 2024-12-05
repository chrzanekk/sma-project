import React, {useState} from 'react';
import {Button, Flex, Input, Select, Text} from '@chakra-ui/react';
import {themeColors} from "@/theme/theme-colors.ts";

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
                width="100px"
                size="sm"
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
                    size={"sm"}
                    onClick={() => onPageChange(currentPage)}
                    isDisabled={currentPage + 1 === totalPages}
                    mr={2}
                >
                    Previous
                </Button>
                <Text>
                    Page {currentPage + 1} of {totalPages}
                </Text>
                <Button
                    size={"sm"}
                    onClick={() => onPageChange(currentPage)}
                    isDisabled={currentPage + 1 === totalPages}
                    ml={2}
                >
                    Next
                </Button>

            {/* Skok do konkretnej strony */}
                <Input
                    width="100px"
                    size={"sm"}
                    borderRadius={"md"}
                    bg={themeColors.bgColorLight()}
                    mr={2}
                    value={gotoPage}
                    isDisabled={currentPage + 1 === totalPages}
                    onChange={(e) => setGotoPage(e.target.value)}
                    placeholder="Go to page"
                />
                <Button size={"sm"}
                        onClick={handleGotoPage}
                        isDisabled={currentPage + 1 === totalPages}
                >Go</Button>
            {/*</Flex>*/}
        </Flex>
    );
};

export default Pagination;
