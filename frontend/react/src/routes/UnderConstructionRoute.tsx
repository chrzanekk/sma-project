import React from "react";
import UnderConstruction from "@/components/shared/UnderConstruction.tsx";
import {useTranslation} from "react-i18next";

interface UnderConstructionRouteProps {
    nameKey?: string;
    nameSpace?: string;
}

const UnderConstructionRoute: React.FC<UnderConstructionRouteProps> = ({
                                                                           nameKey,
                                                                           nameSpace
                                                                       }) => {
    const {t} = useTranslation();
    const name = nameKey ? t(nameKey, {ns: nameSpace}) : "unknown";
    return (
        <UnderConstruction
            title={t('underConstruction.title', {ns: "common"})}
            description={t('underConstruction.message', {ns: "common", name})}
            onBackToHome={() => window.location.href = "/dashboard"}
        />
    );
};

export default UnderConstructionRoute;
