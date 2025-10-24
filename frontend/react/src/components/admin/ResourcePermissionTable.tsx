import {ResourceDTO} from "@/types/resource-types.ts";
import React from "react";
import {useTranslation} from "react-i18next";
import {themeVars, useThemeColors} from "@/theme/theme-colors.ts";
import {useTableStyles} from "@/components/shared/tableStyles.ts";
import useRoles from "@/hooks/UseRoles.tsx";
import {Box, Table, Text} from "@chakra-ui/react";
import {Field} from "@/components/ui/field.tsx";
import CustomStandaloneSelectField from "@/components/shared/CustomStandaloneSelectField.tsx";

interface ResourcePermissionTableProps {
    resources: ResourceDTO[];
    onRoleChange: (resourceId: number, selectedRoles: string[]) => Promise<void>;
    loading: boolean;
}

const ResourcePermissionTable: React.FC<ResourcePermissionTableProps> = ({
                                                                             resources,
                                                                             onRoleChange,
                                                                             loading
                                                                         }) => {
    const {t} = useTranslation(['common', 'resources', 'roles']);
    const themeColors = useThemeColors();
    const {commonCellProps, commonColumnHeaderProps} = useTableStyles();
    const {roles: roleOptions} = useRoles();

    const filteredResources = resources.filter(r => !r.isPublic);

    if (!filteredResources || filteredResources.length === 0) {
        return (
            <Field alignContent="center">
                <Text fontSize={20}>{t("common:dataNotFound")}</Text>
            </Field>
        );
    }

    return (
        <Box>
            <Table.ScrollArea height="auto" borderWidth="1px" borderRadius="md" borderColor="grey">
                <Table.Root size="sm" interactive showColumnBorder color={themeColors.fontColor}>
                    <Table.Header>
                        <Table.Row bg={themeColors.bgColorPrimary}>
                            <Table.ColumnHeader {...commonColumnHeaderProps}>{t('resources:resource')}</Table.ColumnHeader>
                            <Table.ColumnHeader {...commonColumnHeaderProps}>{t('resources:endpoint')}</Table.ColumnHeader>
                            <Table.ColumnHeader {...commonColumnHeaderProps}>{t('resources:allowedRoles')}</Table.ColumnHeader>
                        </Table.Row>
                    </Table.Header>
                    <Table.Body>
                        {resources.filter(r => !r.isPublic).map((resource) => (
                            <Table.Row key={resource.id} bg={themeColors.bgColorSecondary}>
                                <Table.Cell {...commonCellProps}>
                                    <Text fontWeight="bold">{resource.displayName}</Text>
                                    <Text fontSize="sm" color={themeColors.fontColor}>{resource.description}</Text>
                                </Table.Cell>
                                <Table.Cell {...commonCellProps}>
                                    <Text fontFamily="mono" fontSize="sm" color={themeColors.fontColor}>
                                        {resource.httpMethod && `${resource.httpMethod} `}
                                        {resource.endpointPattern}
                                    </Text>
                                </Table.Cell>
                                <Table.Cell {...commonCellProps}>
                                    <CustomStandaloneSelectField
                                        options={roleOptions}
                                        value={resource.allowedRoles}  // Array of role names
                                        onChange={(selectedRoles) => {
                                            onRoleChange(resource.id, selectedRoles).then();
                                        }}
                                        placeholder={t('resources:selectRoles')}
                                        isMulti={true}
                                        bgColor={themeVars.bgColorSecondary}
                                        disabled={loading}
                                    />
                                    {/*<Select*/}
                                    {/*    isMulti*/}
                                    {/*    options={roleOptions}*/}
                                    {/*    value={roleOptions.filter(opt =>*/}
                                    {/*        resource.allowedRoles.includes(opt.value)*/}
                                    {/*    )}*/}
                                    {/*    onChange={(selected) => {*/}
                                    {/*        const roles = selected.map(s => s.value);*/}
                                    {/*        onRoleChange(resource.id, roles).then();*/}
                                    {/*    }}*/}
                                    {/*    isDisabled={loading}*/}
                                    {/*/>*/}
                                </Table.Cell>
                            </Table.Row>
                        ))}
                    </Table.Body>
                </Table.Root>
            </Table.ScrollArea>
        </Box>
    )
}

export default ResourcePermissionTable;