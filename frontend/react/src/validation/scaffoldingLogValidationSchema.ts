import * as Yup from "yup";
import {BaseScaffoldingLogFormValues} from "@/types/scaffolding-log-types.ts";

export const getScaffoldingLogValidationSchema = (
    t: (key: string, options?: any) => string
): Yup.Schema<BaseScaffoldingLogFormValues> => {
    return Yup.object({
        name: Yup.string()
            .min(2, t("errors:verification.minLength", {field: t("scaffoldingLogs:name"), count: 2}))
            .max(20, t("errors:verification.maxLength", {field: t("scaffoldingLogs:name"), count: 20}))
            .required(t("errors:verification.required", {field: t("scaffoldingLogs:name")})),
        additionalInfo: Yup.string()
            .min(2, t("errors:verification.minLength", {field: t("scaffoldingLogs:description"), count: 2}))
            .max(30, t("errors:verification.maxLength", {field: t("scaffoldingLogs:description"), count: 30}))
            .required(t("errors:verification.required", {field: t("scaffoldingLogs:description")})),
    }) as Yup.Schema<BaseScaffoldingLogFormValues>;
};
