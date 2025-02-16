import {useTranslation} from "react-i18next";
import {ContractorDTO, EditContractorDTO, EditContractorFormValues} from "@/types/contractor-types.ts";
import {useEffect, useState} from "react";
import {getContractorById, updateContractor} from "@/services/contractor-service.ts";
import {Formik} from "formik";
import * as Yup from "yup";
import {Country, getCountryOptions} from "@/types/country-type.ts";
import {errorNotification, successNotification} from "@/notifications/notifications.ts";
import {formatMessage} from "@/notifications/FormatMessage.tsx";


interface EditContractorFormProps {
    onSuccess: () => void;
    contractorId: number;
}

const EditContractorForm: React.FC<EditContractorFormProps> = ({onSuccess, contractorId}) => {
    const {t} = useTranslation(['common', 'contractors'])
    const defaultValues: EditContractorFormValues = {
        id: 0,
        name: '',
        taxNumber: '',
        street: '',
        buildingNo: '',
        apartmentNo: '',
        postalCode: '',
        city: '',
        country: '',
        customer: false,
        supplier: false,
        scaffoldingUser: false
    }

    const [initialValues, setInitialValues] = useState<EditContractorFormValues>(defaultValues);
    const [isLoading, setIsLoading] = useState<boolean>(true);
    const countryOptions = getCountryOptions(t);


    useEffect(() => {
        const fetchContractor = async () => {
            setIsLoading(true);
            try {
                const contractor: ContractorDTO = await getContractorById(contractorId);
                setInitialValues({
                    id: contractor.id,
                    name: contractor.name,
                    taxNumber: contractor.taxNumber,
                    street: contractor.street,
                    buildingNo: contractor.buildingNo,
                    apartmentNo: contractor.apartmentNo,
                    postalCode: contractor.postalCode,
                    city: contractor.city,
                    country: contractor.country.name,
                    customer: contractor.customer,
                    supplier: contractor.supplier,
                    scaffoldingUser: contractor.scaffoldingUser
                })
            } catch (err) {
                console.error("Error fetching contractor: ", err);
            } finally {
                setIsLoading(false);
            }
        };
        fetchContractor().catch();
    }, [contractorId]);

    return (
        <Formik<EditContractorFormValues>
            initialValues={initialValues}
            enableReinitialize={true}
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
                    const mappedContractor: EditContractorDTO = {
                        ...formValues,
                        country: Country.fromCode(formValues.country)
                    }
                    await updateContractor(mappedContractor);
                    successNotification(
                        t('success', {ns: "common"}),
                        formatMessage('contractors:notifications.editContractorSuccess', {name: formValues.name})
                    );
                    onSuccess();
                } catch (err: any) {
                    console.error(err);
                    errorNotification(
                        t('error', {ns: "common"}),
                        err.response?.data?.message || t('contractors:notifications.editContractorError')
                    );
                } finally {
                    setSubmitting(false);
                }
            }}
            >

        </Formik>
    )
}