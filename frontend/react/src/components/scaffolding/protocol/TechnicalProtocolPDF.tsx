// src/components/scaffolding/protocol/TechnicalProtocolPDF.tsx

import React from 'react';
import {Document, Font, Image, Page, Text, View} from '@react-pdf/renderer';
import {TechnicalProtocolData} from '@/types/technical-protocol-types';
import {getSelectedCompany} from "@/utils/company-utils.ts";
import {styles} from './technicalProtocolPDFstyles';

// Rejestracja czcionek
Font.register({
    family: 'Anton',
    src: '/fonts/Anton-Regular.ttf',
});

Font.register({
    family: 'Roboto Condensed',
    fonts: [
        {
            src: '/fonts/RobotoCondensed-Regular.ttf',
            fontWeight: 'normal',
            fontStyle: 'normal'
        },
        {
            src: '/fonts/RobotoCondensed-Bold.ttf',
            fontWeight: 'bold',
            fontStyle: 'normal'
        },
        {
            src: '/fonts/RobotoCondensed-Italic.ttf',
            fontWeight: 'normal',
            fontStyle: 'italic'
        },
        {
            src: '/fonts/RobotoCondensed-BoldItalic.ttf',
            fontWeight: 'bold',
            fontStyle: 'italic'
        }
    ]
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

    const dimensions = data.dimensions ? data.dimensions.split(', ') : [];

    return (
        <Document>
            <Page size="A4" style={styles.page}>
                {/* Header */}
                <View style={styles.header}>
                    <View style={styles.headerLeft}>
                        {logoUrl ? (
                            <Image src={logoUrl} style={styles.logo}/>
                        ) : (
                            <Text style={{fontSize: 12, fontWeight: 'bold'}}>{companyName}</Text>
                        )}
                    </View>
                    <View style={styles.headerRight}>
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
                        <View
                            style={[styles.cell, styles.cellWhite, styles.cellLast, {
                                width: '100%',
                                justifyContent: 'center',  // Wyśrodkowanie PIONOWE
                                alignItems: 'center',      // Wyśrodkowanie POZIOME kontenera
                                paddingBottom: 2,
                            }]}>
                            <Text style={[styles.boldText, {
                                fontSize: 14,
                                textAlign: "center",
                                lineHeight: 1,
                            }]}>PROTOKÓŁ
                                ODBIORU TECHNICZNEGO RUSZTOWANIA
                                NR: {data.scaffoldingNumber}</Text>
                        </View>
                    </View>

                    {/* WIERSZ 2 - Część informacyjna */}
                    <View style={styles.tableRow}>
                        <View
                            style={[styles.cell, styles.cellGrey, styles.cellLast, styles.sectionHeader, {
                                width: '100%',
                                justifyContent: "center",
                            }]}>
                            <Text>Część informacyjna:</Text>
                        </View>
                    </View>

                    {/* WIERSZ 3 (nr 1) - Wykonawca rusztowania */}
                    <View style={[styles.tableRow]}>
                        <View style={[styles.cell, styles.cellGrey, styles.cellNum]}>
                            <Text>1</Text>
                        </View>
                        <View style={[styles.cell, styles.cellGrey, styles.cellMedium, {
                            paddingLeft: 3,
                            paddingBottom: 2
                        }]}>
                            <Text style={styles.descriptionText}>Wykonawca rusztowania (przekazujący):</Text>
                        </View>
                        <View style={[styles.cell, styles.cellWhite, styles.cellLast, {
                            width: '61%',
                            paddingLeft: 3,
                            paddingBottom: 2
                        }]}>
                            <Text style={styles.descriptionTextNormal}>{data.companyName || ''}</Text>
                        </View>
                    </View>

                    {/* WIERSZ 4 (nr 2) - Zleceniodawca */}
                    <View style={styles.tableRow}>
                        <View style={[styles.cell, styles.cellGrey, styles.cellNum]}>
                            <Text>2</Text>
                        </View>
                        <View style={[styles.cell, styles.cellGrey, {
                            width: '26.25%',
                            paddingLeft: 3,
                            paddingBottom: 2,
                            justifyContent: 'center'
                        }]}>
                            <Text style={styles.descriptionText}>Zlecający(potwierdzający) </Text>
                            <Text style={styles.descriptionText}>wykonanie/przebudowę/zmianę lokalizacji*</Text>
                        </View>
                        <View style={[styles.cell, styles.cellGrey, {width: '8.75%'}]}>
                            <View style={styles.splitContainer}>
                                <View style={styles.splitCellTop}>
                                    <Text style={styles.subDescriptionText}>Firma</Text>
                                </View>
                                <View style={styles.splitCellBottom}>
                                    <Text style={styles.subDescriptionText}>Numer zlecenia</Text>
                                </View>
                            </View>
                        </View>
                        <View style={[styles.cell, styles.cellWhite, styles.cellLast, {width: '61%'}]}>
                            <View style={styles.splitContainer}>
                                <View style={styles.splitCellTop}>
                                    <Text style={styles.subDescriptionTextNormal}>{data.contractorName || ''}</Text>
                                </View>
                                <View style={styles.splitCellBottom}>
                                    <Text
                                        style={styles.subDescriptionTextNormal}>{/* Numer zlecenia - opcjonalne */}</Text>
                                </View>
                            </View>
                        </View>
                    </View>

                    {/* WIERSZ 5 (nr 3) - Użytkownik rusztowania */}
                    <View style={styles.tableRow}>
                        <View style={[styles.cell, styles.cellGrey, styles.cellNum]}>
                            <Text>3</Text>
                        </View>
                        <View style={[styles.cell, styles.cellGrey, {
                            width: '15%',
                            paddingLeft: 3,
                            paddingBottom: 2,
                            justifyContent: 'center'
                        }]}>
                            <Text style={styles.descriptionText}>Użytkownik rusztowania:</Text>
                        </View>
                        <View style={[styles.cell, styles.cellGrey, {width: '20%'}]}>
                            <View style={styles.splitContainer}>
                                <View style={styles.splitCellTop}>
                                    <Text style={styles.subDescriptionText}>Firma</Text>
                                </View>
                                <View style={styles.splitCellBottom}>
                                    <Text style={styles.subDescriptionText}>Imię i nazwisko </Text>
                                    <Text style={styles.subDescriptionTextSmall}>upoważnionego przedstawiciela
                                        firmy</Text>
                                </View>
                            </View>
                        </View>
                        <View style={[styles.cell, styles.cellWhite, styles.cellLast, {width: '61%'}]}>
                            <View style={styles.splitContainer}>
                                <View style={styles.splitCellTop}>
                                    <Text
                                        style={styles.subDescriptionTextNormal}>{data.scaffoldingUserName || ''}</Text>
                                </View>
                                <View style={styles.splitCellBottom}>
                                    <Text
                                        style={styles.subDescriptionTextNormal}>{data.scaffoldingUserContactLastName} {data.scaffoldingUserContactFirstName}</Text>
                                </View>
                            </View>
                        </View>
                    </View>

                    {/* WIERSZ 6 (nr 4) - Lokalizacja rusztowania */}
                    <View style={styles.tableRow}>
                        <View style={[styles.cell, styles.cellGrey, styles.cellNum]}>
                            <Text>4</Text>
                        </View>
                        <View style={[styles.cell, styles.cellGrey, styles.cellMedium, {
                            paddingLeft: 3,
                            paddingBottom: 2,
                            justifyContent: 'center'
                        }]}>
                            <Text style={styles.descriptionText}>Lokalizacja rusztowania:</Text>
                        </View>
                        <View style={[styles.cell, styles.cellWhite, styles.cellLast, {
                            width: '61%',
                            minHeight: 30,
                            paddingLeft: 3,
                            justifyContent: 'center'
                        }]}>
                            <Text style={styles.subDescriptionTextNormal}>{data.assemblyLocation || ''}</Text>
                        </View>
                    </View>

                    {/* WIERSZ 7 - Część techniczna */}
                    <View style={styles.tableRow}>
                        <View
                            style={[styles.cell, styles.cellGrey, styles.cellLast, styles.sectionHeader, {width: '100%'}]}>
                            <Text>Część techniczna:</Text>
                        </View>
                    </View>

                    {/* WIERSZ 8 (nr 5,6) */}
                    <View style={styles.tableRow}>
                        <View style={[styles.cell, styles.cellGrey, styles.cellNum]}>
                            <Text>5</Text>
                        </View>
                        <View style={[styles.cell, {width: '43.5%'}]}>
                            <View style={styles.splitContainer}>
                                <View style={[styles.splitCellTop, styles.cellGrey, {paddingBottom: 2}]}>
                                    <Text style={styles.descriptionText}>Rodzaj i przeznaczenie rusztowania:</Text>
                                </View>
                                <View style={[styles.splitCellBottom, styles.cellWhite, {paddingBottom: 2}]}>
                                    <Text
                                        style={styles.subDescriptionTextNormal}>{data.scaffoldingPurpose || 'Moduł, do prac spawalniczych rurociągów'}</Text>
                                </View>
                            </View>
                        </View>
                        <View style={[styles.cell, styles.cellGrey, styles.cellNum]}>
                            <Text>6</Text>
                        </View>
                        <View style={[styles.cell, styles.cellLast, {width: '48.5%'}]}>
                            <View style={styles.splitContainer}>
                                <View style={[styles.splitCellTop, styles.cellGrey, {paddingBottom: 2}]}>
                                    <Text style={styles.descriptionText}>Dopuszczalne obciążenie pomostów i konstrukcji
                                        rusztowania:</Text>
                                </View>
                                <View style={[styles.splitCellBottom, styles.cellWhite, {paddingBottom: 2}]}>
                                    <Text style={styles.descriptionTextNormal}>{data.loadLimit || '1,5kN/m2'}</Text>
                                </View>
                            </View>
                        </View>
                    </View>

                    {/* WIERSZ 9 (nr 7,8) */}
                    <View style={styles.tableRow}>
                        <View style={[styles.cell, styles.cellGrey, styles.cellNum]}>
                            <Text>7</Text>
                        </View>
                        <View style={[styles.cell, {width: '43.5%'}]}>
                            <View style={styles.splitContainer}>
                                <View style={[styles.splitCellTop, styles.cellGrey, {paddingBottom: 2}]}>
                                    <Text style={styles.descriptionText}>Oporność uziomu:</Text>
                                </View>
                                <View style={[styles.splitCellBottom, styles.cellWhite, {paddingBottom: 2}]}>
                                    <Text style={styles.descriptionTextNormal}>{data.earthingResistance || ''}</Text>
                                </View>
                            </View>
                        </View>
                        <View style={[styles.cell, styles.cellGrey, styles.cellNum]}>
                            <Text>8</Text>
                        </View>
                        <View style={[styles.cell, styles.cellLast, {width: '48.5%'}]}>
                            <View style={styles.splitContainer}>
                                <View style={[styles.splitCellTop, styles.cellGrey, {paddingBottom: 2}]}>
                                    <Text style={styles.descriptionText}>Data i godzina przekazania rusztowania do
                                        użytkowania</Text>
                                </View>
                                <View style={[styles.splitCellBottom, styles.cellWhite, {paddingBottom: 2}]}>
                                    <Text style={styles.descriptionTextNormal}>{data.assemblyDate}</Text>
                                </View>
                            </View>
                        </View>
                    </View>

                    {/* WIERSZ 10 (nr 9) - Wymiar rusztowania */}
                    <View style={styles.tableRow}>
                        <View style={[styles.cell, styles.cellGrey, styles.cellNum]}>
                            <Text>9</Text>
                        </View>
                        <View style={[styles.cell, styles.cellLast, {
                            width: '96%',
                            flexDirection: 'column',
                        }]}>
                            <View style={[styles.cellGrey, styles.cellGreyFullSize, {
                                paddingLeft: 3,
                                paddingBottom: 2,
                                justifyContent: "center"
                            }]}>
                                <Text style={styles.descriptionText}>Obmiar rusztowania:</Text>
                            </View>
                            <View style={[styles.cellWhite, {padding: 1, minHeight: 100}]}>
                                {dimensions.map((dim, index) => (
                                    <Text key={index} style={[styles.dimensionText, {paddingBottom: 3}]}>{dim}</Text>
                                ))}
                            </View>
                        </View>
                    </View>

                    {/* WIERSZ 11 (nr 10) - Terminarz przeglądów */}
                    <View style={styles.tableRow}>
                        <View style={[styles.cell, styles.cellGrey, styles.cellNum]}>
                            <Text>10</Text>
                        </View>
                        <View style={[styles.cell, styles.cellLast, {width: '96%', flexDirection: 'column'}]}>
                            <View style={[styles.cellGrey, styles.cellGreyFullSize, {
                                paddingLeft: 3,
                                paddingBottom: 2,
                                justifyContent: "center"
                            }]}>
                                <Text style={styles.descriptionText}>Terminy przeglądów rusztowania:</Text>
                            </View>
                            <View style={{flexDirection: 'row', borderBottomWidth: 1, borderBottomColor: '#000'}}>
                                <View style={[styles.cell, styles.cellGrey, {
                                    maxWidth: '12%',
                                    minHeight: 50,
                                    paddingLeft: 2,
                                    paddingBottom: 2,
                                    justifyContent: "center"
                                }]}>
                                    <Text style={styles.descriptionTextNormalBiggerCenter}>Data/godzina i czytelny
                                        podpis osoby
                                        dokonującej
                                        przeglądu</Text>
                                </View>
                                <View style={[styles.cell, styles.cellWhite, {width: '15%'}]}><Text> </Text></View>
                                <View style={[styles.cell, styles.cellWhite, {width: '15%'}]}><Text> </Text></View>
                                <View style={[styles.cell, styles.cellWhite, {width: '15%'}]}><Text> </Text></View>
                                <View style={[styles.cell, styles.cellWhite, {width: '15%'}]}><Text> </Text></View>
                                <View style={[styles.cell, styles.cellWhite, {width: '15%'}]}><Text> </Text></View>
                                <View
                                    style={[styles.cell, styles.cellWhite, styles.cellLast, {width: '15%'}]}><Text> </Text></View>
                            </View>
                            <View style={{flexDirection: 'row'}}>
                                <View style={[styles.cell, styles.cellGrey, {
                                    maxWidth: '12%',
                                    minHeight: 50,
                                    paddingLeft: 2,
                                    paddingBottom: 2,
                                    justifyContent: "center"
                                }]}>
                                    <Text style={styles.descriptionTextNormalBiggerCenter}>Data/godzina i
                                        czytelny podpis osoby dokonującej przeglądu</Text>
                                </View>
                                <View style={[styles.cell, styles.cellWhite, {width: '15%'}]}><Text> </Text></View>
                                <View style={[styles.cell, styles.cellWhite, {width: '15%'}]}><Text> </Text></View>
                                <View style={[styles.cell, styles.cellWhite, {width: '15%'}]}><Text> </Text></View>
                                <View style={[styles.cell, styles.cellWhite, {width: '15%'}]}><Text> </Text></View>
                                <View style={[styles.cell, styles.cellWhite, {width: '15%'}]}><Text> </Text></View>
                                <View
                                    style={[styles.cell, styles.cellWhite, styles.cellLast, {width: '15%'}]}><Text> </Text></View>
                            </View>
                        </View>
                    </View>

                    {/* WIERSZ 12 (nr 11) - Deklaracje */}
                    <View style={styles.tableRow}>
                        <View style={[styles.cell, styles.cellGrey, styles.cellNum]}>
                            <Text>11</Text>
                        </View>
                        <View style={[styles.cell, styles.cellGrey, styles.cellLast, {
                            width: '96%',
                            padding: 3,
                            fontSize: 8
                        }]}>
                            <Text><Text style={styles.boldText}>Wykonawca </Text>(przekazujący rusztowanie) oświadcza,
                                że:</Text>
                            <Text>a) rusztowanie opisane niniejszym protokołem jest kompletne i zostało zmontowane
                                zgodnie ze sztuką budowlaną, normami, dokumentacją techniczno-eksploatacyjną i
                                instrukcją montażu wydaną przez producenta lub projektem indywidualnym oraz spełnia
                                wymagania bezpieczeństwa i higieny pracy;</Text>
                            <Text>b) montaż wykonali uprawnieni monterzy rusztowań</Text>
                            <Text>c) rusztowanie nadaje się do eksploatacji.</Text>
                            <Text><Text style={styles.boldText}>Wykonawca </Text> zobowiązuje się do przeprowadzenia w
                                czasie eksploatacji rusztowania
                                przeglądów jego stanu technicznego zgodnie z obowiązującymi przepisami jak również na
                                doraźne polecenie użytkownika rusztowania.</Text>
                        </View>
                    </View>

                    {/* WIERSZ 13 (nr 12) */}
                    <View style={styles.tableRow}>
                        <View style={[styles.cell, styles.cellGrey, styles.cellNum]}>
                            <Text>12</Text>
                        </View>
                        <View style={[styles.cell, styles.cellGrey, styles.cellLast, {
                            width: '96%',
                            padding: 3,
                            fontSize: 8
                        }]}>
                            <Text><Text style={styles.boldText}>Użytkownik </Text> rusztowania (odbierający) oświadcza,
                                że:</Text>
                            <Text>a) przejmuje rusztowanie do użytkowania;</Text>
                            <Text>b) zobowiązuje się, że nie będzie prowadził żadnych prac związanych z przestawianiem,
                                przebudową oraz innych prac ingerujących w konstrukcję użytkowanego rusztowania;</Text>
                            <Text>c) będzie zgłaszał Wykonawcy rusztowania potrzeby wykonania przeglądu rusztowania w
                                przypadku stwierdzenia wszelkich nieprawidłowości stwarzających zagrożenie dla
                                bezpieczeństwa wykonywanych prac oraz do zabezpieczenia rusztowania przed użytkowaniem,
                                do czasu przeprowadzenia przez Wykonawcę przeglądu i potwierdzenia możliwości dalszego
                                użytkowania rusztowania.</Text>
                        </View>
                    </View>

                    {/* WIERSZ 14 (nr 13) - Podpisy */}
                    <View style={styles.tableRow}>
                        <View style={[styles.cell, styles.cellWhite, {
                            width: '33.33%',
                            padding: 1,
                            minHeight: 60,
                            justifyContent: "flex-end"
                        }]}>
                            <Text style={[styles.smallText, {textAlign: 'center'}]}>
                                Wykonawca (przekazujący rusztowanie)
                            </Text>
                        </View>
                        <View style={[styles.cell, styles.cellWhite, {
                            width: '33.33%',
                            padding: 1,
                            minHeight: 60,
                            justifyContent: "flex-end"
                        }]}>
                            <Text style={[styles.smallText, {textAlign: 'center'}]}>
                                Zlecający(potwierdzający wykonanie robót)
                            </Text>
                        </View>
                        <View style={[styles.cell, styles.cellWhite, styles.cellLast, {
                            width: '33.34%',
                            padding: 1,
                            minHeight: 60,
                            justifyContent: "flex-end"
                        }]}>
                            <Text style={[styles.smallText, {textAlign: 'center'}]}>
                                Użytkownik (odbierający rusztowanie)
                            </Text>
                        </View>
                    </View>

                    {/* WIERSZ 15 (nr 13) */}
                    <View style={styles.tableRow}>
                        <View style={[styles.cell, styles.cellGrey, styles.cellNum]}>
                            <Text>13</Text>
                        </View>
                        <View style={[styles.cell, styles.cellLast, {width: '96%', flexDirection: 'column'}]}>
                            <View style={[styles.cellGrey, styles.cellGreyFullSize, {
                                paddingBottom: 2,
                                paddingLeft: 3,
                                justifyContent: "center"
                            }]}>
                                <Text style={styles.descriptionText}>Rusztowanie opisane niniejszym protokołem zostało
                                    zdemontowane:</Text>
                            </View>
                            <View style={[styles.cellWhite, {width: '100%', flexDirection: "row", minHeight: 40}]}>
                                <View style={[styles.cellWithoutBorder, styles.cellWhite, {
                                    width: '50%',
                                    justifyContent: "flex-end"
                                }]}>
                                    <Text style={[{
                                        textAlign: 'left',
                                        marginLeft: 30,
                                        fontSize: 6
                                    }]}>
                                        Data/Godzina
                                    </Text>
                                </View>
                                <View style={[styles.cellWithoutBorder, styles.cellWhite, {
                                    width: '50%',
                                    justifyContent: "flex-end"
                                }]}>
                                    <Text
                                        style={[{
                                            textAlign: 'right',
                                            marginRight: 30,
                                            fontSize: 6
                                        }]}>
                                        Podpis wykonawcy
                                    </Text>
                                </View>
                            </View>
                        </View>
                    </View>

                    {/* WIERSZ 16 - Informacje */}
                    <View style={[styles.tableRow, styles.tableRowLast]}>
                        <View style={[styles.cell, styles.cellGrey, styles.cellLast, {
                            width: '100%',
                            textAlign: 'left',
                            paddingLeft: 3,
                            fontSize: 6
                        }]}>
                            <Text>--- *Niepotrzebne skreślić</Text>
                        </View>
                    </View>

                </View>

                {/* Footer */}
                <View style={styles.footer}>
                    <Text>Stopka będzie poprawiona</Text>
                    <Text>Stopka będzie poprawiona</Text>
                    <Text>Stopka będzie poprawiona</Text>
                    <Text>Stopka będzie poprawiona</Text>
                    <Text>Stopka będzie poprawiona</Text>
                    <Text>Stopka będzie poprawiona</Text>
                </View>
            </Page>
        </Document>
    );
};

export default TechnicalProtocolPDF;
