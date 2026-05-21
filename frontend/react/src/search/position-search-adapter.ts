import {PositionFilter, PositionFilterBuilder} from "@/filters/position-filter.ts";
import {PositionDTO} from "@/types/position-types.ts";
import {getPositionByFilter} from "@/services/position-service.ts";

export interface PositionSearchAdapterOptions {
    fixed?: Partial<PositionFilter>;
    defaults?: Partial<PositionFilter>;
    mapResults?: (items: PositionDTO[]) => PositionDTO[]
}

export function makePositionSearchAdapter(options: PositionSearchAdapterOptions = {}) {
    const {
        fixed = {},
        defaults = {page: 0, size: 10, sort: "id,asc"},
        mapResults
    } = options;

    return async (q: string): Promise<PositionDTO[]> => {
        const b = new PositionFilterBuilder();

        if(q && q.trim().length > 0) {
            b.withNameContains(q.trim().toLowerCase())
        }


        if (fixed.id !== undefined) b.withId(fixed.id);
        if (fixed.nameContains !== undefined) b.withNameContains(fixed.nameContains);
        if (fixed.descriptionContains !== undefined) b.withDescriptionContains(fixed.descriptionContains);
        if (fixed.companyId !== undefined) b.withCompanyId(fixed.companyId);

        const page = defaults.page ?? 0;
        const size = defaults.size ?? 10;
        b.withPaging(page, size);
        if (defaults.sort) b.withSort(defaults.sort);

        const filter: PositionFilter = b.build();

        const {positions} = await getPositionByFilter(filter as any);
        return mapResults ? mapResults(positions) : positions;
    }
}