import {UserAuditDTO} from "@/types/user-types.ts";

export interface AuditableTypes {
    createdDatetime: string;
    lastModifiedDatetime: string;
    createdBy: UserAuditDTO;
    modifiedBy: UserAuditDTO;
}