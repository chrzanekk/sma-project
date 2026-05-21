import {SelectOption} from "@/components/shared/CustomStandaloneSelectField";
import {TFunction} from "i18next";
import {ScaffoldingOperationType} from "@/enums/scaffolding-operation-types-enum.ts";

export const getScaffoldingOperationTypeOptions = (t: TFunction): SelectOption[] => {
    return Object.values(ScaffoldingOperationType).map((value) => {
        const label = t(`scaffoldingOperationTypes:${value}`);
        return {value, label};
    });
};
