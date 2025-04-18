import * as Yup from "yup";
import {ContractorFormValues} from "@/types/contractor-types.ts";

export const getContractorValidationSchema = (
    t: (key: string, options?: any) => string,
    countryOptions: { value: string; label: string }[]
): Yup.Schema<ContractorFormValues> => {
    return Yup.object({
        name: Yup.string()
            .min(5, t("errors:verification.minLength", {field: t("contractors:name"), count: 5}))
            .max(50, t("errors:verification.maxLength", {field: t("contractors:name"), count: 50}))
            .required(t("errors:verification.required", {field: t("contractors:name")})),
        taxNumber: Yup.string()
            .min(4, t("errors:verification.minLength", {field: t("contractors:taxNumber"), count: 4}))
            .max(20, t("errors:verification.maxLength", {field: t("contractors:taxNumber"), count: 20}))
            .required(t("errors:verification.required", {field: t("contractors:taxNumber")})),
        street: Yup.string()
            .min(4, t("errors:verification.minLength", {field: t("contractors:street"), count: 4}))
            .max(20, t("errors:verification.maxLength", {field: t("contractors:street"), count: 20}))
            .required(t("errors:verification.required", {field: t("contractors:street")})),
        buildingNo: Yup.string()
            .min(1, t("errors:verification.minLength", {field: t("contractors:buildingNo"), count: 1}))
            .max(15, t("errors:verification.maxLength", {field: t("contractors:buildingNo"), count: 15}))
            .required(t("errors:verification.required", {field: t("contractors:buildingNo")})),
        apartmentNo: Yup.string()
            .min(1, t("errors:verification.minLength", {field: t("contractors:apartmentNo"), count: 1}))
            .max(15, t("errors:verification.maxLength", {field: t("contractors:apartmentNo"), count: 15})),
        postalCode: Yup.string()
            .min(2, t("errors:verification.minLength", {field: t("contractors:postalCode"), count: 2}))
            .max(50, t("errors:verification.maxLength", {field: t("contractors:postalCode"), count: 50}))
            .required(t("errors:verification.required", {field: t("contractors:postalCode")})),
        city: Yup.string()
            .min(2, t("errors:verification.minLength", {field: t("contractors:city"), count: 2}))
            .max(50, t("errors:verification.maxLength", {field: t("contractors:city"), count: 50}))
            .required(t("errors:verification.required", {field: t("contractors:city")})),
        country: Yup.string()
            .oneOf(
                countryOptions.map((option) => option.value),
                t("contractors:country")
            )
            .required(t("errors:verification.required", {field: t("contractors:country")})),
        customer: Yup.boolean().required(t("errors:verification.required", {field: t("contractors:customer")})),
        supplier: Yup.boolean().required(t("errors:verification.required", {field: t("contractors:supplier")})),
        scaffoldingUser: Yup.boolean().required(t("errors:verification.required", {field: t("contractors:scaffoldingUser")})),
    }) as Yup.Schema<ContractorFormValues>;
};
