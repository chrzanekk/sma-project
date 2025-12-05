// api-paths.ts

export const ApiPath = {
    AUTH: "/api/auth",
    ACCOUNT: "/api/account",
    USER: "/api/users",
    ROLE: "/api/roles",
    COMPANY: "/api/companies",
    CONTRACTOR: "/api/contractors",
    CONTACT: "/api/contacts",
    CONSTRUCTION_SITE: "/api/construction-sites",
    POSITION: "/api/positions",
    CONTRACT: "/api/contracts",
    RESOURCE: "/api/resources",
    EMPLOYEE: "/api/employees",
    SCAFFOLDING_LOG: "/api/scaffolding-logs",
    SCAFFOLDING_LOG_POSITION: "/api/scaffolding-log-positions",
    UNIT: "/api/units"
} as const;

// Opcjonalnie: typ pomocniczy, jeśli potrzebujesz używać wartości w definicjach typów
export type ApiPathType = typeof ApiPath[keyof typeof ApiPath];
