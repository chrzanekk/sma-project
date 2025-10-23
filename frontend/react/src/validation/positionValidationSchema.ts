import * as Yup from "yup";
import {BasePositionFormValues} from "@/types/position-types.ts";

export const getPositionValidationSchema = (
    t: (key: string, options?: any) => string
): Yup.Schema<BasePositionFormValues> => {
    return Yup.object({
        name: Yup.string()
            .min(2, t("errors:verification.minLength", {field: t("positions:name"), count: 2}))
            .max(20, t("errors:verification.maxLength", {field: t("positions:name"), count: 20}))
            .required(t("errors:verification.required", {field: t("positions:name")})),
        description: Yup.string()
            .min(2, t("errors:verification.minLength", {field: t("positions:description"), count: 2}))
            .max(30, t("errors:verification.maxLength", {field: t("positions:description"), count: 30}))
            .required(t("errors:verification.required", {field: t("positions:description")})),
    }) as Yup.Schema<BasePositionFormValues>;
};
