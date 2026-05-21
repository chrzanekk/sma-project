export const serializeQueryParams = (params: Record<string, any>): string => {
    const query = new URLSearchParams();
    Object.keys(params).forEach((key) => {
        const value = params[key];
        if (Array.isArray(value)) {
            value.forEach((item) => query.append(key, item));
        } else if (value !== undefined && value !== null) {
            query.append(key, value);
        }
    });
    return query.toString();
};
