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
            .max(150, t("errors:verification.maxLength", {
                field: t("scaffoldingLogPositions:assemblyLocation"),
                count: 150
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
                    .test(
                        'required-if-row-started', // Nazwa testu
                        t("errors:verification.required", { field: t("dimensionDescriptions:length") }), // Komunikat
                        function (value) {

                            const { width, height } = this.parent as { width?: string; height?: string };
                            if (!value && !width && !height) {
                                return true;
                            }
                            return !!value;
                        }
                    )
                    .test('is-decimal', t("errors:verification.invalidNumberFormat"), (value) => {
                        if (!value) return true;
                        return numberRegex.test(value);
                    }),

                width: Yup.string()
                    .test(
                        'required-if-row-started',
                        t("errors:verification.required", { field: t("dimensionDescriptions:width") }),
                        function (value) {
                            const { length, height } = this.parent as { length?: string; height?: string };
                            if (!length && !value && !height) {
                                return true;
                            }
                            return !!value;
                        }
                    )
                    .test('is-decimal', t("errors:verification.invalidNumberFormat"), (value) => {
                        if (!value) return true;
                        return numberRegex.test(value);
                    }),

                height: Yup.string()
                    .test(
                        'required-if-row-started',
                        t("errors:verification.required", { field: t("dimensionDescriptions:height") }),
                        function (value) {
                            const { length, width } = this.parent as { length?: string; width?: string };
                            if (!length && !width && !value) {
                                return true;
                            }
                            return !!value;
                        }
                    )
                    .test('is-decimal', t("errors:verification.invalidNumberFormat"), (value) => {
                        if (!value) return true;
                        return numberRegex.test(value);
                    }),

                dimensionType: Yup.string().required(),
                operationType: Yup.string().required(),
            })
        ).test(
            'at-least-one-dimension',
            t("errors:verification.atLeastOneDimensionRequired", {defaultValue: "Przynajmniej jeden wymiar jest wymagany"}), // Dodaj ten klucz do tłumaczeń lub użyj hardcoded stringa na razie
            (dimensions) => {
                if (!dimensions || dimensions.length === 0) return false;

                return dimensions.some(dim =>
                        dim.length && dim.width && dim.height
                );
            }
        )
    }) as unknown as Yup.Schema<BaseScaffoldingLogPositionFormValues>;
};
