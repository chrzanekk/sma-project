import React from "react";
import {useThemeColors} from "@/theme/theme-colors.ts";
import {useTranslation} from "react-i18next";
import BasicLayout from "@/components/shared/BasicLayout.tsx";

interface ContactLayoutProps {
    filters: React.ReactNode;
    addContactButton: React.ReactNode;
    table: React.ReactNode;
    pagination: React.ReactNode;
    bgColor?: string;
}

const ContactLayout: React.FC<ContactLayoutProps> = ({
                                                         filters,
                                                         addContactButton,
                                                         table,
                                                         pagination,
                                                         bgColor = useThemeColors().bgColorPrimary
                                                     }) => {
    const {t} = useTranslation('contacts');
    return (
        <BasicLayout headerTitle={t('contacts:contactList')}
                     filters={filters}
                     addButton={addContactButton}
                     table={table}
                     pagination={pagination}
                     bgColor={bgColor}/>
    );
};

export default ContactLayout;