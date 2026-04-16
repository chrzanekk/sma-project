// src/components/scaffolding/protocol/TechnicalProtocolPDF.tsx

import React from 'react';
import {Document, Font, Image, Page, Text, View} from '@react-pdf/renderer';
import {TechnicalProtocolData} from '@/types/technical-protocol-types';
import {getSelectedCompany} from "@/utils/company-utils.ts";
import {styles} from './technicalProtocolPDFstyles';
import {Trans, useTranslation} from "react-i18next";

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
    const {t} = useTranslation('technicalProtocols');


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
                            }]}>{t('technicalProtocols:titleUppercase')} {data.scaffoldingNumber}</Text>
                        </View>
                    </View>

                    {/* WIERSZ 2 - Część informacyjna */}
                    <View style={styles.tableRow}>
                        <View
                            style={[styles.cell, styles.cellGrey, styles.cellLast, styles.sectionHeader, {
                                width: '100%',
                                justifyContent: "center",
                            }]}>
                            <Text>{t('technicalProtocols:informationPart')}</Text>
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
                            <Text style={styles.descriptionText}>{t('technicalProtocols:scaffoldingPerformer')}</Text>
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
                            <Text style={styles.descriptionText}>{t('technicalProtocols:scaffoldingAcceptor')} </Text>
                        </View>
                        <View style={[styles.cell, styles.cellGrey, {width: '8.75%'}]}>
                            <View style={styles.splitContainer}>
                                <View style={styles.splitCellTop}>
                                    <Text style={styles.subDescriptionText}>{t('technicalProtocols:company')}</Text>
                                </View>
                                <View style={styles.splitCellBottom}>
                                    <Text style={styles.subDescriptionText}>{t('technicalProtocols:orderNumber')}</Text>
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
                            <Text style={styles.descriptionText}>{t('technicalProtocols:scaffoldingUser')}</Text>
                        </View>
                        <View style={[styles.cell, styles.cellGrey, {width: '20%'}]}>
                            <View style={styles.splitContainer}>
                                <View style={styles.splitCellTop}>
                                    <Text
                                        style={styles.subDescriptionText}>{t('technicalProtocols:scaffoldingUserCompany')}</Text>
                                </View>
                                <View style={styles.splitCellBottom}>
                                    <Text
                                        style={styles.subDescriptionText}>{t('technicalProtocols:firstAndLastNameOfScaffoldingUserContactPart1')}</Text>
                                    <Text
                                        style={styles.subDescriptionTextSmall}>{t('technicalProtocols:firstAndLastNameOfScaffoldingUserContactPart2')}</Text>
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
                            <Text
                                style={styles.descriptionText}>{t('technicalProtocols:scaffoldingLocalisation')}</Text>
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
                            <Text>{t('technicalProtocols:technicalPart')}</Text>
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
                                    <Text style={styles.descriptionText}>{t('technicalProtocols:typeAndPurpose')}</Text>
                                </View>
                                <View style={[styles.splitCellBottom, styles.cellWhite, {paddingBottom: 2}]}>
                                    <Text
                                        style={styles.subDescriptionTextNormal}>{data.scaffoldingPurpose || t('technicalProtocols:defaultTypeAndPurpose')}</Text>
                                </View>
                            </View>
                        </View>
                        <View style={[styles.cell, styles.cellGrey, styles.cellNum]}>
                            <Text>6</Text>
                        </View>
                        <View style={[styles.cell, styles.cellLast, {width: '48.5%'}]}>
                            <View style={styles.splitContainer}>
                                <View style={[styles.splitCellTop, styles.cellGrey, {paddingBottom: 2}]}>
                                    <Text style={styles.descriptionText}>{t('technicalProtocols:loadLimit')}</Text>
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
                                    <Text
                                        style={styles.descriptionText}>{t('technicalProtocols:earthingResistance')}</Text>
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
                                    <Text
                                        style={styles.descriptionText}>{t('technicalProtocols:dateAndTimeOfPuttingScaffoldingToUse')}</Text>
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
                                <Text
                                    style={styles.descriptionText}>{t('technicalProtocols:scaffoldingDimensions')}</Text>
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
                                <Text
                                    style={styles.descriptionText}>{t('technicalProtocols:scaffoldingInspectionDates')}</Text>
                            </View>
                            <View style={{flexDirection: 'row', borderBottomWidth: 1, borderBottomColor: '#000'}}>
                                <View style={[styles.cell, styles.cellGrey, {
                                    maxWidth: '12%',
                                    minHeight: 50,
                                    paddingLeft: 2,
                                    paddingBottom: 2,
                                    justifyContent: "center"
                                }]}>
                                    <Text
                                        style={styles.descriptionTextNormalBiggerCenter}>{t('technicalProtocols:signatureOfInspectionWithDate')}</Text>
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
                                    <Text
                                        style={styles.descriptionTextNormalBiggerCenter}>{t('technicalProtocols:signatureOfInspectionWithDate')}</Text>
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
                            <Text>
                                <Trans
                                    i18nKey="technicalProtocols:constructorInfoLine1"
                                    components={[<Text key="0" style={styles.boldText}/>]}
                                />
                            </Text>
                            <Text>{t('technicalProtocols:constructorInfoLine2')}</Text>
                            <Text>{t('technicalProtocols:constructorInfoLine3')}</Text>
                            <Text>{t('technicalProtocols:constructorInfoLine4')}</Text>
                            <Text>
                                <Trans
                                    i18nKey="technicalProtocols:constructorInfoLine5"
                                    components={[<Text key="0" style={styles.boldText}/>]}
                                />
                            </Text>
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
                            <Text>
                                <Trans
                                    i18nKey="technicalProtocols:userInfoLine1"
                                    components={[<Text key="0" style={styles.boldText}/>]}
                                />
                            </Text>
                            <Text>{t('technicalProtocols:userInfoLine2')}</Text>
                            <Text>{t('technicalProtocols:userInfoLine3')}</Text>
                            <Text>{t('technicalProtocols:userInfoLine4')}</Text>
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
                                {t('technicalProtocols:signatureConstructor')}
                            </Text>
                        </View>
                        <View style={[styles.cell, styles.cellWhite, {
                            width: '33.33%',
                            padding: 1,
                            minHeight: 60,
                            justifyContent: "flex-end"
                        }]}>
                            <Text style={[styles.smallText, {textAlign: 'center'}]}>
                                {t('technicalProtocols:ordererConstructor')}
                            </Text>
                        </View>
                        <View style={[styles.cell, styles.cellWhite, styles.cellLast, {
                            width: '33.34%',
                            padding: 1,
                            minHeight: 60,
                            justifyContent: "flex-end"
                        }]}>
                            <Text style={[styles.smallText, {textAlign: 'center'}]}>
                                {t('technicalProtocols:signatureUser')}
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
                                <Text style={styles.descriptionText}>{t('technicalProtocols:dismantlingInfo')}</Text>
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
                                        {t('technicalProtocols:dateAndTime')}
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
                                        {t('technicalProtocols:constructorSignature')}
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
                            <Text>{t('technicalProtocols:deleteAsAppropriate')}</Text>
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
