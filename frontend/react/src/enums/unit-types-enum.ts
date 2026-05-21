export enum UnitType {
    LENGTH = 'length',
    AREA = 'area',
    VOLUME = 'volume',
    DURATION = 'duration',
    SPEED = 'speed',
    DENSITY = 'density',
    FORCE = 'force'
}

export type UnitTypeValue = `${UnitType}`;