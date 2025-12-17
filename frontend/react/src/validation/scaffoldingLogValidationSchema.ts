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
            .min(2, t("errors:verification.minLength", {field: t("scaffoldingLogs:additionalInfo"), count: 2}))
            .max(100, t("errors:verification.maxLength", {field: t("scaffoldingLogs:additionalInfo"), count: 100}))
            .required(t("errors:verification.required", {field: t("scaffoldingLogs:additionalInfo")})),
        contractor: Yup.object()
            .nullable()
            .required(t("errors:verification.required", {field: t("contractors:contractor")}))
            .test(
                'contractor-not-null',
                t("errors:verification.required", {field: t("contractors:contractor")}),
                (value) => value !== null && value !== undefined
            ),
        constructionSite: Yup.object()
            .nullable()
            .required(t("errors:verification.required", {field: t("constructionSites:constructionSite")}))
            .test(
                'constructionSite-not-null',
                t("errors:verification.required", {field: t("constructionSites:constructionSite")}),
                (value) => value !== null && value !== undefined
            ),
    }) as unknown as Yup.Schema<BaseScaffoldingLogFormValues>;
};
