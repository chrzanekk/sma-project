import * as Yup from "yup";
import {BaseScaffoldingLogPositionFormValues} from "@/types/scaffolding-log-position-types.ts";

export const getScaffoldingLogPositionValidationSchema = (
    t: (key: string, options?: any) => string,
    scaffoldingType: { value: string; label: string }[],
    technicalProtocolStatusOptions: { value: string; label: string }[]
): Yup.Schema<BaseScaffoldingLogPositionFormValues> => {

    const numberRegex = /^\d{1,3}(?: ?\d{3})*(?:[.,]\d{1,2})?$/;

    return Yup.object({
        scaffoldingNumber: Yup.string()
            .min(2, t("errors:verification.minLength", {
                field: t("scaffoldingLogPositions:scaffoldingNumber"),
                count: 2
            }))
            .max(20, t("errors:verification.maxLength", {
                field: t("scaffoldingLogPositions:scaffoldingNumber"),
                count: 20
            }))
            .required(t("errors:verification.required", {field: t("scaffoldingLogPositions:scaffoldingNumber")})),
        assemblyLocation: Yup.string()
            .min(2, t("errors:verification.minLength", {
                field: t("scaffoldingLogPositions:assemblyLocation"),
                count: 2
            }))
            .max(30, t("errors:verification.maxLength", {
                field: t("scaffoldingLogPositions:assemblyLocation"),
                count: 30
            }))
            .required(t("errors:verification.required", {field: t("scaffoldingLogPositions:assemblyLocation")})),
        assemblyDate: Yup.string()
            .required(t("errors:verification.required", {field: t("scaffoldingLogPositions:assemblyDate")})),
        scaffoldingType: Yup.string()
            .oneOf(
                scaffoldingType.map((option) => option.value),
                t("scaffoldingLogPositions:scaffoldingType")
            )
            .required(t("errors:verification.required", {field: t("scaffoldingLogPositions:scaffoldingType")})),
        technicalProtocolStatus: Yup.string()
            .oneOf(
                technicalProtocolStatusOptions.map((option) => option.value),
                t("scaffoldingLogPositions:technicalProtocolStatus")
            )
            .required(t("errors:verification.required", {field: t("scaffoldingLogPositions:technicalProtocolStatus")})),
        contractor: Yup.object()
            .nullable()
            .required(t("errors:verification.required", {field: t("scaffoldingLogPositions:contractor")}))
            .test(
                'contractor-not-null',
                t("errors:verification.required", {field: t("scaffoldingLogPositions:contractor")}),
                (value) => value !== null && value !== undefined
            ),
        contractorContact: Yup.object()
            .nullable()
            .required(t("errors:verification.required", {field: t("scaffoldingLogPositions:contractorContact")}))
            .test(
                'contact-not-null',
                t("errors:verification.required", {field: t("scaffoldingLogPositions:contractorContact")}),
                (value) => value !== null && value !== undefined
            ),
        scaffoldingUser: Yup.object()
            .nullable()
            .required(t("errors:verification.required", {field: t("scaffoldingLogPositions:scaffoldingUser")}))
            .test(
                'contractor-not-null',
                t("errors:verification.required", {field: t("scaffoldingLogPositions:scaffoldingUser")}),
                (value) => value !== null && value !== undefined
            ),
        scaffoldingUserContact: Yup.object()
            .nullable()
            .required(t("errors:verification.required", {field: t("scaffoldingLogPositions:scaffoldingUser")}))
            .test(
                'contact-not-null',
                t("errors:verification.required", {field: t("scaffoldingLogPositions:scaffoldingUser")}),
                (value) => value !== null && value !== undefined
            ),
        dimensions: Yup.array().of(
            Yup.object().shape({
                length: Yup.string()
                    .required(t("errors:verification.required", {field: t("dimensionDescriptions:length")}))
                    .test('is-decimal', t("errors:verification.invalidNumberFormat"), (value) => {
                        if (!value) return true; // required obsłuży puste
                        return numberRegex.test(value);
                    }),
                width: Yup.string()
                    .required(t("errors:verification.required", {field: t("dimensionDescriptions:width")}))
                    .test('is-decimal', t("errors:verification.invalidNumberFormat"), (value) => {
                        if (!value) return true;
                        return numberRegex.test(value);
                    }),
                height: Yup.string()
                    .required(t("errors:verification.required", {field: t("dimensionDescriptions:height")}))
                    .test('is-decimal', t("errors:verification.invalidNumberFormat"), (value) => {
                        if (!value) return true;
                        return numberRegex.test(value);
                    }),
                dimensionType: Yup.string().required(),
                operationType: Yup.string().required(),
            })
        )
    }) as unknown as Yup.Schema<BaseScaffoldingLogPositionFormValues>;
};
