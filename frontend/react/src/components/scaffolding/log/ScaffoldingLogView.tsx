import {FetchableScaffoldingLogDTO} from "@/types/scaffolding-log-types.ts";
import React, {useState} from "react";
import {useTranslation} from "react-i18next";
import {Button, Card, Grid, GridItem, HStack, Text, useDisclosure} from "@chakra-ui/react";
import {useThemeColors} from "@/theme/theme-colors.ts";
import {Field} from "@/components/ui/field.tsx";
import ConfirmModal from "@/components/shared/ConfirmModal.tsx";
import {Fa1, FaExplosion, FaPen, FaRegTrashCan, FaTableList} from "react-icons/fa6";

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
    const {t} = useTranslation(['common', 'scaffoldingLogs', 'contractors', 'constructionSites','scaffoldingLogPositions']);
    const {open, onOpen, onClose} = useDisclosure();
    const [selectedLogId, setSelectedLogId] = useState<number | null>(null);
    const themeColors = useThemeColors();


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

    return (
        <Grid templateColumns={"repeat(1,1fr)"}>
            <GridItem colSpan={2} gap={2} mt={1} p={1} borderWidth={"2px"} borderRadius={"md"}
                      borderColor={themeColors.borderColor}>
                tu będą przyciski do sortowania
            </GridItem>
            <GridItem colSpan={1} gap={2} mt={1} p={1} borderWidth={"2px"} borderRadius={"md"}
                      borderColor={themeColors.borderColor}>
                <HStack>
                    {logs.map((log) => (
                        <Card.Root key={log.id}
                                   unstyled={true}
                                   borderWidth={"3px"}
                                   borderRadius={"md"}
                                   borderColor={themeColors.borderColor}>
                            <Card.Body>
                                <Card.Title borderWidth="1px"
                                            borderRadius="sm"
                                            borderColor={themeColors.borderColor}
                                            mt={1} ml={1} mr={1} mb={1}>
                                    <HStack mt={1} ml={1} mr={1} mb={1} justifyContent="center">
                                        <Text fontSize={"sm"}
                                              color={themeColors.fontColor}
                                              textAlign={"center"}>
                                            DB_ID:
                                        </Text>
                                        <Text fontSize={"sm"}
                                              fontWeight="bold"
                                              color={themeColors.fontColor}
                                              textAlign={"center"}>
                                            {log.id}
                                        </Text>
                                    </HStack>
                                    <HStack mt={1} ml={1} mr={1} mb={1}>
                                        <Text fontSize={"sm"}
                                              color={themeColors.fontColor}
                                              textAlign={"center"}>
                                            {t("scaffoldingLogs:name")}:
                                        </Text>
                                        <Text fontSize={"sm"}
                                              fontWeight="bold"
                                              color={themeColors.fontColor}
                                              textAlign={"center"}>
                                            {log.name}
                                        </Text>
                                    </HStack>
                                </Card.Title>

                                <Card.Description mt={1} ml={1} mr={1} mb={1}
                                                  borderWidth={"1px"}
                                                  borderRadius={"md"}
                                                  borderColor={themeColors.borderColor}>
                                    {/*TODO simple list of data: constructionSite, contractor, additionalInfo*/}
                                    <HStack mt={1} ml={1} mr={1} mb={1}>
                                        <Text fontSize={"sm"}
                                              color={themeColors.fontColor}
                                              textAlign={"center"}
                                        >
                                            {t("contractors:contractor")}:
                                        </Text>
                                        <Text fontSize={"sm"}
                                              fontWeight="bold"
                                              color={themeColors.fontColor}
                                              textAlign={"center"}
                                        >
                                            {log.contractor.name}
                                        </Text>
                                    </HStack>

                                    <HStack mt={1} ml={1} mr={1} mb={1}>
                                        <Text fontSize={"sm"}
                                              color={themeColors.fontColor}
                                              textAlign={"center"}
                                        >
                                            {t("constructionSites:constructionSite")}:
                                        </Text>
                                        <Text fontSize={"sm"}
                                              fontWeight="bold"
                                              color={themeColors.fontColor}
                                              textAlign={"center"}
                                        >
                                            {log.constructionSite.name}
                                        </Text>
                                    </HStack>

                                    <HStack mt={1} ml={1} mr={1} mb={1}>
                                        <Text fontSize={"sm"}
                                              color={themeColors.fontColor}
                                              textAlign={"center"}
                                        >
                                            {t("scaffoldingLogs:additionalInfo")}:
                                        </Text>
                                        <Text fontSize={"sm"}
                                              fontWeight="bold"
                                              color={themeColors.fontColor}
                                              textAlign={"center"}
                                        >
                                            {log.additionalInfo}
                                        </Text>
                                    </HStack>

                                </Card.Description>
                            </Card.Body>
                            <Card.Footer mt={1} ml={1} mr={1} mb={1}
                                         borderWidth={"1px"}
                                         borderRadius={"md"}
                                         borderColor={themeColors.borderColor}
                                         backgroundColor={themeColors.bgColorSecondary}
                            >
                                <HStack gap={1} justifyContent="center" mt={1} ml={1} mr={1} mb={1}>
                                    <Button
                                        colorPalette="blue"
                                        size={"2xs"}>
                                        {t('positionList', {ns: "scaffoldingLogPositions"})}
                                        <FaTableList />
                                    </Button>
                                    <Button
                                        colorPalette="green"
                                        size={"2xs"}>
                                        {t('edit', {ns: "common"})}
                                        <FaPen/>
                                    </Button>
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
                </HStack>
            </GridItem>
        </Grid>

    )
}

export default ScaffoldingLogView;