// context/ResourcePermissionContext.tsx
import React, {createContext, useContext, useEffect, useState} from 'react';
import {ResourceDTO} from '@/types/resource-types.ts';
import {getAllResources} from '@/services/resource-service.ts';
import {useAuth} from '@/context/AuthContext.tsx';

interface ResourcePermissionContextType {
    resources: ResourceDTO[];
    loading: boolean;
    canAccessResource: (resourceKey: string) => boolean;
    getUserResources: () => ResourceDTO[];
}

const ResourcePermissionContext = createContext<ResourcePermissionContextType | undefined>(undefined);

export const ResourcePermissionProvider: React.FC<{children: React.ReactNode}> = ({children}) => {
    const {user} = useAuth();
    const [resources, setResources] = useState<ResourceDTO[]>([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const fetchResources = async () => {
            try {
                const data = await getAllResources();
                setResources(data);
            } catch (error) {
                console.error('Error fetching resources:', error);
                setResources([]);
            } finally {
                setLoading(false);
            }
        };

        if (user) {
            fetchResources().then();
        } else {
            setLoading(false);
        }
    }, [user]);

    const canAccessResource = (resourceKey: string): boolean => {
        if (!user?.roles) return false;

        const resource = resources.find(r => r.resourceKey === resourceKey);
        if (!resource) return false;

        // ✅ CRITICAL: Basic resources are ALWAYS accessible for authenticated users
        // These are hardcoded in backend as .authenticated()
        const basicResources = ['ACCOUNT_MANAGEMENT'];
        if (basicResources.includes(resourceKey)) {
            return true;
        }

        // Public resources are accessible by everyone
        if (resource.isPublic) return true;

        // Check if user has any of the required roles
        return user.roles.some(role =>
            resource.allowedRoles.includes(role)
        );
    };

    const getUserResources = (): ResourceDTO[] => {
        if (!user?.roles) return [];

        return resources.filter(resource => {
            if (resource.isPublic) return false; // Exclude public resources

            return user.roles?.some(role =>
                resource.allowedRoles.includes(role)
            );
        });
    };

    return (
        <ResourcePermissionContext.Provider
            value={{
                resources,
                loading,
                canAccessResource,
                getUserResources
            }}
        >
            {children}
        </ResourcePermissionContext.Provider>
    );
};

export const useResourcePermissions = () => {
    const context = useContext(ResourcePermissionContext);
    if (!context) {
        throw new Error('useResourcePermissions must be used within ResourcePermissionProvider');
    }
    return context;
};
