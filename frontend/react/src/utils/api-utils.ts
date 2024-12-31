export const parsePaginationResponse = <T>(response: any): { items: T[]; totalPages: number } => {
    const data = response.data.content || [];
    const totalCount = parseInt(response.headers['x-total-count'], 10) || 0;
    const totalPages = Math.ceil(totalCount / (response.config.params?.size || 10));

    return {
        items: data,
        totalPages,
    };
};