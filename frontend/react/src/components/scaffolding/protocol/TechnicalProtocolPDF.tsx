// src/components/scaffolding/protocol/TechnicalProtocolPDF.tsx

import React from 'react';
import {
    Document,
    Page,
    Text,
    View,
    StyleSheet,
    Image,
    Font
} from '@react-pdf/renderer';
import { TechnicalProtocolData } from '@/types/technical-protocol-types';

// Rejestracja czcionek (opcjonalnie, dla polskich znaków)
Font.register({
    family: 'Roboto',
    fonts: [
        { src: 'https://cdnjs.cloudflare.com/ajax/libs/ink/3.1.10/fonts/Roboto/roboto-regular-webfont.ttf' },
        { src: 'https://cdnjs.cloudflare.com/ajax/libs/ink/3.1.10/fonts/Roboto/roboto-bold-webfont.ttf', fontWeight: 'bold' }
    ]
});
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
        padding: 20,
        fontFamily: 'Archivo Narrow',
        fontSize: 9,
        lineHeight: 1.3,
    },
    header: {
        flexDirection: 'row',
        justifyContent: 'space-between',
        alignItems: 'center',
        marginBottom: 15,
        borderBottom: 2,
        borderBottomColor: '#000',
        paddingBottom: 10,
    },
    logo: {
        width: 80,
        height: 60,
    },
    companyInfo: {
        textAlign: 'right',
        fontSize: 7,
        fontFamily: "Archivo Narrow"
    },
    companyName: {
        fontSize: 11,
        fontWeight: 'bold',
        marginBottom: 2,
        fontFamily: "Anton"
    },
    title: {
        fontSize: 10,
        fontWeight: 'bold',
        textAlign: 'center',
        marginBottom: 10,
        textTransform: 'uppercase',
        borderBottom: 1,
        borderBottomColor: '#000',
        paddingBottom: 5,
    },
    section: {
        marginBottom: 8,
    },
    sectionTitle: {
        fontSize: 9,
        fontWeight: 'bold',
        backgroundColor: '#e0e0e0',
        padding: 4,
        marginBottom: 4,
    },
    table: {
        width: '100%',
        borderWidth: 1,
        borderColor: '#000',
    },
    tableRow: {
        flexDirection: 'row',
        borderBottomWidth: 1,
        borderBottomColor: '#000',
        minHeight: 20,
    },
    tableRowLast: {
        borderBottomWidth: 0,
    },
    tableCell: {
        padding: 4,
        borderRightWidth: 1,
        borderRightColor: '#000',
        justifyContent: 'center',
    },
    tableCellLast: {
        borderRightWidth: 0,
    },
    tableCellSmall: {
        width: '5%',
    },
    tableCellMedium: {
        width: '35%',
    },
    tableCellLarge: {
        width: '60%',
    },
    imageContainer: {
        width: '100%',
        height: 140,
        backgroundColor: '#f0f0f0',
        justifyContent: 'center',
        alignItems: 'center',
        marginVertical: 5,
    },
    imagePlaceholder: {
        fontSize: 8,
        color: '#666',
    },
    footer: {
        position: 'absolute',
        bottom: 15,
        left: 20,
        right: 20,
        fontSize: 7,
        textAlign: 'center',
        color: '#666',
        borderTop: 1,
        borderTopColor: '#ccc',
        paddingTop: 5,
    },
    signatureRow: {
        flexDirection: 'row',
        justifyContent: 'space-between',
        marginTop: 15,
        marginBottom: 5,
    },
    signatureBox: {
        width: '30%',
        textAlign: 'center',
        fontSize: 8,
    },
    smallText: {
        fontSize: 7,
        color: '#444',
        marginTop: 2,
    },
});

interface TechnicalProtocolPDFProps {
    data: TechnicalProtocolData;
    logoUrl?: string;
}

const TechnicalProtocolPDF: React.FC<TechnicalProtocolPDFProps> = ({ data, logoUrl }) => {
    const companyAddress = "ul. 1000-lecia Państwa Polskiego 21\n24-100 Puławy, Polska(Poland)";
    const companyContact = "Tel: (+48) 81 473 13 33\ne-mail: biuro@rchscaffolding.pl\nNIP(VAT/TAX ID): PL9462731518";

    return (
        <Document>
            {/* Strona 1 - Informacje ogólne */}
            <Page size="A4" style={styles.page}>
                {/* Header */}
                <View style={styles.header}>
                    <View>
                        {logoUrl ? (
                            <Image src={logoUrl} style={styles.logo} />
                        ) : (
                            <Text style={{ fontSize: 14, fontWeight: 'bold' }}>RCH SCAFFOLDING</Text>
                        )}
                    </View>
                    <View style={styles.companyInfo}>
                        <Text style={styles.companyName}>RCH SCAFFOLDING Sp. z o. o.</Text>
                        <Text>{companyAddress}</Text>
                        <Text>{companyContact}</Text>
                    </View>
                </View>

                {/* Tytuł */}
                <View style={styles.title}>
                    <Text>SCAFFOLDING TECHNICAL ACCEPTANCE PROTOCOL No: {data.scaffoldingNumber}</Text>
                </View>

                {/* Information part */}
                <View style={styles.section}>
                    <Text style={styles.sectionTitle}>Information part:</Text>
                    <View style={styles.table}>
                        {/* Row 1 */}
                        <View style={styles.tableRow}>
                            <View style={[styles.tableCell, styles.tableCellSmall]}>
                                <Text>1</Text>
                            </View>
                            <View style={[styles.tableCell, styles.tableCellMedium]}>
                                <Text>Scaffolding performer (installers):</Text>
                            </View>
                            <View style={[styles.tableCell, styles.tableCellLarge, styles.tableCellLast]}>
                                <Text>{data.companyName || ''}</Text>
                            </View>
                        </View>

                        {/* Row 2 */}
                        <View style={styles.tableRow}>
                            <View style={[styles.tableCell, styles.tableCellSmall]}>
                                <Text>2</Text>
                            </View>
                            <View style={[styles.tableCell, styles.tableCellMedium]}>
                                <Text>Scaffolding erecting party (ConfIrmer):</Text>
                                <Text style={styles.smallText}>contractor / superintendent / tenant</Text>
                            </View>
                            <View style={[styles.tableCell, styles.tableCellLarge, styles.tableCellLast]}>
                                <Text style={{ fontWeight: 'bold' }}>Company Name:</Text>
                                <Text>{data.contractorName || ''}</Text>
                            </View>
                        </View>

                        {/* Row 3 */}
                        <View style={styles.tableRow}>
                            <View style={[styles.tableCell, styles.tableCellSmall]}>
                                <Text>3</Text>
                            </View>
                            <View style={[styles.tableCell, styles.tableCellMedium]}>
                                <Text>Scaffolding User:</Text>
                            </View>
                            <View style={[styles.tableCell, styles.tableCellLarge, styles.tableCellLast]}>
                                <Text style={{ fontWeight: 'bold' }}>Company Name:</Text>
                                <Text>{data.scaffoldingUserName || ''}</Text>
                                <Text style={{ fontWeight: 'bold', marginTop: 3 }}>Name of the authorized person:</Text>
                                <Text>{data.scaffoldingUserContactLastName || ''} {data.scaffoldingUserContactFirstName || ''}</Text>
                            </View>
                        </View>

                        {/* Row 4 */}
                        <View style={[styles.tableRow, styles.tableRowLast]}>
                            <View style={[styles.tableCell, styles.tableCellSmall]}>
                                <Text>4</Text>
                            </View>
                            <View style={[styles.tableCell, styles.tableCellMedium]}>
                                <Text>Scaffolding location:</Text>
                            </View>
                            <View style={[styles.tableCell, styles.tableCellLarge, styles.tableCellLast]}>
                                <Text>{data.assemblyLocation || ''}</Text>
                            </View>
                        </View>
                    </View>
                </View>

                {/* Technical part */}
                <View style={styles.section}>
                    <Text style={styles.sectionTitle}>Technical part:</Text>
                    <View style={styles.table}>
                        {/* Row 5 */}
                        <View style={styles.tableRow}>
                            <View style={[styles.tableCell, styles.tableCellSmall]}>
                                <Text>5</Text>
                            </View>
                            <View style={[styles.tableCell, { width: '35%' }]}>
                                <Text>Scaffolding type and purpose:</Text>
                            </View>
                            <View style={[styles.tableCell, { width: '30%' }]}>
                                <Text>{data.scaffoldingPurpose || ''}</Text>
                            </View>
                            <View style={[styles.tableCell, { width: '30%' }, styles.tableCellLast]}>
                                <Text style={{ fontWeight: 'bold' }}>Load limit for platforms and scaffolding construction:</Text>
                                <Text>{data.loadLimit || '2,0 kN / m²'}</Text>
                                <Text style={{ fontSize: 7, marginTop: 2 }}>Safe r flour of scaffolding transfer for use:</Text>
                            </View>
                        </View>

                        {/* Row 7 */}
                        <View style={styles.tableRow}>
                            <View style={[styles.tableCell, styles.tableCellSmall]}>
                                <Text>7</Text>
                            </View>
                            <View style={[styles.tableCell, { width: '35%' }]}>
                                <Text>Existing resistance:</Text>
                            </View>
                            <View style={[styles.tableCell, { width: '60%' }, styles.tableCellLast]}>
                                <Text>{data.earthingResistance || ''}</Text>
                            </View>
                        </View>

                        {/* Row 8 */}
                        <View style={styles.tableRow}>
                            <View style={[styles.tableCell, styles.tableCellSmall]}>
                                <Text>8</Text>
                            </View>
                            <View style={[styles.tableCell, { width: '95%' }, styles.tableCellLast]}>
                                <Text>Scaffolding dimensions (with picture if needed):</Text>
                                <Text style={{ marginTop: 3, fontWeight: 'bold' }}>{data.dimensions || ''}</Text>
                                <Text style={{ marginTop: 3 }}>Date: {data.assemblyDate || ''}</Text>
                            </View>
                        </View>

                        {/* Row 9 - Additional information */}
                        <View style={[styles.tableRow, styles.tableRowLast]}>
                            <View style={[styles.tableCell, styles.tableCellSmall]}>
                                <Text>10</Text>
                            </View>
                            <View style={[styles.tableCell, { width: '95%' }, styles.tableCellLast]}>
                                <Text style={{ fontWeight: 'bold' }}>Additional information:</Text>
                                <Text style={{ marginTop: 3 }}>{data.additionalInfo || ''}</Text>
                            </View>
                        </View>
                    </View>
                </View>

                {/* Declarations */}
                <View style={[styles.section, { fontSize: 7 }]}>
                    <Text style={{ fontWeight: 'bold', marginBottom: 3 }}>
                        Performer (scaffolder's) declares that:
                    </Text>
                    <Text>
                        a) scaffolding described in the protocol is assembled and it was approved according to the building law, salvage, occupational and technical documentation and assumes in...
                    </Text>
                    <Text style={{ marginTop: 2 }}>
                        b) scaffolding user performer who for justified scaffolders....
                    </Text>
                </View>

                {/* Signatures */}
                <View style={styles.signatureRow}>
                    <View style={styles.signatureBox}>
                        <Text>Performed (scaffolding erector/user):</Text>
                        <Text style={{ marginTop: 15 }}>_____________________</Text>
                        <Text style={styles.smallText}>Date / hour / signature</Text>
                    </View>
                    <View style={styles.signatureBox}>
                        <Text>Checked by / confirmation of works performed:</Text>
                        <Text style={{ marginTop: 15 }}>_____________________</Text>
                        <Text style={styles.smallText}>Date / hour / signature</Text>
                    </View>
                    <View style={styles.signatureBox}>
                        <Text>User (scaffolding receiver):</Text>
                        <Text style={{ marginTop: 15 }}>_____________________</Text>
                        <Text style={styles.smallText}>Date / hour / signature</Text>
                    </View>
                </View>

                {/* Footer */}
                <View style={styles.footer}>
                    <Text>RCH Scaffolding Sp. z o.o., ul. 1000-lecia Państwa Polskiego 21, 24-100 Puławy</Text>
                    <Text>KRS: 0000 933 000 | REGON: 52 144 244 111 | NIP: 5252 1436 2906 | e-mail: biuro@rchscaffolding.pl</Text>
                    <Text>PLN: PL60 1140 2004 0000 3002 8000 1000, EUR: PL75 1140 2004 0000 3112 0000 1000</Text>
                </View>
            </Page>

            {/* Strona 2 - Maintenance part (tabela) */}
            <Page size="A4" style={styles.page}>
                {/* Header - uproszczony dla strony 2 */}
                <View style={[styles.header, { marginBottom: 10 }]}>
                    <Text style={styles.companyName}>RCH SCAFFOLDING Sp. z o. o.</Text>
                </View>

                <View style={styles.section}>
                    <Text style={styles.sectionTitle}>Maintance part:</Text>
                    <Text style={{ fontSize: 8, marginBottom: 5 }}>Scaffolding no: {data.scaffoldingNumber}</Text>

                    <View style={styles.table}>
                        {/* Header row */}
                        <View style={[styles.tableRow, { backgroundColor: '#e0e0e0' }]}>
                            <View style={[styles.tableCell, { width: '8%' }]}>
                                <Text style={{ fontWeight: 'bold' }}>Inspections</Text>
                            </View>
                            <View style={[styles.tableCell, { width: '10%' }]}>
                                <Text style={{ fontWeight: 'bold' }}>Date and hour</Text>
                            </View>
                            <View style={[styles.tableCell, { width: '25%' }]}>
                                <Text style={{ fontWeight: 'bold' }}>Name and surname of person carrying inspection</Text>
                            </View>
                            <View style={[styles.tableCell, { width: '20%' }]}>
                                <Text style={{ fontWeight: 'bold' }}>Findings after carrying inspection (possible remarks)</Text>
                            </View>
                            <View style={[styles.tableCell, { width: '22%' }]}>
                                <Text style={{ fontWeight: 'bold' }}>Comments</Text>
                            </View>
                            <View style={[styles.tableCell, { width: '15%' }, styles.tableCellLast]}>
                                <Text style={{ fontWeight: 'bold' }}>Date of next inspection</Text>
                            </View>
                        </View>

                        {/* Empty rows for manual filling */}
                        {[...Array(14)].map((_, index) => (
                            <View key={index} style={[styles.tableRow, index === 13 ? styles.tableRowLast : {}]}>
                                <View style={[styles.tableCell, { width: '8%', minHeight: 30 }]}>
                                    <Text> </Text>
                                </View>
                                <View style={[styles.tableCell, { width: '10%' }]}>
                                    <Text> </Text>
                                </View>
                                <View style={[styles.tableCell, { width: '25%' }]}>
                                    <Text> </Text>
                                </View>
                                <View style={[styles.tableCell, { width: '20%' }]}>
                                    <Text> </Text>
                                </View>
                                <View style={[styles.tableCell, { width: '22%' }]}>
                                    <Text> </Text>
                                </View>
                                <View style={[styles.tableCell, { width: '15%' }, styles.tableCellLast]}>
                                    <Text> </Text>
                                </View>
                            </View>
                        ))}
                    </View>
                </View>

                {/* Footer */}
                <View style={styles.footer}>
                    <Text>RCH Scaffolding Sp. z o.o., ul. 1000-lecia Państwa Polskiego 21, 24-100 Puławy</Text>
                    <Text>str. 2 z 2</Text>
                </View>
            </Page>
        </Document>
    );
};

export default TechnicalProtocolPDF;
