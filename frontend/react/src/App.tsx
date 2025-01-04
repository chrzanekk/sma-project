import React from "react";
import {Box} from "@chakra-ui/react";
import DashboardGrid from "@/layout/DashboardGrid.tsx";
import {themeColors} from "@/theme/theme-colors.ts";

const App: React.FC = () => {
    return (
        <Box minHeight="100vh" display="flex" flexDirection="column" bg={themeColors.bgColorLight()}>
            <Box flex="1" p={8}>
                <DashboardGrid
                    topRowContent="Pogoda na dziś: Za oknem wszystko widać"
                    columnsContent={[
                        "Kolumna 1: Wiadomości",
                        "Kolumna 2: Wykresy sprzedaży",
                        "Kolumna 3: Statystyki użytkowników",
                    ]}
                    bottomRowContent="Informacje o najnowszych aktualizacjach"
                    bgColor={themeColors.bgColor()}
                    fontColor={themeColors.fontColor()}
                />
            </Box>
        </Box>
    )
}

export default App;
