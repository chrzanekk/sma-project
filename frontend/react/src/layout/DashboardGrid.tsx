import React from "react";
import {Grid, GridItem, Text} from "@chakra-ui/react";
import {themeColors} from "@/theme/theme-colors.ts";

interface DashboardGridProps {
    topRowContent: string;
    columnsContent: string[];
    bottomRowContent: string;
    bgColor?: string;
    fontColor?: string;
}

const DashboardGrid: React.FC<DashboardGridProps> = ({
                                                         topRowContent,
                                                         columnsContent,
                                                         bottomRowContent,
                                                         bgColor = themeColors.bgColor(),
                                                         fontColor = themeColors.fontColor()
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
                <Text color={fontColor} fontWeight="bold">
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
                        <Text color={fontColor} fontWeight="bold">
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
                <Text color={fontColor} fontWeight="bold">
                    {bottomRowContent}
                </Text>
            </GridItem>
        </Grid>
    );
};

export default DashboardGrid;
