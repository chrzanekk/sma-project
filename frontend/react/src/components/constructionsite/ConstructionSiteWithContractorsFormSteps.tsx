import {useTranslation} from "react-i18next";
import React, {useCallback, useEffect, useRef, useState} from "react";
import {FormikProps} from "formik";
import {Box, Flex, Heading, Input, Spinner, Steps, Table, Text} from "@chakra-ui/react";
import {Button} from "@/components/ui/button.tsx";
import {StepsNextTrigger, StepsPrevTrigger} from "@/components/ui/steps";
import {useThemeColors} from "@/theme/theme-colors.ts";
import {getSelectedCompany} from "@/utils/company-utils.ts";
import {Country, getCountryOptions} from "@/types/country-type.ts";
import {
    ConstructionSiteCreateDTO,
    ConstructionSiteDTO,
    ConstructionSiteFormValues,
    ConstructionSiteUpdateDTO,
    FetchableConstructionSiteDTO
} from "@/types/constrution-site-types.ts";
import {ContractorDTO} from "@/types/contractor-types.ts";
import {
    addConstructionSite,
    createConstructionSite,
    extendedUpdateConstructionSite,
    getConstructionSiteById,
    getContractorsByConstructionSiteIdPaged
} from "@/services/construction-site-service.ts";
import {getContractorsByFilter} from "@/services/contractor-service.ts";
import {getConstructionSiteValidationSchema} from "@/validation/ConstructionSiteValidationSchema.ts";
import {errorNotification, successNotification} from "@/notifications/notifications.ts";
import {formatMessage} from "@/notifications/FormatMessage.tsx";
import CommonConstructionSiteForm from "@/components/constructionsite/CommonConstructionSiteForm.tsx";
import ConstructionSiteSummary from "@/components/constructionsite/ConstructionSiteSummary.tsx";

interface Props {
    onSuccess: () => void;
    constructionSiteId?: number;
}

interface ContractorState {
    contractors: ContractorDTO[];
    page: number;
    hasMore: boolean;
    loading: boolean;
    addedContractors: ContractorDTO[];
    deletedContractors: ContractorDTO[];
}

const ConstructionSiteWithContractorsFormSteps: React.FC<Props> = ({
                                                                       onSuccess,
                                                                       constructionSiteId
                                                                   }) => {
    const {t} = useTranslation(["common", "constructionSites", "contractors", "errors"]);
    const themeColors = useThemeColors();
    const countryOptions = getCountryOptions(t);
    const currentCompany = getSelectedCompany();

    const [formData, setFormData] = useState<ConstructionSiteFormValues | null>(null);
    const [isLoading, setIsLoading] = useState<boolean>(true);
    const [currentStep, setCurrentStep] = useState<number>(0);
    const [isFormValid, setIsFormValid] = useState<boolean>(false);

    const [contractorState, setContractorState] = useState<ContractorState>({
        contractors: [],
        page: 0,
        hasMore: false,
        loading: false,
        addedContractors: [],
        deletedContractors: []
    });

    const [searchQuery, setSearchQuery] = useState("");
    const [searchResults, setSearchResults] = useState<ContractorDTO[]>([]);
    const [searchLoading, setSearchLoading] = useState(false);

    const formRef = useRef<FormikProps<ConstructionSiteFormValues>>(null);
    const scrollContainerRef = useRef<HTMLDivElement>(null);

    // Pobieranie danych budowy + kontrahentów
    useEffect(() => {
        const fetchData = async () => {
            setIsLoading(true);
            try {
                if (constructionSiteId) {
                    // Tryb edycji
                    const site: FetchableConstructionSiteDTO = await getConstructionSiteById(constructionSiteId);
                    setFormData({
                        id: site.id,
                        name: site.name,
                        shortName: site.shortName,
                        address: site.address,
                        country: site.country.code,
                        code: site.code
                    });

                    await fetchContractorsPage(0, true);
                } else {
                    // Tryb dodawania
                    setFormData({
                        id: 0,
                        name: '',
                        shortName: '',
                        address: '',
                        country: '',
                        code: ''
                    });
                    setContractorState(prev => ({
                        ...prev,
                        contractors: [],
                        page: 0,
                        hasMore: false
                    }));
                }
            } catch (err) {
                console.error("Error loading data:", err);
            } finally {
                setIsLoading(false);
            }
        };
        fetchData().catch();
    }, [constructionSiteId]);

    // Pobieranie paginowanej listy kontrahentów przypisanych do budowy
    const fetchContractorsPage = useCallback(async (page: number, replace = false) => {
        if (!constructionSiteId) return;
        try {
            setContractorState(prev => ({...prev, loading: true}));
            const {items, totalPages} = await getContractorsByConstructionSiteIdPaged(constructionSiteId, page, 10);
            setContractorState(prev => ({
                ...prev,
                contractors: replace ? items : [...prev.contractors, ...items],
                page: page + 1,
                hasMore: page + 1 < totalPages,
                loading: false
            }));
        } catch (err) {
            console.error("Błąd ładowania kontrahentów:", err);
            setContractorState(prev => ({...prev, loading: false}));
        }
    }, [constructionSiteId]);

    // Infinite scroll
    const onScroll = () => {
        const container = scrollContainerRef.current;
        if (container && contractorState.hasMore && !contractorState.loading) {
            const {scrollTop, scrollHeight, clientHeight} = container;
            if (scrollHeight - scrollTop <= clientHeight + 20) {
                fetchContractorsPage(contractorState.page).catch();
            }
        }
    };

    // Wyszukiwanie kontrahentów
    const handleSearch = useCallback(async () => {
        if (searchQuery.trim().length < 2) {
            setSearchResults([]);
            return;
        }
        setSearchLoading(true);
        try {
            const {contractors} = await getContractorsByFilter({
                name: searchQuery,
                size: 5,
                page: 0
            });
            setSearchResults(contractors);
        } catch (err) {
            console.error("Error searching contractors:", err);
        } finally {
            setSearchLoading(false);
        }
    }, [searchQuery]);

    // Dodawanie kontrahenta
    const handleAddContractor = (contractor: ContractorDTO) => {
        if (!constructionSiteId) {
            // Tryb dodawania → maksymalnie jeden kontrahent
            setContractorState(prev => ({
                ...prev,
                contractors: [contractor]
            }));
        } else {
            // Tryb edycji → wielu kontrahentów
            const exists = contractorState.contractors.some(c => c.id === contractor.id);
            if (!exists) {
                setContractorState(prev => ({
                    ...prev,
                    contractors: [...prev.contractors, contractor],
                    addedContractors: [...prev.addedContractors, contractor]
                }));
            }
        }
    };

    // Usuwanie kontrahenta
    const handleRemoveContractor = (index: number) => {
        setContractorState(prev => {
            const contractorToRemove = prev.contractors[index];
            return {
                ...prev,
                contractors: prev.contractors.filter((_, i) => i !== index),
                ...(constructionSiteId
                    ? {deletedContractors: [...prev.deletedContractors, contractorToRemove]}
                    : {})
            };
        });
    };

    // Nawigacja kroków
    const handleNext = async () => {
        if (currentStep === 0 && formRef.current) {
            await formRef.current.submitForm();
        } else {
            setCurrentStep(prev => prev + 1);
        }
    };
    const handlePrev = () => setCurrentStep(prev => prev - 1);

    // Obsługa submitu pierwszego kroku
    const handleFormSubmit = async (values: ConstructionSiteFormValues) => {
        setFormData(values);
        setCurrentStep(1);
    };

    // Finalny submit
    const handleFinalSubmit = async () => {
        if (!formData) return;

        try {
            if (constructionSiteId) {
                const payload: ConstructionSiteUpdateDTO = {
                    ...formData,
                    country: Country.fromCode(formData.country),
                    company: currentCompany!,
                    addedContractors: contractorState.addedContractors,
                    deletedContractors: contractorState.deletedContractors
                };
                await extendedUpdateConstructionSite(payload);
                successNotification(
                    t("success", {ns: "common"}),
                    formatMessage("notifications.editConstructionSiteSuccess", {name: formData.name}, "constructionSites")
                );
            } else {
                if (contractorState.contractors.length === 0) {
                    const payload: ConstructionSiteDTO = {
                        ...formData,
                        country: Country.fromCode(formData.country),
                        company: currentCompany!
                    };
                    await addConstructionSite(payload);
                } else {
                    const payload: ConstructionSiteCreateDTO = {
                        ...formData,
                        country: Country.fromCode(formData.country),
                        company: currentCompany!,
                        contractor: contractorState.contractors[0]
                    };
                    await createConstructionSite(payload);
                }
                successNotification(
                    t("success", {ns: "common"}),
                    formatMessage("notifications.addConstructionSiteSuccess", {name: formData.name}, "constructionSites")
                );
            }
            onSuccess();
        } catch (err: any) {
            console.error(err);
            errorNotification(
                t("common:error"),
                err.response?.data?.message || t("constructionSites:notifications.saveConstructionSiteError")
            );
        }
    };

    if (isLoading || !formData) return <div>Loading...</div>;

    return (
        <Steps.Root defaultStep={0} count={3} variant={"solid"} colorPalette={'green'} size={"sm"}>
            <Steps.List>
                <Steps.Item index={0} color={themeColors.fontColor}>
                    <Steps.Indicator/>
                    <Steps.Title>{t(constructionSiteId ? "constructionSites:edit" : "constructionSites:add")}</Steps.Title>
                    <Steps.Separator/>
                </Steps.Item>
                <Steps.Item index={1} color={themeColors.fontColor}>
                    <Steps.Indicator/>
                    <Steps.Title>{t("constructionSites:assign")}</Steps.Title>
                    <Steps.Separator/>
                </Steps.Item>
                <Steps.Item index={2} color={themeColors.fontColor}>
                    <Steps.Indicator/>
                    <Steps.Title>{t("common:summary")}</Steps.Title>
                </Steps.Item>
            </Steps.List>

            {/* Step 0 */}
            <Steps.Content index={0}>
                <CommonConstructionSiteForm
                    initialValues={formData}
                    validationSchema={getConstructionSiteValidationSchema(t, countryOptions)}
                    onSubmit={handleFormSubmit}
                    hideSubmit={true}
                    innerRef={formRef}
                    onValidityChange={setIsFormValid}
                />
            </Steps.Content>

            {/* Step 1 */}
            <Steps.Content index={1}>
                <Heading size="md" mb={2} color={themeColors.fontColor}>
                    {t("constructionSites:assign")}
                </Heading>
                {/* Wyszukiwarka kontrahentów */}
                <Flex gap={2} mb={2}>
                    <Input
                        placeholder={t("common:searchByName")}
                        value={searchQuery}
                        onChange={(e) => setSearchQuery(e.target.value)}
                        onKeyDown={(e) => e.key === "Enter" && handleSearch()}
                    />
                    <Button onClick={handleSearch} disabled={searchLoading}>
                        {searchLoading ? <Spinner size="sm"/> : t("common:search")}
                    </Button>
                </Flex>
                {searchResults.length > 0 && (
                    <Box mb={4}>
                        <Text mb={1}>{t("common:searchResults")}</Text>
                        <Table.Root size="sm">
                            <Table.Body>
                                {searchResults.map((contractor) => (
                                    <Table.Row key={contractor.id}
                                               bg={themeColors.bgColorSecondary}
                                               _hover={{
                                                   textDecoration: "none",
                                                   bg: themeColors.highlightBgColor,
                                                   color: themeColors.fontColorHover,
                                               }}>
                                        <Table.Cell>{contractor.name}</Table.Cell>
                                        <Table.Cell>
                                            <Button
                                                size="xs"
                                                onClick={() => handleAddContractor(contractor)}
                                            >
                                                {t("common:add")}
                                            </Button>
                                        </Table.Cell>
                                    </Table.Row>
                                ))}
                            </Table.Body>
                        </Table.Root>
                    </Box>
                )}

                {/* Lista przypisanych kontrahentów */}
                <Heading size="sm" mb={2} color={themeColors.fontColor}>
                    {t("constructionSites:assigned")}
                </Heading>
                <Table.ScrollArea borderWidth={"1px"} rounded={"sm"} height={"200px"}
                                  onScroll={onScroll} ref={scrollContainerRef}>
                    <Table.Root size="sm" stickyHeader showColumnBorder interactive color={themeColors.fontColor}>
                        <Table.Header>
                            <Table.Row bg={themeColors.bgColorPrimary}>
                                <Table.ColumnHeader color={themeColors.fontColor} textAlign={"center"}>
                                    {t("contractors:name")}
                                </Table.ColumnHeader>
                                <Table.ColumnHeader color={themeColors.fontColor} textAlign={"center"}>
                                    {t("common:actions")}
                                </Table.ColumnHeader>
                            </Table.Row>
                        </Table.Header>
                        <Table.Body>
                            {contractorState.contractors.map((c, idx) => (
                                <Table.Row key={c.id} bg={themeColors.bgColorSecondary}>
                                    <Table.Cell textAlign="center">{c.name}</Table.Cell>
                                    <Table.Cell textAlign="center">
                                        <Button variant="ghost" colorScheme="red" size="2xs"
                                                onClick={() => handleRemoveContractor(idx)}>
                                            {t("common:delete")}
                                        </Button>
                                    </Table.Cell>
                                </Table.Row>
                            ))}
                            {contractorState.loading && (
                                <Table.Row>
                                    <Table.Cell colSpan={2} textAlign="center">
                                        <Spinner size="sm" mt={2}/>
                                    </Table.Cell>
                                </Table.Row>
                            )}
                        </Table.Body>
                    </Table.Root>
                </Table.ScrollArea>
            </Steps.Content>

            {/* Step 2 */}
            <Steps.Content index={2}>
                <ConstructionSiteSummary
                    siteData={formData}
                    addedContractors={constructionSiteId ? contractorState.addedContractors : contractorState.contractors}
                    deletedContractors={constructionSiteId ? contractorState.deletedContractors : []}
                />
                <Flex justify="center" gap={4} mt={4}>
                    <Button onClick={handleFinalSubmit} colorPalette={"green"}>
                        {t("common:save")}
                    </Button>
                </Flex>
            </Steps.Content>

            {/* Navigation */}
            <Flex direction="row" align="center" justify="center" textAlign="center" mb={4} gap={6}>
                {currentStep > 0 && (
                    <StepsPrevTrigger asChild>
                        <Button variant="solid" colorPalette={"green"} size="sm" onClick={handlePrev}>
                            {t("common:previous")}
                        </Button>
                    </StepsPrevTrigger>
                )}
                {currentStep < 2 && (
                    <StepsNextTrigger asChild>
                        <Button
                            variant="solid"
                            colorPalette={"green"}
                            size="sm"
                            onClick={handleNext}
                            disabled={currentStep === 0 && !isFormValid}
                        >
                            {t("common:next")}
                        </Button>
                    </StepsNextTrigger>
                )}
            </Flex>
        </Steps.Root>
    );
};

export default ConstructionSiteWithContractorsFormSteps;
