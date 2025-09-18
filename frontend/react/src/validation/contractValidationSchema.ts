import * as Yup from "yup";
import {BaseContractFormValues} from "@/types/contract-types.ts";

export const getContractValidationSchema = (
    t: (key: string, options?: any) => string,
    currencyOptions: { value: string, label: string }[]
): Yup.Schema<BaseContractFormValues> => {
    return Yup.object({
        number: Yup.string()
            .min(1, t("errors:verification.minLength", {field: t("contracts:number"), count: 1}))
            .max(100, t("errors:verification.maxLength", {field: t("contracts:number"), count: 100}))
            .required(t("errors:verification.required", {field: t("contracts:number")})),
        description: Yup.string()
            .min(1, t("errors:verification.minLength", {field: t("contracts:description"), count: 1}))
            .max(300, t("errors:verification.maxLength", {field: t("contracts:description"), count: 300}))
            .required(t("errors:verification.required", {field: t("contracts:description")})),
        value: Yup.string()
            .min(1, t("errors:verification.minLength", {field: t("contracts:value"), count: 1}))
            .max(20, t("errors:verification.maxLength", {field: t("contracts:value"), count: 20}))
            .required(t("errors:verification.required", {field: t("contracts:value")})),
        currency: Yup.string().oneOf(
            currencyOptions.map((option) => option.value),
            t("contracts:currency")
        )
            .required(t("errors:verification.required", {field: t("contracts:currency")})),
    }) as Yup.Schema<BaseContractFormValues>;
};
