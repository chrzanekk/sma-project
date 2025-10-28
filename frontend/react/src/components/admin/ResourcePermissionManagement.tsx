// components/admin/ResourcePermissionManagement.tsx
import React, {useEffect, useState} from 'react';
import {getAllResources, updateResourceRoles} from '@/services/resource-service';
import {useTranslation} from 'react-i18next';
import {errorNotification, successNotification} from '@/notifications/notifications';
import {ResourceDTO, ResourceUpdateRequest} from "@/types/resource-types.ts";
import ResourcePermissionLayout from "@/components/admin/ResourcePermissionLayout.tsx";
import ResourcePermissionTable from "@/components/admin/ResourcePermissionTable.tsx";

const ResourcePermissionManagement: React.FC = () => {
    const {t} = useTranslation(['common', 'resources']);
    const [resources, setResources] = useState<ResourceDTO[]>([]);
    const [loading, setLoading] = useState(false);

    useEffect(() => {
        fetchResources().then();
    }, []);

    const fetchResources = async () => {
        try {
            const data = await getAllResources();
            setResources(data);
        } catch (error) {
            errorNotification(t('common:error'), t('resources:notifications.errorLoadingResources'));
        }
    };

    const handleRoleChange = async (resourceId: number, selectedRoles: string[]) => {
        setLoading(true);
        try {
            const hasAdmin = selectedRoles.includes('ROLE_ADMIN');

            const resourceUpdate: ResourceUpdateRequest = {
                resourceId: resourceId,
                roleNames: selectedRoles
            }
            await updateResourceRoles(resourceUpdate);

            if (!hasAdmin) {
                successNotification(t('common:success'), t('resources:notifications.rolesUpdatedWithAdmin'));
            } else {
                successNotification(t('common:success'), t('resources:notifications.rolesUpdated'));
            }
            await fetchResources();
        } catch (error) {
            errorNotification(t('common:error'), t('resources:notifications.errorUpdatingRoles'));
        } finally {
            setLoading(false);
        }
    };

    return (
        <ResourcePermissionLayout
            headerTitle={t('resources:resourcePermissions')}
            table={
                <ResourcePermissionTable
                    resources={resources}
                    onRoleChange={handleRoleChange}
                    loading={loading}
                />
            }
        />
    );
};

export default ResourcePermissionManagement;
