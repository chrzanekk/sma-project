import React from "react";
import {Grid, GridItem} from "@chakra-ui/react";

interface DashboardGridProps {
    topRowContent: React.ReactNode;
    columnsContent: React.ReactNode[];
    bottomRowContent?: React.ReactNode;
    bgColor?: string;
}

const DashboardGrid: React.FC<DashboardGridProps> = ({
                                                         topRowContent,
                                                         columnsContent,
                                                         bottomRowContent,
                                                         bgColor = "bgColorPrimary"
                                                     }) => {
    return (
        <Grid
            templateRows="min-content 1fr min-content"
            minH={"calc(100vh - 150px)"}
            gap={5}
            w={"100%"}
        >
            {/* Górny wiersz */}
            <GridItem
                w={"100%"}
                bg={bgColor}
                p={5}
                borderRadius={"xl"}
                boxShadow={"sm"}
            >
                {topRowContent}
            </GridItem>

            {/* Środkowy wiersz - 3 kolumny */}
            <Grid
                templateColumns={{base: "1fr", lg: "repeat(3, 1fr)"}}
                gap={4}
            >
                {columnsContent.map((content, index) => (
                    <GridItem
                        key={index}
                        w={"100%"}
                        h={"100%"}
                        bg={bgColor}
                        p={5}
                        borderRadius={"xl"}
                        boxShadow={"sm"}
                        overflowY={"auto"}
                    >
                        {content}
                    </GridItem>
                ))}
            </Grid>

            {/* Dolny wiersz */}
            {bottomRowContent && (<GridItem
                w={"100%"}
                bg={bgColor}
                p={4}
                borderRadius={"xl"}
                boxShadow={"sm"}
            >
                {bottomRowContent}
            </GridItem>)}
        </Grid>
    );
};

export default DashboardGrid;
