import * as Yup from "yup";
import {CompanyFormValues} from "@/types/company-type.ts";

export const getCompanyValidationSchema = (
    t: (key: string, options?: any) => string
): Yup.Schema<CompanyFormValues> => {
    return Yup.object({
        name: Yup.string()
            .min(2, t("errors:verification.minLength", {field: t("companies:name"), count: 2}))
            .max(200, t("errors:verification.maxLength", {field: t("companies:name"), count: 200}))
            .required(t("errors:verification.required", {field: t("companies:name")})),
        additionalInfo: Yup.string()
            .min(5, t("errors:verification.minLength", {field: t("companies:additionalInfo"), count: 5}))
            .max(300, t("errors:verification.maxLength", {field: t("companies:additionalInfo"), count: 300}))
            .required(t("errors:verification.required", {field: t("companies:additionalInfo")})),
    }) as Yup.Schema<CompanyFormValues>;
};
