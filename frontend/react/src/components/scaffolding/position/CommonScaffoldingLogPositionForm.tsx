import React, {useMemo} from 'react';
import {Form, Formik, FormikHelpers, FormikProps, useFormikContext} from 'formik';
import * as Yup from 'yup';
import {Box, Button, Grid, GridItem, Heading, Separator, SimpleGrid, StackSeparator, VStack} from '@chakra-ui/react';
import {CustomInputField, CustomSelectField, CustomTextAreaField} from '@/components/shared/CustomFormFields';
import {useTranslation} from "react-i18next";
import ContractorPicker from "@/components/contractor/ContractorPicker.tsx";
import {themeVars, useThemeColors} from "@/theme/theme-colors.ts";
import {getSelectedCompanyId} from "@/utils/company-utils.ts";
import {ContractorBaseDTO} from "@/types/contractor-types.ts";
import {makeContractorSearchAdapter} from "@/search/contractor-search-adapter.ts";
import {BaseScaffoldingLogPositionFormValues} from "@/types/scaffolding-log-position-types.ts";
import {ContactBaseDTO} from "@/types/contact-types.ts";
import {getScaffoldingTypeOptions} from "@/utils/scaffolding-type-util.ts";
import {getTechnicalProtocolStatusOptions} from "@/utils/technical-protocol-status-util.ts";
import ContactPicker from "@/components/contact/ContactPicker.tsx";
import {ScaffoldingType} from "@/enums/scaffolding-type-types-enum.ts";
import {TechnicalProtocolStatus} from "@/enums/technical-protocol-status-types-enum.ts";
import DimensionArrayField from "@/components/scaffolding/position/DimensionArrayField.tsx";

interface CommonScaffoldingLogPositionFormProps {
    initialValues: BaseScaffoldingLogPositionFormValues;
    validationSchema: Yup.Schema<BaseScaffoldingLogPositionFormValues>;
    onSubmit: (values: BaseScaffoldingLogPositionFormValues, formikHelpers: FormikHelpers<BaseScaffoldingLogPositionFormValues>) => Promise<void>;
    disabled?: boolean;
    hideSubmit?: boolean;
    innerRef?: React.Ref<FormikProps<BaseScaffoldingLogPositionFormValues>>;
}

const FormContent: React.FC<{ disabled: boolean, hideSubmit: boolean }> = (({
                                                                                disabled,
                                                                                hideSubmit,
                                                                            }) => {
        const {t} = useTranslation(['common', 'scaffoldingLogPositions', 'constructionSites', 'contractors', 'dimensions', 'workingTimes', 'units']);
        const themeColors = useThemeColors();
        const companyId: number = getSelectedCompanyId()!;
        const scaffoldingTypeOptions = React.useMemo(() => getScaffoldingTypeOptions(t), [t]);
        const technicalProtocolStatusOptions = React.useMemo(() => getTechnicalProtocolStatusOptions(t), [t]);


        const {
            values,
            setFieldValue,
            setFieldTouched,
            isValid,
            isSubmitting,
            dirty
        } = useFormikContext<BaseScaffoldingLogPositionFormValues>();

        const contractorSearchFn = useMemo(
            () =>
                makeContractorSearchAdapter({
                    fixed: {companyId},
                    defaults: {page: 0, size: 10, sort: "id,asc"}
                }),
            [companyId]
        );

        const handleContractorChange = (contractor: ContractorBaseDTO | null) => {
            console.log('Wybrano kontrahenta:', contractor);
            void setFieldValue('contractor', contractor);
            void setFieldTouched('contractor', true, false);

            // ZAWSZE wyczyść kontakt przy zmianie kontrahenta
            void setFieldValue('contractorContact', null);
            void setFieldTouched('contractorContact', false, false);
        };

        const handleContractorContactChange = (contact: ContactBaseDTO | null) => {
            // setSelectedContact(contact);
            void setFieldValue('contractorContact', contact);
            void setFieldTouched('contractorContact', true, false);
        };

        const handleScaffoldingUserChange = (contractor: ContractorBaseDTO | null) => {
            console.log('Wybrano użytkownika rusztowania:', contractor);
            void setFieldValue('scaffoldingUser', contractor);
            void setFieldTouched('scaffoldingUser', true, false);

            // ZAWSZE wyczyść kontakt przy zmianie kontrahenta
            void setFieldValue('scaffoldingUserContact', null);
            void setFieldTouched('scaffoldingUserContact', false, false);
        };

        const handleScaffoldingUserContactChange = (contact: ContactBaseDTO | null) => {
            void setFieldValue('scaffoldingUserContact', contact);
            void setFieldTouched('scaffoldingUserContact', true, false);
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
                    <Box gap={1} mt={2} p={2} borderWidth="2px" borderRadius="md" borderColor={themeColors.borderColor}>
                        {/* Wiersz 1: Scaffolding Number 3/6, Assembly Date*/}
                        <Grid templateColumns="repeat(6, 1fr)" gap={4}>
                            {/*wstawić w numer rusztowania wybraną datę (poprawić jeśli ktoś źle wpisał) po numerze np. 123/15/12/2025*/}
                            <GridItem colSpan={4}>
                                <CustomInputField
                                    name="scaffoldingNumber"
                                    label={t('scaffoldingLogPositions:scaffoldingNumber')}
                                    placeholder={t('scaffoldingLogPositions:scaffoldingNumber')}
                                    disabled={disabled}
                                />
                            </GridItem>
                            <GridItem colSpan={2}>
                                <CustomInputField
                                    name="assemblyDate"
                                    label={t('scaffoldingLogPositions:assemblyDate')}
                                    placeholder={t('scaffoldingLogPositions:assemblyDate')}
                                    disabled={disabled}
                                    type={"date"}
                                />
                            </GridItem>
                        </Grid>
                        {/* Wiersz 2: Assembly location 6/6 */}
                        <Grid templateColumns="repeat(6, 1fr)" gap={4}>
                            <GridItem colSpan={6}>
                                <CustomTextAreaField
                                    name="assemblyLocation"
                                    label={t('scaffoldingLogPositions:assemblyLocation')}
                                    placeholder={t('scaffoldingLogPositions:assemblyLocation')}
                                    disabled={disabled}
                                />
                            </GridItem>
                        </Grid>
                        {/* Wiersz 3: ScaffoldingType 3/6, TechnicalProtocolStatus 3/6 */}
                        <Grid templateColumns="repeat(6, 1fr)" gap={4}>
                            {/*wstawić w numer rusztowania wybraną datę (poprawić jeśli ktoś źle wpisał) po numerze np. 123/15/12/2025*/}
                            <GridItem colSpan={3}>
                                <CustomSelectField
                                    name="scaffoldingType"
                                    placeholder={t('scaffoldingLogPositions:scaffoldingType')}
                                    options={scaffoldingTypeOptions}
                                    bgColor={themeVars.bgColorPrimary}
                                    width={"3fr"}
                                    defaultValue={ScaffoldingType.BASIC}
                                />
                            </GridItem>
                            <GridItem colSpan={3}>
                                <CustomSelectField
                                    name="technicalProtocolStatus"
                                    placeholder={t('scaffoldingLogPositions:technicalProtocolStatus')}
                                    options={technicalProtocolStatusOptions}
                                    bgColor={themeVars.bgColorPrimary}
                                    width={"3fr"}
                                    defaultValue={TechnicalProtocolStatus.TO_BE_CREATED}
                                />
                            </GridItem>
                        </Grid>
                        {/*Wiersz 4: DismantlingNotificationDate 3/6, DismantlingDate 3/6*/}
                        <Separator orientation={"horizontal"} height={"100%"}>
                            <Grid templateColumns="repeat(6, 1fr)" gap={4}>
                                <GridItem colSpan={3}>
                                    <CustomInputField
                                        name="dismantlingNotificationDate"
                                        label={t('scaffoldingLogPositions:dismantlingNotificationDate')}
                                        placeholder={t('scaffoldingLogPositions:dismantlingNotificationDate')}
                                        disabled={disabled}
                                        type={"date"}
                                    />
                                </GridItem>
                                <GridItem colSpan={3}>
                                    <CustomInputField
                                        name="dismantlingDate"
                                        label={t('scaffoldingLogPositions:dismantlingDate')}
                                        placeholder={t('scaffoldingLogPositions:dismantlingDate')}
                                        disabled={disabled}
                                        type={"date"}
                                    />
                                </GridItem>
                            </Grid>
                        </Separator>
                    </Box>

                    {/*Box wyboru zleceniodawcy + kontaktu + użytkownika + kontaktu + opcja skopiowania zleceniodawcy do użytkownika jeśli taki sam*/}
                    <Box gap={1} mt={2} p={2} borderWidth="2px" borderRadius="md" borderColor={themeColors.borderColor}>
                        <Separator orientation={"vertical"} height={"100%"}>
                            <VStack separator={<StackSeparator/>}>
                                <Box ml={2} mr={2}>
                                    <Heading size={"xl"} color={themeColors.fontColor}>
                                        {t("scaffoldingLogPositions:contractor")}
                                    </Heading>
                                    <Box>
                                        <ContractorPicker
                                            formikRef={formikContextAdapter}
                                            selected={values.contractor || null}
                                            onSelectChange={handleContractorChange}
                                            searchFn={contractorSearchFn}
                                            showDetails={false}
                                        />
                                        {values.contractor && (
                                            <Box mt={4}>
                                                <ContactPicker
                                                    formikRef={formikContextAdapter}
                                                    selected={values.contractorContact}
                                                    onSelectChange={handleContractorContactChange}
                                                    contractorId={values.contractor!.id}
                                                    showDetails={false}
                                                />
                                            </Box>
                                        )}
                                    </Box>
                                </Box>
                                <Box ml={2} mr={2}>
                                    <Heading size={"xl"} color={themeColors.fontColor}>
                                        {t("scaffoldingLogPositions:scaffoldingUser")}
                                    </Heading>
                                    <Box>
                                        <ContractorPicker
                                            formikRef={formikContextAdapter}
                                            selected={values.scaffoldingUser || null}
                                            onSelectChange={handleScaffoldingUserChange}
                                            searchFn={contractorSearchFn}
                                            showDetails={false}
                                        />
                                        {values.scaffoldingUser && (
                                            <Box mt={4}>
                                                <ContactPicker
                                                    formikRef={formikContextAdapter}
                                                    selected={values.scaffoldingUserContact}
                                                    onSelectChange={handleScaffoldingUserContactChange}
                                                    contractorId={values.scaffoldingUser!.id}
                                                    showDetails={false}
                                                />
                                            </Box>
                                        )}
                                    </Box>
                                </Box>
                            </VStack>
                        </Separator>
                    </Box>
                    <DimensionArrayField/>
                    <Box>
                        <Separator orientation={"vertical"} height={"100%"}>

                        </Separator>
                    </Box>
                    {/*BOX na czas pracy (lista bo może być 5 osób x 4h i 3osoby x 3h - różne kombinacje
                     - stworzyć dynamiczną obsługę listy(dodawanie enterem + opcja usuń (krzyżyk po prawej stronie)
                     workingTimes: ScaffoldingLogPositionWorkingTimeBaseDTO[];*/}
                    <Box>
                        <Separator orientation={"vertical"} height={"100%"}>

                        </Separator>
                    </Box>
                </SimpleGrid>
                <SimpleGrid columns={1}>
                    <Box>
                        <Separator orientation={"horizontal"} width={"100%"}>
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
                        </Separator>
                    </Box>
                </SimpleGrid>
            </Form>
        )
    }
);

const CommonScaffoldingLogForm: React.FC<CommonScaffoldingLogPositionFormProps> = ({
                                                                                       initialValues,
                                                                                       validationSchema,
                                                                                       onSubmit,
                                                                                       disabled = false,
                                                                                       hideSubmit = false,
                                                                                       innerRef,
                                                                                   }) => {
    return (
        <Formik<BaseScaffoldingLogPositionFormValues>
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
