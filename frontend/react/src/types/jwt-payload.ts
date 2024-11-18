export interface JwtPayload {
    id: number;
    email: string;
    sub: string; // Użytkownik (subject) - zazwyczaj nazwa użytkownika lub e-mail
    iat: number; // Czas wystawienia tokena (issued at)
    exp: number; // Czas wygaśnięcia tokena (expiration)
    authorities: Set<string>; // Uprawnienia użytkownika, jako pojedynczy string z rolami oddzielonymi przecinkami
}