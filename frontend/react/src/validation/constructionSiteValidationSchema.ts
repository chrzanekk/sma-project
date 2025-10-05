import {ConstructionSiteFormValues} from "@/types/constrution-site-types.ts";
import * as Yup from "yup";

export const getConstructionSiteValidationSchema = (
    t: (key: string, options?: any) => string,
    countryOptions: { value: string, label: string } []
): Yup.Schema<ConstructionSiteFormValues> => {
    return Yup.object({
        name: Yup.string()
            .min(5, t("errors:verification.minLength", {field: t("constructionSites:name"), count: 5}))
            .max(50, t("errors:verification.maxLength", {field: t("constructionSites:name"), count: 50}))
            .required(t("errors:verification.required", {field: t("constructionSites:name")})),
        address: Yup.string()
            .min(3, t("errors:verification.minLength", {field: t("constructionSites:address"), count: 3}))
            .max(50, t("errors:verification.maxLength", {field: t("constructionSites:address"), count: 50}))
            .required(t("errors:verification.required", {field: t("constructionSites:address")})),
        country: Yup.string()
            .oneOf(
                countryOptions.map((option) => option.value),
                t("contractors:country")
            )
            .required(t("errors:verification.required", {field: t("constructionSites:country")})),
        shortName: Yup.string()
            .min(3, t("errors:verification.minLength", {field: t("constructionSites:shortName"), count: 3}))
            .max(8, t("errors:verification.maxLength", {field: t("constructionSites:shortName"), count: 8}))
            .required(t("errors:verification.required", {field: t("constructionSites:shortName")})),
        code: Yup.string()
            .min(3, t("errors:verification.minLength", {field: t("constructionSites:code"), count: 3}))
            .max(3, t("errors:verification.maxLength", {field: t("constructionSites:code"), count: 3}))
            .required(t("errors:verification.required", {field: t("constructionSites:code")})),
    }) as Yup.Schema<ConstructionSiteFormValues>
};