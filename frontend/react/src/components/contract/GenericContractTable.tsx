import {useTranslation} from "react-i18next";
import {Box, Button, HStack, Table, Text, useDisclosure} from "@chakra-ui/react";
import {useState} from "react";
import {Field} from "@/components/ui/field.tsx";
import DateFormatter from "@/utils/date-formatter.ts";
import ConfirmModal from "@/components/shared/ConfirmModal.tsx";
import {useThemeColors} from "@/theme/theme-colors.ts";
import {useTableStyles} from "@/components/shared/tableStyles.ts";
import {BaseContractDTO, ContractDTO, FetchableContractDTO} from "@/types/contract-types.ts";
import EditContractDialog from "@/components/contract/EditContractDialog.tsx";

function isFetchableContract(
    contract: BaseContractDTO
): contract is FetchableContractDTO {
    return "createdDatetime" in contract;
}


interface Props<T extends BaseContractDTO> {
    contracts: T[];
    onDelete: (id: number) => void;
    fetchContracts: () => void;
    onSortChange: (field: string) => void;
    sortField: string | null;
    sortDirection: "asc" | "desc";
    extended?: boolean;
}

const GenericContractTable = <T extends ContractDTO>({
                                                         contracts,
                                                         onDelete,
                                                         fetchContracts,
                                                         onSortChange,
                                                         sortField,
                                                         sortDirection,
                                                         extended = false,
                                                     }: Props<T>) => {
    const {t} = useTranslation(["common", "contracts"]);
    const {open, onOpen, onClose} = useDisclosure();
    const {commonCellProps, commonColumnHeaderProps} = useTableStyles();
    const [selectedContractId, setSelectedContractId] = useState<number | null>(null);
    const themeColors = useThemeColors();

    const handleDeleteClick = (id: number) => {
        setSelectedContractId(id);
        onOpen();
    };

    const confirmDelete = () => {
        if (selectedContractId !== null) {
            onDelete(selectedContractId);
        }
        onClose();
    };

    const renderSortIndicator = (field: string) => {

        if (sortField === field) {
            return sortDirection === "asc" ? "↑" : "↓";
        }
        return null;
    };

    if (!contracts || contracts.length === 0) {
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
                                onClick={() => onSortChange("number")}
                            >
                                {t("contracts:number")} {renderSortIndicator("number")}
                            </Table.ColumnHeader>
                            <Table.ColumnHeader
                                {...commonColumnHeaderProps}
                                onClick={() => onSortChange("description")}
                            >
                                {t("contracts:description")} {renderSortIndicator("description")}
                            </Table.ColumnHeader>
                            <Table.ColumnHeader
                                {...commonColumnHeaderProps}
                                onClick={() => onSortChange("value")}
                            >
                                {t("contracts:value")} {renderSortIndicator("value")}
                            </Table.ColumnHeader>
                            <Table.ColumnHeader
                                {...commonColumnHeaderProps}
                                onClick={() => onSortChange("currency")}
                            >
                                {t("contracts:currency")} {renderSortIndicator("currency")}
                            </Table.ColumnHeader>
                            <Table.ColumnHeader
                                {...commonColumnHeaderProps}
                                onClick={() => onSortChange("contractor")}
                            >
                                {t("contracts:contractor")} {renderSortIndicator("contractor")}
                            </Table.ColumnHeader>
                            <Table.ColumnHeader
                                {...commonColumnHeaderProps}
                                onClick={() => onSortChange("constructionSite")}
                            >
                                {t("contracts:constructionSite")} {renderSortIndicator("constructionSite")}
                            </Table.ColumnHeader>
                            <Table.ColumnHeader
                                {...commonColumnHeaderProps}
                                onClick={() => onSortChange("startDate")}
                            >
                                {t("contracts:startDate")} {renderSortIndicator("startDate")}
                            </Table.ColumnHeader>
                            <Table.ColumnHeader
                                {...commonColumnHeaderProps}
                                onClick={() => onSortChange("endDate")}
                            >
                                {t("contracts:endDate")} {renderSortIndicator("endDate")}
                            </Table.ColumnHeader>
                            <Table.ColumnHeader
                                {...commonColumnHeaderProps}
                                onClick={() => onSortChange("signupDate")}
                            >
                                {t("contracts:signupDate")} {renderSortIndicator("signupDate")}
                            </Table.ColumnHeader>
                            <Table.ColumnHeader
                                {...commonColumnHeaderProps}
                                onClick={() => onSortChange("realEndDate")}
                            >
                                {t("contracts:realEndDate")} {renderSortIndicator("realEndDate")}
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
                        {contracts.map((contract) => (
                            <Table.Row key={contract.id}
                                       bg={themeColors.bgColorSecondary}
                                       _hover={{
                                           textDecoration: 'none',
                                           bg: themeColors.highlightBgColor,
                                           color: themeColors.fontColorHover
                                       }}
                            >
                                <Table.Cell {...commonCellProps} width={"2%"}>{contract.id}</Table.Cell>
                                <Table.Cell {...commonCellProps} width={"10%"}>{contract.number}</Table.Cell>
                                <Table.Cell {...commonCellProps} width={"20%"}>{contract.description}</Table.Cell>
                                <Table.Cell {...commonCellProps} width={"10%"}>{contract.value}</Table.Cell>
                                <Table.Cell {...commonCellProps} width={"2%"}>{contract.currency}</Table.Cell>
                                <Table.Cell {...commonCellProps} width={"10%"}>{contract.contractor?.name}</Table.Cell>
                                <Table.Cell {...commonCellProps}
                                            width={"10%"}>{contract.constructionSite?.name}</Table.Cell>
                                <Table.Cell {...commonCellProps} width={"20%"}>{contract.startDate}</Table.Cell>
                                <Table.Cell {...commonCellProps} width={"20%"}>{contract.endDate}</Table.Cell>
                                <Table.Cell {...commonCellProps} width={"20%"}>{contract.signupDate}</Table.Cell>
                                <Table.Cell {...commonCellProps} width={"20%"}>{contract.realEndDate}</Table.Cell>
                                {extended && isFetchableContract(contract) && (
                                    <>
                                        {/* Poniższe właściwości mogą być undefined dla BaseContactDTOForContractor */}
                                        {"createdDatetime" in contract && (
                                            <Table.Cell {...commonCellProps} width={"5%"} fontSize={"x-small"}>
                                                {DateFormatter.formatDateTime(
                                                    (contract as FetchableContractDTO).createdDatetime
                                                )}
                                            </Table.Cell>
                                        )}
                                        {"createdBy" in contract && (
                                            <Table.Cell {...commonCellProps} width={"5%"} fontSize={"x-small"}>
                                                <Box>
                                                    {(contract as FetchableContractDTO).createdBy.firstName.charAt(0)}. {(contract as FetchableContractDTO).createdBy.lastName}
                                                </Box>
                                            </Table.Cell>
                                        )}
                                        {"lastModifiedDatetime" in contract && (
                                            <Table.Cell {...commonCellProps} width={"5%"} fontSize={"x-small"}>
                                                {DateFormatter.formatDateTime(
                                                    (contract as FetchableContractDTO).lastModifiedDatetime
                                                )}
                                            </Table.Cell>
                                        )}
                                        {"modifiedBy" in contract && (
                                            <Table.Cell {...commonCellProps} width={"5%"} fontSize={"x-small"}>
                                                <Box>
                                                    {(contract as FetchableContractDTO).modifiedBy.firstName.charAt(0)}. {(contract as FetchableContractDTO).modifiedBy.lastName}
                                                </Box>
                                            </Table.Cell>
                                        )}
                                    </>
                                )}
                                {extended && (
                                    <Table.Cell {...commonCellProps}>
                                        <HStack gap={1} justifyContent="center">
                                            <EditContractDialog
                                                fetchContracts={fetchContracts}
                                                contractId={contract.id!}
                                                contractorId={1}
                                                constructionSiteId={1}
                                            />
                                            <Button
                                                colorPalette="orange"
                                                size="2xs"
                                                onClick={() => handleDeleteClick(contract.id!)}
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

export default GenericContractTable;
