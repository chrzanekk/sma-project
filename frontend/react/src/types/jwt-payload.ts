export interface JwtPayload {
    id: number;
    email: string;
    sub: string;
    iat: number;
    exp: number;
    authorities: string[];
}

export interface JwtTokenResponse {
    id_token: string;
}