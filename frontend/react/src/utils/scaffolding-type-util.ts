import {SelectOption} from "@/components/shared/CustomStandaloneSelectField";
import {TFunction} from "i18next";
import {ScaffoldingType} from "@/enums/scaffolding-type-types-enum.ts";

export const getScaffoldingTypeOptions = (t: TFunction): SelectOption[] => {
    return Object.values(ScaffoldingType).map((value) => {
        const label = t(`units:unitTypes.${value}`);
        return {value, label};
    });
};
