import {UserAuditDTO} from "@/types/user-types.ts";

export interface AuditableType {
    createdDatetime: string;
    lastModifiedDatetime: string;
    createdBy: UserAuditDTO;
    modifiedBy: UserAuditDTO;
}