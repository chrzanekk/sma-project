import React, {useState} from 'react';
import {Button, Flex, Input, Select, Text} from '@chakra-ui/react';

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
        const page = parseInt(gotoPage, 10)-1;
        if (page >= 0 && page < totalPages) {
            onPageChange(page);
        }
    };
    return (
        <Flex justify="space-between"
              align="center"
              mt={2}
              p={2}
              bg={"green.300"}
              borderRadius={"md"}
              fontSize={"sm"}
        >
            {/* Wybór liczby wierszy */}
            <Select
                width="100px" // Zmniejszona szerokość
                size="sm" // Mniejszy rozmiar
                value={rowsPerPage}
                onChange={(e) => onRowsPerPageChange(Number(e.target.value))}
            >
                <option value={5}>5</option>
                <option value={10}>10</option>
                <option value={20}>20</option>
                <option value={50}>50</option>
                <option value={100}>100</option>
            </Select>

            {/* Nawigacja między stronami */}
            <Flex align="center">
                <Button
                    size={"sm"}
                    onClick={() => onPageChange(currentPage - 1)}
                    isDisabled={currentPage === 1}
                    mr={2}
                >
                    Previous
                </Button>
                <Text>
                    Page {currentPage} of {totalPages}
                </Text>
                <Button
                    size={"sm"}
                    onClick={() => onPageChange(currentPage + 1)}
                    isDisabled={currentPage === totalPages}
                    ml={2}
                >
                    Next
                </Button>
            </Flex>

            {/* Skok do konkretnej strony */}
            <Flex align="center">
                <Input
                    width="70px"
                    size={"sm"} // Mniejszy input
                    mr={2}
                    value={gotoPage}
                    onChange={(e) => setGotoPage(e.target.value)}
                    placeholder="Go to page"
                />
                <Button size={"sm"} onClick={handleGotoPage}>Go</Button>
            </Flex>
        </Flex>
    );
};

export default Pagination;
