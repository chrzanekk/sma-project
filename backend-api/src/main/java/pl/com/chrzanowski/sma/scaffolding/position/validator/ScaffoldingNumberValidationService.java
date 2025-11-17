package pl.com.chrzanowski.sma.scaffolding.position.validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import pl.com.chrzanowski.sma.common.exception.ScaffoldingLogPositionException;
import pl.com.chrzanowski.sma.common.exception.error.ScaffoldingLogPositionErrorCode;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.Year;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class ScaffoldingNumberValidationService {

    private static final Logger log = LoggerFactory.getLogger(ScaffoldingNumberValidationService.class);

    // Pattern: NUMER[LITERA]/DD/MM/YYYY
    // Dopuszczamy więcej cyfr w regex, ale potem walidujemy zakres
    private static final Pattern SCAFFOLDING_NUMBER_PATTERN =
            Pattern.compile("^(\\d+)([a-zA-Z]{0,2})/(\\d{1,2})/(\\d{1,2})/(\\d{4})$");

    private static final Pattern TOO_MANY_LETTERS_PATTERN =
            Pattern.compile("^(\\d+)([a-zA-Z]{3,})");

    private static final int YEAR_TOLERANCE = 3; // +/- 3 lata od aktualnego
    private static final int MIN_NUMBER = 1;
    private static final int MAX_NUMBER = 9999;
    private static final int MAX_LETTER_LENGTH = 2;

    /**
     * Waliduje numer rusztowania według wzorca: NUMER[LITERA]/DZIEŃ/MIESIĄC/ROK
     *
     * @param scaffoldingNumber numer do walidacji (np. "1/17/11/2025", "25a/31/12/2024", "100AA/01/01/2023")
     * @throws ScaffoldingLogPositionException jeśli numer jest nieprawidłowy
     */
    public void validateScaffoldingNumber(String scaffoldingNumber) {
        if (scaffoldingNumber == null || scaffoldingNumber.isBlank()) {
            throw new ScaffoldingLogPositionException(
                    ScaffoldingLogPositionErrorCode.INVALID_SCAFFOLDING_NUMBER_FORMAT,
                    "Numer rusztowania nie może być pusty",
                    Map.of("scaffoldingNumber", "null or blank")
            );
        }

        String trimmedNumber = scaffoldingNumber.trim();

        // Sprawdź czy użytkownik nie odwrócił kolejności (litera+numer zamiast numer+litera)
        if (trimmedNumber.matches("^[a-zA-Z].*")) {
            throw new ScaffoldingLogPositionException(
                    ScaffoldingLogPositionErrorCode.INVALID_SCAFFOLDING_NUMBER_FORMAT,
                    "Nieprawidłowa kolejność: numer musi być PRZED literą. Prawidłowy format: NUMER[LITERA]/DZIEŃ/MIESIĄC/ROK",
                    Map.of(
                            "scaffoldingNumber", trimmedNumber,
                            "error", "Letter before number",
                            "expectedFormat", "NUMER[LITERA]/DZIEŃ/MIESIĄC/ROK",
                            "examples", "1/17/11/2025, 25a/31/12/2024, 100AA/01/01/2023"
                    )
            );
        }

        Matcher tooManyLettersMatcher = TOO_MANY_LETTERS_PATTERN.matcher(trimmedNumber);
        if (tooManyLettersMatcher.find()) {
            String numberPart = tooManyLettersMatcher.group(1);
            String letterPart = tooManyLettersMatcher.group(2);
            throw new ScaffoldingLogPositionException(
                    ScaffoldingLogPositionErrorCode.INVALID_SCAFFOLDING_NUMBER_FORMAT,
                    String.format("Część literowa może mieć maksymalnie %d litery (np. a, AA, zz). Znaleziono: '%s' (%d liter)",
                            MAX_LETTER_LENGTH, letterPart, letterPart.length()),
                    Map.of(
                            "scaffoldingNumber", trimmedNumber,
                            "numberPart", numberPart,
                            "letterPart", letterPart,
                            "letterPartLength", letterPart.length(),
                            "maxLength", MAX_LETTER_LENGTH
                    )
            );
        }

        Matcher matcher = SCAFFOLDING_NUMBER_PATTERN.matcher(trimmedNumber);

        if (!matcher.matches()) {
            throw new ScaffoldingLogPositionException(
                    ScaffoldingLogPositionErrorCode.INVALID_SCAFFOLDING_NUMBER_FORMAT,
                    "Nieprawidłowy format numeru rusztowania. Wymagany format: NUMER[LITERA]/DZIEŃ/MIESIĄC/ROK (np. 1/17/11/2025 lub 25a/31/12/2024)",
                    Map.of(
                            "scaffoldingNumber", trimmedNumber,
                            "expectedFormat", "NUMER[LITERA]/DZIEŃ/MIESIĄC/ROK",
                            "examples", "1/17/11/2025, 25a/31/12/2024, 100AA/01/01/2023"
                    )
            );
        }

        try {
            // Wyciągnij komponenty
            String numberPart = matcher.group(1);
            String letterPart = matcher.group(2); // może być pusty ""
            int day = Integer.parseInt(matcher.group(3));
            int month = Integer.parseInt(matcher.group(4));
            int year = Integer.parseInt(matcher.group(5));

            // Waliduj część numeryczną (1-9999) - NAJPIERW!
            validateNumberRange(numberPart, trimmedNumber);

            // Waliduj część literową (0-2 litery, tylko a-z, A-Z)
            validateLetterPart(letterPart, trimmedNumber);

            // Waliduj rok (+/- 3 lata od aktualnego)
            validateYear(year, trimmedNumber);

            // Waliduj datę (sprawdź czy dzień/miesiąc/rok tworzą poprawną datę)
            validateDate(day, month, year, trimmedNumber);

            log.debug("Walidacja numeru rusztowania {} zakończona sukcesem (numer: {}, litery: '{}')",
                    trimmedNumber, numberPart, letterPart.isEmpty() ? "brak" : letterPart);

        } catch (NumberFormatException e) {
            throw new ScaffoldingLogPositionException(
                    ScaffoldingLogPositionErrorCode.INVALID_SCAFFOLDING_NUMBER_FORMAT,
                    "Błąd parsowania komponentów numeru rusztowania: " + e.getMessage(),
                    Map.of("scaffoldingNumber", trimmedNumber, "error", e.getMessage())
            );
        }
    }

    private void validateNumberRange(String numberPart, String fullNumber) {
        try {
            int number = Integer.parseInt(numberPart);

            if (number < MIN_NUMBER || number > MAX_NUMBER) {
                throw new ScaffoldingLogPositionException(
                        ScaffoldingLogPositionErrorCode.INVALID_SCAFFOLDING_NUMBER_RANGE,
                        String.format("Numer rusztowania musi być w zakresie %d-%d", MIN_NUMBER, MAX_NUMBER),
                        Map.of(
                                "scaffoldingNumber", fullNumber,
                                "numberPart", numberPart,
                                "number", number,
                                "minNumber", MIN_NUMBER,
                                "maxNumber", MAX_NUMBER
                        )
                );
            }
        } catch (NumberFormatException e) {
            // Numer jest zbyt duży dla Integer lub ma nieprawidłowy format
            throw new ScaffoldingLogPositionException(
                    ScaffoldingLogPositionErrorCode.INVALID_SCAFFOLDING_NUMBER_RANGE,
                    String.format("Numer rusztowania jest nieprawidłowy lub przekracza maksymalną wartość %d", MAX_NUMBER),
                    Map.of(
                            "scaffoldingNumber", fullNumber,
                            "numberPart", numberPart,
                            "maxNumber", MAX_NUMBER,
                            "error", e.getMessage()
                    )
            );
        }
    }

    private void validateLetterPart(String letterPart, String fullNumber) {
        // letterPart może być pusty (dozwolone)
        if (letterPart == null || letterPart.isEmpty()) {
            return;
        }

        // Sprawdź długość (max 2 litery)
        if (letterPart.length() > MAX_LETTER_LENGTH) {
            throw new ScaffoldingLogPositionException(
                    ScaffoldingLogPositionErrorCode.INVALID_SCAFFOLDING_NUMBER_FORMAT,
                    String.format("Część literowa może mieć maksymalnie %d litery (np. a, AA, zz)", MAX_LETTER_LENGTH),
                    Map.of(
                            "scaffoldingNumber", fullNumber,
                            "letterPart", letterPart,
                            "letterPartLength", letterPart.length(),
                            "maxLength", MAX_LETTER_LENGTH
                    )
            );
        }

        // Sprawdź czy zawiera tylko litery a-z, A-Z (regex już to zapewnia, ale double-check)
        if (!letterPart.matches("^[a-zA-Z]+$")) {
            throw new ScaffoldingLogPositionException(
                    ScaffoldingLogPositionErrorCode.INVALID_SCAFFOLDING_NUMBER_FORMAT,
                    "Część literowa może zawierać tylko litery a-z lub A-Z (bez cyfr, spacji lub znaków specjalnych)",
                    Map.of(
                            "scaffoldingNumber", fullNumber,
                            "letterPart", letterPart,
                            "allowedCharacters", "a-z, A-Z"
                    )
            );
        }

        // Opcjonalnie: sprawdź czy nie ma mieszanych wielkości liter (np. "aA" - może być nieintuicyjne)
        if (letterPart.length() == 2) {
            boolean hasLowerCase = letterPart.chars().anyMatch(Character::isLowerCase);
            boolean hasUpperCase = letterPart.chars().anyMatch(Character::isUpperCase);

            if (hasLowerCase && hasUpperCase) {
                log.warn("Numer rusztowania {} ma mieszane wielkości liter: '{}'. " +
                                "Zalecane jest używanie jednolitej wielkości (np. 'aa' lub 'AA')",
                        fullNumber, letterPart);
            }
        }
    }

    private void validateYear(int year, String fullNumber) {
        int currentYear = Year.now().getValue();
        int minYear = currentYear - YEAR_TOLERANCE;
        int maxYear = currentYear + YEAR_TOLERANCE;

        if (year < minYear || year > maxYear) {
            throw new ScaffoldingLogPositionException(
                    ScaffoldingLogPositionErrorCode.INVALID_SCAFFOLDING_NUMBER_YEAR,
                    String.format("Rok musi być w zakresie %d-%d (obecny rok ±%d lat)",
                            minYear, maxYear, YEAR_TOLERANCE),
                    Map.of(
                            "scaffoldingNumber", fullNumber,
                            "year", year,
                            "currentYear", currentYear,
                            "minYear", minYear,
                            "maxYear", maxYear,
                            "yearTolerance", YEAR_TOLERANCE
                    )
            );
        }
    }

    private void validateDate(int day, int month, int year, String fullNumber) {
        try {
            LocalDate.of(year, month, day);
        } catch (DateTimeException e) {
            throw new ScaffoldingLogPositionException(
                    ScaffoldingLogPositionErrorCode.INVALID_SCAFFOLDING_NUMBER_DATE,
                    String.format("Nieprawidłowa data w numerze rusztowania: dzień %d, miesiąc %d, rok %d. %s",
                            day, month, year, e.getMessage()),
                    Map.of(
                            "scaffoldingNumber", fullNumber,
                            "day", day,
                            "month", month,
                            "year", year,
                            "error", e.getMessage()
                    )
            );
        }
    }
}
