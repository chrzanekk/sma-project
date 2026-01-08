import React from "react";
import {useTranslation} from "react-i18next";
import BasicLayout from "@/components/shared/BasicLayout.tsx";
import {useNavigate} from "react-router-dom";
import {Button, Heading, HStack} from "@chakra-ui/react";
import {FaArrowLeft} from "react-icons/fa6";

interface ScaffoldingLogPositionLayoutProps {
    filters: React.ReactNode;
    addLogPositionButton: React.ReactNode;
    table: React.ReactNode;
    pagination: React.ReactNode;
    bgColor: string;
    scaffoldingLogName: string;
}

const ScaffoldingLogPositionLayout: React.FC<ScaffoldingLogPositionLayoutProps> = ({
                                                                                       filters,
                                                                                       addLogPositionButton,
                                                                                       table,
                                                                                       pagination,
                                                                                       bgColor,
                                                                                       scaffoldingLogName
                                                                                   }) => {
    const {t} = useTranslation('scaffoldingLogPositions');
    const navigate = useNavigate();
    const headerWithBack = (
        <HStack justify="center" width="100%" position="relative">
            <Button
                size="xs"
                variant="ghost"
                position="absolute"
                left={0}
                onClick={() => navigate('/logList')} // Lub navigate(-1) dla "Wstecz"
            >
                <FaArrowLeft />
            </Button>
            <Heading>
                {`${t('scaffoldingLogPositions:positionList')} - ${scaffoldingLogName}`}
            </Heading>
        </HStack>
    );

    return (
        <BasicLayout headerTitle={headerWithBack}
                     filters={filters}
                     addButton={addLogPositionButton}
                     table={table}
                     pagination={pagination}
                     bgColor={bgColor}/>
    );
};

export default ScaffoldingLogPositionLayout;