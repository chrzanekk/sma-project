import React, {useState} from "react";
import {ConstructionSiteDTO} from "@/types/constrution-site-types.ts";
import {useTranslation} from "react-i18next";
import AsyncSearchSelect, {AsyncSearchSelectOption} from "@/components/shared/AsyncSearchSelect.tsx";
import {Flex} from "@chakra-ui/react";

export interface ConstructionSiteSearchWithSelectProps {
    searchFn: (query: string) => Promise<ConstructionSiteDTO[]>;
    onSelect: (constructionSite: ConstructionSiteDTO) => void;
    minChars?: number;
    debounceMs?: number;
    autoSearch?: boolean;
    initialQuery?: string;
    size?: "sm" | "md";
    onInputKeyDown?: React.KeyboardEventHandler<HTMLInputElement>;
    enableEnterSubmit?: boolean;
}

const ConstructionSiteSearchWithSelect: React.FC<ConstructionSiteSearchWithSelectProps> = ({
                                                                                               searchFn,
                                                                                               onSelect,
                                                                                               minChars = 2,
                                                                                               debounceMs = 300,
                                                                                               size = "md"
                                                                                           }) => {
    const {t} = useTranslation(["common"]);

    const [selectedOption, setSelectedOption] = useState<AsyncSearchSelectOption<ConstructionSiteDTO> | null>(null);

    const loadOptions = async (term: string): Promise<AsyncSearchSelectOption<ConstructionSiteDTO>[]> => {
        const data = await searchFn(term);
        return (data ?? []).map((c) => ({
            value: c.id!,
            label: c.name,
            raw: c
        }))
    }

    const handleChange = (opt: AsyncSearchSelectOption<ConstructionSiteDTO> | null) => {
        setSelectedOption(opt);
        if (opt?.raw) {
            onSelect(opt.raw);
            setSelectedOption(null);
        }
    };

    return (
        <Flex direction={"column"} gap={2}>
            <AsyncSearchSelect
                loadOptions={loadOptions}
                value={selectedOption}
                onChange={handleChange}
                placeholder={t("common:searchByName")}
                minChars={minChars}
                debounceMs={debounceMs}
                size={size}
                noOptionsMessage={t("common:dataNotFound")}
                clearable={true}
            />
        </Flex>
    )
}

export default ConstructionSiteSearchWithSelect;