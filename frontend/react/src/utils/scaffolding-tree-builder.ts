// scaffolding-tree-builder.ts
import {FetchableScaffoldingLogPositionDTO} from "@/types/scaffolding-log-position-types.ts";

export const buildScaffoldingTree = (
    parents: FetchableScaffoldingLogPositionDTO[],
    children: FetchableScaffoldingLogPositionDTO[]
): FetchableScaffoldingLogPositionDTO[] => {

    const childrenByParentId = new Map<number, FetchableScaffoldingLogPositionDTO[]>();

    children.forEach(child => {
        const parentId = child.parentPosition?.id;
        if (parentId !== undefined) {
            if (!childrenByParentId.has(parentId)) {
                childrenByParentId.set(parentId, []);
            }
            childrenByParentId.get(parentId)!.push(child);
        }
    });

    return parents.map(parent => {
        // Jawna konstrukcja obiektu zamiast spread — TypeScript nie gubi typu
        const result: FetchableScaffoldingLogPositionDTO = {
            ...parent,
            mappedChildPositions: childrenByParentId.get(parent.id!) ?? []
        };
        return result;
    });
};