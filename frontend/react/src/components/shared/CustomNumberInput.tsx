import React from 'react';
import { useField, useFormikContext } from 'formik';
import { Box, Input, Text } from '@chakra-ui/react';
import { useThemeColors } from '@/theme/theme-colors';
import { themeVars } from '@/theme/theme-colors';

interface CustomNumberInputProps {
    name: string;
    placeholder?: string;
}

export const CustomNumberInput: React.FC<CustomNumberInputProps> = ({ name, placeholder }) => {
    const themeColors = useThemeColors();
    const [field, meta] = useField(name);
    const { setFieldValue } = useFormikContext();

    // Funkcja formatująca do wyświetlania (dodaje spacje)
    const formatDisplayValue = (val: string) => {
        if (!val) return "";
        // Zamień kropkę na przecinek dla polskiego formatu (opcjonalne) lub zostaw jak jest
        // Tutaj: zostawiamy to co user wpisał, ale dodajemy spacje tysięczne dla części całkowitej
        const parts = val.replace(',', '.').split('.');
        parts[0] = parts[0].replace(/\B(?=(\d{3})+(?!\d))/g, " ");
        // Jeśli user wpisał przecinek, przywróć go w wyświetlaniu
        const separator = val.includes(',') ? ',' : '.';
        return parts.length > 1 ? `${parts[0]}${separator}${parts[1]}` : parts[0];
    };

    // Obsługa zmiany inputa
    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const value = e.target.value;

        // 1. Pozwól tylko na cyfry, kropkę, przecinek i spacje
        if (!/^[0-9.,\s]*$/.test(value)) return;

        // 2. Usuń spacje do analizy logicznej
        const rawValue = value.replace(/\s/g, '');

        // 3. Zamień pierwszy przecinek na kropkę (dla ujednolicenia logiki), ale zapamiętaj co wpisał user
        // Chcemy pozwolić wpisywać przecinek, ale w Formiku trzymać format z kropką (BigDecimal-friendly)
        // LUB trzymać tak jak user wpisał i czyścić przy submit.
        // Poniżej: Trzymamy "czysty" format (np. "1234.56") w Formiku, ale input wyświetla sformatowany.

        // Prostsze podejście: Pozwól wpisywać, waliduj regexem na bieżąco.

        // Sprawdź czy nie ma więcej niż jednej kropki/przecinka
        const dotsAndCommas = rawValue.match(/[.,]/g);
        if (dotsAndCommas && dotsAndCommas.length > 1) return;

        // Sprawdź miejsca po przecinku
        const parts = rawValue.split(/[.,]/);
        if (parts[1] && parts[1].length > 2) return; // Blokuj wpisanie 3. cyfry po przecinku

        // Sprawdź max długość części całkowitej (np. 7 cyfr = do 9 mln)
        if (parts[0].length > 7) return;

        // Zapisz do Formika wartość "surową" (bez spacji), ale z kropką czy przecinkiem?
        // Backend Java BigDecimal woli kropkę. Więc najlepiej tutaj zamieniać ',' na '.'.
        const normalizedValue = rawValue.replace(',', '.');

        setFieldValue(name, normalizedValue).then();
    };

    // Wartość do wyświetlenia w polu (z separatorem tysięcy, jeśli chcemy)
    // Uwaga: Jeśli Formik trzyma "1234.56", a chcemy wyświetlić "1 234.56"
    const displayValue = formatDisplayValue(field.value);

    return (
        <Box mb={2}>
            <Input
                {...field}
                value={displayValue} // Nadpisujemy value z field, żeby sterować formatowaniem
                onChange={handleChange} // Nadpisujemy onChange
                placeholder={placeholder}
                size="sm"
                color={themeColors.fontColor}
                bg={themeVars.bgColorPrimary}
                borderRadius="md"
            />
            {meta.touched && meta.error && (
                <Text color="red.500" fontSize="xs" mt="1">
                    {meta.error}
                </Text>
            )}
        </Box>
    );
};
