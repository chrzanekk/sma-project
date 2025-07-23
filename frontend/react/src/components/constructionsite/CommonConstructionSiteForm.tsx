import {ConstructionSiteFormValues} from "@/types/constrution-site-types.ts";
import {Form, Formik, FormikHelpers, FormikProps} from "formik";
import * as Yup from 'yup';
import React, {useEffect} from "react";
import {useTranslation} from "react-i18next";
import {getCountryOptions} from "@/types/country-type.ts";
import {CustomInputField, CustomSelectField, CustomTextAreaField} from "@/components/shared/CustomFormFields";
import {Button, Grid, GridItem, Stack} from "@chakra-ui/react";


interface CommonConstructionSiteFormProps {
    initialValues: ConstructionSiteFormValues;
    validationSchema: Yup.Schema<ConstructionSiteFormValues>
    onSubmit: (
        values: ConstructionSiteFormValues,
        formikHelpers: FormikHelpers<ConstructionSiteFormValues>
    ) => Promise<void>;
    disabled?: boolean;
    hideSubmit?: boolean;
    onValidityChange?: (isValid: boolean) => void;
    innerRef?: React.Ref<FormikProps<ConstructionSiteFormValues>>;
}

const CommonConstructionSiteForm: React.FC<CommonConstructionSiteFormProps> = (
    ({
         initialValues,
         validationSchema,
         onSubmit,
         disabled = false,
         hideSubmit = false,
         onValidityChange,
         innerRef
     }) => {
        const {t} = useTranslation(['common', 'constructionSites']);
        const countryOptions = getCountryOptions(t);

        return (
            <Formik<ConstructionSiteFormValues>
                initialValues={initialValues}
                validationSchema={validationSchema}
                onSubmit={onSubmit}
                enableReinitialize
                validateOnMount
                innerRef={innerRef}>

                {({isValid, isSubmitting, dirty}) => {
                    useEffect(() => {
                        if (onValidityChange) {
                            onValidityChange(isValid);
                        }
                    }, [isValid, onValidityChange]);
                    return (
                        <Form>
                            <Stack gap={2}>
                                <Grid templateColumns="repeat(3, 2fr)" gap={2}>
                                    <GridItem colSpan={3}>
                                        <CustomInputField name={"name"}
                                                          label={t('constructionSites:name')}
                                                          placeholder={t('constructionSites:name')}
                                                          disabled={disabled}
                                        />
                                    </GridItem>
                                </Grid>
                                <Grid templateColumns="repeat(6, 1fr)" gap={2}>
                                    <GridItem colSpan={4}>
                                        <CustomInputField name={"shortName"}
                                                          label={t('constructionSites:shortName')}
                                                          placeholder={t('constructionSites:shortName')}
                                                          disabled={disabled}
                                        />
                                    </GridItem>
                                    <GridItem colSpan={2}>
                                        <CustomInputField name={"code"}
                                                          label={t('constructionSites:code')}
                                                          placeholder={t('constructionSites:code')}
                                                          disabled={disabled}
                                        />
                                    </GridItem>
                                </Grid>
                                <Grid templateColumns="repeat(6, 1fr)" gap={2}>
                                    <GridItem colSpan={4}>
                                        <CustomTextAreaField name={"address"}
                                                             label={t('constructionSites:address')}
                                                             placeholder={t('constructionSites:address')}
                                                             disabled={disabled}
                                        />
                                    </GridItem>
                                    <GridItem colSpan={2}>
                                        <CustomSelectField name={"country"}
                                                           label={t('constructionSites:country')}
                                                           placeholder={t('constructionSites:country')}
                                                           width={"100%"}
                                                           options={countryOptions}
                                                           disabled={disabled}
                                        />
                                    </GridItem>
                                </Grid>
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
        )
    }
)

export default CommonConstructionSiteForm;