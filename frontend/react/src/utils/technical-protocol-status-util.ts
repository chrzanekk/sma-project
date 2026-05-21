import {SelectOption} from "@/components/shared/CustomStandaloneSelectField";
import {TFunction} from "i18next";
import {TechnicalProtocolStatus} from "@/enums/technical-protocol-status-types-enum.ts";

export const getTechnicalProtocolStatusOptions = (t: TFunction): SelectOption[] => {
    return Object.values(TechnicalProtocolStatus).map((value) => {
        const label = t(`technicalProtocolStatuses:${value}`);
        return {value, label};
    });
};
