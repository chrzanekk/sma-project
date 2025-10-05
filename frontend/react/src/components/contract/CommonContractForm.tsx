import React, {useEffect, useMemo, useRef, useState} from 'react';
import {Form, Formik, FormikHelpers, FormikProps, useFormikContext} from 'formik';
import * as Yup from 'yup';
import {Box, Button, Grid, GridItem, Heading, Separator, SimpleGrid} from '@chakra-ui/react';
import {CustomInputField, CustomSelectField} from '@/components/shared/CustomFormFields';
import {useTranslation} from "react-i18next";
import {BaseContractFormValues} from "@/types/contract-types.ts";
import {getCurrencyOptions} from "@/types/currency-types.ts";
import ContractorPicker from "@/components/contractor/ContractorPicker.tsx";
import ContactPicker from "@/components/contact/ContactPicker.tsx";
import {ContractorBaseDTO} from "@/types/contractor-types.ts";
import {ConstructionSiteBaseDTO} from "@/types/constrution-site-types.ts";
import {ContactBaseDTO} from "@/types/contact-types.ts";
import {makeContractorSearchAdapter} from "@/search/contractor-search-adapter.ts";
import {makeConstructionSiteSearchAdapter} from "@/search/construction-site-search-adapter.ts";
import ConstructionSitePicker from "@/components/constructionsite/ConstructionSitePicker.tsx";
import {useThemeColors} from "@/theme/theme-colors.ts";
import {getSelectedCompanyId} from "@/utils/company-utils.ts";

interface CommonContractFormProps {
    initialValues: BaseContractFormValues;
    validationSchema: Yup.Schema<BaseContractFormValues>;
    onSubmit: (values: BaseContractFormValues, formikHelpers: FormikHelpers<BaseContractFormValues>) => Promise<void>;
    disabled?: boolean;
    hideSubmit?: boolean;
    innerRef?: React.Ref<FormikProps<BaseContractFormValues>>;
}

const FormContent: React.FC<{ disabled: boolean; hideSubmit: boolean }> = ({
                                                                               disabled,
                                                                               hideSubmit
                                                                           }) => {
    const {t} = useTranslation(['common', 'contracts', 'constructionSites', 'contractors', 'contacts']);
    const currencyOptions = getCurrencyOptions();
    const themeColors = useThemeColors();
    const companyId: number = getSelectedCompanyId()!;

    const {
        values,
        setFieldValue,
        setFieldTouched,
        isValid,
        isSubmitting,
        dirty
    } = useFormikContext<BaseContractFormValues>();

    const formikRef = useRef<FormikProps<BaseContractFormValues>>(null);

    const [selectedContractor, setSelectedContractor] = useState<ContractorBaseDTO | null>(null);
    const [selectedSite, setSelectedSite] = useState<ConstructionSiteBaseDTO | null>(null);
    const [selectedContact, setSelectedContact] = useState<ContactBaseDTO | null>(null);

    // Śledź poprzednie ID kontraktu, żeby wiedzieć kiedy załadowano nowy kontrakt
    const prevContractIdRef = useRef<number | undefined>(undefined);

    // POPRAWIONY useEffect: Synchronizuj TYLKO gdy załadowano NOWY kontrakt (zmiana ID)
    useEffect(() => {
        const currentContractId = values.id;

        // Jeśli ID kontraktu się zmieniło (załadowano nowy kontrakt do edycji)
        if (currentContractId !== prevContractIdRef.current) {
            prevContractIdRef.current = currentContractId;

            // Ustaw pickery na wartości z initialValues
            setSelectedContractor(values.contractor || null);
            setSelectedSite(values.constructionSite || null);
            setSelectedContact(values.contact || null);
        }
    }, [values.id, values.contractor, values.constructionSite, values.contact]);

    const contractorSearchFn = useMemo(
        () =>
            makeContractorSearchAdapter({
                fixed: {companyId},
                defaults: {page: 0, size: 10, sort: "id,asc"},
            }),
        [companyId]
    );

    const constructionSiteSearchFn = useMemo(
        () => makeConstructionSiteSearchAdapter({
            fixed: {companyId},
            defaults: {page: 0, size: 10, sort: "id,asc"},
        }),
        [companyId]
    );

    const handleContractorChange = (contractor: ContractorBaseDTO | null) => {
        console.log('Wybrano kontrahenta:', contractor);
        setSelectedContractor(contractor);
        void setFieldValue('contractor', contractor);
        void setFieldTouched('contractor', true, false);

        // ZAWSZE wyczyść kontakt przy zmianie kontrahenta
        setSelectedContact(null);
        void setFieldValue('contact', null);
        void setFieldTouched('contact', false, false);
    };

    const handleContactChange = (contact: ContactBaseDTO | null) => {
        setSelectedContact(contact);
        void setFieldValue('contact', contact);
        void setFieldTouched('contact', true, false);
    };

    const handleSiteChange = (site: ConstructionSiteBaseDTO | null) => {
        setSelectedSite(site);
        void setFieldValue('constructionSite', site);
        void setFieldTouched('constructionSite', true, false);
    };

    return (
        <Form>
            <SimpleGrid columns={3} gap={2}>
                <Box gap={1}>
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
                </Box>

                <Box>
                    <Separator orientation={"vertical"} height={"100%"}>
                        <Box ml={2} mr={2}>
                            <Heading size={"xl"} color={themeColors.fontColor}>
                                {t("contractors:contractor")}
                            </Heading>
                            <Box>
                                <ContractorPicker
                                    formikRef={formikRef}
                                    selected={selectedContractor}
                                    onSelectChange={handleContractorChange}
                                    searchFn={contractorSearchFn}
                                />

                                {selectedContractor && (
                                    <Box mt={4}>
                                        <ContactPicker
                                            formikRef={formikRef}
                                            selected={selectedContact}
                                            onSelectChange={handleContactChange}
                                            contractorId={selectedContractor.id}
                                        />
                                    </Box>
                                )}
                            </Box>
                        </Box>
                    </Separator>
                </Box>

                <Box>
                    <Separator orientation={"vertical"} height={"100%"}>
                        <Box ml={2} mr={2}>
                            <Heading size={"xl"} color={themeColors.fontColor}>
                                {t("constructionSites:constructionSite")}
                            </Heading>
                            <ConstructionSitePicker
                                formikRef={formikRef}
                                selected={selectedSite}
                                onSelectChange={handleSiteChange}
                                searchFn={constructionSiteSearchFn}
                            />
                        </Box>
                    </Separator>
                </Box>
            </SimpleGrid>

            {!hideSubmit && (
                <Grid templateColumns="repeat(6, 1fr)" gap={4} mt={4}>
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
        </Form>
    );
};

const CommonContractForm: React.FC<CommonContractFormProps> = ({
                                                                   initialValues,
                                                                   validationSchema,
                                                                   onSubmit,
                                                                   disabled = false,
                                                                   hideSubmit = false,
                                                                   innerRef,
                                                               }) => {
    return (
        <Formik<BaseContractFormValues>
            initialValues={initialValues}
            validationSchema={validationSchema}
            onSubmit={onSubmit}
            enableReinitialize
            innerRef={innerRef}
        >
            <FormContent disabled={disabled} hideSubmit={hideSubmit}/>
        </Formik>
    );
};

export default CommonContractForm;
