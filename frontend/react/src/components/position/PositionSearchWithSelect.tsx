import React from "react";
import {PositionBaseDTO} from "@/types/position-types.ts";
import {useTranslation} from "react-i18next";
import AsyncSearchSelect, {AsyncSearchSelectOption} from "@/components/shared/AsyncSearchSelect.tsx";
import {Flex} from "@chakra-ui/react";

export interface PositionSearchProps {
    value: PositionBaseDTO | null | undefined;
    searchFn: (query: string) => Promise<PositionBaseDTO[]>;
    onSelect: (position: PositionBaseDTO | null) => void;
    minChars?: number;
    debounceMs?: number;
    autoSearch?: boolean;
    initialQuery?: string;
    placeholder?: string;
    isDisabled?: boolean;
    width?: string | undefined;
    size?: "sm" | "md";
}

const PositionSearchWithSelect: React.FC<PositionSearchProps> = ({
                                                                     value,
                                                                     searchFn,
                                                                     onSelect,
                                                                     minChars = 2,
                                                                     debounceMs = 300,
                                                                     size = "md",
                                                                     width = '100%',
                                                                     isDisabled = false,
                                                                 }) => {
    const {t} = useTranslation(['common', 'positions'])

    const loadOptions = async (term: string): Promise<AsyncSearchSelectOption<PositionBaseDTO>[]> => {
        try {
            const data = await searchFn(term);
            return (data ?? []).map((c) => ({
                value: c.id!,
                label: c.name,
                raw: c,
            }));
        } catch (err: any) {
            console.error(err);
            return [];
        }
    };

    const handleChange = (opt: AsyncSearchSelectOption<PositionBaseDTO> | null) => {
        onSelect(opt?.raw ?? null);
    };
    const getCurrentValue = (): AsyncSearchSelectOption<PositionBaseDTO> | null => {
        if (!value || !value.id) return null;
        return {
            value: value.id,
            label: value.name,
            raw: value
        }
    }

    return (
        <Flex direction="column" gap={2}>
            <AsyncSearchSelect<PositionBaseDTO>
                loadOptions={loadOptions}
                value={getCurrentValue()}
                onChange={handleChange}
                placeholder={t("common:searchByName")}
                minChars={minChars}
                debounceMs={debounceMs}
                disabled={isDisabled}
                size={size}
                width={width}
                noOptionsMessage={t("common:dataNotFound")}
                clearable={true}/>
        </Flex>
    );
};

export default PositionSearchWithSelect;