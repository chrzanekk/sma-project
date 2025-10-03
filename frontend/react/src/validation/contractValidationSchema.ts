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
        ).required(t("errors:verification.required", {field: t("contracts:currency")})),
        // Walidacja dat
        startDate: Yup.string()
            .required(t("errors:verification.required", { field: t("contracts:startDate") }))
            .test(
                'startDate-before-endDate',
                t("errors:verification.startDateAfterEndDate"),
                function (value) {
                    const { endDate } = this.parent;
                    if (!value || !endDate) return true;
                    return new Date(value) <= new Date(endDate);
                }
            ),

        endDate: Yup.string()
            .required(t("errors:verification.required", { field: t("contracts:endDate") })),

        signupDate: Yup.string()
            .required(t("errors:verification.required", { field: t("contracts:signupDate") })),

        realEndDate: Yup.string()
            .nullable()
            .test(
                'realEndDate-after-startDate',
                t("errors:verification.realEndDateBeforeStartDate"),
                function (value) {
                    const { startDate } = this.parent;
                    if (!value || !startDate) return true;
                    return new Date(value) >= new Date(startDate);
                }
            ),

        // Walidacja obiektów (contractor, contact, constructionSite)
        contractor: Yup.object()
            .nullable()
            .required(t("errors:verification.required", { field: t("contractors:contractor") }))
            .test(
                'contractor-not-null',
                t("errors:verification.required", { field: t("contractors:contractor") }),
                (value) => value !== null && value !== undefined
            ),

        contact: Yup.object()
            .nullable()
            .required(t("errors:verification.required", { field: t("contacts:contact") }))
            .test(
                'contact-not-null',
                t("errors:verification.required", { field: t("contacts:contact") }),
                (value) => value !== null && value !== undefined
            ),

        constructionSite: Yup.object()
            .nullable()
            .required(t("errors:verification.required", { field: t("constructionSites:constructionSite") }))
            .test(
                'constructionSite-not-null',
                t("errors:verification.required", { field: t("constructionSites:constructionSite") }),
                (value) => value !== null && value !== undefined
            ),

    }) as unknown as Yup.Schema<BaseContractFormValues>;
};
