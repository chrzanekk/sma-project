import {SelectOption} from "@/components/shared/CustomStandaloneSelectField";
import {TFunction} from "i18next";
import {DimensionType} from "@/enums/dimension-types-enum.ts";

export const getDimensionTypeOptions = (t: TFunction): SelectOption[] => {
    return Object.values(DimensionType).map((value) => {
        const label = t(`dimensionTypes:${value}`);
        return {value, label};
    });
};
