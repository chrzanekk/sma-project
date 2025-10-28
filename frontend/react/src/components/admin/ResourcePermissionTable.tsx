import {ResourceDTO} from "@/types/resource-types.ts";
import React, {useMemo} from "react";
import {useTranslation} from "react-i18next";
import {themeVars, useThemeColors} from "@/theme/theme-colors.ts";
import {useTableStyles} from "@/components/shared/tableStyles.ts";
import useRoles from "@/hooks/UseRoles.tsx";
import {Box, Table, Text} from "@chakra-ui/react";
import {Field} from "@/components/ui/field.tsx";
import CustomStandaloneSelectField from "@/components/shared/CustomStandaloneSelectField.tsx";
import {errorNotification} from "@/notifications/notifications.ts";
import {formatMessage} from "@/notifications/FormatMessage.tsx";

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

    const CRITICAL_RESOURCES = [
        'RESOURCE_MANAGEMENT',
        'ROLE_MANAGEMENT',
        'USER_MANAGEMENT'
    ];

    const sortedResources = useMemo(() => {
        return [...resources]
            .filter(r => !r.isPublic)
            .sort((a, b) => a.resourceKey.localeCompare(b.resourceKey));
    }, [resources]);


    const handleRoleChange = async (resource: ResourceDTO, selectedRoles: string[]) => {
        try {
            // ✅ Validation 1: Prevent removing all roles from critical resources
            if (CRITICAL_RESOURCES.includes(resource.resourceKey) && selectedRoles.length === 0) {
                errorNotification(
                    t('resources:validation.criticalResourceTitle'),
                    formatMessage(
                        'criticalResourceCannotBeDeleted',
                        {resourceName: t(`resources:${resource.displayName}`)},
                        'resources'
                    )
                );
                return; // Cancel operation
            }

            if (selectedRoles.length === 0) {
                errorNotification(
                    t('resources:validation.noRolesTitle'),
                    formatMessage(
                        'resourceNeedAtLeastOneRole',
                        {resourceName: t(`resources:${resource.displayName}`)},
                        'resources'
                    )
                );
                return;
            }

            await onRoleChange(resource.id, selectedRoles);

        } catch (error: any) {
            console.error('Error updating resource roles:', error);

            // Backend should return error in response.data with code and message
            const errorCode = error.response?.data?.code;
            const errorDetails = error.response?.data?.details;

            if (errorCode) {
                errorNotification(
                    t('common:error'),
                    formatMessage(errorCode, errorDetails, 'resources')
                );
            } else {
                errorNotification(
                    t('common:error'),
                    t('resources:notifications.updateFailed')
                );
            }
        }
    };

    if (!sortedResources || sortedResources.length === 0) {
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
                        {sortedResources.map((resource) => (
                            <Table.Row key={resource.id} bg={themeColors.bgColorSecondary} opacity={resource.allowedRoles.length === 0 ? 0.6 : 1}>
                                <Table.Cell {...commonCellProps}>
                                    <Text fontWeight="bold">{t(`resources:${resource.displayName}`)}</Text>
                                    <Text fontSize="sm" color={themeColors.fontColor}>{t(`resources:${resource.description}`)}</Text>
                                </Table.Cell>
                                <Table.Cell {...commonCellProps}>
                                    <Text fontFamily="mono" fontSize="sm" color={themeColors.fontColor}>
                                        {resource.httpMethod && `${resource.httpMethod} `}
                                        {resource.endpointPattern}
                                    </Text>
                                    {resource.allowedRoles.length === 0 && (
                                        <Text fontSize="xs" color="orange.500" fontWeight="bold">
                                            ⚠️ {t('resources:warnings.noRolesAssigned')}
                                        </Text>)}
                                </Table.Cell>
                                <Table.Cell {...commonCellProps}>
                                    <CustomStandaloneSelectField
                                        options={roleOptions}
                                        value={resource.allowedRoles}  // Array of role names
                                        onChange={(selectedRoles) => {
                                            handleRoleChange(resource, selectedRoles).then();
                                        }}
                                        placeholder={t('resources:selectRoles')}
                                        isMulti={true}
                                        bgColor={themeVars.bgColorSecondary}
                                        disabled={loading}
                                    />
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