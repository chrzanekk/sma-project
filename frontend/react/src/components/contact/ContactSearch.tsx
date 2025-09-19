// src/components/contact/ContactSearch.tsx
import React, {useCallback, useEffect, useMemo, useRef, useState} from "react";
import {Flex, Table, Text} from "@chakra-ui/react";
import {useTranslation} from "react-i18next";
import {useThemeColors} from "@/theme/theme-colors.ts";
import {BaseContactFormValues} from "@/types/contact-types.ts";
import {CustomInputSearchField} from "@/components/shared/CustomFormFields.tsx";

export interface ContactSearchProps {
    searchFn: (query: string) => Promise<BaseContactFormValues[]>;
    onSelect: (contact: BaseContactFormValues) => void;
    minChars?: number;
    debounceMs?: number;
    autoSearch?: boolean;
    initialQuery?: string;
    size?: "sm" | "md";
    onInputKeyDown?: React.KeyboardEventHandler<HTMLInputElement>;
    enableEnterSubmit?: boolean; // default true
}

const ContactSearch: React.FC<ContactSearchProps> = ({
                                                         searchFn,
                                                         onSelect,
                                                         minChars = 2,
                                                         debounceMs = 300,
                                                         autoSearch = false,
                                                         initialQuery = "",
                                                         size = "sm",
                                                         onInputKeyDown,
                                                         enableEnterSubmit = true,
                                                     }) => {
    const {t} = useTranslation(["common", "contacts"]);
    const themeColors = useThemeColors();
    const [query, setQuery] = useState<string>(initialQuery);
    const [loading, setLoading] = useState<boolean>(false);
    const [results, setResults] = useState<BaseContactFormValues[]>([]);
    const [error, setError] = useState<string | null>(null);
    const debounceTimer = useRef<number | null>(null);

    const canSearch = useMemo(() => query.trim().length >= minChars, [query, minChars]);

    const doSearch = useCallback(async () => {
        if (!canSearch) {
            setResults([]);
            setError(null);
            return;
        }
        setLoading(true);
        setError(null);
        try {
            const data = await searchFn(query.trim());
            setResults(data ?? []);
        } catch (e: any) {
            setError(e?.message || "Error");
            setResults([]);
        } finally {
            setLoading(false);
        }
    }, [canSearch, query, searchFn]);

    // Auto-search z debounce
    useEffect(() => {
        if (!autoSearch) return;
        if (debounceTimer.current) window.clearTimeout(debounceTimer.current);
        debounceTimer.current = window.setTimeout(() => {
            void doSearch();
        }, debounceMs);
        return () => {
            if (debounceTimer.current) window.clearTimeout(debounceTimer.current);
        };
    }, [autoSearch, debounceMs, doSearch, query]);

    const handleSearch = () => {
        void doSearch();
    };

    const handleReset = () => {
        setQuery("");
        setResults([]);
        setError(null);
    };

    return (
        <Flex direction="column" gap={2}>
            <CustomInputSearchField
                name="contactSearch"
                label={t("contacts:search")}
                placeholder={t("contacts:searchPlaceholder", "Wyszukaj kontakt (nazwisko)")}
                searchTerm={query}
                setSearchTerm={setQuery}
                handleSearch={handleSearch}
                handleReset={handleReset}
                isSearching={loading}
                size={size}
                minChars={minChars}
                onKeyDown={onInputKeyDown}
                enableEnterSubmit={enableEnterSubmit}
            />

            {error && (
                <Text color="red.500" fontSize="sm">
                    {error}
                </Text>
            )}

            {results.length > 0 && (
                <Table.ScrollArea borderWidth={"1px"} rounded={"sm"} height={"150px"} borderRadius="md"
                                  borderColor="grey">
                    <Table.Root size={"sm"} stickyHeader showColumnBorder interactive color={themeColors.fontColor}>
                        <Table.Header>
                            <Table.Row bg={themeColors.bgColorPrimary}>
                                <Table.ColumnHeader color={themeColors.fontColor}>
                                    {t("contacts:firstName")}
                                </Table.ColumnHeader>
                                <Table.ColumnHeader color={themeColors.fontColor} textAlign={"center"}>
                                    {t("contacts:lastName")}
                                </Table.ColumnHeader>
                                <Table.ColumnHeader color={themeColors.fontColor} textAlign={"end"}>
                                    {t("contacts:phoneNumber")}
                                </Table.ColumnHeader>
                            </Table.Row>
                        </Table.Header>
                        <Table.Body>
                            {results.map((contact, idx) => (
                                <Table.Row
                                    key={idx}
                                    onClick={() => onSelect(contact)}
                                    style={{cursor: "pointer"}}
                                    bg={themeColors.bgColorSecondary}
                                    _hover={{
                                        textDecoration: "none",
                                        bg: themeColors.highlightBgColor,
                                        color: themeColors.fontColorHover,
                                    }}
                                >
                                    <Table.Cell>{contact.firstName}</Table.Cell>
                                    <Table.Cell textAlign={"center"}>{contact.lastName}</Table.Cell>
                                    <Table.Cell textAlign={"end"}>{contact.phoneNumber}</Table.Cell>
                                </Table.Row>
                            ))}
                        </Table.Body>
                    </Table.Root>
                </Table.ScrollArea>
            )}

            {!loading && results.length === 0 && canSearch && autoSearch && !error && (
                <Text fontSize="sm" color="gray.500">
                    {t("common:dataNotFound")}
                </Text>
            )}
        </Flex>
    );
};

export default ContactSearch;
