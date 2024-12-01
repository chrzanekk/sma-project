export interface JwtPayload {
    id: number;
    email: string;
    sub: string;
    iat: number;
    exp: number;
    authorities: string[];
}