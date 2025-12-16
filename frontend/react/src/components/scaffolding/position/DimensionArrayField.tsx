import React, {useCallback, useEffect, useRef} from 'react';
import {FieldArray, useFormikContext} from 'formik';
import {Box, Button, Grid, GridItem, Heading} from '@chakra-ui/react';
import {useTranslation} from 'react-i18next';
import {useThemeColors} from '@/theme/theme-colors';
import {CustomSelectField} from '@/components/shared/CustomFormFields';
import {
    BaseScaffoldingLogPositionFormValues,
    ScaffoldingLogPositionDimensionBaseDTO
} from '@/types/scaffolding-log-position-types.ts';
import {ScaffoldingOperationType} from '@/enums/scaffolding-operation-types-enum.ts';
import {DimensionType} from '@/enums/dimension-types-enum.ts';
import {getScaffoldingOperationTypeOptions} from "@/utils/scaffolding-operation-type-util.ts";
import {getDimensionTypeOptions} from "@/utils/dimension-type-util.ts";
import {UnitBaseDTO} from '@/types/unit-types.ts';
import {CustomNumberInput} from "@/components/shared/CustomNumberInput.tsx";

const DimensionsArrayField: React.FC = () => {
    const {t} = useTranslation(['common', 'scaffoldingLogPositions', 'dimensionTypes', 'scaffoldingOperationTypes', 'dimensionDescriptions']);
    const themeColors = useThemeColors();
    const {values, setFieldValue} = useFormikContext<BaseScaffoldingLogPositionFormValues>();

    const prevAssemblyDateRef = useRef(values.assemblyDate);
    const operationOptions = getScaffoldingOperationTypeOptions(t);
    const dimensionOptions = getDimensionTypeOptions(t);

    const createEmptyDimension = useCallback((): ScaffoldingLogPositionDimensionBaseDTO => ({
        height: "",
        width: "",
        length: "",
        dimensionType: DimensionType.BASIC_STRUCTURE,
        operationType: ScaffoldingOperationType.ASSEMBLY,
        assemblyDate: values.assemblyDate || "",
        dismantlingDate: "",
        unit: null as unknown as UnitBaseDTO,
    }), [values.assemblyDate]);

    useEffect(() => {
        if (!values.dimensions || values.dimensions.length === 0) {
            // Dodano 'void' aby uciszyć 'Promise ignored'
            void setFieldValue('dimensions', [createEmptyDimension()]);
        }
    }, [createEmptyDimension, setFieldValue, values.dimensions]);

    useEffect(() => {
        const dimensions = values.dimensions || [];
        if (dimensions.length === 0) return;

        const lastItem = dimensions[dimensions.length - 1];
        const isLastItemFilled = lastItem &&
            lastItem.height !== "" &&
            lastItem.width !== "" &&
            lastItem.length !== "";

        if (isLastItemFilled) {
            void setFieldValue('dimensions', [...dimensions, createEmptyDimension()]);
        }
    }, [values.dimensions, setFieldValue, createEmptyDimension]);

    // 3. Synchronizacja dat
    useEffect(() => {
        if (prevAssemblyDateRef.current !== values.assemblyDate) {
            prevAssemblyDateRef.current = values.assemblyDate;

            const newMainDate = values.assemblyDate || "";
            const currentDimensions = values.dimensions || [];

            const updatedDimensions = currentDimensions.map(dim => {
                if (dim.operationType === ScaffoldingOperationType.DISMANTLING) {
                    return {...dim, dismantlingDate: newMainDate};
                } else {
                    return {...dim, assemblyDate: newMainDate};
                }
            });

            // Dodano 'void'
            void setFieldValue('dimensions', updatedDimensions);
        }
    }, [values.assemblyDate, values.dimensions, setFieldValue]);

    return (
        <FieldArray name="dimensions">
            {({remove}) => (
                <Box mt={2} p={2}>
                    <Heading size="md" mb={4} color={themeColors.fontColor} textAlign={"center"}>
                        {t('scaffoldingLogPositions:dimensions')}
                    </Heading>

                    <Grid templateColumns="repeat(13, 1fr)" gap={2} mb={2}>
                        {/* Nagłówki bez zmian */}
                        <GridItem colSpan={2}><Heading
                            size="xs" textAlign={"center"}>{t('dimensionDescriptions:length')}</Heading></GridItem>
                        <GridItem colSpan={2}><Heading size="xs"
                                                       textAlign={"center"}>{t('dimensionDescriptions:width')}</Heading></GridItem>
                        <GridItem colSpan={2}><Heading
                            size="xs" textAlign={"center"}>{t('dimensionDescriptions:height')}</Heading></GridItem>
                        <GridItem colSpan={3}><Heading size="xs"
                                                       textAlign={"center"}>{t('dimensionTypes:type')}</Heading></GridItem>
                        <GridItem colSpan={3}><Heading
                            size="xs"
                            textAlign={"center"}>{t('scaffoldingOperationTypes:operation')}</Heading></GridItem>
                        <GridItem colSpan={1}></GridItem>
                    </Grid>

                    {/* Poprawka: Zmieniono 'dimension' na '_' (nieużywany argument) */}
                    {values.dimensions && values.dimensions.map((_, index) => {

                        const handleOperationChange = (newOperation: ScaffoldingOperationType) => {
                            // Dodano 'void' dla wszystkich setFieldValue
                            void setFieldValue(`dimensions.${index}.operationType`, newOperation);

                            const mainDate = values.assemblyDate || "";

                            if (newOperation === ScaffoldingOperationType.DISMANTLING) {
                                void setFieldValue(`dimensions.${index}.assemblyDate`, "");
                                void setFieldValue(`dimensions.${index}.dismantlingDate`, mainDate);
                            } else {
                                void setFieldValue(`dimensions.${index}.dismantlingDate`, "");
                                void setFieldValue(`dimensions.${index}.assemblyDate`, mainDate);
                            }
                        };

                        return (
                            <Grid key={index} templateColumns="repeat(13, 1fr)" gap={2} mb={2} alignItems="center">
                                {/* Pola bez zmian */}
                                <GridItem colSpan={2}>
                                    <CustomNumberInput
                                        name={`dimensions.${index}.length`}
                                        placeholder="0.00"
                                    />
                                </GridItem>
                                <GridItem colSpan={2}>
                                    <CustomNumberInput
                                        name={`dimensions.${index}.width`}
                                        placeholder="0.00"
                                    />
                                </GridItem>
                                <GridItem colSpan={2}>
                                    <CustomNumberInput
                                        name={`dimensions.${index}.height`}
                                        placeholder="0.00"
                                    />
                                </GridItem>
                                <GridItem colSpan={3}>
                                    <CustomSelectField
                                        name={`dimensions.${index}.dimensionType`}
                                        options={dimensionOptions}
                                        defaultValue={DimensionType.BASIC_STRUCTURE}
                                    />
                                </GridItem>
                                <GridItem colSpan={3}>
                                    <CustomSelectField
                                        name={`dimensions.${index}.operationType`}
                                        options={operationOptions}
                                        defaultValue={ScaffoldingOperationType.ASSEMBLY}
                                        onValueChange={(val) => handleOperationChange(val)}
                                    />
                                </GridItem>
                                <GridItem colSpan={1}>
                                    {values.dimensions.length > 1 && (
                                        <Button
                                            size="sm"
                                            colorPalette="red"
                                            variant="ghost"
                                            onClick={() => remove(index)}
                                        >
                                            X
                                        </Button>
                                    )}
                                </GridItem>
                            </Grid>
                        );
                    })}
                </Box>
            )}
        </FieldArray>
    );
};

export default DimensionsArrayField;
