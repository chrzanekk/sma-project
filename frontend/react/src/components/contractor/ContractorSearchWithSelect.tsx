import React, {useMemo} from "react";
import {Flex} from "@chakra-ui/react";
import {useTranslation} from "react-i18next";
import {ContractorBaseDTO, ContractorDTO} from "@/types/contractor-types.ts";
import AsyncSearchSelect, {AsyncSearchSelectOption} from "@/components/shared/AsyncSearchSelect.tsx";

export interface ContractorSearchProps {
    searchFn: (query: string) => Promise<ContractorDTO[]>;
    onSelect: (contractor: ContractorDTO) => void;
    selected?: ContractorDTO | ContractorBaseDTO | null;
    minChars?: number;
    debounceMs?: number;
    autoSearch?: boolean;
    initialQuery?: string;
    size?: "sm" | "md";
    onInputKeyDown?: React.KeyboardEventHandler<HTMLInputElement>;
    enableEnterSubmit?: boolean;
    placeholder?: string;
    label?: string
}

const ContractorSearchWithSelect: React.FC<ContractorSearchProps> = ({
                                                                         searchFn,
                                                                         onSelect,
                                                                         selected,
                                                                         minChars = 2,
                                                                         debounceMs = 300,
                                                                         size = "md",
                                                                         placeholder,
                                                                         label
                                                                     }) => {
    const {t} = useTranslation(["common", "contractors"]);

    const selectedOption = useMemo<AsyncSearchSelectOption<ContractorDTO> | null>(() => {
        if (!selected) return null;
        return {
            value: selected.id!,
            label: selected.name,
            raw: selected as ContractorDTO,
        };
    }, [selected]);

    const loadOptions = async (term: string):
        Promise<AsyncSearchSelectOption<ContractorDTO>[]> => {
        const data = await searchFn(term);
        return (data ?? []).map((c) => ({
            value: c.id!,
            label: c.name,
            raw: c,
        }));
    };

    const handleChange = (opt: AsyncSearchSelectOption<ContractorDTO> | null) => {
        if (!opt) {
            onSelect(null as unknown as ContractorDTO);
            return;
        }

        if (opt?.raw) {
            onSelect(opt.raw);
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
                label={label}
            />
        </Flex>
    );
};

export default ContractorSearchWithSelect;
