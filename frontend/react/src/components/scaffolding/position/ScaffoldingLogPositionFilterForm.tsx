import * as Yup from 'yup';
import {useTranslation} from "react-i18next";
import {Form, Formik, FormikHelpers} from "formik";
import {Button, Flex} from "@chakra-ui/react";
import {CustomInputField, CustomInputFilterField, CustomSelectField} from "@/components/shared/CustomFormFields.tsx";
import React from "react";
import {themeVars, useThemeColors} from "@/theme/theme-colors.ts";
import {getScaffoldingTypeOptions} from "@/utils/scaffolding-type-util.ts";
import {getTechnicalProtocolStatusOptions} from "@/utils/technical-protocol-status-util.ts";

interface FilterValues {
    scaffoldingNumberContains?: string;
    assemblyLocationContains?: string;
    assemblyDateGreaterOrEqual?: string;
    assemblyDateLessOrEqual?: string;
    dismantlingDateGreaterOrEqual?: string;
    dismantlingDateLessOrEqual?: string;
    dismantlingNotificationDateGreaterOrEqual?: string;
    dismantlingNotificationDateLessOrEqual?: string;
    technicalProtocolStatus?: string;
    scaffoldingType?: string;
    scaffoldingFullDimensionGreaterOrEqual?: string;
    scaffoldingFullDimensionLessOrEqual?: string;
    scaffoldingLogNameContains?: string;
    scaffoldingLogAdditionalInfoContains?: string;
    contractorNameContains?: string;
    contractorContactNameContains?: string;
    scaffoldingUserNameContains?: string;
    scaffoldingUserContactNameContains?: string;
}

const validationSchema = Yup.object({
    scaffoldingNumberContains: Yup.string(),
    assemblyLocationContains: Yup.string(),
    assemblyDateGreaterOrEqual: Yup.string(),
    assemblyDateLessOrEqual: Yup.string(),
    dismantlingDateGreaterOrEqual: Yup.string(),
    dismantlingDateLessOrEqual: Yup.string(),
    dismantlingNotificationDateGreaterOrEqual: Yup.string(),
    dismantlingNotificationDateLessOrEqual: Yup.string(),
    scaffoldingFullDimensionGreaterOrEqual: Yup.string(),
    scaffoldingFullDimensionLessOrEqual: Yup.string(),
    scaffoldingLogNameContains: Yup.string(),
    scaffoldingLogAdditionalInfoContains: Yup.string(),
    contractorNameContains: Yup.string(),
    contractorContactNameContains: Yup.string(),
    scaffoldingUserNameContains: Yup.string(),
    scaffoldingUserContactNameContains: Yup.string(),
    technicalProtocolStatus: Yup.string(),
    scaffoldingType: Yup.string(),
});

interface FilterProps {
    onSubmit: (values: FilterValues) => void;
    isFullLogPositionList?: boolean;
}

const ScaffoldingLogPositionFilterForm: React.FC<FilterProps> = ({
                                                                     onSubmit,
                                                                     isFullLogPositionList = false
                                                                 }) => {
    const {t} = useTranslation(['common', 'scaffoldingLogPositions']);
    const themeColors = useThemeColors();
    const scaffoldingTypeOptions = React.useMemo(() => getScaffoldingTypeOptions(t), [t]);
    const technicalProtocolStatusOptions = React.useMemo(() => getTechnicalProtocolStatusOptions(t), [t]);

    return (
        <Formik<FilterValues>
            initialValues={{
                scaffoldingNumberContains: '',
                assemblyLocationContains: '',
                assemblyDateGreaterOrEqual: '',
                assemblyDateLessOrEqual: '',
                dismantlingDateGreaterOrEqual: '',
                dismantlingDateLessOrEqual: '',
                dismantlingNotificationDateGreaterOrEqual: '',
                dismantlingNotificationDateLessOrEqual: '',
                scaffoldingFullDimensionGreaterOrEqual: '',
                scaffoldingFullDimensionLessOrEqual: '',
                scaffoldingLogNameContains: '',
                scaffoldingLogAdditionalInfoContains: '',
                contractorNameContains: '',
                contractorContactNameContains: '',
                scaffoldingUserNameContains: '',
                scaffoldingUserContactNameContains: '',
                technicalProtocolStatus: '',
                scaffoldingType: '',
            }}
            validationSchema={validationSchema}
            onSubmit={(values, {setSubmitting}: FormikHelpers<FilterValues>) => {
                setSubmitting(false);
                onSubmit(values);
            }}>
            {({handleSubmit, resetForm}) => {
                return (
                    <Form onSubmit={handleSubmit}>
                        <Flex
                            gap={1}
                            px={1}
                            py={1}
                            justifyContent={"center"}
                            flexWrap={"wrap"}
                        >
                            <CustomInputFilterField name="scaffoldingNumberContains"
                                                    placeholder={t('scaffoldingLogPositions:scaffoldingNumberContains')}/>
                            <CustomInputFilterField name="assemblyLocationContains"
                                                    placeholder={t('scaffoldingLogPositions:assemblyLocationContains')}/>
                            <CustomInputFilterField name="scaffoldingFullDimensionGreaterOrEqual"
                                                    placeholder={t('scaffoldingLogPositions:scaffoldingFullDimensionGreaterOrEqual')}/>
                            <CustomInputFilterField name="scaffoldingFullDimensionLessOrEqual"
                                                    placeholder={t('scaffoldingLogPositions:scaffoldingFullDimensionLessOrEqual')}/>
                            <CustomInputFilterField name="contractorNameContains"
                                                    placeholder={t('scaffoldingLogPositions:contractorNameContains')}/>
                            <CustomInputFilterField name="contractorContactNameContains"
                                                    placeholder={t('scaffoldingLogPositions:contractorContactNameContains')}/>
                            <CustomInputFilterField name="scaffoldingUserNameContains"
                                                    placeholder={t('scaffoldingLogPositions:scaffoldingUserNameContains')}/>
                            <CustomInputFilterField name="scaffoldingUserContactNameContains"
                                                    placeholder={t('scaffoldingLogPositions:scaffoldingUserContactNameContains')}/>
                        </Flex>
                        <Flex
                            gap={1}
                            px={1}
                            py={1}
                            justifyContent={"center"}
                            flexWrap={"wrap"}
                        >
                            <CustomInputField name="assemblyDateGreaterOrEqual"
                                              placeholder={t('scaffoldingLogPositions:assemblyDateGreaterOrEqual')}
                                              label={t('scaffoldingLogPositions:assemblyDateGreaterOrEqual')}
                                              type="date"
                                              width={"150px"}
                                              fontSize={"2xs"}
                                              inputBGColor={themeColors.bgColorSecondary}
                            />
                            <CustomInputField name="assemblyDateLessOrEqual"
                                              placeholder={t('scaffoldingLogPositions:assemblyDateLessOrEqual')}
                                              label={t('scaffoldingLogPositions:assemblyDateLessOrEqual')}
                                              type="date"
                                              width={"150px"}
                                              fontSize={"2xs"}
                                              inputBGColor={themeColors.bgColorSecondary}
                            />
                            <CustomInputField name="dismantlingDateGreaterOrEqual"
                                              placeholder={t('scaffoldingLogPositions:dismantlingDateGreaterOrEqual')}
                                              label={t('scaffoldingLogPositions:dismantlingDateGreaterOrEqual')}
                                              type="date"
                                              width={"150px"}
                                              fontSize={"2xs"}
                                              inputBGColor={themeColors.bgColorSecondary}
                            />
                            <CustomInputField name="dismantlingDateLessOrEqual"
                                              placeholder={t('scaffoldingLogPositions:dismantlingDateLessOrEqual')}
                                              label={t('scaffoldingLogPositions:dismantlingDateLessOrEqual')}
                                              type="date"
                                              width={"150px"}
                                              fontSize={"2xs"}
                                              inputBGColor={themeColors.bgColorSecondary}
                            />
                            <CustomInputField name="dismantlingNotificationDateGreaterOrEqual"
                                              placeholder={t('scaffoldingLogPositions:dismantlingNotificationDateGreaterOrEqual')}
                                              label={t('scaffoldingLogPositions:dismantlingNotificationDateGreaterOrEqual')}
                                              type="date"
                                              width={"150px"}
                                              fontSize={"2xs"}
                                              inputBGColor={themeColors.bgColorSecondary}
                            />
                            <CustomInputField name="dismantlingNotificationDateLessOrEqual"
                                              placeholder={t('scaffoldingLogPositions:dismantlingNotificationDateLessOrEqual')}
                                              label={t('scaffoldingLogPositions:dismantlingNotificationDateLessOrEqual')}
                                              type="date"
                                              width={"150px"}
                                              fontSize={"2xs"}
                                              inputBGColor={themeColors.bgColorSecondary}
                            />
                            <CustomSelectField
                                name="technicalProtocolStatus"
                                placeholder={t('scaffoldingLogPositions:technicalProtocolStatus')}
                                options={technicalProtocolStatusOptions}
                                bgColor={themeVars.bgColorSecondary}
                                width={"150px"}
                            />
                            <CustomSelectField
                                name="scaffoldingType"
                                placeholder={t('scaffoldingLogPositions:scaffoldingType')}
                                options={scaffoldingTypeOptions}
                                bgColor={themeVars.bgColorSecondary}
                                width={"150px"}
                            />
                        </Flex>
                        {isFullLogPositionList && (
                            <Flex
                                gap={1}
                                px={1}
                                py={1}
                                justifyContent={"center"}
                                flexWrap={"wrap"}
                            >
                                <CustomInputFilterField name="scaffoldingLogNameContains"
                                                        placeholder={t('scaffoldingLogPositions:scaffoldingLogNameContains')}/>
                                <CustomInputFilterField name="scaffoldingLogAdditionalInfoContains"
                                                        placeholder={t('scaffoldingLogPositions:scaffoldingLogAdditionalInfoContains')}/>
                            </Flex>
                        )}
                        <Flex gap={1} justifyContent={"center"}>
                            <Button type="submit" colorPalette="blue"
                                    size={"2xs"}>
                                {t('search')}
                            </Button>
                            <Button
                                type="button"
                                colorPalette="orange"
                                size={"2xs"}
                                onClick={() => {
                                    resetForm();
                                    onSubmit({});
                                }}
                            >
                                {t('common:clearFilters')}
                            </Button>
                        </Flex>
                    </Form>
                )
            }}
        </Formik>
    )
}

export default ScaffoldingLogPositionFilterForm;