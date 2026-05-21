import {UnitType} from "@/enums/unit-types-enum";
import {SelectOption} from "@/components/shared/CustomStandaloneSelectField";
import {TFunction} from "i18next";

export const getUnitTypeOptions = (t: TFunction): SelectOption[] => {
    return Object.values(UnitType).map((value) => {
        const label = t(`units:unitTypes.${value}`);
        return {value, label};
    });
};
