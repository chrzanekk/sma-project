import React from "react";
import Navbar from "@/layout/Navbar.tsx";
import {Box} from "@chakra-ui/react";
import DashboardGrid from "@/layout/DashboardGrid.tsx";

const App: React.FC = () => {
    return (
        <Box minHeight="100vh" display="flex" flexDirection="column" bg="gray.100">
            <Navbar/>
            <Box flex="1" p={8}>
                <DashboardGrid
                    topRowContent="Pogoda na dziś: Słonecznie"
                    columnsContent={[
                        "Kolumna 1: Wiadomości",
                        "Kolumna 2: Wykresy sprzedaży",
                        "Kolumna 3: Statystyki użytkowników",
                    ]}
                    bottomRowContent="Informacje o najnowszych aktualizacjach"
                />
            </Box>

        </Box>
    )
}

export default App;
