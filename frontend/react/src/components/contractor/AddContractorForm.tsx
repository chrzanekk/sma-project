import {useTranslation} from "react-i18next";
import {ContractorDTO, ContractorFormValues} from "@/types/contractor-types.ts";
import {addContractor} from "@/services/contractor-service.ts";
import {errorNotification, successNotification} from "@/notifications/notifications.ts";
import {formatMessage} from "@/notifications/FormatMessage.tsx";
import React from "react";
import {Country, getCountryOptions} from "@/types/country-type.ts";
import {getContractorValidationSchema} from "@/validation/contractorValidationSchema.ts";
import CommonContractorForm from "@/components/contractor/CommonContractorForm.tsx";
import {getSelectedCompany} from "@/utils/company-utils.ts";


interface AddContractorFormProps {
    onSuccess: (data: ContractorDTO) => void;
}

const AddContractorForm: React.FC<AddContractorFormProps> = ({onSuccess}) => {
    const {t} = useTranslation(['common', 'contractors']);
    const countryOptions = getCountryOptions(t);
    const currentCompany = getSelectedCompany();

    const initialValues: ContractorFormValues = {
        name: "",
        taxNumber: "",
        street: "",
        buildingNo: "",
        apartmentNo: "",
        postalCode: "",
        city: "",
        country: countryOptions[0].value,
        customer: false,
        supplier: false,
        scaffoldingUser: false,
    };

    const validationSchema = getContractorValidationSchema(t, countryOptions);

    const handleSubmit = async (
        values: ContractorFormValues,
        {setSubmitting}: { setSubmitting: (isSubmitting: boolean) => void }
    ) => {
        setSubmitting(true);
        try {
            const mappedContractor: ContractorDTO = {
                ...values,
                country: Country.fromCode(values.country),
                company: currentCompany!
            };
            await addContractor(mappedContractor);
            successNotification(
                t("success", {ns: "common"}),
                formatMessage("notifications.addContractorSuccess", {name: values.name}, "contractors")
            );
            onSuccess(mappedContractor);
        } catch (err: any) {
            console.error(err);
            errorNotification(
                t("error", {ns: "common"}),
                err.response?.data?.message || t("contractors:notifications.addContractorError")
            );
        } finally {
            setSubmitting(false);
        }
    };

    return (
        <CommonContractorForm
            initialValues={initialValues}
            validationSchema={validationSchema}
            onSubmit={handleSubmit}
        />
    );
};

export default AddContractorForm;