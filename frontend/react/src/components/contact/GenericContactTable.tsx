import { useTranslation } from "react-i18next";
import {
    Box,
    Button,
    HStack,
    Table,
    Text,
    useDisclosure,
} from "@chakra-ui/react";
import { useState } from "react";
import { useTheme } from "next-themes";
import { Field } from "@/components/ui/field.tsx";
import DateFormatter from "@/utils/date-formatter.ts";
import ConfirmModal from "@/components/shared/ConfirmModal.tsx";
import classNames from "classnames";
import "@/theme/css/global-table-styles.css";
import "@/theme/css/contact-table-styles.css";
import EditContactDrawer from "@/components/contact/EditContactDrawer.tsx";
import EditContactDialog from "@/components/contact/EditContactDialog.tsx";
import {BaseContactDTOForContractor, FetchableContactDTO} from "@/types/contact-types.ts";

function isFetchableContact(
    contact: BaseContactDTOForContractor
): contact is FetchableContactDTO {
    return "createdDatetime" in contact;
}


interface Props<T extends BaseContactDTOForContractor> {
    contacts: T[];
    onDelete: (id: number) => void;
    fetchContacts: () => void;
    onSortChange: (field: string) => void;
    sortField: string | null;
    sortDirection: "asc" | "desc";
    extended?: boolean; // jeśli true, renderujemy dodatkowe kolumny
}

const ContactTable = <T extends BaseContactDTOForContractor>({
                                                                 contacts,
                                                                 onDelete,
                                                                 fetchContacts,
                                                                 onSortChange,
                                                                 sortField,
                                                                 sortDirection,
                                                                 extended = false,
                                                             }: Props<T>) => {
    const { t } = useTranslation(["common", "contacts"]);
    const { open, onOpen, onClose } = useDisclosure();
    const [selectedContactId, setSelectedContactId] = useState<number | null>(null);
    const { theme } = useTheme();

    const tableClass = classNames("custom-table", "contact-table", {
        "dark-theme": theme === "dark",
    });

    const handleDeleteClick = (id: number) => {
        setSelectedContactId(id);
        onOpen();
    };

    const confirmDelete = () => {
        if (selectedContactId !== null) {
            onDelete(selectedContactId);
        }
        onClose();
    };

    if (!contacts || contacts.length === 0) {
        return (
            <Field alignContent="center">
                <Text fontSize={20}>{t("dataNotFound")}</Text>
            </Field>
        );
    }

    const renderSortIndicator = (field: string) => {
        if (sortField === field) {
            return sortDirection === "asc" ? "↑" : "↓";
        }
        return null;
    };

    return (
        <Box>
            <Table.Root className={tableClass}>
                <Table.Header>
                    <Table.Row>
                        <Table.ColumnHeader
                            cursor="pointer"
                            onClick={() => onSortChange("id")}
                        >
                            ID {renderSortIndicator("id")}
                        </Table.ColumnHeader>
                        <Table.ColumnHeader
                            cursor="pointer"
                            onClick={() => onSortChange("firstName")}
                        >
                            {t("contacts:firstName")} {renderSortIndicator("firstName")}
                        </Table.ColumnHeader>
                        <Table.ColumnHeader
                            cursor="pointer"
                            onClick={() => onSortChange("lastName")}
                        >
                            {t("contacts:lastName")} {renderSortIndicator("lastName")}
                        </Table.ColumnHeader>
                        <Table.ColumnHeader
                            cursor="pointer"
                            onClick={() => onSortChange("phoneNumber")}
                        >
                            {t("contacts:phoneNumber")} {renderSortIndicator("phoneNumber")}
                        </Table.ColumnHeader>
                        <Table.ColumnHeader
                            cursor="pointer"
                            onClick={() => onSortChange("email")}
                        >
                            {t("contacts:email")} {renderSortIndicator("email")}
                        </Table.ColumnHeader>
                        <Table.ColumnHeader
                            cursor="pointer"
                            onClick={() => onSortChange("additionalInfo")}
                        >
                            {t("contacts:additionalInfo")} {renderSortIndicator("additionalInfo")}
                        </Table.ColumnHeader>
                        {extended && (
                            <>
                                <Table.ColumnHeader
                                    cursor="pointer"
                                    onClick={() => onSortChange("createdDatetime")}
                                >
                                    {t("createDate")} {renderSortIndicator("createdDatetime")}
                                </Table.ColumnHeader>
                                <Table.ColumnHeader
                                    cursor="pointer"
                                    onClick={() => onSortChange("createdBy")}
                                >
                                    {t("createdBy")} {renderSortIndicator("createdBy")}
                                </Table.ColumnHeader>
                                <Table.ColumnHeader
                                    cursor="pointer"
                                    onClick={() => onSortChange("lastModifiedDatetime")}
                                >
                                    {t("lastModifiedDate")} {renderSortIndicator("lastModifiedDatetime")}
                                </Table.ColumnHeader>
                                <Table.ColumnHeader
                                    cursor="pointer"
                                    onClick={() => onSortChange("modifiedBy")}
                                >
                                    {t("lastModifiedBy")} {renderSortIndicator("modifiedBy")}
                                </Table.ColumnHeader>
                            </>
                        )}
                        <Table.ColumnHeader>{t("edit")}</Table.ColumnHeader>
                    </Table.Row>
                </Table.Header>
                <Table.Body>
                    {contacts.map((contact) => (
                        <Table.Row key={contact.id}>
                            <Table.Cell>{contact.id}</Table.Cell>
                            <Table.Cell>{contact.firstName}</Table.Cell>
                            <Table.Cell>{contact.lastName}</Table.Cell>
                            <Table.Cell>{contact.phoneNumber}</Table.Cell>
                            <Table.Cell>{contact.email}</Table.Cell>
                            <Table.Cell>{contact.additionalInfo}</Table.Cell>
                            {extended && isFetchableContact(contact) && (
                                <>
                                    {/* Poniższe właściwości mogą być undefined dla BaseContactDTOForContractor */}
                                    {"createdDatetime" in contact && (
                                        <Table.Cell>
                                            {DateFormatter.formatDateTime(
                                                (contact as FetchableContactDTO).createdDatetime
                                            )}
                                        </Table.Cell>
                                    )}
                                    {"createdByFirstName" in contact && (
                                        <Table.Cell>
                                            <div>{(contact as FetchableContactDTO).createdByFirstName}</div>
                                            <div>{(contact as FetchableContactDTO).createdByLastName}</div>
                                        </Table.Cell>
                                    )}
                                    {"lastModifiedDatetime" in contact && (
                                        <Table.Cell>
                                            {DateFormatter.formatDateTime(
                                                (contact as FetchableContactDTO).lastModifiedDatetime
                                            )}
                                        </Table.Cell>
                                    )}
                                    {"modifiedByFirstName" in contact && (
                                        <Table.Cell>
                                            <div>{(contact as FetchableContactDTO).modifiedByFirstName}</div>
                                            <div>{(contact as FetchableContactDTO).modifiedByLastName}</div>
                                        </Table.Cell>
                                    )}
                                </>
                            )}
                            <Table.Cell>
                                <HStack gap={1} alignContent="center">
                                    <EditContactDrawer
                                        fetchContacts={fetchContacts}
                                        contactId={contact.id!}
                                    />
                                    <EditContactDialog
                                        fetchContacts={fetchContacts}
                                        contactId={contact.id!}
                                    />
                                    <Button
                                        colorPalette="red"
                                        size="2xs"
                                        onClick={() => handleDeleteClick(contact.id!)}
                                    >
                                        {t("delete", { ns: "common" })}
                                    </Button>
                                </HStack>
                            </Table.Cell>
                        </Table.Row>
                    ))}
                </Table.Body>
            </Table.Root>

            <ConfirmModal
                isOpen={open}
                onClose={onClose}
                onConfirm={confirmDelete}
                title={t("deleteConfirmation.title", { ns: "common" })}
                message={t("deleteConfirmation.message", { ns: "common" })}
                confirmText={t("delete", { ns: "common" })}
                cancelText={t("cancel", { ns: "common" })}
            />
        </Box>
    );
};

export default ContactTable;
