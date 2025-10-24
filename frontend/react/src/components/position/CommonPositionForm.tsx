import * as Yup from "yup";
import {Form, Formik, FormikHelpers, FormikProps} from "formik";
import React from "react";
import {useTranslation} from "react-i18next";
import {Button, Grid, GridItem, Stack} from "@chakra-ui/react";
import {CustomInputField, CustomTextAreaField} from "@/components/shared/CustomFormFields.tsx";
import {BasePositionFormValues} from "@/types/position-types.ts";

interface CommonPositionFormProps {
    initialValues: BasePositionFormValues;
    validationSchema: Yup.Schema<BasePositionFormValues>;
    onSubmit: (values: BasePositionFormValues, formikHelpers: FormikHelpers<BasePositionFormValues>) => Promise<void>;
    disabled?: boolean;
    hideSubmit?: boolean;
    innerRef?: React.Ref<FormikProps<BasePositionFormValues>>;
}

const CommonPositionForm: React.FC<CommonPositionFormProps> = (({
                                                                    initialValues,
                                                                    validationSchema,
                                                                    onSubmit,
                                                                    disabled = false,
                                                                    hideSubmit = false,
                                                                    innerRef,
                                                                }) => {
    const {t} = useTranslation(['common', 'positions'])

    return (
        <Formik<BasePositionFormValues>
            initialValues={initialValues}
            validationSchema={validationSchema}
            onSubmit={onSubmit}
            enableReinitialize
            innerRef={innerRef}>
            {({isValid, isSubmitting, dirty}) => (
                <Form>
                    <Stack gap={4}>
                        {/* Wiersz 1: Name 3/3 */}
                        <Grid templateColumns="repeat(3, 1fr)" gap={4}>
                            <GridItem colSpan={3}>
                                <CustomInputField
                                    name="name"
                                    label={t('positions:name')}
                                    placeholder={t('positions:name')}
                                    disabled={disabled}
                                />
                            </GridItem>
                        </Grid>

                        {/* Wiersz 2: Description 3/3 */}
                        <Grid templateColumns="repeat(3, 1fr)" gap={4}>
                            <GridItem colSpan={3}>
                                <CustomTextAreaField
                                    name="description"
                                    label={t('positions:description')}
                                    placeholder={t('positions:description')}
                                    disabled={disabled}
                                />
                            </GridItem>
                        </Grid>

                        {/* Wiersz 3: Button wyśrodkowany */}
                        {!hideSubmit && (<Grid templateColumns="repeat(3, 1fr)" gap={4}>
                            <GridItem colSpan={3} textAlign="center">
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
            )}
        </Formik>
    )
})
export default CommonPositionForm;