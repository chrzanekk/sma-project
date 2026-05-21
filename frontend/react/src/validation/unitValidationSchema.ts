import * as Yup from "yup";
import {BaseUnitFormValues} from "@/types/unit-types.ts";
import {UnitType} from "@/enums/unit-types-enum.ts";

export const getUnitValidationSchema = (
    t: (key: string, options?: any) => string
): Yup.Schema<BaseUnitFormValues> => {
    const unitTypeValues = Object.values(UnitType);

    return Yup.object({
        symbol: Yup.string()
            .min(2, t("errors:verification.minLength", {field: t("units:symbol"), count: 2}))
            .max(20, t("errors:verification.maxLength", {field: t("units:symbol"), count: 20}))
            .required(t("errors:verification.required", {field: t("units:symbol")})),

        description: Yup.string()
            .min(2, t("errors:verification.minLength", {field: t("units:description"), count: 2}))
            .max(30, t("errors:verification.maxLength", {field: t("units:description"), count: 30}))
            .optional(),

        unitType: Yup.string()
            .oneOf(unitTypeValues as string[], t("errors:verification.invalidSelection", {field: t("units:unitType")}))
            .required(t("errors:verification.required", {field: t("units:unitType")})),
    });
};
