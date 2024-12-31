import { useState, useEffect } from "react";
import { getAllRoles } from "@/services/role-service.ts";
import {RoleDTO, RoleOption} from "@/types/role-types.ts";

const useRoles = () => {
    const [roles, setRoles] = useState<RoleOption[]>([]);
    const [isLoading, setIsLoading] = useState<boolean>(true);
    const [error, setError] = useState<string | null>(null);

    useEffect(() => {
        const fetchRoles = async () => {
            setIsLoading(true);
            setError(null);
            try {
                const rolesFromBackend = await getAllRoles();
                const formattedRoles = rolesFromBackend.map((role: RoleDTO) => ({
                    value: role.name,
                    label: role.name.replace("ROLE_", ""),
                }));
                setRoles(formattedRoles);
                console.log("Roles" + formattedRoles);
            } catch (err) {
                console.error("Error fetching roles:", err);
                setError("Failed to fetch roles");
            } finally {
                setIsLoading(false);
            }
        };

        fetchRoles().catch();
    }, []);

    return { roles, isLoading, error };
};

export default useRoles;
