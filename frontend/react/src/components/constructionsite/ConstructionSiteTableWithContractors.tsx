import React, {useState} from "react";
import {Box, Button, Collapsible, Spinner, Table, Text, useDisclosure,} from "@chakra-ui/react";
import {useTranslation} from "react-i18next";
import ConfirmModal from "@/components/shared/ConfirmModal.tsx";
import {FetchableConstructionSiteDTO} from "@/types/constrution-site-types.ts";
import {useThemeColors} from "@/theme/theme-colors.ts";
import {useTableStyles} from "@/components/shared/tableStyles.ts";
import DateFormatter from "@/utils/date-formatter.ts";
import {getContractorsByConstructionSiteIdPaged} from "@/services/construction-site-service.ts";
import {ContractorDTO} from "@/types/contractor-types.ts";
import GenericContractorTable from "@/components/contractor/GenericContractorTable.tsx";

interface Props {
    constructionSites: FetchableConstructionSiteDTO[];
    onDelete: (id: number) => void;
    onSortChange: (field: string) => void;
    sortField: string | null;
    sortDirection: "asc" | "desc";
    contractorFetchContractors: (customFilter?: Record<string, any>, page?: number, size?: number) => Promise<void>;
    contractorOnDelete: (id: number) => void;
    contractorOnSortChange: (field: string) => void;
    contractorSortField: string | null;
    contractorSortDirection: "asc" | "desc";
}

type ContractorState = {
    contractors: ContractorDTO[];
    page: number;
    hasMore: boolean;
    loading: boolean;
};

const ConstructionSiteTableWithContractors: React.FC<Props> = ({
                                                                   constructionSites,
                                                                   onDelete,
                                                                   onSortChange,
                                                                   sortField,
                                                                   sortDirection,
                                                                   contractorFetchContractors,
                                                                   contractorOnDelete,
                                                                   contractorOnSortChange,
                                                                   contractorSortField,
                                                                   contractorSortDirection,
                                                               }) => {
    const {t} = useTranslation(["common", "constructionSites", "contractors"]);
    const themeColors = useThemeColors();
    const {commonCellProps, commonColumnHeaderProps} = useTableStyles();
    const [selectedConstructionSiteId, setSelectedConstructionSiteId] = useState<number | null>(null);
    const [expandedRow, setExpandedRow] = useState<number | null>(null);
    const [contractorStates, setContractorStates] = useState<{ [key: number]: ContractorState }>({});

    const handleDeleteClick = (id: number) => {
        setSelectedConstructionSiteId(id);
        onOpen();
    };

    const confirmDelete = () => {
        if (selectedConstructionSiteId !== null) {
            onDelete(selectedConstructionSiteId);
        }
        onClose();
    };

    const toggleExpand = async (constructionSiteId: number) => {
        if (expandedRow === constructionSiteId) {
            setExpandedRow(null);
            setContractorStates((prev) => {
                const newStates = {...prev};
                delete newStates[constructionSiteId];
                return newStates;
            });
        } else {
            setExpandedRow(constructionSiteId);
            setContractorStates({
                [constructionSiteId]: {
                    contractors: [],
                    page: 0,
                    hasMore: true,
                    loading: true,
                },
            });

            try {
                const initial = await getContractorsByConstructionSiteIdPaged(constructionSiteId, 0, 10);
                setContractorStates({
                    [constructionSiteId]: {
                        contractors: initial.items,
                        page: 1,
                        hasMore: initial.totalPages > 1,
                        loading: false,
                    },
                });
            } catch (err) {
                console.error("Błąd ładowania kontrahentów:", err);
            }
        }
    };

    const loadMoreContractors = async (constructionSiteId: number) => {
        const state = contractorStates[constructionSiteId];
        if (!state || !state.hasMore || state.loading) return;

        setContractorStates((prev) => ({
            ...prev,
            [constructionSiteId]: {...state, loading: true},
        }));

        try {
            const more = await getContractorsByConstructionSiteIdPaged(constructionSiteId, state.page, 10);
            setContractorStates((prev) => ({
                ...prev,
                [constructionSiteId]: {
                    contractors: [...state.contractors, ...more.items],
                    page: state.page + 1,
                    hasMore: state.page + 1 < more.totalPages,
                    loading: false,
                },
            }));
        } catch (err) {
            console.error("Błąd ładowania kolejnych kontrahentów:", err);
        }
    };

    const renderSortIndicator = (field: string) => {
        if (sortField === field) {
            return sortDirection === "asc" ? "↑" : "↓";
        }
        return null;
    };

    if (!constructionSites || constructionSites.length === 0) {
        return <Text fontSize={20}>{t("dataNotFound", {ns: "common"})}</Text>;
    }

    return (
        <>
            <Table.ScrollArea height="auto" borderWidth="1px" borderRadius="md" borderColor="grey">
                <Table.Root size="sm" interactive showColumnBorder color={themeColors.fontColor}>
                    <Table.Header>
                        <Table.Row bg={themeColors.bgColorPrimary}>
                            <Table.ColumnHeader {...commonColumnHeaderProps}
                                                onClick={() => onSortChange("id")}>ID {renderSortIndicator("id")}</Table.ColumnHeader>
                            <Table.ColumnHeader {...commonColumnHeaderProps}
                                                onClick={() => onSortChange("name")}>{t("name")} {renderSortIndicator("name")}</Table.ColumnHeader>
                            <Table.ColumnHeader {...commonColumnHeaderProps}
                                                onClick={() => onSortChange("address")}>{t("address")} {renderSortIndicator("address")}</Table.ColumnHeader>
                            <Table.ColumnHeader {...commonColumnHeaderProps}
                                                onClick={() => onSortChange("country")}>{t("country")} {renderSortIndicator("country")}</Table.ColumnHeader>
                            <Table.ColumnHeader {...commonColumnHeaderProps}
                                                onClick={() => onSortChange("shortName")}>
                                {t("shortName")} {renderSortIndicator("shortName")}
                            </Table.ColumnHeader>
                            <Table.ColumnHeader {...commonColumnHeaderProps}
                                                onClick={() => onSortChange("code")}>
                                {t("code")} {renderSortIndicator("code")}
                            </Table.ColumnHeader>
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
                        </Table.Row>
                    </Table.Header>
                    <Table.Body>
                        {constructionSites.map((cs) => {
                            const constructionSiteId = cs.id!;
                            const contractorState = contractorStates[cs.id!];
                            return (
                                <React.Fragment key={cs.id}>
                                    <Table.Row
                                        onClick={() => toggleExpand(cs.id!)}
                                        bg={themeColors.bgColorSecondary}
                                        _hover={{bg: themeColors.highlightBgColor, color: themeColors.fontColorHover}}
                                        cursor="pointer"
                                    >
                                        <Table.Cell {...commonCellProps}>{cs.id}</Table.Cell>
                                        <Table.Cell {...commonCellProps}>{cs.name}</Table.Cell>
                                        <Table.Cell {...commonCellProps}>{cs.address}</Table.Cell>
                                        <Table.Cell {...commonCellProps}>{cs.country?.name}</Table.Cell>
                                        <Table.Cell {...commonCellProps}>{cs.shortName}</Table.Cell>
                                        <Table.Cell {...commonCellProps}>{cs.code}</Table.Cell>
                                        {"createdDatetime" in cs && (
                                            <Table.Cell {...commonCellProps} width={"5%"} fontSize={"x-small"}>
                                                {DateFormatter.formatDateTime(
                                                    (cs as FetchableConstructionSiteDTO).createdDatetime
                                                )}
                                            </Table.Cell>
                                        )}
                                        {"createdBy" in cs && (
                                            <Table.Cell {...commonCellProps} width={"5%"} fontSize={"x-small"}>
                                                <Box>
                                                    {(cs as FetchableConstructionSiteDTO).createdBy.firstName.charAt(0)}. {(cs as FetchableConstructionSiteDTO).createdBy.lastName}
                                                </Box>
                                            </Table.Cell>
                                        )}
                                        {"lastModifiedDatetime" in cs && (
                                            <Table.Cell {...commonCellProps} width={"5%"} fontSize={"x-small"}>
                                                {DateFormatter.formatDateTime(
                                                    (cs as FetchableConstructionSiteDTO).lastModifiedDatetime
                                                )}
                                            </Table.Cell>
                                        )}
                                        {"modifiedBy" in cs && (
                                            <Table.Cell {...commonCellProps} width={"5%"} fontSize={"x-small"}>
                                                <Box>
                                                    {(cs as FetchableConstructionSiteDTO).modifiedBy.firstName.charAt(0)}. {(cs as FetchableConstructionSiteDTO).modifiedBy.lastName}
                                                </Box>
                                            </Table.Cell>
                                        )}
                                        <Table.Cell {...commonCellProps}>
                                            <Button colorPalette="red" size="2xs"
                                                    onClick={() => handleDeleteClick(cs.id!)}>
                                                {t("delete", {ns: "common"})}
                                            </Button>
                                        </Table.Cell>
                                    </Table.Row>

                                    {/* Rozwijany wiersz z kontrahentami */}
                                    {expandedRow === constructionSiteId && (
                                        <Table.Row bg={themeColors.bgColorSecondary}>
                                            <Table.Cell colSpan={5} {...commonCellProps}>
                                                <Collapsible.Root open={true}>
                                                    <Collapsible.Content>
                                                        <Box
                                                            maxH="300px"
                                                            overflowY="auto"
                                                            onScroll={(e) => {
                                                                const target = e.target as HTMLElement;
                                                                if (target.scrollHeight - target.scrollTop <= target.clientHeight + 20) {
                                                                    loadMoreContractors(constructionSiteId).catch();
                                                                }
                                                            }}
                                                        >
                                                            <GenericContractorTable
                                                                contractors={contractorState?.contractors ?? []}
                                                                onDelete={contractorOnDelete}
                                                                fetchContractors={contractorFetchContractors}
                                                                onSortChange={contractorOnSortChange}
                                                                sortField={contractorSortField}
                                                                sortDirection={contractorSortDirection}
                                                            />
                                                            {contractorState?.loading && <Spinner size="sm" mt={2}/>}
                                                        </Box>
                                                    </Collapsible.Content>
                                                </Collapsible.Root>
                                            </Table.Cell>
                                        </Table.Row>
                                    )}
                                </React.Fragment>
                            );
                        })}
                    </Table.Body>
                </Table.Root>
            </Table.ScrollArea>

            <ConfirmModal
                isOpen={isOpen}
                onClose={onClose}
                onConfirm={confirmDelete}
                title={t("deleteConfirmation.title", {ns: "common"})}
                message={t("deleteConfirmation.message", {ns: "common"})}
                confirmText={t("delete", {ns: "common"})}
                cancelText={t("cancel", {ns: "common"})}
            />
        </>
    );
};

export default ConstructionSiteTableWithContractors;