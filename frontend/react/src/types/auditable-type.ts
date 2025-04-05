import {UserDTO} from "@/types/user-types.ts";

export interface AuditableType {
    createdDatetime: string;
    lastModifiedDatetime: string;
    createdById: number;
    createdBy: UserDTO;
    modifiedBy: UserDTO;
}