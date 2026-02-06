import React from "react";
import {
    DialogBody,
    DialogCloseTrigger,
    DialogContent,
    DialogHeader,
    DialogRoot,
    DialogTrigger,
} from "@/components/ui/dialog";
import {Badge, HStack, IconButton, Table, Text, VStack} from "@chakra-ui/react";
import {useTranslation} from "react-i18next";
import {FaMagnifyingGlass} from "react-icons/fa6";
import {FetchableScaffoldingLogPositionDTO} from "@/types/scaffolding-log-position-types";
import {useThemeColors} from "@/theme/theme-colors";
import {useTableStyles} from "@/components/shared/tableStyles";

interface DetailsDialogProps {
    position: FetchableScaffoldingLogPositionDTO;
}

const ScaffoldingLogPositionDetailsDialog: React.FC<DetailsDialogProps> = ({position}) => {
    const {t} = useTranslation([
        "scaffoldingLogPositions",
        "dimensionDescriptions",
        "dimensionTypes",
        "scaffoldingOperationTypes",
        "employees",
        "units",
        "scaffoldingTypes"
    ]);
    const themeColors = useThemeColors();
    const {commonCellProps, commonColumnHeaderProps} = useTableStyles();

    return (
        <DialogRoot size="xl" placement="center">
            <DialogTrigger asChild>
                <IconButton aria-label="Szczegóły" size="2xs" colorPalette="cyan" variant="solid">
                    <FaMagnifyingGlass/>
                </IconButton>
            </DialogTrigger>
            <DialogContent>
                <DialogHeader>
                    {t("scaffoldingLogPositions:detailsTitle", {number: position.scaffoldingNumber})}
                </DialogHeader>
                <DialogCloseTrigger/>
                <DialogBody pb={4}>
                    <VStack gap={4} align="stretch">
                        {/* Tabela 1: Wymiary */}
                        <VStack align="start" gap={1}>
                            <Text fontWeight="bold" fontSize="sm"
                                  color={themeColors.fontColor}>
                                {t("dimensionDescriptions:partialDimensions")}
                            </Text>
                            <Table.ScrollArea borderWidth="1px"
                                              borderRadius="md"
                                              borderColor="grey">
                                <Table.Root size="sm" interactive
                                            showColumnBorder>
                                    <Table.Header>
                                        <Table.Row
                                            bg={themeColors.bgColorPrimary}>
                                            <Table.ColumnHeader  {...commonColumnHeaderProps}>{t("dimensionDescriptions:hshort")}</Table.ColumnHeader>
                                            <Table.ColumnHeader  {...commonColumnHeaderProps}>{t("dimensionDescriptions:wshort")}</Table.ColumnHeader>
                                            <Table.ColumnHeader  {...commonColumnHeaderProps}>{t("dimensionDescriptions:lshort")}</Table.ColumnHeader>
                                            <Table.ColumnHeader  {...commonColumnHeaderProps}>{t("dimensionDescriptions:unitShort")}</Table.ColumnHeader>
                                            <Table.ColumnHeader  {...commonColumnHeaderProps}>{t("scaffoldingTypes:scaffoldingTypeShort")}</Table.ColumnHeader>
                                            <Table.ColumnHeader  {...commonColumnHeaderProps}>{t("scaffoldingOperationTypes:operation")}</Table.ColumnHeader>
                                        </Table.Row>
                                    </Table.Header>
                                    <Table.Body>
                                        {position.dimensions?.length > 0 ? (
                                            position.dimensions.map((dim, idx) => (
                                                <Table.Row key={dim.id || idx}
                                                           _hover={{
                                                               textDecoration: "none",
                                                               bg: themeColors.highlightBgColor,
                                                               color: themeColors.fontColorHover,
                                                           }}>
                                                    <Table.Cell {...commonCellProps}>{dim.height} </Table.Cell>
                                                    <Table.Cell {...commonCellProps}>{dim.width} </Table.Cell>
                                                    <Table.Cell {...commonCellProps}>{dim.length}</Table.Cell>
                                                    <Table.Cell {...commonCellProps}> {dim.unit?.symbol}</Table.Cell>
                                                    <Table.Cell {...commonCellProps}><Badge
                                                        variant="outline"
                                                        size={"xs"}>{t(`dimensionTypes:${dim.dimensionType}`)}</Badge></Table.Cell>
                                                    <Table.Cell
                                                        fontSize="xs" {...commonCellProps}>{t(`scaffoldingOperationTypes:${dim.operationType}`)}</Table.Cell>
                                                </Table.Row>
                                            ))
                                        ) : (
                                            <Table.Row>
                                                <Table.Cell colSpan={5}
                                                            textAlign="center"
                                                            color={themeColors.fontColor}>
                                                    {t("dimensionDescriptions:noDimensions")}
                                                </Table.Cell>
                                            </Table.Row>
                                        )}
                                    </Table.Body>
                                </Table.Root>
                            </Table.ScrollArea>
                        </VStack>

                        {/* Tabela 2: Czas pracy */}
                        <VStack align="start" gap={1}>
                            <Text fontWeight="bold" fontSize="sm"
                                  color={themeColors.fontColor}>
                                {t("workingTimes", {defaultValue: "Czas pracy"})}
                            </Text>
                            <Table.ScrollArea borderWidth="1px"
                                              borderRadius="md"
                                              borderColor="grey">
                                <Table.Root size="sm" interactive
                                            showColumnBorder>
                                    <Table.Header>
                                        <Table.Row
                                            bg={themeColors.bgColorPrimary}>
                                            <Table.ColumnHeader {...commonColumnHeaderProps}>{t("employees:employees")}</Table.ColumnHeader>
                                            <Table.ColumnHeader {...commonColumnHeaderProps}>{t("units:hours")}</Table.ColumnHeader>
                                            <Table.ColumnHeader {...commonColumnHeaderProps}>{t("scaffoldingOperationTypes:operation")}</Table.ColumnHeader>
                                        </Table.Row>
                                    </Table.Header>
                                    <Table.Body>
                                        {position.workingTimes?.length > 0 ? (
                                            position.workingTimes.map((wt, idx) => (
                                                <Table.Row key={wt.id || idx}
                                                           _hover={{
                                                               textDecoration: "none",
                                                               bg: themeColors.highlightBgColor,
                                                               color: themeColors.fontColorHover,
                                                           }}>
                                                    <Table.Cell {...commonCellProps}>{wt.numberOfWorkers}</Table.Cell>
                                                    <Table.Cell {...commonCellProps}>{wt.numberOfHours} {wt.unit?.symbol}</Table.Cell>
                                                    <Table.Cell
                                                        fontSize="xs" {...commonCellProps}>{t(`scaffoldingOperationTypes:${wt.operationType}`)}</Table.Cell>
                                                </Table.Row>
                                            ))
                                        ) : (
                                            <Table.Row>
                                                <Table.Cell colSpan={3}
                                                            textAlign="center"
                                                            color="gray.500">
                                                    {t("noWorkingTime", {defaultValue: "Brak zapisów czasu pracy"})}
                                                </Table.Cell>
                                            </Table.Row>
                                        )}
                                    </Table.Body>
                                </Table.Root>
                            </Table.ScrollArea>

                        </VStack>

                        <HStack align="start" gap={4}>
                            {/* Tabela 3: Zleceniodawca */}
                            <VStack align="start" flex={1} gap={1}>
                                <Text fontWeight="bold" fontSize="sm"
                                      color={themeColors.fontColor}>
                                    {t("scaffoldingLogPositions:contractor")}
                                </Text>
                                <Table.ScrollArea borderWidth="1px"
                                                  borderRadius="md"
                                                  borderColor="grey">
                                    <Table.Root size="sm" variant="outline"
                                                interactive showColumnBorder>
                                        <Table.Header>
                                            <Table.Row
                                                bg={themeColors.bgColorPrimary}>
                                                <Table.ColumnHeader {...commonColumnHeaderProps}>
                                                    {t("scaffoldingLogPositions:contractor")}
                                                </Table.ColumnHeader>
                                                <Table.ColumnHeader {...commonColumnHeaderProps}>
                                                    {t("scaffoldingLogPositions:contractorContact")}
                                                </Table.ColumnHeader>
                                            </Table.Row>
                                        </Table.Header>
                                        <Table.Body>
                                            <Table.Row _hover={{
                                                textDecoration: "none",
                                                bg: themeColors.highlightBgColor,
                                                color: themeColors.fontColorHover,
                                            }}>
                                                <Table.Cell {...commonCellProps}>
                                                    {position.contractor.name}
                                                </Table.Cell>
                                                <Table.Cell {...commonCellProps}>
                                                    {position.contractorContact.firstName} {position.contractorContact.lastName}
                                                </Table.Cell>
                                            </Table.Row>
                                        </Table.Body>
                                    </Table.Root>
                                </Table.ScrollArea>
                            </VStack>
                            {/* Tabela 4: Użytkownik */}
                            <VStack align="start" flex={1} gap={1}>
                                <Text fontWeight="bold" fontSize="sm"
                                      color={themeColors.fontColor}>
                                    {t("scaffoldingLogPositions:scaffoldingUser")}
                                </Text>
                                <Table.ScrollArea borderWidth="1px"
                                                  borderRadius="md"
                                                  borderColor="grey">
                                    <Table.Root size="sm" variant="outline"
                                                interactive showColumnBorder>
                                        <Table.Header>
                                            <Table.Row
                                                bg={themeColors.bgColorPrimary}>
                                                <Table.ColumnHeader {...commonColumnHeaderProps}>
                                                    {t("scaffoldingLogPositions:scaffoldingUser")}
                                                </Table.ColumnHeader>
                                                <Table.ColumnHeader {...commonColumnHeaderProps}>
                                                    {t("scaffoldingLogPositions:scaffoldingUserContact")}
                                                </Table.ColumnHeader>
                                            </Table.Row>
                                        </Table.Header>
                                        <Table.Body>
                                            <Table.Row _hover={{
                                                textDecoration: "none",
                                                bg: themeColors.highlightBgColor,
                                                color: themeColors.fontColorHover,
                                            }}>
                                                <Table.Cell {...commonCellProps}>
                                                    {position.scaffoldingUser.name}
                                                </Table.Cell>
                                                <Table.Cell {...commonCellProps}>
                                                    {position.scaffoldingUserContact.firstName} {position.scaffoldingUserContact.lastName}
                                                </Table.Cell>
                                            </Table.Row>
                                        </Table.Body>
                                    </Table.Root>
                                </Table.ScrollArea>
                            </VStack>
                        </HStack>
                    </VStack>
                </DialogBody>
            </DialogContent>
        </DialogRoot>
    );
};

export default ScaffoldingLogPositionDetailsDialog;
