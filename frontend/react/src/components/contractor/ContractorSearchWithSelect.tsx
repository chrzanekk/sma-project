import React, {useState} from "react";
import {Flex} from "@chakra-ui/react";
import {useTranslation} from "react-i18next";
import {ContractorDTO} from "@/types/contractor-types.ts";
import AsyncSearchSelect, {AsyncSearchSelectOption} from "@/components/shared/AsyncSearchSelect.tsx";

export interface ContractorSearchProps {
    searchFn: (query: string) => Promise<ContractorDTO[]>;
    onSelect: (contractor: ContractorDTO) => void;
    minChars?: number;
    debounceMs?: number;
    autoSearch?: boolean;
    initialQuery?: string;
    size?: "sm" | "md";
    onInputKeyDown?: React.KeyboardEventHandler<HTMLInputElement>;
    enableEnterSubmit?: boolean;
    placeholder?: string;
}

const ContractorSearchWithSelect: React.FC<ContractorSearchProps> = ({
                                                                         searchFn,
                                                                         onSelect,
                                                                         minChars = 2,
                                                                         debounceMs = 300,
                                                                         size = "md",
                                                                         placeholder
                                                                     }) => {
    const {t} = useTranslation(["common", "contractors"]);


    const [selectedOption, setSelectedOption] = useState<AsyncSearchSelectOption<ContractorDTO> | null>(null);

    // adaptacja loadOptions: z q -> Option[]
    const loadOptions = async (term: string): Promise<AsyncSearchSelectOption<ContractorDTO>[]> => {
        const data = await searchFn(term);
        return (data ?? []).map((c) => ({
            value: c.id!,
            label: c.name,
            raw: c,
        }));
    };

    const handleChange = (opt: AsyncSearchSelectOption<ContractorDTO> | null) => {
        setSelectedOption(opt);
        if (opt?.raw) {
            onSelect(opt.raw);
            setSelectedOption(null);
        }
    };

    return (
        <Flex direction="column" gap={2}>
            <AsyncSearchSelect<ContractorDTO>
                loadOptions={loadOptions}
                value={selectedOption}
                onChange={handleChange}
                placeholder={placeholder || t("common:searchByName")}
                minChars={minChars}
                debounceMs={debounceMs}
                size={size}
                noOptionsMessage={t("common:dataNotFound")}
                clearable={true}
            />
        </Flex>
    );
};

export default ContractorSearchWithSelect;
