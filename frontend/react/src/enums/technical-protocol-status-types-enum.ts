export enum TechnicalProtocolStatus {
    TO_BE_CREATED = 'toBeCreated',
    CREATED = 'created',
    IN_SIGNATURE = 'inSignature',
    IN_SIGNATURE_2 = 'inSignature2',
    RECEIVED = 'received',
}

export type TechnicalProtocolStatusValue = `${TechnicalProtocolStatus}`;