export class Country {
    public static readonly POLAND = new Country("PL", "Polska");
    public static readonly ENGLAND = new Country("EN-GB", "Anglia");

    private static readonly countries: Country[] = [
        Country.POLAND,
        Country.ENGLAND,
    ];

    private constructor(public readonly code: string, public readonly name: string) {
    }

    public static fromCode(code: string): Country {
        const country = Country.countries.find(
            (c) => c.code.toLowerCase() === code.toLowerCase()
        );
        if (!country) {
            throw new Error(`Unknown country code: ${code}`);
        }
        return country;
    }
}


export const getCountryOptions = (t: (key: string, options?: any) => string) => [
    {value: Country.POLAND.code, label: t("poland", {ns: "countries", defaultValue: Country.POLAND.name})},
    {value: Country.ENGLAND.code, label: t("england", {ns: "countries", defaultValue: Country.ENGLAND.name})},
]