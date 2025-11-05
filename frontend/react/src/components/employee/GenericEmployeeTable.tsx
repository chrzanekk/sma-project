import {EmployeeBaseDTO, EmployeeDTO, FetchableEmployeeDTO} from "@/types/employee-types.ts";
import {useTranslation} from "react-i18next";
import {Box, Text, Table, useDisclosure} from "@chakra-ui/react";
import {useTableStyles} from "@/components/shared/tableStyles.ts";
import {useState} from "react";
import {useThemeColors} from "@/theme/theme-colors.ts";
import {Field} from "@/components/ui/field.tsx";

function isFetchableEmployee(
    employee: EmployeeBaseDTO
): employee is FetchableEmployeeDTO {
    return "createDatetime" in employee;
}

interface Props<T extends EmployeeBaseDTO> {
    employees: T[];
    onDelete: (id: number) => void;
    fetchEmployees: () => void;
    onSortChange: (field: string) => void;
    sortField: string | null;
    sortDirection: "asc" | "desc";
    extended?: boolean;
}

const GenericEmployeeTable = <T extends EmployeeDTO>({
                                                         employees,
                                                         fetchEmployees,
                                                         onDelete,
                                                         onSortChange,
                                                         sortField,
                                                         sortDirection,
                                                         extended = false,
                                                     }: Props<T>) => {
    const {t} = useTranslation(['common', 'employees']);
    const {open, onOpen, onClose} = useDisclosure();
    const {commonCellProps, commonColumnHeaderProps} = useTableStyles();
    const [selectedEmployeeId, setSelectedEmployeeId] = useState<number | null>(null);
    const themeColors = useThemeColors();

    const handleDeleteClick = (id: number) => {
        setSelectedEmployeeId(id);
        onOpen();
    };

    const confirmDelete = () => {
        if (selectedEmployeeId !== null) {
            onDelete(selectedEmployeeId);
        }
        onClose();
    };

    const renderSortIndicator = (field: string) => {

        if (sortField === field) {
            return sortDirection === "asc" ? "↑" : "↓";
        }
        return null;
    };

    if (!employees || employees.length === 0) {
        return (
            <Field alignContent="center">
                <Text fontSize={20}>{t("dataNotFound")}</Text>
            </Field>
        )
    }

    return (
        <Box>
            <Table.ScrollArea height={"auto"} borderWidth={"1px"} borderRadius={"md"} borderColor={"grey"}>
                <Table.Root size={"sm"}
                            interactive
                            showColumnBorder
                            color={themeColors.fontColor}
                >
                    <Table.Header>
                        <Table.Row bg={themeColors.bgColorPrimary}>
                            <Table.ColumnHeader
                                {...commonColumnHeaderProps}
                                onClick={() => onSortChange("id")}
                            >
                                ID {renderSortIndicator("id")}
                            </Table.ColumnHeader>
                            <Table.ColumnHeader
                                {...commonColumnHeaderProps}
                                onClick={() => onSortChange("firstName")}
                            >
                                {t("employees:firstName")} {renderSortIndicator("firstName")}
                            </Table.ColumnHeader>
                            <Table.ColumnHeader
                                {...commonColumnHeaderProps}
                                onClick={() => onSortChange("lastName")}
                            >
                                {t("employees:lastName")} {renderSortIndicator("lastName")}
                            </Table.ColumnHeader>
                            <Table.ColumnHeader
                                {...commonColumnHeaderProps}
                                onClick={() => onSortChange("hourRate")}
                            >
                                {t("employees:hourRate")} {renderSortIndicator("hourRate")}
                            </Table.ColumnHeader>
                            <Table.ColumnHeader
                                {...commonColumnHeaderProps}
                                onClick={() => onSortChange("position")}
                            >
                                {t("positions:position")} {renderSortIndicator("position")}
                            </Table.ColumnHeader>
                            {extended && (

                            )}
                        </Table.Row>
                    </Table.Header>
                    <Table.Body>

                    </Table.Body>
                </Table.Root>
            </Table.ScrollArea>
        </Box>
)

}