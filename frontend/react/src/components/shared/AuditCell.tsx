import React from "react";
import { Table, Box } from "@chakra-ui/react";
import DateFormatter from "@/utils/date-formatter.ts";


interface AuditCellProps {
    value?: string | null;
    user?: { firstName: string; lastName: string } | null;
    cellProps?: any;
    width?: string;
    fontSize?: string;
}

const AuditCell: React.FC<AuditCellProps> = ({
                                                 value,
                                                 user,
                                                 cellProps,
                                                 width = "5%",
                                                 fontSize = "x-small",
                                             }) => {
    if (!value || !user) return <Table.Cell {...cellProps} width={width} fontSize={fontSize}></Table.Cell>;

    return (
        <Table.Cell {...cellProps} width={width} fontSize={fontSize}>
            {DateFormatter.formatDateTime(value)}
            <Box>
                {user.firstName.charAt(0)}. {user.lastName}
            </Box>
        </Table.Cell>
    );
};

export default AuditCell;
