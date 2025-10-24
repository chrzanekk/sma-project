import * as Yup from "yup";
import {BasePositionFormValues} from "@/types/contact-types.ts";

export const getContactValidationSchema = (
    t: (key: string, options?: any) => string
): Yup.Schema<BasePositionFormValues> => {
    return Yup.object({
        firstName: Yup.string()
            .min(2, t("errors:verification.minLength", {field: t("contacts:firstName"), count: 2}))
            .max(20, t("errors:verification.maxLength", {field: t("contacts:firstName"), count: 20}))
            .required(t("errors:verification.required", {field: t("contacts:firstName")})),
        lastName: Yup.string()
            .min(2, t("errors:verification.minLength", {field: t("contacts:lastName"), count: 2}))
            .max(30, t("errors:verification.maxLength", {field: t("contacts:lastName"), count: 30}))
            .required(t("errors:verification.required", {field: t("contacts:lastName")})),
        phoneNumber: Yup.string()
            .min(5, t("errors:verification.minLength", {field: t("contacts:phoneNumber"), count: 5}))
            .max(25, t("errors:verification.maxLength", {field: t("contacts:phoneNumber"), count: 25}))
            .required(t("errors:verification.required", {field: t("contacts:phoneNumber")})),
        email: Yup.string()
            .min(5, t("errors:verification.minLength", {field: t("contacts:email"), count: 5}))
            .max(75, t("errors:verification.maxLength", {field: t("contacts:email"), count: 75}))
            .required(t("errors:verification.required", {field: t("contacts:email")})),
        additionalInfo: Yup.string()
            .min(5, t("errors:verification.minLength", {field: t("contacts:additionalInfo"), count: 5}))
            .max(200, t("errors:verification.maxLength", {field: t("contacts:additionalInfo"), count: 200}))
            .required(t("errors:verification.required", {field: t("contacts:additionalInfo")})),
    }) as Yup.Schema<BasePositionFormValues>;
};
