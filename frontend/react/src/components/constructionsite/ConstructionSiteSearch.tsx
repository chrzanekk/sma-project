import React, {useCallback, useEffect, useMemo, useRef, useState} from "react";
import {ConstructionSiteDTO} from "@/types/constrution-site-types.ts";
import {useTranslation} from "react-i18next";
import {useThemeColors} from "@/theme/theme-colors.ts";
import {Flex, HStack, Table, Text} from "@chakra-ui/react";
import {CustomInputSearchField} from "@/components/shared/CustomFormFields.tsx";
import {Button} from "@/components/ui/button.tsx";

export interface ConstructionSiteSearchProps {
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

const ConstructionSiteSearch: React.FC<ConstructionSiteSearchProps> = ({
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
    const {t} = useTranslation(["common", "constructionSites"]);
    const [query, setQuery] = useState<string>(initialQuery);
    const [loading, setLoading] = useState<boolean>(false);
    const [results, setResults] = useState<ConstructionSiteDTO[]>([]);
    const [error, setError] = useState<string | null>(null);
    const debounceTimer = useRef<number | null>(null);
    const themeColors = useThemeColors();

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
                name="constructionSite"
                label={t("constructionSites:search")}
                placeholder={t("common:searchByName")}
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
                <Table.ScrollArea borderWidth={"1px"} rounded={"sm"} height={"120px"} borderRadius="md"
                                  borderColor="grey">
                    <Table.Root size={"sm"} stickyHeader showColumnBorder interactive>
                        <Table.Body>
                            {results.map((contractor) => (
                                <Table.Row key={contractor.id} bg={themeColors.bgColorPrimary}>
                                    <Table.Cell>{contractor.name}</Table.Cell>
                                    <Table.Cell>
                                        <HStack gap={1} justifyContent={"center"}>
                                            <Button
                                                size="2xs"
                                                onClick={() => onSelect(contractor)}
                                                justifyContent={"center"}
                                            >
                                                {t("common:add")}
                                            </Button>
                                        </HStack>
                                    </Table.Cell>
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
}

export default ConstructionSiteSearch;