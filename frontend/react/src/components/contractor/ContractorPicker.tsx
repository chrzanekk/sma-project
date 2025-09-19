// components/contract/ContractorPicker.tsx
import React, {useMemo} from "react";
import {Box, Button, VStack} from "@chakra-ui/react";
import {useTranslation} from "react-i18next";
import {FormikProps} from "formik";
import ContractorSearch from "@/components/contractor/ContractorSearch";
import {ContractorBaseDTO, ContractorDTO, ContractorFormValues} from "@/types/contractor-types";
import {mapContractorBaseToFormValues} from "@/utils/contractor-mappers.ts";
import ContractorBaseSummary from "@/components/contractor/ContractorBaseSummary.tsx";

interface Props {
    formikRef: React.RefObject<FormikProps<any>>;
    selected: ContractorBaseDTO | null;
    onSelectChange: (c: ContractorBaseDTO | null) => void;
    searchFn: (q: string) => Promise<ContractorDTO[]>; // zwraca pełne ContractorDTO, wybieramy base
}

const ContractorPicker: React.FC<Props> = ({formikRef, selected, onSelectChange, searchFn}) => {
    const {t} = useTranslation(["common", "contractors"]);

    const selectedAsFormValues = useMemo<ContractorFormValues | undefined>(() => {
        return selected ? mapContractorBaseToFormValues(selected) : undefined;
    }, [selected]);

    const handleSelect = (c: ContractorDTO) => {
        const base: ContractorBaseDTO = {
            id: c.id,
            name: c.name,
            taxNumber: c.taxNumber,
            street: c.street,
            buildingNo: c.buildingNo,
            apartmentNo: c.apartmentNo,
            postalCode: c.postalCode,
            city: c.city,
            country: c.country,
            customer: c.customer,
            supplier: c.supplier,
            scaffoldingUser: c.scaffoldingUser,
            company: c.company,
        };
        formikRef.current?.setFieldTouched("contractor", true, false);
        formikRef.current?.setFieldValue("contractor", base, true);
        onSelectChange(base);
    };

    const handleReset = () => {
        formikRef.current?.setFieldTouched("contractor", true, false);
        formikRef.current?.setFieldValue("contractor", null, true);
        onSelectChange(null);
    };

    return (
        <Box>
            <ContractorSearch
                searchFn={async (q) => {
                    const items = await searchFn(q);
                    return items;
                }}
                onSelect={handleSelect}
                minChars={2}
                debounceMs={300}
                autoSearch={true}
                size="sm"
            />
            {selected && (
                <VStack align="center" justify="center" mt={2}>
                    //todo create simple table with selected data
                    <ContractorBaseSummary contractorData={selectedAsFormValues}/>
                    <Box><Button size="2xs" colorPalette="red" onClick={handleReset}>
                        {t("common:resetSelected")}
                    </Button></Box>
                </VStack>
            )}
        </Box>
    );
};

export default ContractorPicker;
