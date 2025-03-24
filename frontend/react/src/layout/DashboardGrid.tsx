import React from "react";
import {Grid, GridItem, Text} from "@chakra-ui/react";
import {useThemeColors} from "@/theme/theme-colors.ts";

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
                                                         bgColor = useThemeColors().bgColorPrimary,
                                                         fontColor = useThemeColors().fontColor
                                                     }) => {
    return (
        <Grid
            templateRows="30% 30% 30%"
            height={"calc(100vh - 110px)"}
            gap={3}
        >
            {/* Górny wiersz */}
            <GridItem
                w="100%"
                bg={bgColor}
                p={2}
                borderRadius="lg"
                minHeight={"30%"}
                overflowY={"auto"}
            >
                <Text color={fontColor} fontWeight="bold">
                    {topRowContent}
                </Text>
            </GridItem>

            {/* Środkowy wiersz - 3 kolumny */}
            <Grid templateColumns="repeat(3, 1fr)" gap={3}>
                {columnsContent.map((content, index) => (
                    <GridItem
                        key={index}
                        w="100%"
                        h={"100%"}
                        bg={bgColor}
                        p={2}
                        borderRadius="lg"
                        minHeight={"30%"}
                        overflowY="auto"
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
                p={2}
                borderRadius="lg"
                minHeight={"30%"}
                overflow="auto"
            >
                <Text color={fontColor} fontWeight="bold">
                    {bottomRowContent}
                </Text>
            </GridItem>
        </Grid>
    );
};

export default DashboardGrid;
