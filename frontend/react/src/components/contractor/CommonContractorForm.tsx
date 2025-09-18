import React, {useEffect} from "react";
import {Form, Formik, FormikHelpers, FormikProps} from "formik";
import {Button, Grid, GridItem, Stack} from "@chakra-ui/react";
import {CustomInputField, CustomSelectField} from "@/components/shared/CustomFormFields";
import {ContractorFormValues} from "@/types/contractor-types.ts";
import * as Yup from 'yup';
import {useTranslation} from "react-i18next";
import {getBooleanOptions} from "@/components/shared/formOptions.ts";
import {getCountryOptions} from "@/types/country-types.ts";

interface CommonContractorFormProps {
    initialValues: ContractorFormValues;
    validationSchema: Yup.Schema<ContractorFormValues>
    onSubmit: (
        values: ContractorFormValues,
        formikHelpers: FormikHelpers<ContractorFormValues>
    ) => Promise<void>;
    disabled?: boolean;
    hideSubmit?: boolean;
    onValidityChange?: (isValid: boolean) => void;
    innerRef?: React.Ref<FormikProps<ContractorFormValues>>;
}

const CommonContractorForm: React.FC<CommonContractorFormProps> =
    ((
            {
                initialValues,
                validationSchema,
                onSubmit,
                disabled = false,
                hideSubmit = false,
                onValidityChange,
                innerRef
            }) => {
            const {t} = useTranslation(['common', 'contractors']);
            const booleanOptions = getBooleanOptions(t);
            const countryOptions = getCountryOptions(t);
            return (
                <Formik<ContractorFormValues>
                    initialValues={initialValues}
                    validationSchema={validationSchema}
                    onSubmit={onSubmit}
                    enableReinitialize
                    validateOnMount
                    innerRef={innerRef}
                >
                    {({isValid, isSubmitting, dirty}) => {
                        useEffect(() => {
                            if (onValidityChange) {
                                onValidityChange(isValid);
                            }
                        }, [isValid, onValidityChange]);
                        return (
                            <Form>
                                <Stack gap={4}>
                                    {/* Wiersz 1: Name (4/6) i Tax Number (2/6) */}
                                    <Grid templateColumns="repeat(6, 1fr)" gap={4}>
                                        <GridItem colSpan={4}>
                                            <CustomInputField
                                                name="name"
                                                label={t('contractors:name')}
                                                placeholder={t('contractors:name')}
                                                disabled={disabled}
                                            />
                                        </GridItem>
                                        <GridItem colSpan={2}>
                                            <CustomInputField
                                                name="taxNumber"
                                                label={t('contractors:taxNumber')}
                                                placeholder={t('contractors:taxNumber')}
                                                disabled={disabled}
                                            />
                                        </GridItem>
                                    </Grid>

                                    {/* Wiersz 2: Street (6/12), BuildingNo (3/12), ApartmentNo (3/12) */}
                                    <Grid templateColumns="repeat(12, 0.5fr)" gap={4}>
                                        <GridItem colSpan={6}>
                                            <CustomInputField
                                                name="street"
                                                label={t('contractors:street')}
                                                placeholder={t('contractors:street')}
                                                disabled={disabled}
                                            />
                                        </GridItem>
                                        <GridItem colSpan={3}>
                                            <CustomInputField
                                                name="buildingNo"
                                                label={t('contractors:buildingNo')}
                                                placeholder={t('contractors:buildingNo')}
                                                disabled={disabled}
                                            />
                                        </GridItem>
                                        <GridItem colSpan={3}>
                                            <CustomInputField
                                                name="apartmentNo"
                                                label={t('contractors:apartmentNo')}
                                                placeholder={t('contractors:apartmentNo')}
                                                disabled={disabled}
                                            />
                                        </GridItem>
                                    </Grid>

                                    {/* Wiersz 3: PostalCode (1/6), City (3/6), Country (2/6) */}
                                    <Grid templateColumns="repeat(12, 0.5fr)" gap={4}>
                                        <GridItem colSpan={3}>
                                            <CustomInputField
                                                name="postalCode"
                                                label={t('contractors:postalCode')}
                                                placeholder={t('contractors:postalCode')}
                                                disabled={disabled}
                                            />
                                        </GridItem>
                                        <GridItem colSpan={6}>
                                            <CustomInputField
                                                name="city"
                                                label={t('contractors:city')}
                                                placeholder={t('contractors:city')}
                                                disabled={disabled}
                                            />
                                        </GridItem>
                                        <GridItem colSpan={3}>
                                            <CustomSelectField
                                                name="country"
                                                label={t("contractors:country")}
                                                placeholder={t("contractors:country")}
                                                width="100%"
                                                options={countryOptions}
                                                disabled={disabled}
                                            />
                                        </GridItem>
                                    </Grid>

                                    {/* Wiersz 4: Customer (2/6), Supplier (2/6), ScaffoldingUser (2/6) */}
                                    <Grid templateColumns="repeat(6, 1fr)" gap={4}>
                                        <GridItem colSpan={2}>
                                            <CustomSelectField
                                                name="customer"
                                                label={t("contractors:customer")}
                                                placeholder={t("contractors:customer")}
                                                width="100%"
                                                options={booleanOptions}
                                                disabled={disabled}
                                            />
                                        </GridItem>
                                        <GridItem colSpan={2}>
                                            <CustomSelectField
                                                name="supplier"
                                                label={t("contractors:supplier")}
                                                placeholder={t("contractors:supplier")}
                                                width="100%"
                                                options={booleanOptions}
                                                disabled={disabled}
                                            />
                                        </GridItem>
                                        <GridItem colSpan={2}>
                                            <CustomSelectField
                                                name="scaffoldingUser"
                                                label={t("contractors:scaffoldingUser")}
                                                placeholder={t("contractors:scaffoldingUser")}
                                                width="100%"
                                                options={booleanOptions}
                                                disabled={disabled}
                                            />
                                        </GridItem>
                                    </Grid>
                                    {/* Wiersz 5: Button wyśrodkowany */}
                                    {!hideSubmit && (<Grid templateColumns="repeat(6, 1fr)" gap={4}>
                                        <GridItem colSpan={6} textAlign="center">
                                            <Button
                                                disabled={!isValid || isSubmitting || !dirty}
                                                type="submit"
                                                colorPalette="green"
                                                width="400px"
                                            >
                                                {t('save', {ns: "common"})}
                                            </Button>
                                        </GridItem>
                                    </Grid>)}
                                </Stack>

                            </Form>
                        )
                    }}
                </Formik>
            );
        }
    );

export default CommonContractorForm;
