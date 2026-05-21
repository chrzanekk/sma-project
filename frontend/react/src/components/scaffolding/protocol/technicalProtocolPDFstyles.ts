// src/components/scaffolding/protocol/technicalProtocolPDFstyles.ts

import {StyleSheet} from '@react-pdf/renderer';

const ROW_SPLIT_HEIGHT = 25; // zwiększona wysokość

export const styles = StyleSheet.create({
    page: {
        padding: 15,
        fontFamily: 'Roboto Condensed',
        fontSize: 8,
        lineHeight: 1,
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
    headerLeft: {
        width: '50%',
    },
    headerRight: {
        width: '50%',
        alignItems: 'flex-end',
    },
    logo: {
        width: 70,
        height: 50,
    },
    companyInfo: {
        textAlign: 'right',
        fontSize: 7,
        fontFamily: 'Roboto Condensed',
        paddingBottom: 0.5
    },
    companyName: {
        textAlign: 'right',
        fontSize: 10,
        paddingBottom: 5,
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
        padding: 0,
        borderRightWidth: 1,
        borderRightColor: '#000',
        fontSize: 7,
    },
    cellWithoutBorder: {
        padding: 0,
        fontSize: 7,
    },
    cellGreyFullSize: {
        borderBottomWidth: 1,
        borderBottomColor: '#000'
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

    cellNum: {
        width: '4%',
        justifyContent: 'center',
        alignItems: 'center',
        fontWeight: 'bold',
    },
    cellMedium: {
        width: '35%',
    },

    // komórki dzielone na górę/dół
    splitContainer: {
        flexDirection: 'column',
        minHeight: ROW_SPLIT_HEIGHT,
    },

    splitCellTop: {
        flex: 1,
        paddingLeft: 3,
        borderBottomWidth: 1,
        borderBottomColor: '#000',
        fontSize: 7,
        justifyContent: "center"
    },
    splitCellBottom: {
        flex: 1,
        paddingLeft: 3,
        fontSize: 7,
        justifyContent: "center"
    },

    // Tytuł protokołu
    titleRow: {
        padding: 4,
        textAlign: 'center',
        fontWeight: 'bold',
        fontSize: 9,
        justifyContent: "center"
    },

    // Sekcja nagłówkowa
    sectionHeader: {
        fontWeight: 'bold',
        fontSize: 10,
        lineHeight: 1,
        paddingBottom: 2,
        paddingLeft: 1
    },

    // Tekst
    boldText: {
        fontWeight: 'bold',
    },
    normalText: {
        fontWeight: "normal",
    },

    smallText: {
        fontSize: 7,
    },

    descriptionText: {
        textAlign: "left",
        fontSize: 8,
        fontWeight: "bold"
    },
    descriptionTextNormal: {
        textAlign: "left",
        fontSize: 8,
        fontWeight: "normal"
    },
    descriptionTextNormalBiggerCenter: {
        textAlign: "left",
        fontSize: 9,
        fontWeight: "normal"
    },

    subDescriptionText: {
        textAlign: "left",
        fontSize: 6,
        fontWeight: "bold"
    },
    subDescriptionTextSmall: {
        textAlign: "left",
        fontSize: 5,
        fontWeight: "bold"
    },

    subDescriptionTextNormal: {
        textAlign: "left",
        fontSize: 7,
        fontWeight: "normal"
    },
    dimensionText: {
        textAlign: "left",
        fontSize: 10,
        fontWeight: "normal"
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
});