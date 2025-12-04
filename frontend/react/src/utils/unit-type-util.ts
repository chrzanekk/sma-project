import {UnitType} from "@/types/enums/unit-types-enum";
import {SelectOption} from "@/components/shared/CustomStandaloneSelectField";
import {TFunction} from "i18next";

export const getUnitTypeOptions = (t: TFunction): SelectOption[] =>
    Object.values(UnitType).map((value) => ({
        value,                                     // "length"
        label: t(`units:unitType.${value}`),      // np. units.unitType.length
    }));
