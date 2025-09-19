// components/contract/ContractorPicker.tsx
import React from "react";
import { Box, Flex, Text, Button } from "@chakra-ui/react";
import { useTranslation } from "react-i18next";
import { FormikProps } from "formik";
import ContractorSearch from "@/components/contractor/ContractorSearch";
import { ContractorBaseDTO, ContractorDTO } from "@/types/contractor-types";

interface Props {
    formikRef: React.RefObject<FormikProps<any>>;
    selected: ContractorBaseDTO | null;
    onSelectChange: (c: ContractorBaseDTO | null) => void;
    searchFn: (q: string) => Promise<ContractorDTO[]>; // zwraca pełne ContractorDTO, wybieramy base
}

const ContractorPicker: React.FC<Props> = ({ formikRef, selected, onSelectChange, searchFn }) => {
    const { t } = useTranslation(["common", "contractors"]);

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
                <Flex align="center" justify="space-between" mt={2}>
                    <Text fontSize="sm">
                        {t("contractors:selected")}: {selected.name}
                    </Text>
                    <Button size="2xs" colorPalette="red" onClick={handleReset}>
                        {t("common:resetSelected")}
                    </Button>
                </Flex>
            )}
        </Box>
    );
};

export default ContractorPicker;
