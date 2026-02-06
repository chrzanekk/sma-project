import { FetchableScaffoldingLogPositionDTO } from "@/types/scaffolding-log-position-types";

export const buildScaffoldingTree = (
    flatPositions: FetchableScaffoldingLogPositionDTO[]
): FetchableScaffoldingLogPositionDTO[] => {
    if (!flatPositions) return [];

    // 1. Mapa dla szybkiego dostępu po ID
    const positionMap = new Map<number, FetchableScaffoldingLogPositionDTO>();

    // Kopiujemy obiekty i inicjalizujemy pustą tablicę dzieci, żeby nie mutować oryginału w nieprzewidziany sposób
    flatPositions.forEach(pos => {
        if (pos.id) {
            positionMap.set(pos.id, { ...pos, mappedChildPositions: [] });
        }
    });

    const rootPositions: FetchableScaffoldingLogPositionDTO[] = [];

    // 2. Budowanie drzewa
    positionMap.forEach((position) => {
        if (position.parentPosition && position.parentPosition.id) {
            // Jeśli ma rodzica, znajdź go w mapie i dodaj się do jego dzieci
            const parent = positionMap.get(position.parentPosition.id);
            if (parent) {
                // Dodajemy do rodzica
                parent.mappedChildPositions?.push(position);
            } else {
                // Sytuacja brzegowa: Rodzic nie istnieje w tym zestawie danych (np. inna strona paginacji)
                // Decyzja: czy pokazujemy jako sierotę (root), czy ukrywamy?
                // Tutaj zakładamy, że pokazujemy jako root, żeby dane nie zginęły.
                rootPositions.push(position);
            }
        } else {
            // Jeśli nie ma rodzica, jest korzeniem
            rootPositions.push(position);
        }
    });

    // 3. Opcjonalne sortowanie dzieci (np. po ID rosnąco)
    positionMap.forEach(pos => {
        if (pos.mappedChildPositions && pos.mappedChildPositions.length > 0) {
            pos.mappedChildPositions.sort((a, b) => (a.id || 0) - (b.id || 0));
        }
    });

    return rootPositions;
};
