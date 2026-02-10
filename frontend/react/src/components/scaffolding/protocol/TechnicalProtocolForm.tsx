// src/components/scaffolding/protocol/TechnicalProtocolForm.tsx

import React, {useState} from 'react';
import {Box, Button, Grid, GridItem, Heading, Input, Text, Textarea} from '@chakra-ui/react'; // Dodano Input, Text, Textarea
import {useTranslation} from 'react-i18next';
import {PDFDownloadLink} from '@react-pdf/renderer';
import {useThemeColors} from '@/theme/theme-colors';
import {themeVars} from "@/theme/theme-colors"; // Dodano themeVars
import {BaseScaffoldingLogPositionFormValues} from '@/types/scaffolding-log-position-types';
import {TechnicalProtocolData} from '@/types/technical-protocol-types';
import TechnicalProtocolPDF from './TechnicalProtocolPDF';
import {getSelectedCompany} from "@/utils/company-utils.ts";

// --- Lokalne komponenty UI (zamiast tych z Formika) ---
// Dzięki temu nie musisz wrapować tego w <Formik> ani psuć głównego CustomTextAreaField

const SimpleInputField = ({ label, value, onChange, placeholder }: any) => {
    const themeColors = useThemeColors();
    return (
        <Box mb={2}>
            {label && (
                <Text fontSize="sm" fontWeight="bold" mb="1" color={themeColors.fontColor} textAlign={"center"}>
                    {label}
                </Text>
            )}
            <Input
                value={value}
                onChange={onChange}
                placeholder={placeholder}
                size="sm"
                color={themeColors.fontColor}
                bg={themeVars.bgColorPrimary}
                borderRadius="md"
            />
        </Box>
    );
};

const SimpleTextAreaField = ({ label, value, onChange, placeholder, rows = 3 }: any) => {
    const themeColors = useThemeColors();
    return (
        <Box mb={2}>
            {label && (
                <Text fontSize="sm" fontWeight="bold" mb="1" color={themeColors.fontColor} textAlign={"center"}>
                    {label}
                </Text>
            )}
            <Textarea
                value={value}
                onChange={onChange}
                placeholder={placeholder}
                size="sm"
                color={themeColors.fontColor}
                bg={themeVars.bgColorPrimary}
                borderRadius="md"
                rows={rows}
            />
        </Box>
    );
};
// -----------------------------------------------------

interface TechnicalProtocolFormProps {
    position: BaseScaffoldingLogPositionFormValues;
}

const TechnicalProtocolForm: React.FC<TechnicalProtocolFormProps> = ({position}) => {
    const {t} = useTranslation(['common', 'scaffoldingLogPositions','companies']);
    const themeColors = useThemeColors();
    const selectedCompany = getSelectedCompany();


    const formatDimensions = () => {
        if (!position.dimensions || position.dimensions.length === 0) return '';

        return position.dimensions
            .filter(dim => dim.length || dim.width || dim.height)
            .map(dim => `${dim.length || '?'}m x ${dim.width || '?'}m x ${dim.height || '?'}m`)
            .join(', ');
    };

    const [protocolData, setProtocolData] = useState<TechnicalProtocolData>({
        scaffoldingNumber: position.scaffoldingNumber || '',
        companyName: selectedCompany?.name || '',
        contractorName: position.contractor?.name || '',
        contractorContactFirstName: position.contractorContact?.firstName || '',
        contractorContactLastName: position.contractorContact?.lastName || '',
        scaffoldingUserName: position.scaffoldingUser?.name || '',
        scaffoldingUserContactFirstName: position.scaffoldingUserContact?.firstName || '', // Poprawiono dostęp do name
        scaffoldingUserContactLastName: position.scaffoldingUserContact?.lastName || '', // Poprawiono dostęp do name
        assemblyLocation: position.assemblyLocation || '',
        assemblyDate: position.assemblyDate || '',
        dimensions: formatDimensions(),
        scaffoldingPurpose: '',
        loadLimit: '2,0 kN/m²',
        earthingResistance: '',
        additionalInfo: '',
    });

    const handleInputChange = (field: keyof TechnicalProtocolData, value: string) => {
        setProtocolData(prev => ({...prev, [field]: value}));
    };

    const filename = `Protocol_${position.scaffoldingNumber?.replace(/\//g, '_') || 'draft'}_${new Date().toISOString().split('T')[0]}.pdf`;

    return (
        <Box p={4}>
            <Heading size="lg" mb={4} color={themeColors.fontColor}>
                {t('scaffoldingLogPositions:technicalProtocol')}
            </Heading>

            <Box mb={4} p={4} borderWidth={1} borderRadius="md" borderColor={themeColors.borderColor}>
                <Heading size="md" mb={3} color={themeColors.fontColor}>
                    {t('common:autoFilledData')}
                </Heading>
                <Grid templateColumns="repeat(2, 1fr)" gap={4} color={themeColors.fontColor}>
                    <GridItem>
                        <strong>{t('scaffoldingLogPositions:scaffoldingNumber')}:</strong> {protocolData.scaffoldingNumber}
                    </GridItem>
                    <GridItem>
                        <strong>{t('scaffoldingLogPositions:assemblyDate')}:</strong> {protocolData.assemblyDate}
                    </GridItem>
                    <GridItem colSpan={2}>
                        <strong>{t('companies:company')}:</strong> {protocolData.companyName}
                    </GridItem>
                    <GridItem colSpan={2}>
                        <strong>{t('scaffoldingLogPositions:contractor')}:</strong> {protocolData.contractorName}
                    </GridItem>
                    <GridItem colSpan={2}>
                        <strong>{t('scaffoldingLogPositions:scaffoldingUser')}:</strong> {protocolData.scaffoldingUserName}
                    </GridItem>
                    <GridItem colSpan={2}>
                        <strong>{t('scaffoldingLogPositions:scaffoldingUserContact')}:</strong> {protocolData.scaffoldingUserContactLastName} + {protocolData.scaffoldingUserContactFirstName}
                    </GridItem>
                    <GridItem colSpan={2}>
                        <strong>{t('scaffoldingLogPositions:assemblyLocation')}:</strong> {protocolData.assemblyLocation}
                    </GridItem>
                    <GridItem colSpan={2}>
                        <strong>{t('scaffoldingLogPositions:dimensions')}:</strong> {protocolData.dimensions}
                    </GridItem>
                </Grid>
            </Box>

            <Box mb={4} p={4} borderWidth={1} borderRadius="md" borderColor={themeColors.borderColor}>
                <Heading size="md" mb={3} color={themeColors.fontColor}>
                    {t('common:additionalData')}
                </Heading>
                <Grid templateColumns="repeat(1, 1fr)" gap={4}>
                    <GridItem>
                        <SimpleTextAreaField
                            label={t('scaffoldingLogPositions:scaffoldingPurpose')}
                            placeholder={t('scaffoldingLogPositions:scaffoldingPurposePlaceholder')}
                            value={protocolData.scaffoldingPurpose}
                            onChange={(e: React.ChangeEvent<HTMLTextAreaElement>) =>
                                handleInputChange('scaffoldingPurpose', e.target.value)
                            }
                        />
                    </GridItem>
                    <GridItem>
                        <SimpleInputField
                            label={t('scaffoldingLogPositions:loadLimit')}
                            placeholder="2,0 kN/m²"
                            value={protocolData.loadLimit}
                            onChange={(e: React.ChangeEvent<HTMLInputElement>) =>
                                handleInputChange('loadLimit', e.target.value)
                            }
                        />
                    </GridItem>
                    <GridItem>
                        <SimpleTextAreaField
                            label={t('scaffoldingLogPositions:earthingResistance')}
                            placeholder={t('scaffoldingLogPositions:earthingResistancePlaceholder')}
                            value={protocolData.earthingResistance}
                            onChange={(e: React.ChangeEvent<HTMLTextAreaElement>) =>
                                handleInputChange('earthingResistance', e.target.value)
                            }
                        />
                    </GridItem>
                    <GridItem>
                        <SimpleTextAreaField
                            label={t('scaffoldingLogPositions:additionalInfo')}
                            placeholder={t('scaffoldingLogPositions:additionalInfoPlaceholder')}
                            value={protocolData.additionalInfo}
                            onChange={(e: React.ChangeEvent<HTMLTextAreaElement>) =>
                                handleInputChange('additionalInfo', e.target.value)
                            }
                        />
                    </GridItem>
                </Grid>
            </Box>

            <Box textAlign="center">
                <PDFDownloadLink
                    document={<TechnicalProtocolPDF
                        data={protocolData}
                        logoUrl={"/img/companies/rch_logo.png"}
                    />}
                    fileName={filename}
                >
                    {({loading}) => (
                        <Button
                            colorPalette="blue"
                            size="lg"
                            loading={loading}
                        >
                            {loading ? t('common:generating') : t('common:downloadPDF')}
                        </Button>
                    )}
                </PDFDownloadLink>
            </Box>
        </Box>
    );
};

export default TechnicalProtocolForm;
