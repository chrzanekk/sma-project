import React, {useEffect, useState} from "react";
import {useTranslation} from "react-i18next";
import {getSelectedCompany} from "@/utils/company-utils.ts";
import {
    ConstructionSiteDTO,
    ConstructionSiteFormValues,
    FetchableConstructionSiteDTO
} from "@/types/constrution-site-types.ts";
import {Country, getCountryOptions} from "@/types/country-types.ts";
import {getConstructionSiteById, updateConstructionSite} from "@/services/construction-site-service.ts";
import {getConstructionSiteValidationSchema} from "@/validation/constructionSiteValidationSchema.ts";
import {errorNotification, successNotification} from "@/notifications/notifications.ts";
import {formatMessage} from "@/notifications/FormatMessage.tsx";
import CommonConstructionSiteForm from "@/components/constructionsite/CommonConstructionSiteForm.tsx";

interface EditConstructionSiteFormProps {
    onSuccess: () => void;
    constructionSiteId: number;
    hideSubmit?: boolean;
}

const EditContractorForm: React.FC<EditConstructionSiteFormProps> = ({
                                                                         onSuccess,
                                                                         constructionSiteId,
                                                                         hideSubmit = false
                                                                     }) => {
    const {t} = useTranslation(['common', 'constructionSites', 'errors'])
    const currentCompany = getSelectedCompany();
    const defaultValues: ConstructionSiteFormValues = {
        id: 0,
        name: '',
        shortName: '',
        address: '',
        country: '',
        code: ''
    }

    const [initialValues, setInitialValues] = useState<ConstructionSiteFormValues>(defaultValues);
    const [isLoading, setIsLoading] = useState<boolean>(true);
    const countryOptions = getCountryOptions(t);

    useEffect(() => {
        const fetchConstructionSite = async () => {
            setIsLoading(true);
            try {
                const constructionSite: FetchableConstructionSiteDTO = await getConstructionSiteById(constructionSiteId);
                setInitialValues({
                    id: constructionSite.id,
                    name: constructionSite.name,
                    shortName: constructionSite.shortName,
                    address: constructionSite.address,
                    country: constructionSite.country.code,
                    code: constructionSite.code
                })
            } catch (err) {
                console.error("Error fetching constructionSite: ", err);
            } finally {
                setIsLoading(false);
            }
        };
        fetchConstructionSite().catch();
    }, [constructionSiteId]);

    const validationSchema = getConstructionSiteValidationSchema(t, countryOptions);

    const handleSubmit = async (
        values: ConstructionSiteFormValues,
        {setSubmitting}: { setSubmitting: (isSubmitting: boolean) => void }
    ) => {
        setSubmitting(true);
        try {
            const mappedConstructionSite: ConstructionSiteDTO = {
                ...values,
                id: constructionSiteId,
                country: Country.fromCode(values.country),
                company: currentCompany!
            };
            await updateConstructionSite(mappedConstructionSite);
            successNotification(
                t("success", {ns: "common"}),
                formatMessage("notifications.editConstructionSiteSuccess", {name: values.name}, "constructionSites")
            );
            onSuccess();
        } catch (err: any) {
            console.error(err);
            errorNotification(
                t("common:error"),
                err.response?.data?.message || t("constructionSites:notifications.editConstructionSiteError")
            );
        } finally {
            setSubmitting(false);
        }
    }

    if (isLoading) return <div>Loading...</div>;

    return (
        <CommonConstructionSiteForm
            initialValues={initialValues}
            validationSchema={validationSchema}
            onSubmit={handleSubmit}
            hideSubmit={hideSubmit}
        />

    )
}

export default EditContractorForm;