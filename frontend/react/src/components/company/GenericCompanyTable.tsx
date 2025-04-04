import {CompanyBaseDTO, FetchableCompanyDTO} from "@/types/company-type.ts";
import {useTranslation} from "react-i18next";
import {useTableStyles} from "@/components/shared/tableStyles.ts";
import {useState} from "react";
import {useThemeColors} from "@/theme/theme-colors.ts";
import {Field} from "@/components/ui/field.tsx";
import {Box, Button, HStack, Table, Text, useDisclosure} from "@chakra-ui/react";
import DateFormatter from "@/utils/date-formatter.ts";
import ConfirmModal from "@/components/shared/ConfirmModal.tsx";
import EditCompanyDialog from "@/components/company/EditCompanyDialog.tsx";

function isFetchableCompany(
    company: CompanyBaseDTO
): company is FetchableCompanyDTO {
    return "createdDatetime" in company;
}

interface Props<T extends CompanyBaseDTO> {
    companies: T[];
    onDelete: (id: number) => void;
    fetchCompanies: () => void;
    onSortChange: (field: string) => void;
    sortField: string | null;
    sortDirection: "asc" | "desc";
    extended?: boolean;
}

const GenericCompanyTable = <T extends CompanyBaseDTO>({
                                                           companies,
                                                           onDelete,
                                                           fetchCompanies,
                                                           onSortChange,
                                                           sortField,
                                                           sortDirection,
                                                           extended = false
                                                       }: Props<T>) => {
    const {t} = useTranslation(["common", "companies"]);
    const {open, onOpen, onClose} = useDisclosure();
    const {commonCellProps, commonColumnHeaderProps} = useTableStyles();
    const [selectCompanyId, setSelectedCompanyId] = useState<number | null>(null);
    const themeColors = useThemeColors();

    const handleDeleteClick = (id: number) => {
        setSelectedCompanyId(id);
        onOpen();
    }

    const confirmDelete = () => {
        if (selectCompanyId !== null) {
            onDelete(selectCompanyId);
        }
        onClose();
    }

    const renderSortIndicator = (field: string) => {

        if (sortField === field) {
            return sortDirection === "asc" ? "↑" : "↓";
        }
        return null;
    };

    if (!companies || companies.length === 0) {
        return (
            <Field alignContent="center">
                <Text fontSize={20}>{t("dataNotFound")}</Text>
            </Field>
        );
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
                                onClick={() => onSortChange("name")}
                            >
                                {t("companies:name")} {renderSortIndicator("name")}
                            </Table.ColumnHeader>
                            <Table.ColumnHeader
                                {...commonColumnHeaderProps}
                                onClick={() => onSortChange("taxNumber")}
                            >
                                {t("companies:taxNumber")} {renderSortIndicator("taxNumber")}
                            </Table.ColumnHeader>
                            <Table.ColumnHeader
                                {...commonColumnHeaderProps}
                                onClick={() => onSortChange("phoneNumber")}
                            >
                                {t("companies:phoneNumber")} {renderSortIndicator("phoneNumber")}
                            </Table.ColumnHeader>
                            <Table.ColumnHeader
                                {...commonColumnHeaderProps}
                                onClick={() => onSortChange("email")}
                            >
                                {t("companies:email")} {renderSortIndicator("email")}
                            </Table.ColumnHeader>
                            <Table.ColumnHeader
                                {...commonColumnHeaderProps}
                                onClick={() => onSortChange("additionalInfo")}
                            >
                                {t("companies:additionalInfo")} {renderSortIndicator("additionalInfo")}
                            </Table.ColumnHeader>
                            {extended && (
                                <>
                                    <Table.ColumnHeader
                                        {...commonColumnHeaderProps}
                                        onClick={() => onSortChange("createdDatetime")}
                                    >
                                        {t("createDate")} {renderSortIndicator("createdDatetime")}
                                    </Table.ColumnHeader>
                                    <Table.ColumnHeader
                                        {...commonColumnHeaderProps}
                                        onClick={() => onSortChange("createdBy")}
                                    >
                                        {t("createdBy")} {renderSortIndicator("createdBy")}
                                    </Table.ColumnHeader>
                                    <Table.ColumnHeader
                                        {...commonColumnHeaderProps}
                                        onClick={() => onSortChange("lastModifiedDatetime")}
                                    >
                                        {t("lastModifiedDate")} {renderSortIndicator("lastModifiedDatetime")}
                                    </Table.ColumnHeader>
                                    <Table.ColumnHeader
                                        {...commonColumnHeaderProps}
                                        onClick={() => onSortChange("modifiedBy")}
                                    >
                                        {t("lastModifiedBy")} {renderSortIndicator("modifiedBy")}
                                    </Table.ColumnHeader>
                                    <Table.ColumnHeader {...commonColumnHeaderProps}
                                    >{t("edit")}
                                    </Table.ColumnHeader>
                                </>
                            )}
                        </Table.Row>
                    </Table.Header>
                    <Table.Body>
                        {companies.map((company) => (
                            <Table.Row key={company.id}
                                       bg={themeColors.bgColorSecondary}
                                       _hover={{
                                           textDecoration: 'none',
                                           bg: themeColors.highlightBgColor,
                                           color: themeColors.fontColorHover
                                       }}
                            >
                                <Table.Cell {...commonCellProps} width={"2%"}>{company.id}</Table.Cell>
                                <Table.Cell {...commonCellProps} width={"10%"}>{company.name}</Table.Cell>
                                <Table.Cell {...commonCellProps} width={"10%"}>{""}</Table.Cell>
                                <Table.Cell {...commonCellProps} width={"10%"}>{""}</Table.Cell>
                                <Table.Cell {...commonCellProps} width={"10%"}>{""}</Table.Cell>
                                <Table.Cell {...commonCellProps} width={"30%"}>{company.additionalInfo}</Table.Cell>
                                {extended && isFetchableCompany(company) && (
                                    <>
                                        {/* Poniższe właściwości mogą być undefined dla BaseContactDTOForContractor */}
                                        {"createdDatetime" in company && (
                                            <Table.Cell {...commonCellProps} width={"5%"} fontSize={"x-small"}>
                                                {DateFormatter.formatDateTime(
                                                    (company as FetchableCompanyDTO).createdDatetime
                                                )}
                                            </Table.Cell>
                                        )}
                                        {"createdByFirstName" in company && (
                                            <Table.Cell {...commonCellProps} width={"5%"} fontSize={"x-small"}>
                                                <Box>
                                                    {(company as FetchableCompanyDTO).createdByFirstName.charAt(0)}. {(company as FetchableCompanyDTO).createdByLastName}
                                                </Box>
                                            </Table.Cell>
                                        )}
                                        {"lastModifiedDatetime" in company && (
                                            <Table.Cell {...commonCellProps} width={"5%"} fontSize={"x-small"}>
                                                {DateFormatter.formatDateTime(
                                                    (company as FetchableCompanyDTO).lastModifiedDatetime
                                                )}
                                            </Table.Cell>
                                        )}
                                        {"modifiedByFirstName" in company && (
                                            <Table.Cell {...commonCellProps} width={"5%"} fontSize={"x-small"}>
                                                <Box>
                                                    {(company as FetchableCompanyDTO).modifiedByFirstName.charAt(0)}. {(company as FetchableCompanyDTO).modifiedByLastName}
                                                </Box>
                                            </Table.Cell>
                                        )}
                                    </>
                                )}
                                {extended && (
                                    <Table.Cell {...commonCellProps}>
                                        <HStack gap={1} justifyContent="center">
                                            <EditCompanyDialog
                                                fetchCompanies={fetchCompanies}
                                                companyId={company.id!}
                                            />
                                            <Button
                                                colorPalette="orange"
                                                size="2xs"
                                                onClick={() => handleDeleteClick(company.id!)}
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
    )

}

export default GenericCompanyTable;

