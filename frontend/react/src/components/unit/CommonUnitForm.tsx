import * as Yup from "yup";
import {Form, Formik, FormikHelpers, FormikProps} from "formik";
import React from "react";
import {useTranslation} from "react-i18next";
import {Button, Grid, GridItem, Stack} from "@chakra-ui/react";
import {CustomInputField, CustomTextAreaField} from "@/components/shared/CustomFormFields.tsx";
import {BaseUnitFormValues} from "@/types/unit-types.ts";
import CustomStandaloneSelectField from "@/components/shared/CustomStandaloneSelectField.tsx";
import {getUnitTypeOptions} from "@/utils/unit-type-util.ts";

interface CommonUnitFormProps {
    initialValues: BaseUnitFormValues;
    validationSchema: Yup.Schema<BaseUnitFormValues>;
    onSubmit: (values: BaseUnitFormValues, formikHelpers: FormikHelpers<BaseUnitFormValues>) => Promise<void>;
    disabled?: boolean;
    hideSubmit?: boolean;
    innerRef?: React.Ref<FormikProps<BaseUnitFormValues>>;
}

const CommonUnitForm: React.FC<CommonUnitFormProps> = (({
                                                            initialValues,
                                                            validationSchema,
                                                            onSubmit,
                                                            disabled = false,
                                                            hideSubmit = false,
                                                            innerRef,
                                                        }) => {
    const {t} = useTranslation(['common', 'units'])
    const unitTypeOptions = React.useMemo(
        () => getUnitTypeOptions(t),
        [t]
    );

    return (
        <Formik<BaseUnitFormValues>
            initialValues={initialValues}
            validationSchema={validationSchema}
            onSubmit={onSubmit}
            enableReinitialize
            innerRef={innerRef}>
            {({isValid, isSubmitting, dirty, values, setFieldValue}) => (
                <Form>
                    <Stack gap={4}>
                        {/* Wiersz 1: Name 3/3 */}
                        <Grid templateColumns="repeat(3, 1fr)" gap={4}>
                            <GridItem colSpan={3}>
                                <CustomInputField
                                    name="name"
                                    label={t('units:symbol')}
                                    placeholder={t('units:symbol')}
                                    disabled={disabled}
                                />
                            </GridItem>
                        </Grid>

                        {/* Wiersz 2: Description 3/3 */}
                        <Grid templateColumns="repeat(3, 1fr)" gap={4}>
                            <GridItem colSpan={3}>
                                <CustomTextAreaField
                                    name="description"
                                    label={t('units:description')}
                                    placeholder={t('units:description')}
                                    disabled={disabled}
                                />
                            </GridItem>
                        </Grid>
                        <Grid templateColumns="repeat(3, 1fr)" gap={4}>
                            <GridItem colSpan={3}>
                                <CustomStandaloneSelectField
                                    label={t('units:unitType')}
                                    placeholder={t('units:unitType')}
                                    options={unitTypeOptions}
                                    value={values.unitType ? [values.unitType] : []}
                                    onChange={(selectedValues) =>
                                        setFieldValue("unitType", selectedValues[0] ?? "")
                                    }
                                    isMulti={false}
                                    disabled={disabled}
                                />
                            </GridItem>
                        </Grid>
                        {/*TODO add select from UnitTypes enum*/}

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
export default CommonUnitForm;