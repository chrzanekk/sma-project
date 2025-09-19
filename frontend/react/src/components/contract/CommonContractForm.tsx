import React from 'react';
import {Form, Formik, FormikHelpers, FormikProps} from 'formik';
import * as Yup from 'yup';
import {Box, Button, Grid, GridItem} from '@chakra-ui/react';
import {CustomInputField, CustomSelectField} from '@/components/shared/CustomFormFields';
import {useTranslation} from "react-i18next";
import {BaseContractFormValues} from "@/types/contract-types.ts";
import {getCurrencyOptions} from "@/types/currency-types.ts";

interface CommonContractFormProps {
    initialValues: BaseContractFormValues;
    validationSchema: Yup.Schema<BaseContractFormValues>;
    onSubmit: (values: BaseContractFormValues, formikHelpers: FormikHelpers<BaseContractFormValues>) => Promise<void>;
    disabled?: boolean;
    hideSubmit?: boolean;
    innerRef?: React.Ref<FormikProps<BaseContractFormValues>>;
}

const CommonContractForm: React.FC<CommonContractFormProps> = (({
                                                                    initialValues,
                                                                    validationSchema,
                                                                    onSubmit,
                                                                    disabled = false,
                                                                    hideSubmit = false,
                                                                    innerRef,
                                                                }) => {
            const {t} = useTranslation(['common', 'contracts']);
            const currencyOptions = getCurrencyOptions();

            return (
                <Formik<BaseContractFormValues>
                    initialValues={initialValues}
                    validationSchema={validationSchema}
                    onSubmit={onSubmit}
                    enableReinitialize
                    innerRef={innerRef}
                >
                    {({isValid, isSubmitting, dirty}) => (
                        <Form>
                            <Box gap={1}>
                                {/* Wiersz 1: Number 6/6 */}
                                <Grid templateColumns="repeat(6, 1fr)" gap={4}>
                                    <GridItem colSpan={6}>
                                        <CustomInputField
                                            name="number"
                                            label={t('contracts:number')}
                                            placeholder={t('contracts:number')}
                                            disabled={disabled}
                                        />
                                    </GridItem>
                                </Grid>

                                {/* Wiersz 2: description 6/6 */}
                                <Grid templateColumns="repeat(6, 1fr)" gap={4}>
                                    <GridItem colSpan={6}>
                                        <CustomInputField
                                            name="description"
                                            label={t('contracts:description')}
                                            placeholder={t('contracts:description')}
                                            disabled={disabled}
                                        />
                                    </GridItem>
                                </Grid>

                                {/* Wiersz 3: Value 4/6 Currency 2/6 */}
                                <Grid templateColumns="repeat(6, 1fr)" gap={4}>
                                    <GridItem colSpan={4}>
                                        <CustomInputField
                                            name="value"
                                            label={t('contracts:value')}
                                            placeholder={t('contracts:value')}
                                            disabled={disabled}
                                        />
                                    </GridItem>
                                    <GridItem colSpan={2}>
                                        <CustomSelectField
                                            name="currency"
                                            label={t('contracts:currency')}
                                            placeholder={t('contracts:currency')}
                                            width={"100%"}
                                            options={currencyOptions}
                                            disabled={disabled}
                                        />
                                    </GridItem>
                                </Grid>
                                <Grid templateColumns="repeat(6, 1fr)" gap={4}>
                                    <GridItem colSpan={3}>
                                        <CustomInputField
                                            name="startDate"
                                            label={t('contracts:startDate')}
                                            placeholder={t('contracts:startDate')}
                                            disabled={disabled}
                                            type={"date"}
                                        />
                                    </GridItem>
                                    <GridItem colSpan={3}>
                                        <CustomInputField
                                            name="endDate"
                                            label={t('contracts:endDate')}
                                            placeholder={t('contracts:endDate')}
                                            disabled={disabled}
                                            type={"date"}
                                        />
                                    </GridItem>
                                </Grid>
                                <Grid templateColumns="repeat(6, 1fr)" gap={4}>
                                    <GridItem colSpan={3}>
                                        <CustomInputField
                                            name="signupDate"
                                            label={t('contracts:signupDate')}
                                            placeholder={t('contracts:signupDate')}
                                            disabled={disabled}
                                            type={"date"}
                                        />
                                    </GridItem>
                                    <GridItem colSpan={3}>
                                        <CustomInputField
                                            name="realEndDate"
                                            label={t('contracts:realEndDate')}
                                            placeholder={t('contracts:realEndDate')}
                                            disabled={disabled}
                                            type={"date"}
                                        />
                                    </GridItem>
                                </Grid>

                                {/* Wiersz 4: Button wyśrodkowany */}
                                {!hideSubmit && (
                                    <Grid templateColumns="repeat(6, 1fr)" gap={4}>
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
                                    </Grid>
                                )}
                            </Box>
                        </Form>
                    )}
                </Formik>
            );
        }
    )
;

export default CommonContractForm;
