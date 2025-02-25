import {useTranslation} from "react-i18next";
import {ContractorDTO, EditContractorDTO, EditContractorFormValues} from "@/types/contractor-types.ts";
import React, {useEffect, useState} from "react";
import {getContractorById, updateContractor} from "@/services/contractor-service.ts";
import {Country, getCountryOptions} from "@/types/country-type.ts";
import {errorNotification, successNotification} from "@/notifications/notifications.ts";
import {formatMessage} from "@/notifications/FormatMessage.tsx";
import {getContractorValidationSchema} from "@/validation/contractorValidationSchema.ts";
import CommonContractorForm from "@/components/contractor/CommonContractorForm.tsx";


interface EditContractorFormProps {
    onSuccess: () => void;
    contractorId: number;
}

const EditContractorForm: React.FC<EditContractorFormProps> = ({onSuccess, contractorId}) => {
    const {t} = useTranslation(['common', 'contractors', 'errors'])
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
                    country: contractor.country.code,
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

    const validationSchema = getContractorValidationSchema(t, countryOptions);

    const handleSubmit = async (
        values: EditContractorFormValues,
        {setSubmitting}: { setSubmitting: (isSubmitting: boolean) => void }
    ) => {
        setSubmitting(true);
        try {
            const mappedContractor: EditContractorDTO = {
                ...values,
                id: contractorId,
                country: Country.fromCode(values.country),
            };
            await updateContractor(mappedContractor);
            successNotification(
                t("success", {ns: "common"}),
                formatMessage("notifications.editContractorSuccess", {name: values.name}, "contractors")
            );
            onSuccess();
        } catch (err: any) {
            console.error(err);
            errorNotification(
                t("common:error"),
                err.response?.data?.message || t("contractors:notifications.editContractorError")
            );
        } finally {
            setSubmitting(false);
        }
    }

    if (isLoading) return <div>Loading...</div>;

    return (
        <CommonContractorForm initialValues={initialValues}
                              validationSchema={validationSchema}
                              onSubmit={handleSubmit}/>

    );
}

export default EditContractorForm;