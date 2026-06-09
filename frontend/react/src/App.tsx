import React from "react";
import {Badge, Box, Flex, Heading, HStack, List, Separator, Text, VStack} from "@chakra-ui/react";
import DashboardGrid from "@/layout/DashboardGrid.tsx";
import {useThemeColors} from "@/theme/theme-colors.ts";
import {FaClipboardList} from "react-icons/fa6";
import {FaChartLine, FaInfoCircle, FaUserClock} from "react-icons/fa";
import {getSelectedCompany} from "@/utils/company-utils.ts";

const App: React.FC = () => {
    const themeColors = useThemeColors();
    const selectedCompany = getSelectedCompany();
    return (
        <Flex bg={themeColors.bgColorSecondary} mt={4} mb={4}>
            <DashboardGrid
                topRowContent={
                    <Box color={themeColors.fontColor}>
                        <HStack gap={3} mb={2}>
                            <Heading size="md" fontWeight="bold"> Witamy w Systemie Zarządzania Firmą Rusztowaniową</Heading>
                            <Badge colorPalette="orange" variant="subtle">Wersja Rozwojowa - dostępna tylko poprzez przeglądarkę WWW - brak wsparcia wersji mobilnej</Badge>
                        </HStack>
                        <Text fontSize="sm" color={themeColors.fontColor} opacity={0.8}>
                            Pulpit jest obecnie w fazie projektowej. W niedalekiej przyszłości znajdziesz tutaj
                            swoje kluczowe powiadomienia, szybkie akcje oraz inne użyteczne informacje.
                        </Text>
                        <Text fontSize="sm" color={themeColors.fontColor} opacity={0.8}>
                            Aplikacja służy głównie do obsługi firm z branży rusztowaniowej. Na chwilę obecną dostępnych jest tylko kilka modółów.
                        </Text>
                    </Box>
                }
                columnsContent={[
                    <VStack align="flex-start" gap={2} color={themeColors.fontColor} w={"100%"}>
                        <HStack>
                            <FaClipboardList color="green"/>
                            <Heading size="sm">Aktualnie zaimplementowane moduły</Heading>
                        </HStack>
                        <List.Root gap={1} mt={2} ml={2}>
                            <List.Item>
                                <Text fontWeight="bold">Dziennik Rusztowań  {"->"} Lista dzienników</Text>
                                <Text fontSize="xs" opacity={0.7}>Ewidencja zmontowanych konstrukcji podzielona na dzienniki</Text>
                            </List.Item>
                            <List.Item>
                                <Text fontWeight="bold">Kadry {"->"} Pracownicy</Text>
                                <Text fontSize="xs" opacity={0.7}>Podstawowa możliwość dodania pracowników - na chwilę obecną nigdzie nie używana</Text>
                            </List.Item>
                            <List.Item>
                                <Text fontWeight="bold">Kontrahenci</Text>
                                <Text fontSize="xs" opacity={0.7}>Zarządzanie kontrahentami firmy (dodawanie, edycja, usuwanie, wraz z kontaktami/przedstawicielami kontrahentów) - wymagane do korzystania z dzienników</Text>
                            </List.Item>
                            <List.Item>
                                <Text fontWeight="bold">Kontakty</Text>
                                <Text fontSize="xs" opacity={0.7}>Zarządzanie kontaktami (dodawanie, edycja, usuwanie) - wymagane do korzystania z dzienników</Text>
                            </List.Item>
                            <List.Item>
                                <Text fontWeight="bold">Budowy</Text>
                                <Text fontSize="xs" opacity={0.7}>Zarządzanie budowami (dodawanie, edycja, usuwanie + dodawanie istniejących kontrahentów) - wymagane do korzystania z dzienników</Text>
                            </List.Item>
                            <List.Item>
                                <Text fontWeight="bold">Kontrakty</Text>
                                <Text fontSize="xs" opacity={0.7}>Zarządzanie kontraktami (dodawanie, edycja, usuwanie) </Text>
                            </List.Item>
                            <List.Item>
                                <Text fontWeight="bold">[ADMIN] Użytkownicy</Text>
                                <Text fontSize="xs" opacity={0.7}>Zarządzanie Użytkownikami (dodawanie, edycja, usuwanie) </Text>
                            </List.Item>
                            <List.Item>
                                <Text fontWeight="bold">[ADMIN] Role</Text>
                                <Text fontSize="xs" opacity={0.7}>Zarządzanie rolami (dodawanie, edycja, usuwanie) </Text>
                            </List.Item>
                            <List.Item>
                                <Text fontWeight="bold">[ADMIN] Firmy - wersja testowa</Text>
                                <Text fontSize="xs" opacity={0.7}>Zarządzanie firmami (dodawanie, edycja, usuwanie) - jeden użytkownik może mieć dostęp do kilku firm</Text>
                            </List.Item>
                            <List.Item>
                                <Text fontWeight="bold">[ADMIN] Stanowiska</Text>
                                <Text fontSize="xs" opacity={0.7}>Zarządzanie stanowiskami (dodawanie, edycja, usuwanie) - wymagane do określenia stanowisk pracowników</Text>
                            </List.Item>
                            <List.Item>
                                <Text fontWeight="bold">[ADMIN] Uprawnienia</Text>
                                <Text fontSize="xs" opacity={0.7}>Zarządzanie uprawnieniami - w skrócie: jakie role użytkownika mają dostęp do konkretnych zasobów aplikacji</Text>
                            </List.Item>
                            <List.Item>
                                <Text fontWeight="bold">[ADMIN] Jednostki miary</Text>
                                <Text fontSize="xs" opacity={0.7}>Zarządzanie jednostkami miar - oprócz wstępnie zdefiniowanych można zdefiniować nowe - na chwilę obecną bazowe są wystarczające</Text>
                            </List.Item>
                            <List.Item>
                                <Text fontWeight="bold">Wybór języka</Text>
                                <Text fontSize="xs" opacity={0.7}>Można obecnie zmienić język strony na angielski - brak tłumaczenia treści</Text>
                            </List.Item>
                        </List.Root>
                    </VStack>,

                    // Kolumna 2: Miejsce na wykresy
                    <VStack align="flex-start" gap={3} color={themeColors.fontColor} w="100%">
                        <HStack>
                            <FaChartLine color="green"/>
                            <Heading size="sm">Aktywność (Wkrótce)</Heading>
                        </HStack>
                        <Separator/>
                        <Box
                            w="100%"
                            h="150px"
                            display="flex"
                            alignItems="center"
                            justifyContent="center"
                            bg={themeColors.bgColorSecondary}
                            borderRadius="md"
                            border="1px dashed"
                            borderColor="gray.400"
                        >
                            <Text fontSize="sm" opacity={0.5}>[ Miejsce na wykresy itp. ]</Text>
                        </Box>
                        <Separator/>
                        <HStack>
                            <FaClipboardList color="red"/>
                            <Heading size="sm">Planowane moduły (W przyszłości)</Heading>
                        </HStack>
                        <List.Root gap={1} mt={2} ml={3}>

                            <List.Item>
                                <Text fontWeight="bold">Powiadomienia z aplikacji </Text>
                                <Text fontSize="xs" opacity={0.7}>System powiadomień z aplikacji: via email, w oknie przeglądarki</Text>
                            </List.Item>
                            <List.Item>
                                <Text fontWeight="bold">Wersja moblina - przeglądarka </Text>
                                <Text fontSize="xs" opacity={0.7}>Obecne moduły są przeznaczone do wyświetlania poprzez przeglądarkę na komputerze/laptopie. W przyszłości częśc funkcjonalności będzie również dostępna poprzez przeglądarkę na urządzenia mobilne</Text>
                            </List.Item>
                            <List.Item>
                                <Text fontWeight="bold">Magazyn </Text>
                                <Text fontSize="xs" opacity={0.7}>Ewidencja materiału - również na budowach, ewidencja wydań/przyjęć magazynowych elementów nierusztowaniowych</Text>
                            </List.Item>
                            <List.Item>
                                <Text fontWeight="bold">Harmonogramy </Text>
                                <Text fontSize="xs" opacity={0.7}>Tworzenie harmonogramów pracowników pod wyjazdy na kontrakty</Text>
                            </List.Item>
                            <List.Item>
                                <Text fontWeight="bold">Kalkulatory </Text>
                                <Text fontSize="xs" opacity={0.7}>Kalkulatory rentowności, wyliczenia stawek r-g</Text>
                            </List.Item>
                            <List.Item>
                                <Text fontWeight="bold">Statystyki </Text>
                                <Text fontSize="xs" opacity={0.7}>Statystyki przerobu (na bazie dzienników rusztowań)</Text>
                            </List.Item>
                            <List.Item>
                                <Text fontWeight="bold">Flota </Text>
                                <Text fontSize="xs" opacity={0.7}>Obsługa powiadomień odnośnie floty: przeglądy itp.</Text>
                            </List.Item>
                            <List.Item>
                                <Text fontWeight="bold">Aplikacja natywna IOS/Android </Text>
                                <Text fontSize="xs" opacity={0.7}>W dalekiej przyszłości planowana jest natywna aplikacja dla systemów IOS(iPhone)/Android</Text>
                            </List.Item>
                        </List.Root>
                        <Separator/>

                    </VStack>,

                    // Kolumna 3: Ostatnio zalogowani użytkownicy (z zaślepką)
                    <VStack align="flex-start" gap={3} color={themeColors.fontColor} w={"100%"}>
                        <HStack>
                            <FaUserClock color="blue"/>
                            <Heading size="sm">Ostatnio aktywni (Wkrótce - poniżej wersja poglądowa)</Heading>
                        </HStack>
                        <Separator/>
                        <List.Root gap={3} mt={2} w="100%" ml={2}>
                            <List.Item>
                                <Flex justify="space-between" align="center">
                                    <Text fontSize="sm" fontWeight="bold">Jan Kowalski</Text>
                                    <Badge size="sm" colorPalette="green">Online</Badge>
                                </Flex>
                                <Text fontSize="xs" opacity={0.7}>Administrator</Text>
                            </List.Item>
                            <List.Item>
                                <Flex justify="space-between" align="center">
                                    <Text fontSize="sm">Piotr Nowak</Text>
                                    <Text fontSize="xs" opacity={0.7}>2 godz. temu</Text>
                                </Flex>
                            </List.Item>
                            <List.Item>
                                <Flex justify="space-between" align="center">
                                    <Text fontSize="sm">Tomasz Zięba</Text>
                                    <Text fontSize="xs" opacity={0.7}>Wczoraj</Text>
                                </Flex>
                            </List.Item>
                        </List.Root>
                    </VStack>
                ]}
                bottomRowContent={
                    <HStack justify="center" gap={2} color={themeColors.fontColor} opacity={0.6}>
                        <FaInfoCircle/>
                        <Text fontSize="s">Aplikacja w trakcie prac deweloperskich. Wszelkie błędy, pomysły i sugestię proszę zgłaszać na konrad.chrzanowski@gmail.com</Text>
                    </HStack>
                }
            />
        </Flex>
    )
}

export default App;
