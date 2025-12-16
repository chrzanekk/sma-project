// src/components/scaffolding/position/WorkingTimesArrayField.tsx
import React, { useCallback, useEffect } from "react";
import { FieldArray, useFormikContext } from "formik";
import { Box, Button, Grid, GridItem, Heading } from "@chakra-ui/react";
import { useTranslation } from "react-i18next";
import { useThemeColors } from "@/theme/theme-colors";
import { CustomNumberInput } from "@/components/shared/CustomNumberInput";
import { CustomSelectField } from "@/components/shared/CustomFormFields";
import {
    BaseScaffoldingLogPositionFormValues,
    ScaffoldingLogPositionWorkingTimeBaseDTO,
} from "@/types/scaffolding-log-position-types";
import { ScaffoldingOperationType } from "@/enums/scaffolding-operation-types-enum";
import { getScaffoldingOperationTypeOptions } from "@/utils/scaffolding-operation-type-util";
import { UnitBaseDTO } from "@/types/unit-types";

const WorkingTimesArrayField: React.FC = () => {
    const { t } = useTranslation([
        "common",
        "scaffoldingLogPositions",
        "scaffoldingOperationTypes",
    ]);
    const themeColors = useThemeColors();
    const { values, setFieldValue } =
        useFormikContext<BaseScaffoldingLogPositionFormValues>();

    const operationOptions = getScaffoldingOperationTypeOptions(t);

    const createEmptyWorkingTime = useCallback(
        (): ScaffoldingLogPositionWorkingTimeBaseDTO => ({
            id: undefined,
            numberOfWorkers: 0,
            numberOfHours: "",
            unit: null as unknown as UnitBaseDTO,
            operationType: ScaffoldingOperationType.ASSEMBLY,
        }),
        []
    );

    useEffect(() => {
        if (!values.workingTimes || values.workingTimes.length === 0) {
            void setFieldValue("workingTimes", [createEmptyWorkingTime()]);
        }
    }, [createEmptyWorkingTime, setFieldValue, values.workingTimes]);


    useEffect(() => {
        const workingTimes = values.workingTimes || [];
        if (workingTimes.length === 0) return;

        const lastItem = workingTimes[workingTimes.length - 1];
        const isLastItemFilled =
            lastItem &&
            lastItem.numberOfWorkers > 0 &&
            lastItem.numberOfHours.trim() !== "";

        if (isLastItemFilled) {
            void setFieldValue("workingTimes", [
                ...workingTimes,
                createEmptyWorkingTime(),
            ]);
        }
    }, [values.workingTimes, setFieldValue, createEmptyWorkingTime]);

    return (
        <FieldArray name="workingTimes">
            {({ remove }) => (
                <Box mt={2} p={2}>
                    <Heading size="md" mb={4} color={themeColors.fontColor} textAlign={"center"}>
                        {t("scaffoldingLogPositions:workingTimes")}
                    </Heading>

                    <Grid templateColumns="repeat(11, 1fr)" gap={2} mb={2}>
                        <GridItem colSpan={3}>
                            <Heading size="xs" textAlign={"center"}>
                                {t("scaffoldingLogPositions:numberOfWorkers")}
                            </Heading>
                        </GridItem>
                        <GridItem colSpan={3}>
                            <Heading size="xs" textAlign={"center"}>
                                {t("scaffoldingLogPositions:numberOfHours")}
                            </Heading>
                        </GridItem>
                        <GridItem colSpan={4}>
                            <Heading size="xs" textAlign={"center"}>
                                {t("scaffoldingOperationTypes:operation")}
                            </Heading>
                        </GridItem>
                        <GridItem colSpan={1} />
                    </Grid>

                    {values.workingTimes &&
                        values.workingTimes.map((_, index) => (
                            <Grid
                                key={index}
                                templateColumns="repeat(11, 1fr)"
                                gap={2}
                                mb={2}
                                alignItems="center"
                            >
                                {/* numberOfWorkers – integer */}
                                <GridItem colSpan={3}>
                                    <CustomNumberInput
                                        name={`workingTimes.${index}.numberOfWorkers`}
                                        placeholder="0"
                                        // jeśli w CustomNumberInput masz ograniczenia, możesz dodać osobny komponent integer,
                                        // ale tu możesz po prostu wymusić brak miejsc po przecinku w regexie walidacji
                                    />
                                </GridItem>

                                {/* numberOfHours – decimal z dwoma miejscami (ten sam CustomNumberInput co w dimensions) */}
                                <GridItem colSpan={3}>
                                    <CustomNumberInput
                                        name={`workingTimes.${index}.numberOfHours`}
                                        placeholder="0.00"
                                    />
                                </GridItem>

                                {/* operationType */}
                                <GridItem colSpan={4}>
                                    <CustomSelectField
                                        name={`workingTimes.${index}.operationType`}
                                        options={operationOptions}
                                        defaultValue={ScaffoldingOperationType.ASSEMBLY}
                                    />
                                </GridItem>

                                {/* Remove */}
                                <GridItem colSpan={1}>
                                    {values.workingTimes.length > 1 && (
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
                        ))}
                </Box>
            )}
        </FieldArray>
    );
};

export default WorkingTimesArrayField;
