// components/contract/ConstructionSitePicker.tsx
import React from "react";
import { Box, Flex, Text, Button } from "@chakra-ui/react";
import { useTranslation } from "react-i18next";
import { FormikProps } from "formik";
import ConstructionSiteSearch from "@/components/constructionsite/ConstructionSiteSearch";
import {
    ConstructionSiteBaseDTO,
    ConstructionSiteDTO,
} from "@/types/constrution-site-types";

interface Props {
    formikRef: React.RefObject<FormikProps<any>>;
    selected: ConstructionSiteBaseDTO | null;
    onSelectChange: (c: ConstructionSiteBaseDTO | null) => void;
    searchFn: (q: string) => Promise<ConstructionSiteDTO[]>; // zwraca pełne, wybieramy base
}

const ConstructionSitePicker: React.FC<Props> = ({ formikRef, selected, onSelectChange, searchFn }) => {
    const { t } = useTranslation(["common", "constructionSites"]);

    const handleSelect = (s: ConstructionSiteDTO) => {
        const base: ConstructionSiteBaseDTO = {
            id: s.id,
            name: s.name,
            address: s.address,
            country: s.country,
            shortName: s.shortName,
            code: s.code,
        };
        formikRef.current?.setFieldTouched("constructionSite", true, false);
        formikRef.current?.setFieldValue("constructionSite", base, true);
        onSelectChange(base);
    };

    const handleReset = () => {
        formikRef.current?.setFieldTouched("constructionSite", true, false);
        formikRef.current?.setFieldValue("constructionSite", null, true);
        onSelectChange(null);
    };

    return (
        <Box>
            <ConstructionSiteSearch
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
                        {t("constructionSites:selected")}: {selected.name}
                    </Text>
                    <Button size="2xs" colorPalette="red" onClick={handleReset}>
                        {t("common:resetSelected")}
                    </Button>
                </Flex>
            )}
        </Box>
    );
};

export default ConstructionSitePicker;
