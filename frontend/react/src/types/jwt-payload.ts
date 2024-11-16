export interface JwtPayload {
    sub: string; // Użytkownik (subject) - zazwyczaj nazwa użytkownika lub e-mail
    iat: number; // Czas wystawienia tokena (issued at)
    exp: number; // Czas wygaśnięcia tokena (expiration)
    authorities: string; // Uprawnienia użytkownika, jako pojedynczy string z rolami oddzielonymi przecinkami
}