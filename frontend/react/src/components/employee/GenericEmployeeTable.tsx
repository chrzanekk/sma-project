import {EmployeeBaseDTO, EmployeeDTO, FetchableEmployeeDTO} from "@/types/employee-types.ts";
import {useTranslation} from "react-i18next";
import {Box, Button, HStack, Table, Text, useDisclosure} from "@chakra-ui/react";
import {useTableStyles} from "@/components/shared/tableStyles.ts";
import {useState} from "react";
import {useThemeColors} from "@/theme/theme-colors.ts";
import {Field} from "@/components/ui/field.tsx";
import AuditCell from "@/components/shared/AuditCell.tsx";
import ConfirmModal from "@/components/shared/ConfirmModal.tsx";
import EditEmployeeDrawer from "@/components/employee/EditEmployeeDrawer.tsx";

function isFetchableEmployee(
    employee: EmployeeBaseDTO
): employee is FetchableEmployeeDTO {
    return "createdDatetime" in employee;
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
                                <>
                                    <Table.ColumnHeader
                                        {...commonColumnHeaderProps}
                                        onClick={() => onSortChange("createdDatetime")}
                                    >
                                        {t("created")} {renderSortIndicator("createdDatetime")}
                                    </Table.ColumnHeader>
                                    <Table.ColumnHeader
                                        {...commonColumnHeaderProps}
                                        onClick={() => onSortChange("lastModifiedDatetime")}
                                    >
                                        {t("lastModified")} {renderSortIndicator("lastModifiedDatetime")}
                                    </Table.ColumnHeader>
                                    <Table.ColumnHeader {...commonColumnHeaderProps}
                                    >{t("edit")}
                                    </Table.ColumnHeader>
                                </>
                            )}
                        </Table.Row>
                    </Table.Header>
                    <Table.Body>
                        {employees.map((employee) => (
                            <Table.Row key={employee.id}
                                       bg={themeColors.bgColorSecondary}
                                       _hover={{
                                           textDecoration: 'none',
                                           bg: themeColors.highlightBgColor,
                                           color: themeColors.fontColorHover
                                       }}>
                                <Table.Cell {...commonCellProps} width={"3%"}>{employee.id}</Table.Cell>
                                <Table.Cell {...commonCellProps} width={"25%"}>{employee.firstName}</Table.Cell>
                                <Table.Cell {...commonCellProps} width={"25%"}>{employee.lastName}</Table.Cell>
                                <Table.Cell {...commonCellProps} width={"10%"}>{employee.hourRate}</Table.Cell>
                                <Table.Cell {...commonCellProps} width={"15%"}>{employee.position.name}</Table.Cell>
                                {extended && isFetchableEmployee(employee) && (
                                    <>
                                        <AuditCell
                                            value={employee.createdDatetime}
                                            user={employee.createdBy}
                                            cellProps={commonCellProps}
                                        />
                                        <AuditCell
                                            value={employee.lastModifiedDatetime}
                                            user={employee.modifiedBy}
                                            cellProps={commonCellProps}
                                        />
                                    </>
                                )}
                                {extended && (
                                    <Table.Cell {...commonCellProps}>
                                        <HStack gap={1} justifyContent="center">
                                            <EditEmployeeDrawer fetchEmployees={fetchEmployees}
                                                                employeeId={employee.id!}/>
                                            <Button
                                                colorPalette="orange"
                                                size="2xs"
                                                onClick={() => handleDeleteClick(employee.id!)}
                                            >
                                                {t("delete", {ns: "common"})}
                                            </Button>
                                        </HStack>
                                    </Table.Cell>
                                )}
                            </Table.Row>
                        ))}
                    </Table.Body>
                </Table.Root>
            </Table.ScrollArea>
            <ConfirmModal
                isOpen={open}
                onClose={onClose}
                onConfirm={confirmDelete}
                title={t("deleteConfirmation.title", {ns: "common"})}
                message={t("deleteConfirmation.message", {ns: "common"})}
                confirmText={t("delete", {ns: "common"})}
                cancelText={t("cancel", {ns: "common"})}
            />
        </Box>
    );
};

export default GenericEmployeeTable;