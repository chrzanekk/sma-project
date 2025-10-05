// currency-types.ts
export class Currency {
    private static readonly _currencies: Currency[] = [];

    public static readonly ALL: Record<string, Currency> = {};

    public static readonly currencies: Currency[] = Currency._currencies;

    private constructor(
        public readonly code: string,
        public readonly name: string,
        public readonly symbol?: string
    ) {
        Currency._currencies.push(this);
        Currency.ALL[code.toUpperCase()] = this;
    }

    public static fromCode(code: string): Currency {
        const currency = Currency.ALL[code.toUpperCase()];
        if (!currency) {
            throw new Error(`Unknown currency code: ${code}`);
        }
        return currency;
    }

    public static getCurrencyOptions(): { value: string; label: string }[] {
        return Currency.currencies.map((currency) => ({
            value: currency.code,
            label: `${currency.code} – ${currency.name}`,
        }));
    }
}

// Fragment listy najpopularniejszych walut (rozszerz według potrzeb!)
const currencyData: { code: string; name: string; symbol?: string }[] = [
    { code: "PLN", name: "Polski złoty", symbol: "zł" },
    { code: "EUR", name: "Euro", symbol: "€" },
    { code: "USD", name: "US Dollar", symbol: "$" },
    { code: "GBP", name: "British Pound", symbol: "£" },
    { code: "CHF", name: "Swiss Franc", symbol: "Fr" },
    { code: "JPY", name: "Japanese Yen", symbol: "¥" },
    { code: "CZK", name: "Czech Koruna", symbol: "Kč" },
    { code: "NOK", name: "Norwegian Krone", symbol: "kr" },
    // ...dodaj kolejne wg listy ISO 4217[web:43][web:44]
];

currencyData.forEach(({ code, name, symbol }) => new Currency(code, name, symbol));

// Opcje do selecta:
export const getCurrencyOptions = Currency.getCurrencyOptions;
