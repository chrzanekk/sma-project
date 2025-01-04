import React from "react";
import { Grid, GridItem, Text } from "@chakra-ui/react";

interface DashboardGridProps {
    topRowContent: string;
    columnsContent: string[];
    bottomRowContent: string;
    bgColor?: string;
}

const DashboardGrid: React.FC<DashboardGridProps> = ({
                                                         topRowContent,
                                                         columnsContent,
                                                         bottomRowContent,
                                                         bgColor = "green.400",
                                                     }) => {
    return (
        <Grid templateRows="1fr 2fr 1fr"
              height="calc(100vh - 140px)"
              gap={6}

        >
            {/* Górny wiersz */}
            <GridItem
                w="100%"
                bg={bgColor}
                p={4}
                borderRadius="lg"
            >
                <Text color="white" fontWeight="bold">
                    {topRowContent}
                </Text>
            </GridItem>

            {/* Środkowy wiersz - 3 kolumny */}
            <Grid templateColumns="repeat(3, 1fr)" gap={6}>
                {columnsContent.map((content, index) => (
                    <GridItem
                        key={index}
                        w="100%"
                        bg={bgColor}
                        p={4}
                        borderRadius="lg"
                    >
                        <Text color="white" fontWeight="bold">
                            {content}
                        </Text>
                    </GridItem>
                ))}
            </Grid>

            {/* Dolny wiersz */}
            <GridItem
                w="100%"
                bg={bgColor}
                p={4}
                borderRadius="lg"
            >
                <Text color="white" fontWeight="bold">
                    {bottomRowContent}
                </Text>
            </GridItem>
        </Grid>
    );
};

export default DashboardGrid;
