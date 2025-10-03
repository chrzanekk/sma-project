import React, {useState} from "react";
import {Box, Button, Collapsible, HStack, Spinner, Table, Text,} from "@chakra-ui/react";
import {useTranslation} from "react-i18next";
import {FetchableConstructionSiteDTO} from "@/types/constrution-site-types.ts";
import {useThemeColors} from "@/theme/theme-colors.ts";
import {useTableStyles} from "@/components/shared/tableStyles.ts";
import {getContractorsByConstructionSiteIdPaged} from "@/services/construction-site-service.ts";
import {ContractorDTO} from "@/types/contractor-types.ts";
import GenericContractorTable from "@/components/contractor/GenericContractorTable.tsx";
import EditConstructionSiteDialog from "@/components/constructionsite/EditConstructionSiteDialog.tsx";
import AuditCell from "@/components/shared/AuditCell.tsx";

interface Props {
    constructionSites: FetchableConstructionSiteDTO[];
    onDelete: (id: number) => void;
    fetchConstructionSites: () => void;
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
                                                                   fetchConstructionSites,
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
    const [expandedRow, setExpandedRow] = useState<number | null>(null);
    const [contractorStates, setContractorStates] = useState<{ [key: number]: ContractorState }>({});

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
            setContractorStates({});
            setContractorStates((prev) => ({
                ...prev,
                [constructionSiteId]: {
                    contractors: [],
                    page: 0,
                    hasMore: true,
                    loading: true,
                },
            }));

            try {
                const initial = await getContractorsByConstructionSiteIdPaged(constructionSiteId, 0, 10);
                setContractorStates((prev) => ({
                    ...prev,
                    [constructionSiteId]: {
                        contractors: initial.items,
                        page: 1,
                        hasMore: initial.totalPages > 1,
                        loading: false,
                    },
                }));
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
        return (
            <Box>
                <Text fontSize={20}>{t("dataNotFound", {ns: "common"})}</Text>
            </Box>
        );
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
                                                onClick={() => onSortChange("name")}>{t("constructionSites:name")} {renderSortIndicator("name")}</Table.ColumnHeader>
                            <Table.ColumnHeader {...commonColumnHeaderProps}
                                                onClick={() => onSortChange("address")}>{t("constructionSites:address")} {renderSortIndicator("address")}</Table.ColumnHeader>
                            <Table.ColumnHeader {...commonColumnHeaderProps}
                                                onClick={() => onSortChange("country")}>{t("constructionSites:country")} {renderSortIndicator("country")}</Table.ColumnHeader>
                            <Table.ColumnHeader {...commonColumnHeaderProps}
                                                onClick={() => onSortChange("constructionSites:shortName")}>
                                {t("constructionSites:shortName")} {renderSortIndicator("shortName")}
                            </Table.ColumnHeader>
                            <Table.ColumnHeader {...commonColumnHeaderProps}
                                                onClick={() => onSortChange("code")}>
                                {t("constructionSites:code")} {renderSortIndicator("code")}
                            </Table.ColumnHeader>
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
                        </Table.Row>
                    </Table.Header>
                    <Table.Body>
                        {constructionSites.map((cs) => {
                            const constructionSiteId = cs.id!;
                            const contractorState = contractorStates[constructionSiteId];
                            return (
                                <React.Fragment key={cs.id}>
                                    <Table.Row
                                        onClick={() => toggleExpand(cs.id!)}
                                        bg={themeColors.bgColorSecondary}
                                        _hover={{bg: themeColors.highlightBgColor, color: themeColors.fontColorHover}}
                                        cursor="pointer"
                                    >
                                        <Table.Cell {...commonCellProps} width={"2%"}>{cs.id}</Table.Cell>
                                        <Table.Cell {...commonCellProps} width={"20%"}>{cs.name}</Table.Cell>
                                        <Table.Cell {...commonCellProps} width={"25%"}>{cs.address}</Table.Cell>
                                        <Table.Cell {...commonCellProps} width={"15%"}>{cs.country?.name}</Table.Cell>
                                        <Table.Cell {...commonCellProps}>{cs.shortName}</Table.Cell>
                                        <Table.Cell {...commonCellProps}>{cs.code}</Table.Cell>
                                        <AuditCell
                                            value={cs.createdDatetime}
                                            user={cs.createdBy}
                                            cellProps={commonCellProps}
                                        />
                                        <AuditCell
                                            value={cs.lastModifiedDatetime}
                                            user={cs.modifiedBy}
                                            cellProps={commonCellProps}
                                        />
                                        <Table.Cell {...commonCellProps}
                                                    onClick={(e) => e.stopPropagation()}>
                                            <HStack gap={1} justifyContent={"center"}>
                                                <EditConstructionSiteDialog
                                                    fetchConstructionSites={fetchConstructionSites}
                                                    constructionSiteId={constructionSiteId}
                                                />
                                                <Button colorPalette="red" size="2xs"
                                                        onClick={() => onDelete(cs.id!)}>
                                                    {t("delete", {ns: "common"})}
                                                </Button>
                                            </HStack>

                                        </Table.Cell>
                                    </Table.Row>

                                    {/* Rozwijany wiersz z kontrahentami */}
                                    {expandedRow === constructionSiteId && (
                                        <Table.Row bg={themeColors.bgColorSecondary}
                                                   _hover={{
                                                       textDecoration: 'none',
                                                       bg: themeColors.highlightBgColor,
                                                       color: themeColors.fontColorHover
                                                   }}>
                                            <Table.Cell colSpan={11} {...commonCellProps}>
                                                <Collapsible.Root open={expandedRow === constructionSiteId}>
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
                                                                contractors={contractorStates[constructionSiteId]?.contractors ?? []}
                                                                onDelete={contractorOnDelete}
                                                                fetchContractors={contractorFetchContractors}
                                                                onSortChange={contractorOnSortChange}
                                                                sortField={contractorSortField}
                                                                sortDirection={contractorSortDirection}
                                                                extended={false}
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
        </>
    );
};

export default ConstructionSiteTableWithContractors;