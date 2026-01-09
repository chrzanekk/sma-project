import React, {useEffect, useState} from "react";
import {Box, Button, Heading, Table} from "@chakra-ui/react";
import {useTranslation} from "react-i18next";
import {getUserById, updateUser} from "@/services/user-service.ts";
import {getCompaniesByFilter} from "@/services/company-service.ts"; // Zakładamy, że taka funkcja istnieje
import {UserDTO, UserFormDTO} from "@/types/user-types.ts";
import {CompanyBaseDTO} from "@/types/company-types.ts";
import {CustomInputSearchField} from "@/components/shared/CustomFormFields.tsx";
import {errorNotification, successNotification} from "@/notifications/notifications.ts";
import {formatMessage} from "@/notifications/FormatMessage.tsx";
import {useThemeColors} from "@/theme/theme-colors.ts";
import useUser from "@/hooks/UseUser.tsx";

interface EditUserCompaniesFormProps {
    userId: number;
    onSuccess: () => void;
}

const EditUserCompaniesForm: React.FC<EditUserCompaniesFormProps> = ({userId, onSuccess}) => {
    const {t} = useTranslation(["common", "users", "errors", "companies", "auth"]);
    const { user: currentUser, updateUser: updateLocalUser } = useUser();
    const themeColors = useThemeColors();
    const [searchTerm, setSearchTerm] = useState("");
    const [searchResults, setSearchResults] = useState<CompanyBaseDTO[]>([]);
    const [user, setUser] = useState<UserDTO | null>(null);
    const [assignedCompanies, setAssignedCompanies] = useState<CompanyBaseDTO[]>([]);

    // Pobranie danych użytkownika (wraz z przypisanymi firmami)
    useEffect(() => {
        const fetchUser = async () => {
            try {
                const fetchedUser: UserDTO = await getUserById(userId);
                setUser(fetchedUser);
                setAssignedCompanies(fetchedUser.companies);
            } catch (err) {
                console.error("Błąd pobierania danych użytkownika", err);
            }
        };
        fetchUser().catch();
    }, [userId]);

    // Wyszukiwanie firm po nazwie
    const handleSearch = async () => {
        try {
            // Zakładamy, że getCompaniesByFilter przyjmuje obiekt z kluczem nameStartsWith
            const result = await getCompaniesByFilter({nameStartsWith: searchTerm});
            setSearchResults(result.companies);
        } catch (err) {
            console.error("Błąd wyszukiwania firm", err);
        }
    };

    // Dodanie firmy do listy przypisanych (upewniamy się, że nie ma duplikatów)
    const handleAddCompany = (company: CompanyBaseDTO) => {
        if (!assignedCompanies.find(c => c.id === company.id)) {
            setAssignedCompanies([...assignedCompanies, company]);
        }
        // Po wybraniu czyścimy wyniki wyszukiwania i pole wyszukiwania
        setSearchResults([]);
        setSearchTerm("");
    };

    // Usuwanie firmy z listy przypisanych
    const handleRemoveCompany = (companyId: number) => {
        setAssignedCompanies(assignedCompanies.filter(c => c.id !== companyId));
    };

    // Aktualizacja użytkownika z nową listą firm (przekazujemy jedynie tablicę id firm)
    const handleUpdateCompanies = async () => {
        if (!user) return;
        try {
            const updatedUser: UserFormDTO = {
                ...user,
                roles: user.roles ? Array.from(user.roles).map(role => role.name) : [],
                companies: assignedCompanies
            };
            await updateUser(updatedUser);
            successNotification(
                t("success", {ns: "common"}),
                formatMessage("notifications.userCompaniesUpdatedSuccess", {login: user.login})
            );

            if (currentUser && currentUser.id === userId) {
                updateLocalUser({ companies: assignedCompanies });
            }

            onSuccess();
        } catch (err: any) {
            console.error("Błąd aktualizacji firm", err);
            errorNotification(
                t("error", {ns: "common"}),
                err.response?.data?.message || t("notifications.userCompaniesUpdatedError", {ns: "common"})
            );
        }
    };

    return (
        <Box>
            {/* Pole wyszukiwania firm */}
            <Box mb={4}>
                <CustomInputSearchField
                    name={"companySearch"}
                    searchTerm={searchTerm}
                    setSearchTerm={setSearchTerm}
                    handleSearch={handleSearch}
                    placeholder={t("users:updateProfile.searchCompanyPlaceholder", "Wyszukaj firmę (nazwa)")}
                />
                {searchResults.length > 0 && (
                    <Box mt={2}>
                        <Heading size="xl" mb={2} textAlign={"center"} color={themeColors.fontColor}>
                            {t("companies:companiesList")}
                        </Heading>
                        <Table.ScrollArea borderWidth="1px" rounded="sm" height="200px">
                            <Table.Root size="sm" stickyHeader showColumnBorder interactive
                                        color={themeColors.fontColor}>
                                <Table.Header>
                                    <Table.Row bg={themeColors.bgColorPrimary}>
                                        <Table.ColumnHeader bg={themeColors.bgColorPrimary}
                                                            color={themeColors.fontColor} textAlign={"center"}>
                                            {t("companies:name")}
                                        </Table.ColumnHeader>
                                    </Table.Row>
                                </Table.Header>

                                <Table.Body>
                                    {searchResults.map((company, idx) => (
                                        <Table.Row
                                            key={idx}
                                            onClick={() => handleAddCompany(company)}
                                            style={{cursor: "pointer"}}
                                            bg={themeColors.bgColorSecondary}
                                            _hover={{
                                                bg: themeColors.highlightBgColor,
                                                color: themeColors.fontColorHover,
                                            }}
                                        >
                                            <Table.Cell textAlign={"center"}>{company.name}</Table.Cell>
                                        </Table.Row>
                                    ))}
                                </Table.Body>
                            </Table.Root>
                        </Table.ScrollArea>
                    </Box>
                )}
            </Box>

            {/* Tabela z aktualnie przypisanymi firmami */}
            <Box mb={4}>
                <Heading size="xl" mb={2} color={themeColors.fontColor} textAlign={"center"}>
                    {t("auth:updateProfile.assignedCompanies")}
                </Heading>
                <Table.ScrollArea borderWidth="1px" rounded="sm" height="200px">
                    <Table.Root size="sm" stickyHeader showColumnBorder interactive
                                color={themeColors.fontColor}>
                        <Table.Header>
                            <Table.Row bg={themeColors.bgColorPrimary}>
                                <Table.ColumnHeader color={themeColors.fontColor} textAlign={"center"}>
                                    {t("companies:name")}
                                </Table.ColumnHeader>
                                <Table.ColumnHeader color={themeColors.fontColor} textAlign="center">
                                    {t("common:actions")}
                                </Table.ColumnHeader>
                            </Table.Row>
                        </Table.Header>
                        <Table.Body>
                            {assignedCompanies.length > 0 ? (
                                assignedCompanies.map((company, idx) => (
                                    <Table.Row
                                        key={idx}
                                        bg={themeColors.bgColorSecondary}
                                        _hover={{
                                            bg: themeColors.highlightBgColor,
                                            color: themeColors.fontColorHover,
                                        }}
                                    >
                                        <Table.Cell>{company.name}</Table.Cell>
                                        <Table.Cell textAlign="center">
                                            <Button
                                                size="xs"
                                                colorPalette="red"
                                                onClick={() => handleRemoveCompany(company.id!)}
                                            >
                                                {t("common:delete")}
                                            </Button>
                                        </Table.Cell>
                                    </Table.Row>
                                ))
                            ) : (
                                <Table.Row bg={themeColors.bgColorSecondary}>
                                    <Table.Cell colSpan={2} textAlign="center">
                                        <Box>{t("auth:updateProfile.noCompaniesAssigned")}</Box>
                                    </Table.Cell>
                                </Table.Row>
                            )}
                        </Table.Body>
                    </Table.Root>
                </Table.ScrollArea>
            </Box>

            {/* Przycisk do aktualizacji listy firm */}
            <Button colorPalette="blue" onClick={handleUpdateCompanies}>
                {t("auth:updateProfile.updateCompanies")}
            </Button>
        </Box>
    );
};

export default EditUserCompaniesForm;
