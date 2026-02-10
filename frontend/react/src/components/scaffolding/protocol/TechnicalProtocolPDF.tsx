// src/components/scaffolding/protocol/TechnicalProtocolPDF.tsx

import React from 'react';
import {Document, Font, Image, Page, StyleSheet, Text, View} from '@react-pdf/renderer';
import {TechnicalProtocolData} from '@/types/technical-protocol-types';
import {getSelectedCompany} from "@/utils/company-utils.ts";

// Rejestracja czcionek
Font.register({
    family: 'Anton',
    src: '/fonts/Anton-Regular.ttf',
});

Font.register({
    family: 'Archivo Narrow',
    src: '/fonts/ArchivoNarrow-VariableFont.ttf',
});

const styles = StyleSheet.create({
    page: {
        padding: 15,
        fontFamily: 'Archivo Narrow',
        fontSize: 8,
        lineHeight: 1.2,
    },
    header: {
        flexDirection: 'row',
        justifyContent: 'space-between',
        alignItems: 'center',
        marginBottom: 10,
        borderBottom: 2,
        borderBottomColor: '#000',
        paddingBottom: 8,
    },
    logo: {
        width: 70,
        height: 50,
    },
    companyInfo: {
        textAlign: 'right',
        fontSize: 7,
        fontFamily: "Archivo Narrow"
    },
    companyName: {
        textAlign: 'right',
        fontSize: 10,
        fontWeight: 'bold',
        marginBottom: 2,
        fontFamily: "Anton"
    },

    // Tabela główna
    table: {
        width: '100%',
        borderWidth: 1,
        borderColor: '#000',
    },

    // Wiersze
    tableRow: {
        flexDirection: 'row',
        borderBottomWidth: 1,
        borderBottomColor: '#000',
    },
    tableRowLast: {
        borderBottomWidth: 0,
    },

    // Komórki bazowe
    cell: {
        padding: 3,
        borderRightWidth: 1,
        borderRightColor: '#000',
        justifyContent: 'center',
        fontSize: 7,
    },
    cellLast: {
        borderRightWidth: 0,
    },
    cellGrey: {
        backgroundColor: '#d3d3d3',
    },
    cellWhite: {
        backgroundColor: '#ffffff',
    },

    // Szerokości kolumn
    cellNum: {
        width: '4%',
        alignItems: 'center',
        fontWeight: 'bold',
    },
    cellSmall: {
        width: '20%',
    },
    cellMedium: {
        width: '35%',
    },
    cellLarge: {
        width: '45%',
    },
    cellFull: {
        width: '96%',
    },

    // Wiersze z podziałami pionowymi
    splitRow: {
        flexDirection: 'row',
        flex: 1,
    },
    splitColumn: {
        flexDirection: 'column',
    },
    splitCellTop: {
        flex: 1,
        padding: 2,
        borderBottomWidth: 1,
        borderBottomColor: '#000',
        fontSize: 7,
    },
    splitCellBottom: {
        flex: 2,
        padding: 2,
        fontSize: 7,
    },

    // Tytuł protokołu
    titleRow: {
        padding: 4,
        textAlign: 'center',
        fontWeight: 'bold',
        fontSize: 9,
    },

    // Sekcja nagłówkowa
    sectionHeader: {
        padding: 3,
        fontWeight: 'bold',
        fontSize: 7,
    },

    // Tekst
    boldText: {
        fontWeight: 'bold',
    },
    smallText: {
        fontSize: 6,
    },

    // Footer
    footer: {
        position: 'absolute',
        bottom: 10,
        left: 15,
        right: 15,
        fontSize: 6,
        textAlign: 'center',
        borderTop: 1,
        borderTopColor: '#000',
        paddingTop: 3,
    },

    // Podpisy
    signatureRow: {
        flexDirection: 'row',
        justifyContent: 'space-between',
        marginTop: 10,
        marginBottom: 5,
    },
    signatureBox: {
        width: '30%',
        textAlign: 'center',
        fontSize: 7,
    },
});

interface TechnicalProtocolPDFProps {
    data: TechnicalProtocolData;
    logoUrl?: string;
}

const TechnicalProtocolPDF: React.FC<TechnicalProtocolPDFProps> = ({data, logoUrl}) => {
    const company = getSelectedCompany();
    const companyName = company?.name || "RCH SCAFFOLDING Sp. z o.o.";
    const companyAddress1 = "ul. 1000-lecia Państwa Polskiego 13";
    const companyAddress2 = "24-100 Puławy, Polska(Poland)";
    const companyContact1 = "Tel: (+48) 81 473 13 33";
    const companyContact2 = "e-mail: biuro@rchscaffolding.pl";
    const taxNumber = "NIP(VAT/TAX ID): PL9462731518";

    // Formatowanie wymiarów
    const dimensions = data.dimensions ? data.dimensions.split(', ') : [];

    return (
        <Document>
            <Page size="A4" style={styles.page}>
                {/* Header */}
                <View style={styles.header}>
                    <View>
                        {logoUrl ? (
                            <Image src={logoUrl} style={styles.logo}/>
                        ) : (
                            <Text style={{fontSize: 12, fontWeight: 'bold'}}>{companyName}</Text>
                        )}
                    </View>
                    <View>
                        <Text style={styles.companyName}>{companyName}</Text>
                        <Text style={styles.companyInfo}>{companyAddress1}</Text>
                        <Text style={styles.companyInfo}>{companyAddress2}</Text>
                        <Text style={styles.companyInfo}>{companyContact1}</Text>
                        <Text style={styles.companyInfo}>{companyContact2}</Text>
                        <Text style={styles.companyInfo}>{taxNumber}</Text>
                    </View>
                </View>

                {/* Tabela główna */}
                <View style={styles.table}>

                    {/* WIERSZ 1 - Tytuł */}
                    <View style={styles.tableRow}>
                        <View style={[styles.cell, styles.cellWhite, styles.cellLast, styles.titleRow, {width: '100%'}]}>
                            <Text>PROTOKÓŁ ODBIORU TECHNICZNEGO RUSZTOWANIA NR: {data.scaffoldingNumber} {data.assemblyDate}</Text>
                        </View>
                    </View>

                    {/* WIERSZ 2 - Część informacyjna */}
                    <View style={styles.tableRow}>
                        <View style={[styles.cell, styles.cellGrey, styles.cellLast, styles.sectionHeader, {width: '100%'}]}>
                            <Text>Część informacyjna:</Text>
                        </View>
                    </View>

                    {/* WIERSZ 3 (nr 1 na zrzucie) - Wykonawca rusztowania */}
                    <View style={styles.tableRow}>
                        <View style={[styles.cell, styles.cellGrey, styles.cellNum]}>
                            <Text>1</Text>
                        </View>
                        <View style={[styles.cell, styles.cellGrey, styles.cellMedium]}>
                            <Text>Wykonawca rusztowania (instalujący):</Text>
                        </View>
                        <View style={[styles.cell, styles.cellWhite, styles.cellLast, {width: '61%'}]}>
                            <Text>{data.companyName || ''}</Text>
                        </View>
                    </View>

                    {/* WIERSZ 4 (nr 2 na zrzucie) - Zleceniodawca */}
                    <View style={styles.tableRow}>
                        <View style={[styles.cell, styles.cellGrey, styles.cellNum]}>
                            <Text>2</Text>
                        </View>
                        <View style={[styles.cell, styles.cellGrey, {width: '26.25%'}]}>
                            <Text>Zleceniodawca/przyjmujący(zlecający wykonanie rusztowania lokalizację):</Text>
                        </View>
                        <View style={[styles.cell, styles.cellGrey, {width: '8.75%'}]}>
                            <View style={[styles.splitCellTop, {borderBottomColor: '#000'}]}>
                                <Text>Firma</Text>
                            </View>
                            <View style={styles.splitCellBottom}>
                                <Text>Nazwa obiektu</Text>
                            </View>
                        </View>
                        <View style={[styles.cell, styles.cellWhite, styles.cellLast, {width: '61%'}]}>
                            <View style={[styles.splitCellTop, {borderBottomColor: '#000'}]}>
                                <Text style={styles.boldText}>{data.contractorName || ''}</Text>
                            </View>
                            <View style={styles.splitCellBottom}>
                                <Text>{/* Nazwa obiektu - opcjonalne */}</Text>
                            </View>
                        </View>
                    </View>

                    {/* WIERSZ 5 (nr 3 na zrzucie) - Użytkownik rusztowania */}
                    <View style={styles.tableRow}>
                        <View style={[styles.cell, styles.cellGrey, styles.cellNum]}>
                            <Text>3</Text>
                        </View>
                        <View style={[styles.cell, styles.cellGrey, {width: '17.5%'}]}>
                            <Text>Użytkownik rusztowania:</Text>
                        </View>
                        <View style={[styles.cell, styles.cellGrey, {width: '17.5%'}]}>
                            <View style={[styles.splitCellTop]}>
                                <Text>Firma</Text>
                            </View>
                            <View style={styles.splitCellBottom}>
                                <Text>Imię i nazwisko upoważnionej osoby firmowej</Text>
                            </View>
                        </View>
                        <View style={[styles.cell, styles.cellWhite, styles.cellLast, {width: '61%'}]}>
                            <View style={[styles.splitCellTop]}>
                                <Text>{data.scaffoldingUserName || ''}</Text>
                            </View>
                            <View style={styles.splitCellBottom}>
                                <Text>{data.scaffoldingUserContactLastName} {data.scaffoldingUserContactFirstName}</Text>
                            </View>
                        </View>
                    </View>

                    {/* WIERSZ 6 (nr 4 na zrzucie) - Lokalizacja rusztowania */}
                    <View style={styles.tableRow}>
                        <View style={[styles.cell, styles.cellGrey, styles.cellNum]}>
                            <Text>4</Text>
                        </View>
                        <View style={[styles.cell, styles.cellGrey, styles.cellMedium]}>
                            <Text>Lokalizacja rusztowania:</Text>
                        </View>
                        <View style={[styles.cell, styles.cellWhite, styles.cellLast, {width: '61%'}]}>
                            <Text>{data.assemblyLocation || ''}</Text>
                        </View>
                    </View>

                    {/* WIERSZ 7 - Część techniczna */}
                    <View style={styles.tableRow}>
                        <View style={[styles.cell, styles.cellGrey, styles.cellLast, styles.sectionHeader, {width: '100%'}]}>
                            <Text>Część techniczna:</Text>
                        </View>
                    </View>

                    {/* WIERSZ 8 (nr 5,6 na zrzucie) - Rodzaj i przeznaczenie + Obciążenie */}
                    <View style={styles.tableRow}>
                        <View style={[styles.cell, styles.cellGrey, styles.cellNum]}>
                            <Text>5</Text>
                        </View>
                        <View style={{width: '44%'}}>
                            <View style={[styles.splitCellTop, styles.cellGrey]}>
                                <Text>Rodzaj i przeznaczenie rusztowania:</Text>
                            </View>
                            <View style={[styles.splitCellBottom, styles.cellWhite, {flex: 3}]}>
                                <Text>{data.scaffoldingPurpose || 'Moduł, do prac spawalniczych rurocigów'}</Text>
                            </View>
                        </View>
                        <View style={[styles.cell, styles.cellGrey, styles.cellNum, {borderLeftWidth: 1}]}>
                            <Text>6</Text>
                        </View>
                        <View style={[styles.cell, styles.cellLast, {width: '47%'}]}>
                            <View style={[styles.splitCellTop, styles.cellGrey]}>
                                <Text>Dopuszczalne obciążenie pomostów i konstrukcji rusztowania:</Text>
                            </View>
                            <View style={[styles.splitCellBottom, styles.cellWhite, {flex: 3}]}>
                                <Text style={styles.boldText}>{data.loadLimit || '1,5kN/m2'}</Text>
                                <Text style={styles.smallText}>Data i sposób przekazania rusztowania do użytkowania</Text>
                                <Text>{data.assemblyDate}</Text>
                            </View>
                        </View>
                    </View>

                    {/* WIERSZ 9 (nr 7,8 na zrzucie) - Opomność uziomi + Wymiar rusztowania */}
                    <View style={styles.tableRow}>
                        <View style={[styles.cell, styles.cellGrey, styles.cellNum]}>
                            <Text>7</Text>
                        </View>
                        <View style={{width: '44%'}}>
                            <View style={[styles.splitCellTop, styles.cellGrey]}>
                                <Text>Opomość uziomi:</Text>
                            </View>
                            <View style={[styles.splitCellBottom, styles.cellWhite, {flex: 3}]}>
                                <Text>{data.earthingResistance || ''}</Text>
                            </View>
                        </View>
                        <View style={[styles.cell, styles.cellGrey, styles.cellNum, {borderLeftWidth: 1}]}>
                            <Text>8</Text>
                        </View>
                        <View style={[styles.cell, styles.cellLast, {width: '47%'}]}>
                            <View style={[styles.splitCellTop, styles.cellGrey]}>
                                <Text>Data odbioru</Text>
                            </View>
                            <View style={[styles.splitCellBottom, styles.cellWhite, {flex: 3}]}>
                                <Text>{data.assemblyDate}</Text>
                            </View>
                        </View>
                    </View>

                    {/* WIERSZ 10 (nr 9 na zrzucie) - Wymiar rusztowania */}
                    <View style={styles.tableRow}>
                        <View style={[styles.cell, styles.cellGrey, styles.cellNum]}>
                            <Text>9</Text>
                        </View>
                        <View style={[styles.cell, styles.cellLast, {width: '96%'}]}>
                            <View style={[styles.splitCellTop, styles.cellGrey, {flex: 1}]}>
                                <Text>Obmiar rusztowania:</Text>
                            </View>
                            <View style={[styles.splitCellBottom, styles.cellWhite, {flex: 4, paddingTop: 3}]}>
                                {dimensions.map((dim, index) => (
                                    <Text key={index} style={{marginBottom: 1}}>{dim}</Text>
                                ))}
                            </View>
                        </View>
                    </View>

                    {/* WIERSZ 11 (nr 10 na zrzucie) - Terminarz przeglądów */}
                    <View style={styles.tableRow}>
                        <View style={[styles.cell, styles.cellGrey, styles.cellNum]}>
                            <Text>10</Text>
                        </View>
                        <View style={[styles.cell, styles.cellLast, {width: '96%', flexDirection: 'column'}]}>
                            <View style={[styles.splitCellTop, styles.cellGrey]}>
                                <Text>Terminarz przeglądów rusztowania:</Text>
                            </View>
                            <View style={{flexDirection: 'row', flex: 1, borderTopWidth: 1, borderTopColor: '#000'}}>
                                <View style={[styles.cell, styles.cellGrey, {width: '14.28%', minHeight: 20}]}>
                                    <Text>Data/godzina rozpoczęcia przeglądu</Text>
                                </View>
                                <View style={[styles.cell, styles.cellWhite, {width: '14.28%'}]}>
                                    <Text> </Text>
                                </View>
                                <View style={[styles.cell, styles.cellWhite, {width: '14.28%'}]}>
                                    <Text> </Text>
                                </View>
                                <View style={[styles.cell, styles.cellWhite, {width: '14.28%'}]}>
                                    <Text> </Text>
                                </View>
                                <View style={[styles.cell, styles.cellWhite, {width: '14.28%'}]}>
                                    <Text> </Text>
                                </View>
                                <View style={[styles.cell, styles.cellWhite, {width: '14.28%'}]}>
                                    <Text> </Text>
                                </View>
                                <View style={[styles.cell, styles.cellWhite, styles.cellLast, {width: '14.32%'}]}>
                                    <Text> </Text>
                                </View>
                            </View>
                            <View style={{flexDirection: 'row', borderTopWidth: 1, borderTopColor: '#000'}}>
                                <View style={[styles.cell, styles.cellGrey, {width: '14.28%', minHeight: 20}]}>
                                    <Text>Data/godzina i czytelne podpisy osób dokonujących przeglądu</Text>
                                </View>
                                <View style={[styles.cell, styles.cellWhite, {width: '14.28%'}]}>
                                    <Text> </Text>
                                </View>
                                <View style={[styles.cell, styles.cellWhite, {width: '14.28%'}]}>
                                    <Text> </Text>
                                </View>
                                <View style={[styles.cell, styles.cellWhite, {width: '14.28%'}]}>
                                    <Text> </Text>
                                </View>
                                <View style={[styles.cell, styles.cellWhite, {width: '14.28%'}]}>
                                    <Text> </Text>
                                </View>
                                <View style={[styles.cell, styles.cellWhite, {width: '14.28%'}]}>
                                    <Text> </Text>
                                </View>
                                <View style={[styles.cell, styles.cellWhite, styles.cellLast, {width: '14.32%'}]}>
                                    <Text> </Text>
                                </View>
                            </View>
                        </View>
                    </View>

                    {/* WIERSZ 12 (nr 11 na zrzucie) - Deklaracje */}
                    <View style={styles.tableRow}>
                        <View style={[styles.cell, styles.cellGrey, styles.cellNum]}>
                            <Text>11</Text>
                        </View>
                        <View style={[styles.cell, styles.cellGrey, styles.cellLast, {width: '96%', fontSize: 6}]}>
                            <Text>Wykonawca (przedstawiciel rusztowania) oświadcza, że:</Text>
                            <Text>a) rusztowanie opisane w protokole je zmontowane i jest zatwierdzone według prawa budowlanego, ratunkowego, zajęciowego i dokumentacji technicznej i zakłąda w rusztowanie...</Text>
                            <Text>b) inspekcja użytkownika podmiot który dla podwykonawstwa czy uczestników lub dostarczanego obiektu do pracownych zgodnie z wymaganiem odpowiedzialnej regulacji...</Text>
                        </View>
                    </View>

                    {/* WIERSZ 13 (nr 12 na zrzucie) */}
                    <View style={styles.tableRow}>
                        <View style={[styles.cell, styles.cellGrey, styles.cellNum]}>
                            <Text>12</Text>
                        </View>
                        <View style={[styles.cell, styles.cellGrey, styles.cellLast, {width: '96%', fontSize: 6}]}>
                            <Text>Rusztowanie i pozostawione bezopodstawne:</Text>
                            <Text>a) zabezpieczenia te są oznaczone przez ocenę jego obecności lub prząglądów, szczeście w roli zarządu zdarzeń przez przyjądzeniem do zmiennie w miejście otrzymanym, odlądowane że...</Text>
                        </View>
                    </View>

                    {/* WIERSZ 14 (nr 13 na zrzucie) - Podpisy */}
                    <View style={styles.tableRow}>
                        <View style={[styles.cell, styles.cellWhite, {width: '33.33%', minHeight: 35}]}>
                            <Text style={[styles.boldText, {textAlign: 'center', marginBottom: 15}]}>
                                Wykonawca (przedstawiciel rusztowania) lub osoba przełożonej:
                            </Text>
                            <Text style={{textAlign: 'center'}}>_____________________</Text>
                            <Text style={[styles.smallText, {textAlign: 'center'}]}>Data / godzina</Text>
                        </View>
                        <View style={[styles.cell, styles.cellWhite, {width: '33.33%'}]}>
                            <Text style={[styles.boldText, {textAlign: 'center', marginBottom: 15}]}>
                                Zleceniodawca/oszczężający wykonane prace:
                            </Text>
                            <Text style={{textAlign: 'center'}}>_____________________</Text>
                            <Text style={[styles.smallText, {textAlign: 'center'}]}>Data / godzina / podpis</Text>
                        </View>
                        <View style={[styles.cell, styles.cellWhite, styles.cellLast, {width: '33.34%'}]}>
                            <Text style={[styles.boldText, {textAlign: 'center', marginBottom: 15}]}>
                                Użytkownik (odbierający rusztowanie)
                            </Text>
                            <Text style={{textAlign: 'center'}}>_____________________</Text>
                            <Text style={[styles.smallText, {textAlign: 'center'}]}>Data / godzina</Text>
                        </View>
                    </View>

                    {/* WIERSZ 15 (nr 13 na dole zrzutu) */}
                    <View style={styles.tableRow}>
                        <View style={[styles.cell, styles.cellGrey, styles.cellNum]}>
                            <Text>13</Text>
                        </View>
                        <View style={[styles.cell, styles.cellLast, {width: '96%'}]}>
                            <View style={[styles.splitCellTop, styles.cellGrey]}>
                                <Text>Rusztowanie uszczone w terminii (lub przerwanie przez użytkownika) zostało zdemontowave:</Text>
                            </View>
                            <View style={[styles.splitCellBottom, styles.cellWhite, {flex: 2}]}>
                                <Text> </Text>
                            </View>
                        </View>
                    </View>

                    {/* WIERSZ 16 - Data ważności */}
                    <View style={[styles.tableRow, styles.tableRowLast]}>
                        <View style={[styles.cell, styles.cellGrey, styles.cellLast, {width: '100%', textAlign: 'center', padding: 2}]}>
                            <Text>Data ważności:</Text>
                        </View>
                    </View>

                </View>

                {/* Footer */}
                <View style={styles.footer}>
                    <Text>str. 1 z 1</Text>
                    <Text>Stopka będzie poprawiona</Text>
                </View>
            </Page>
        </Document>
    );
};

export default TechnicalProtocolPDF;
