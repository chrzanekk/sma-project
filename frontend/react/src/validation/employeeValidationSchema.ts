import * as Yup from "yup";
import {BaseEmployeeFormValues} from "@/types/employee-types.ts";

export const getEmployeeValidationSchema = (
    t: (key: string, options?: any) => string
): Yup.Schema<BaseEmployeeFormValues> => {
    return Yup.object({
        firstName: Yup.string()
            .min(2, t("errors:verification.minLength", {field: t("employees:firstName"), count: 2}))
            .max(20, t("errors:verification.maxLength", {field: t("employees:firstName"), count: 20}))
            .required(t("errors:verification.required", {field: t("employees:firstName")})),
        lastName: Yup.string()
            .min(2, t("errors:verification.minLength", {field: t("employees:firstName"), count: 2}))
            .max(20, t("errors:verification.maxLength", {field: t("employees:firstName"), count: 20}))
            .required(t("errors:verification.required", {field: t("employees:firstName")})),
        hourRate: Yup.string()
            .min(1, t("errors:verification.minLength", {field: t("employees:hourRate"), count: 1}))
            .max(20, t("errors:verification.maxLength", {field: t("employees:hourRate"), count: 20}))
            .required(t("errors:verification.required", {field: t("employees:hourRate")})),
        position: Yup.object()
            .nullable()
            .required(t("errors:verification.required", {field: t("positions:position")}))
            .test(
                'position-not-null',
                t("errors:verification.required", {field: t("positions:position")}),
                (value) => value !== null && value !== undefined
            ),
    }) as unknown as Yup.Schema<BaseEmployeeFormValues>;
};
