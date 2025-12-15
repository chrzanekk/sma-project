// src/components/contact/ContactSearchWithSelect.tsx
import React, {useState} from "react";
import {Flex} from "@chakra-ui/react";
import {useTranslation} from "react-i18next";
import {ContactDTO} from "@/types/contact-types";
import AsyncSearchSelect, {AsyncSearchSelectOption} from "@/components/shared/AsyncSearchSelect";

export interface ContactSearchWithSelectProps {
    searchFn: (query: string) => Promise<ContactDTO[]>;
    onSelect: (contact: ContactDTO | null) => void;
    minChars?: number;
    debounceMs?: number;
    size?: "sm" | "md" | "lg" | "xs";
    placeholder?: string;
    noOptionsMessageText?: string;
    label?: string
}

const ContactSearchWithSelect: React.FC<ContactSearchWithSelectProps> = ({
                                                                             searchFn,
                                                                             onSelect,
                                                                             minChars = 2,
                                                                             debounceMs = 300,
                                                                             size = "md",
                                                                             placeholder,
                                                                             noOptionsMessageText,
                                                                             label
                                                                         }) => {
    const {t} = useTranslation(["common", "contacts"]);
    const [selectedOption, setSelectedOption] = useState<AsyncSearchSelectOption<ContactDTO> | null>(null);

    const loadOptions = async (term: string):
        Promise<AsyncSearchSelectOption<ContactDTO>[]> => {
        const data = await searchFn(term);
        return (data ?? []).map((c) => ({
            value: c.id!,
            label: [c.firstName, c.lastName].filter(Boolean).join(" "),
            raw: c,
        }));
    };

    const handleChange = (opt: AsyncSearchSelectOption<ContactDTO> | null) => {
        setSelectedOption(opt);

        if (!opt) {
            onSelect(null);
            return
        }
        if (opt?.raw) {
            onSelect(opt.raw);
        }
    };

    return (
        <Flex direction="column" gap={2}>
            <AsyncSearchSelect<ContactDTO>
                loadOptions={loadOptions}
                value={selectedOption}
                onChange={handleChange}
                placeholder={placeholder ?? t("contacts:searchByLastName")}
                minChars={minChars}
                debounceMs={debounceMs}
                size={size}
                clearable={true}
                noOptionsMessage={noOptionsMessageText ?? t("common:dataNotFound")}
                label={label}
            />
        </Flex>
    );
};

export default ContactSearchWithSelect;
