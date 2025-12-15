import React, {useMemo} from 'react';
import {Form, Formik, FormikHelpers, FormikProps, useFormikContext} from 'formik';
import * as Yup from 'yup';
import {Box, Button, Grid, GridItem, Heading, Separator, SimpleGrid} from '@chakra-ui/react';
import {CustomInputField, CustomTextAreaField} from '@/components/shared/CustomFormFields';
import {useTranslation} from "react-i18next";
import {BaseScaffoldingLogFormValues} from "@/types/scaffolding-log-types.ts";
import ContractorPicker from "@/components/contractor/ContractorPicker.tsx";
import ConstructionSitePicker from "@/components/constructionsite/ConstructionSitePicker.tsx";
import {useThemeColors} from "@/theme/theme-colors.ts";
import {getSelectedCompanyId} from "@/utils/company-utils.ts";
import {ContractorBaseDTO} from "@/types/contractor-types.ts";
import {ConstructionSiteBaseDTO} from "@/types/constrution-site-types.ts";
import {makeContractorSearchAdapter} from "@/search/contractor-search-adapter.ts";
import {makeConstructionSiteSearchAdapter} from "@/search/construction-site-search-adapter.ts";

interface CommonScaffoldingLogFormProps {
    initialValues: BaseScaffoldingLogFormValues;
    validationSchema: Yup.Schema<BaseScaffoldingLogFormValues>;
    onSubmit: (values: BaseScaffoldingLogFormValues, formikHelpers: FormikHelpers<BaseScaffoldingLogFormValues>) => Promise<void>;
    disabled?: boolean;
    hideSubmit?: boolean;
    innerRef?: React.Ref<FormikProps<BaseScaffoldingLogFormValues>>;
}

const FormContent: React.FC<{ disabled: boolean, hideSubmit: boolean }> = (({
                                                                                disabled,
                                                                                hideSubmit,
                                                                            }) => {
        const {t} = useTranslation(['common', 'scaffoldingLogs', 'constructionSites', 'contractors']);
        const themeColors = useThemeColors();
        const companyId: number = getSelectedCompanyId()!;

        const {
            values,
            setFieldValue,
            setFieldTouched,
            isValid,
            isSubmitting,
            dirty
        } = useFormikContext<BaseScaffoldingLogFormValues>();

        const contractorSearchFn = useMemo(
            () =>
                makeContractorSearchAdapter({
                    fixed: {companyId},
                    defaults: {page: 0, size: 10, sort: "id,asc"}
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
            void setFieldValue('contractor', contractor);
            void setFieldTouched('contractor', true, false);
        };

        const handleSiteChange = (site: ConstructionSiteBaseDTO | null) => {
            void setFieldValue('constructionSite', site);
            void setFieldTouched('constructionSite', true, false);
        };

        const formikContextAdapter = {
            current: {
                setFieldValue,
                setFieldTouched
            }
        } as unknown as React.RefObject<FormikProps<any>>;


        return (
            <Form>
                <SimpleGrid columns={3} gap={2}>

                    <Box gap={1}>
                        {/* Wiersz 1: Name 6/6*/}
                        <Grid templateColumns="repeat(6, 1fr)" gap={4}>
                            <GridItem colSpan={6}>
                                <CustomInputField
                                    name="name"
                                    label={t('scaffoldingLogs:name')}
                                    placeholder={t('scaffoldingLogs:name')}
                                    disabled={disabled}
                                />
                            </GridItem>
                        </Grid>

                        {/* Wiersz 2: Additional Info 6/6 */}
                        <Grid templateColumns="repeat(6, 1fr)" gap={4}>
                            <GridItem colSpan={6}>
                                <CustomTextAreaField
                                    name="additionalInfo"
                                    label={t('scaffoldingLogs:additionalInfo')}
                                    placeholder={t('scaffoldingLogs:additionalInfo')}
                                    disabled={disabled}
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
                                        formikRef={formikContextAdapter}
                                        selected={values.contractor || null}
                                        onSelectChange={handleContractorChange}
                                        searchFn={contractorSearchFn}
                                    />
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
                                    formikRef={formikContextAdapter}
                                    selected={values.constructionSite || null}
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
        )
    }
);

const CommonScaffoldingLogForm: React.FC<CommonScaffoldingLogFormProps> = ({
                                                                               initialValues,
                                                                               validationSchema,
                                                                               onSubmit,
                                                                               disabled = false,
                                                                               hideSubmit = false,
                                                                               innerRef,
                                                                           }) => {
    return (
        <Formik<BaseScaffoldingLogFormValues>
            initialValues={initialValues}
            validationSchema={validationSchema}
            onSubmit={onSubmit}
            enableReinitialize
            innerRef={innerRef}>
            <FormContent disabled={disabled} hideSubmit={hideSubmit}/>
        </Formik>
    );
};

export default CommonScaffoldingLogForm;
