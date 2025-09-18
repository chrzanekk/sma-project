import {ConstructionSiteDTO, ConstructionSiteFormValues} from "@/types/constrution-site-types.ts";
import React from "react";
import {useTranslation} from "react-i18next";
import {Country, getCountryOptions} from "@/types/country-types.ts";
import {getSelectedCompany} from "@/utils/company-utils.ts";
import {getConstructionSiteValidationSchema} from "@/validation/constructionSiteValidationSchema.ts";
import {errorNotification, successNotification} from "@/notifications/notifications.ts";
import {formatMessage} from "@/notifications/FormatMessage.tsx";
import {addConstructionSite} from "@/services/construction-site-service.ts";
import CommonConstructionSiteForm from "@/components/constructionsite/CommonConstructionSiteForm.tsx";

interface AddConstructionSiteFormProps {
    onSuccess: (data: ConstructionSiteDTO) => void;
}

const AddConstructionSiteForm: React.FC<AddConstructionSiteFormProps> = ({onSuccess}) => {
    const {t} = useTranslation(['common', 'constructionSites']);
    const countryOptions = getCountryOptions(t);
    const currentCompany = getSelectedCompany();

    const initialValues: ConstructionSiteFormValues = {
        name: "",
        address: "",
        country: countryOptions[0].value,
        shortName: "",
        code: ""
    }

    const validationSchema = getConstructionSiteValidationSchema(t, countryOptions);

    const handleSubmit = async (
        values: ConstructionSiteFormValues,
        {setSubmitting}: { setSubmitting: (isSubmitting: boolean) => void }
    ) => {
        setSubmitting(true);
        try {
            const mappedConstructionSite: ConstructionSiteDTO = {
                ...values,
                country: Country.fromCode(values.country),
                company: currentCompany!
            };
            await addConstructionSite(mappedConstructionSite);
            successNotification(
                t("success", {ns: "common"}),
                formatMessage("notifications.addConstructionSiteSuccess", {name: values.name}, "constructionSites")
            );
            onSuccess(mappedConstructionSite);
        } catch (err: any) {
            console.error(err);
            errorNotification(
                t("error", {ns: "common"}),
                err.response?.data?.message || t("contractors:notifications.addConstructionSiteError")
            );
        } finally {
            setSubmitting(false);
        }
    };

    return (
        <CommonConstructionSiteForm
            initialValues={initialValues}
            validationSchema={validationSchema}
            onSubmit={handleSubmit}
        />
    )
}

export default AddConstructionSiteForm;