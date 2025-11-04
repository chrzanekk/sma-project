import {BaseEmployeeFormValues} from "@/types/employee-types.ts";
import {Formik, FormikHelpers} from "formik";
import React, {useMemo} from "react";
import {useTranslation} from "react-i18next";
import {Button, Grid, GridItem, Stack, Text} from "@chakra-ui/react";
import {CustomInputField} from "@/components/shared/CustomFormFields.tsx";
import {PositionBaseDTO} from "@/types/position-types.ts";
import PositionSearchWithSelect from "@/components/position/PositionSearchWithSelect.tsx";
import {makePositionSearchAdapter} from "@/search/position-search-adapter.ts";
import {getSelectedCompanyId} from "@/utils/company-utils.ts";
import {useThemeColors} from "@/theme/theme-colors.ts";

interface CommonEmployeeFormProps {
    initialValues: BaseEmployeeFormValues;
    validationSchema: Yup.Schema<BaseEmployeeFormValues>;
    onSubmit: (values: BaseEmployeeFormValues, formikHelpers: FormikHelpers<BaseEmployeeFormValues>) => Promise<void>;
    disabled?: boolean;
    hideSubmit?: boolean;
    innerRef?: React.Ref<FormikProps<BaseEmployeeFormValues>>;
}

const CommonEmployeeForm: React.FC<CommonEmployeeFormProps> = (({
                                                                    initialValues,
                                                                    validationSchema,
                                                                    onSubmit,
                                                                    disabled = false,
                                                                    hideSubmit = false,
                                                                    innerRef
                                                                }) => {
    const {t} = useTranslation(['common', 'employees','positions'])
    const currentCompanyId = getSelectedCompanyId();
    const positionSearchFn = useMemo(
        () => makePositionSearchAdapter({
            fixed: {currentCompanyId},
            defaults: {page: 0, size: 10, sort: "name,asc"},
        }),
        [currentCompanyId]
    );

    return (
        <Formik<BaseEmployeeFormValues>
            initialValues={initialValues}
            validationSchema={validationSchema}
            onSubmit={onSubmit}
            enableReinitialize
            innerRef={innerRef}>
            {({isValid, isSubmitting, dirty}) => (
                <Stack gap={4}>
                    <CustomInputField name={"firstName"}
                                      label={t('employees:firstName')}
                                      placeholder={t('employees:firstName')}
                                      disabled={disabled}/>
                    <CustomInputField name={"lastName"}
                                      label={t('employees:lastName')}
                                      placeholder={t('employees:lastName')}
                                      disabled={disabled}/>
                    <CustomInputField name={"hourRate"}
                                      label={t('employees:hourRate')}
                                      placeholder={t('employees:hourRate')}
                                      disabled={disabled}/>
                    {/*TODO fix choosing position for employee*/}
                    <div>
                        <Text fontSize="sm"
                              fontWeight="bold"
                              mb="1"
                              color={useThemeColors().fontColor}
                              textAlign={"center"}
                        >
                            {t('positions:position')}
                        </Text>
                        <PositionSearchWithSelect
                            value={initialValues.position}
                            onSelect={(position: PositionBaseDTO | null) => {
                                setFieldValue("position", position).catch();
                                setFieldTouched("position", true, false).catch();
                            }}
                            searchFn={positionSearchFn}
                            placeholder={t('positions:position')}
                            size={"md"}
                            width={"100%"}
                        />
                        {touched.position && errors.position && (
                            <Text color="red.500" fontSize="xs" mt="1">
                                {errors.position as string}
                            </Text>
                        )}
                    </div>
                    {!hideSubmit && (<Grid templateColumns="repeat(3, 1fr)" gap={4}>
                        <GridItem colSpan={3} textAlign="center">
                            <Button
                                disabled={!isValid || isSubmitting || !dirty}
                                type="submit"
                                colorPalette="green"
                                width="400px"
                            >
                                {t('save', {ns: "common"})}
                            </Button>
                        </GridItem>
                    </Grid>)}
                </Stack>
            )}
        </Formik>
    )
})
export default CommonEmployeeForm;