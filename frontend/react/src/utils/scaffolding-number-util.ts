// src/utils/scaffolding-number-util.ts
export const getNextChildSuffix = (existingChildren: any[]): string => {
    if (!existingChildren || existingChildren.length === 0) {
        return "a";
    }

    // Wyciągamy ostatnią literę z numerów dzieci.
    // Zakładamy, że numery to np. "1a/12/05/2026"
    // Wyciągamy to, co jest przed pierwszym slashem
    const prefixes = existingChildren.map(child => child.scaffoldingNumber.split('/')[0]);

    // Szukamy liter w tych prefixach
    const letters = prefixes
        .map(prefix => prefix.replace(/[0-9]/g, '')) // Usuwa cyfry, zostawia 'a', 'b' itd.
        .filter(l => l.length > 0)
        .sort();

    if (letters.length === 0) return "a";

    const lastLetter = letters[letters.length - 1]; // Np. "b"

    // Konwersja charCode na następną literę (np. "b" (98) -> "c" (99))
    const nextCharCode = lastLetter.charCodeAt(0) + 1;

    // Jeśli skończyłby się alfabet po 'z', można tu dodać zabezpieczenie (np. "aa"),
    // ale zazwyczaj na budowie nie ma aż tylu modyfikacji jednego rusztowania.
    return String.fromCharCode(nextCharCode);
}