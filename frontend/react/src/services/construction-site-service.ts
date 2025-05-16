import {getSelectedCompanyId} from "@/utils/company-utils.ts";


const CONSTRUCTION_SITE_API_BASE = "/api/construction-sites";

export const getConstructionSitesByFilter = async (filter: Record<string, any>) => {
    try {
        const queryParams = serializeQueryParams({
            ...filter,
            companyId: getSelectedCompanyId(),
            size: filter.size || 10,
            page: filter.page || 0,
            sort: filter.sort || 'id,asc'
        });
        const response = await api.get(`${CONSTRUCTION_SITE_API_BASE}/page?${queryParams}`, getAuthConfig());
        const {items, totalPages} = parsePaginationResponse(response);
        return {
            constructionSites: items as FetchableConstructionSiteDTO[],
            totalPages
        }
    } catch (err) {
        return {constructionSites: [], totalPages: 1};
    }
}