import {useTranslation} from "react-i18next";
import CustomInputField, {CustomSelectField, getBooleanOptions} from "@/components/shared/FormConfig.tsx";
import {Form, Formik} from "formik";
import * as Yup from 'yup';
import {AddContractorDTO, AddContractorFormValues} from "@/types/contractor-types.ts";
import {addContractor} from "@/services/contractor-service.ts";
import {errorNotification, successNotification} from "@/notifications/notifications.ts";
import {formatMessage} from "@/notifications/FormatMessage.tsx";
import React from "react";
import {Button, Stack} from "@chakra-ui/react";
import {Country, getCountryOptions} from "@/types/country-type.ts";


interface AddContractorFormProps {
    onSuccess: () => void;
}

const AddContractorForm: React.FC<AddContractorFormProps> = ({onSuccess}) => {
        const {t} = useTranslation(['common', 'contractors']);
        const booleanOptions = getBooleanOptions(t);
        const countryOptions = getCountryOptions(t);

        return (
            <Formik<AddContractorFormValues>
                initialValues={{
                    name: '',
                    taxNumber: '',
                    street: '',
                    buildingNo: '',
                    apartmentNo: '',
                    postalCode: '',
                    city: '',
                    country: countryOptions[0].value,
                    customer: false,
                    supplier: false,
                    scaffoldingUser: false
                }}
                validationSchema={Yup.object({
                    name: Yup.string()
                        .min(5, t('verification.minLength', {field: t('contractors:name'), count: 5}))
                        .max(50, t('verification.maxLength', {field: t('contractors:name'), count: 50}))
                        .required(t('verification.required', {field: t('contractors:name')})),
                    taxNumber: Yup.string()
                        .min(4, t('verification.minLength', {field: t('contractors:taxNumber'), count: 4}))
                        .max(20, t('verification.maxLength', {field: t('contractors:taxNumber'), count: 20}))
                        .required(t('verification.required', {field: t('contractors:taxNumber')})),
                    street: Yup.string()
                        .min(4, t('verification.minLength', {field: t('contractors:street'), count: 4}))
                        .max(20, t('verification.maxLength', {field: t('contractors:street'), count: 20}))
                        .required(t('verification.required', {field: t('contractors:street')})),
                    buildingNo: Yup.string()
                        .min(2, t('verification.minLength', {field: t('contractors:buildingNo'), count: 2}))
                        .max(15, t('verification.maxLength', {field: t('contractors:buildingNo'), count: 15}))
                        .required(t('verification.required', {field: t('contractors:buildingNo')})),
                    apartmentNo: Yup.string()
                        .min(2, t('verification.minLength', {field: t('contractors:apartmentNo'), count: 2}))
                        .max(15, t('verification.maxLength', {field: t('contractors:apartmentNo'), count: 15}))
                        .required(t('verification.required', {field: t('contractors:apartmentNo')})),
                    postalCode: Yup.string()
                        .min(2, t('verification.minLength', {field: t('contractors:postalCode'), count: 2}))
                        .max(50, t('verification.maxLength', {field: t('contractors:postalCode'), count: 50}))
                        .required(t('verification.required', {field: t('contractors:postalCode')})),
                    city: Yup.string()
                        .min(2, t('verification.minLength', {field: t('contractors:city'), count: 2}))
                        .max(50, t('verification.maxLength', {field: t('contractors:city'), count: 50}))
                        .required(t('verification.required', {field: t('contractors:city')})),
                    country: Yup.string()
                        .oneOf(countryOptions.map((option: {
                            value: string;
                            label: string
                        }) => option.value), t('contractors:country'))
                        .required(t('verification.required', {field: t('contractors:country')})),
                    customer: Yup.boolean()
                        .required(t("verification.required", {field: t("contractors:customer")})),
                    supplier: Yup.boolean()
                        .required(t("verification.required", {field: t("contractors:supplier")})),
                    scaffoldingUser: Yup.boolean()
                        .required(t("verification.required", {field: t("contractors:scaffoldingUser")}))
                })}
                onSubmit={async (formValues, {setSubmitting}) => {
                    setSubmitting(true);
                    try {
                        const mappedContractor: AddContractorDTO = {
                            ...formValues,
                            country: Country.fromCode(formValues.country)
                        }
                        await addContractor(mappedContractor);
                        successNotification(
                            t('success', {ns: "common"}),
                            formatMessage('contractors:notifications.addContractorSuccess', {name: formValues.name})
                        );
                        onSuccess();
                    } catch (err: any) {
                        console.error(err);
                        errorNotification(
                            t('error', {ns: "common"}),
                            err.response?.data?.message || t('contractors:notifications.addContractorError')
                        );
                    } finally {
                        setSubmitting(false);
                    }
                }}
            >
                {({isValid, isSubmitting, dirty}) => (
                    <Form>
                        <Stack gap={"2px"}>
                            <CustomInputField name={"name"}
                                              label={t('contractors:name')}
                                              placeholder={t('contractors:name')}/>
                            <CustomInputField name={"taxNumber"}
                                              label={t('contractors:taxNumber')}
                                              placeholder={t('contractors:taxNumber')}/>
                            <CustomInputField name={"street"}
                                              label={t('contractors:street')}
                                              placeholder={t('contractors:street')}/>
                            <CustomInputField name={"buildingNo"}
                                              label={t('contractors:buildingNo')}
                                              placeholder={t('contractors:buildingNo')}/>
                            <CustomInputField name={"apartmentNo"}
                                              label={t('contractors:apartmentNo')}
                                              placeholder={t('contractors:apartmentNo')}/>
                            <CustomInputField name={"postalCode"}
                                              label={t('contractors:postalCode')}
                                              placeholder={t('contractors:postalCode')}/>
                            <CustomInputField name={"city"}
                                              label={t('contractors:city')}
                                              placeholder={t('contractors:city')}/>
                            <CustomSelectField name={"country"}
                                               label={t("contractors:country")}
                                               placeholder={t("contractors:country")}
                                               width={"400px"}
                                               options={countryOptions}/>
                            <CustomSelectField name={"customer"}
                                               label={t('contractors:customer')}
                                               placeholder={t('contractors:customer')}
                                               width={"400px"}
                                               options={booleanOptions}/>
                            <CustomSelectField name={"supplier"}
                                               label={t('contractors:supplier')}
                                               placeholder={t('contractors:supplier')}
                                               width={"400px"}
                                               options={booleanOptions}/>
                            <CustomSelectField name={"scaffoldingUser"}
                                               label={t('contractors:scaffoldingUser')}
                                               placeholder={t('contractors:scaffoldingUser')}
                                               width={"400px"}
                                               options={booleanOptions}/>
                            <Button disabled={!isValid || isSubmitting || !dirty} type="submit" colorPalette="green">
                                {t('contractors:add')}
                            </Button>
                        </Stack>
                    </Form>
                )}
            </Formik>
        )
            ;
    }
;

export default AddContractorForm;