import {FetchableScaffoldingLogDTO} from "@/types/scaffolding-log-types.ts";
import React, {useState} from "react";
import {useTranslation} from "react-i18next";
import {Box, Button, Card, Grid, GridItem, HStack, Text, useDisclosure, Wrap} from "@chakra-ui/react";
import {useThemeColors} from "@/theme/theme-colors.ts";
import {Field} from "@/components/ui/field.tsx";
import ConfirmModal from "@/components/shared/ConfirmModal.tsx";
import {FaArrowDownAZ, FaArrowDownZA, FaExplosion, FaRegTrashCan, FaTableList} from "react-icons/fa6";
import EditScaffoldingLogDialog from "@/components/scaffolding/log/EditScaffoldingLogDialog.tsx";
import { useNavigate } from "react-router-dom";

interface Props {
    logs: FetchableScaffoldingLogDTO[]
    onDelete: (id: number) => void;
    fetchLogs: () => void;
    onSortChange: (field: string) => void;
    sortField: string | null;
    sortDirection: "asc" | "desc";
}

const ScaffoldingLogView: React.FC<Props> = ({
                                                 logs,
                                                 onDelete,
                                                 fetchLogs,
                                                 onSortChange,
                                                 sortField,
                                                 sortDirection
                                             }) => {
    const {t} = useTranslation(['common', 'scaffoldingLogs', 'contractors', 'constructionSites', 'scaffoldingLogPositions']);
    const {open, onOpen, onClose} = useDisclosure();
    const [selectedLogId, setSelectedLogId] = useState<number | null>(null);
    const themeColors = useThemeColors();
    const navigate = useNavigate();

    const handleDeleteClick = (id: number) => {
        setSelectedLogId(id);
        onOpen();
    };

    const confirmDelete = () => {
        if (selectedLogId !== null) {
            onDelete(selectedLogId);
        }
        onClose();
    };

    if (!logs || logs.length === 0) {
        return (
            <Field alignContent={"center"}>
                <Text fontSize={20}>{t('dataNotFound')}</Text>
            </Field>)
    }

    const renderSortIndicator = (field: string) => {
        if (sortField === field) {
            return sortDirection === "asc" ? "↑" : "↓";
        }
        return null;
    };

    const renderSortIcon = (field: string) => {
        if (sortField === field) {
            return sortDirection === "asc" ? <FaArrowDownZA/> : <FaArrowDownAZ/>;
        }
        return null;
    };

    return (
        <Grid templateColumns={"repeat(1,1fr)"}>
            <GridItem colSpan={2} gap={1} borderWidth={"2px"} borderRadius={"md"}
                      borderColor={themeColors.borderColor}>

                <HStack gap={2} justifyContent={"center"}>
                    <Button
                        colorPalette="blue"
                        size={"2xs"}
                        onClick={() => onSortChange("id")}>
                        {renderSortIcon("id")}
                        {t('sort.id', {ns: "scaffoldingLogs"})}
                        {renderSortIndicator("id")}
                    </Button>
                    <Button
                        colorPalette="blue"
                        size={"2xs"}
                        onClick={() => onSortChange("name")}>
                        {renderSortIcon("name")}
                        {t('sort.name', {ns: "scaffoldingLogs"})}
                        {renderSortIndicator("name")}
                    </Button>
                    <Button
                        colorPalette="blue"
                        size={"2xs"}
                        onClick={() => onSortChange("constructionSiteName")}>
                        {renderSortIcon("constructionSiteName")}
                        {t('sort.constructionSiteName', {ns: "scaffoldingLogs"})}
                        {renderSortIndicator("constructionSiteName")}
                    </Button>
                    <Button
                        colorPalette="blue"
                        size={"2xs"}
                        onClick={() => onSortChange("contractorName")}>
                        {renderSortIcon("contractorName")}
                        {t('sort.contractorName', {ns: "scaffoldingLogs"})}
                        {renderSortIndicator("contractorName")}
                    </Button>
                </HStack>

            </GridItem>
            <GridItem colSpan={1} gap={2} mt={1} p={1} borderWidth={"2px"} borderRadius={"md"}
                      borderColor={themeColors.borderColor}>
                <Wrap justify={"center"}>
                    {logs.map((log) => (
                        <Card.Root key={log.id}
                                   unstyled={true}
                                   borderWidth={"3px"}
                                   borderRadius={"md"}
                                   width={"350px"}
                                   borderColor={"blackAlpha.400"}>
                            <Card.Body>
                                <Box borderWidth={"1px"}
                                     borderRadius={"sm"}
                                     borderColor={themeColors.borderColor}
                                     mt={1} ml={1} mr={1} mb={1}
                                     height={"55px"}>
                                    <HStack mt={1} ml={1} mr={1} mb={1} justifyContent={"center"}>
                                        <Text fontSize={"sm"}
                                              color={themeColors.fontColor}
                                              textAlign={"center"}>
                                            DB_ID:
                                        </Text>
                                        <Text fontSize={"sm"}
                                              fontWeight={"bold"}
                                              color={themeColors.fontColor}
                                              textAlign={"center"}>
                                            {log.id}
                                        </Text>
                                    </HStack>
                                    <HStack mt={1} ml={1} mr={1} mb={1} justifyContent={"center"}>
                                        <Text fontSize={"lg"}
                                              color={themeColors.fontColor}
                                              textAlign={"center"}>
                                            {t("scaffoldingLogs:name")}:
                                        </Text>
                                        <Text fontSize={"lg"}
                                              fontWeight={"bold"}
                                              color={themeColors.fontColor}
                                              textAlign={"center"}>
                                            {log.name}
                                        </Text>
                                    </HStack>
                                </Box>
                                <Box mt={1} ml={1} mr={1} mb={1}
                                     borderWidth={"1px"}
                                     borderRadius={"md"}
                                     borderColor={themeColors.borderColor}
                                     height={"150px"}>
                                    <Grid templateColumns={"repeat(6,1fr)"} mt={2} ml={1} mr={1}>
                                        <GridItem colSpan={2} borderWidth={"1px"}>
                                            <Text fontSize={"md"}
                                                  color={themeColors.fontColor}
                                                  textAlign={"center"}
                                            >
                                                {t("contractors:contractor")}:
                                            </Text>
                                        </GridItem>
                                        <GridItem colSpan={4} borderWidth={"1px"}>
                                            <Text fontSize={"md"}
                                                  fontWeight="bold"
                                                  color={themeColors.fontColor}
                                                  textAlign={"center"}
                                            >
                                                {log.contractor.name}
                                            </Text>
                                        </GridItem>
                                    </Grid>

                                    <Grid templateColumns={"repeat(6,1fr)"} ml={1} mr={1}>
                                        <GridItem colSpan={2} borderWidth={"1px"}>
                                            <Text fontSize={"md"}
                                                  color={themeColors.fontColor}
                                                  textAlign={"center"}
                                            >
                                                {t("constructionSites:constructionSite")}:
                                            </Text>
                                        </GridItem>
                                        <GridItem colSpan={4} borderWidth={"1px"}>
                                            <Text fontSize={"md"}
                                                  fontWeight="bold"
                                                  color={themeColors.fontColor}
                                                  textAlign={"center"}
                                            >
                                                {log.constructionSite.name}
                                            </Text>
                                        </GridItem>
                                    </Grid>

                                    <Grid templateColumns={"repeat(6,1fr)"} ml={1} mr={1}>
                                        <GridItem colSpan={2} borderWidth={"1px"}>
                                            <Text fontSize={"md"}
                                                  color={themeColors.fontColor}
                                                  textAlign={"center"}
                                            >
                                                {t("scaffoldingLogs:additionalInfo")}:
                                            </Text>
                                        </GridItem>
                                        <GridItem colSpan={4} borderWidth={"1px"}>
                                            <Text fontSize={"small"}
                                                  fontWeight="bold"
                                                  color={themeColors.fontColor}
                                                  textAlign={"left"}
                                                  ml={1}
                                            >
                                                {log.additionalInfo}
                                            </Text>
                                        </GridItem>
                                    </Grid>
                                </Box>

                            </Card.Body>
                            <Card.Footer mt={1} ml={1} mr={1} mb={1}
                                         borderWidth={"1px"}
                                         borderRadius={"md"}
                                         borderColor={themeColors.borderColor}
                                         backgroundColor={themeColors.bgColorSecondary}
                            >
                                <HStack justifyContent="center" mt={1} ml={1} mr={1} mb={1}>
                                    <Button
                                        colorPalette="blue"
                                        size={"2xs"}
                                        onClick={() => navigate(`/scaffolding-logs/${log.id}/positions`, {
                                            state: { logName: log.name }
                                        })}
                                    >
                                        {t('positionList', {ns: "scaffoldingLogPositions"})}
                                        <FaTableList/>
                                    </Button>
                                    <EditScaffoldingLogDialog
                                        fetchLogs={fetchLogs}
                                        logId={log.id!}/>
                                    <Button
                                        colorPalette="red"
                                        size={"2xs"}
                                        onClick={() => handleDeleteClick(log.id!)}>
                                        {t('delete', {ns: "common"})}
                                        <FaExplosion/>
                                        <FaRegTrashCan/>
                                    </Button>
                                </HStack>
                                {/*TODO button to navigate to positions of log -> should route to ScaffoldingLogPositionTable - there i need to implement BACK button*/}
                                {/*TODO button to edit - dialog like, similar to addDialog*/}

                            </Card.Footer>
                        </Card.Root>
                    ))}


                    <ConfirmModal
                        isOpen={open}
                        onClose={onClose}
                        onConfirm={confirmDelete}
                        title={t("deleteConfirmation.title", {ns: "common"})}
                        message={t("deleteConfirmation.message", {ns: "common"})}
                        confirmText={t("delete", {ns: "common"})}
                        cancelText={t("cancel", {ns: "common"})}
                    />
                </Wrap>
            </GridItem>
        </Grid>

    )
}

export default ScaffoldingLogView;