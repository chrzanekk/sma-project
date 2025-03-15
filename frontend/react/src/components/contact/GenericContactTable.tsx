import {useTranslation} from "react-i18next";
import {Box, Button, HStack, Table, Text, useDisclosure} from "@chakra-ui/react";
import {useState} from "react";
import {Field} from "@/components/ui/field.tsx";
import DateFormatter from "@/utils/date-formatter.ts";
import ConfirmModal from "@/components/shared/ConfirmModal.tsx";
import "@/theme/css/global-table-styles.css";
import "@/theme/css/contact-table-styles.css";
import EditContactDialog from "@/components/contact/EditContactDialog.tsx";
import {BaseContactDTOForContractor, FetchableContactDTO} from "@/types/contact-types.ts";
import {themeColors} from "@/theme/theme-colors.ts";
import {useTableStyles} from "@/components/shared/tableStyles.ts";

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
    const {t} = useTranslation(["common", "contacts"]);
    const {open, onOpen, onClose} = useDisclosure();
    const [selectedContactId, setSelectedContactId] = useState<number | null>(null);
    const {commonCellProps, commonColumnHeaderProps} = useTableStyles();

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
            <Table.ScrollArea height={"auto"} borderWidth={"1px"} borderRadius={"md"} borderColor={"grey"}>
                <Table.Root size={"sm"}
                            stickyHeader
                            showColumnBorder
                            interactive
                            color={themeColors.fontColor()}
                >
                    <Table.Header>
                        <Table.Row bg={themeColors.bgColorPrimary()}>
                            <Table.ColumnHeader
                                {...commonColumnHeaderProps}
                                onClick={() => onSortChange("id")}
                            >
                                ID {renderSortIndicator("id")}
                            </Table.ColumnHeader>
                            <Table.ColumnHeader
                                {...commonColumnHeaderProps}
                                onClick={() => onSortChange("firstName")}
                            >
                                {t("contacts:firstName")} {renderSortIndicator("firstName")}
                            </Table.ColumnHeader>
                            <Table.ColumnHeader
                                {...commonColumnHeaderProps}
                                onClick={() => onSortChange("lastName")}
                            >
                                {t("contacts:lastName")} {renderSortIndicator("lastName")}
                            </Table.ColumnHeader>
                            <Table.ColumnHeader
                                {...commonColumnHeaderProps}
                                onClick={() => onSortChange("phoneNumber")}
                            >
                                {t("contacts:phoneNumber")} {renderSortIndicator("phoneNumber")}
                            </Table.ColumnHeader>
                            <Table.ColumnHeader
                                {...commonColumnHeaderProps}
                                onClick={() => onSortChange("email")}
                            >
                                {t("contacts:email")} {renderSortIndicator("email")}
                            </Table.ColumnHeader>
                            <Table.ColumnHeader
                                {...commonColumnHeaderProps}
                                onClick={() => onSortChange("additionalInfo")}
                            >
                                {t("contacts:additionalInfo")} {renderSortIndicator("additionalInfo")}
                            </Table.ColumnHeader>
                            {extended && (
                                <>
                                    <Table.ColumnHeader
                                        {...commonColumnHeaderProps}
                                        onClick={() => onSortChange("createdDatetime")}
                                    >
                                        {t("createDate")} {renderSortIndicator("createdDatetime")}
                                    </Table.ColumnHeader>
                                    <Table.ColumnHeader
                                        {...commonColumnHeaderProps}
                                        onClick={() => onSortChange("createdBy")}
                                    >
                                        {t("createdBy")} {renderSortIndicator("createdBy")}
                                    </Table.ColumnHeader>
                                    <Table.ColumnHeader
                                        {...commonColumnHeaderProps}
                                        onClick={() => onSortChange("lastModifiedDatetime")}
                                    >
                                        {t("lastModifiedDate")} {renderSortIndicator("lastModifiedDatetime")}
                                    </Table.ColumnHeader>
                                    <Table.ColumnHeader
                                        {...commonColumnHeaderProps}
                                        onClick={() => onSortChange("modifiedBy")}
                                    >
                                        {t("lastModifiedBy")} {renderSortIndicator("modifiedBy")}
                                    </Table.ColumnHeader>
                                    <Table.ColumnHeader color={themeColors.fontColor()}
                                                        textAlign={"center"}
                                                        borderColor={"gray"}
                                    >{t("edit")}
                                    </Table.ColumnHeader>
                                </>
                            )}

                        </Table.Row>
                    </Table.Header>
                    <Table.Body>
                        {contacts.map((contact) => (
                            <Table.Row key={contact.id}
                                       bg={themeColors.bgColorSecondary()}
                                       _hover={{
                                           textDecoration: 'none',
                                           bg: themeColors.highlightBgColor(),
                                           color: themeColors.fontColorHover()
                                       }}
                            >
                                <Table.Cell {...commonCellProps}>{contact.id}</Table.Cell>
                                <Table.Cell {...commonCellProps}>{contact.firstName}</Table.Cell>
                                <Table.Cell {...commonCellProps}>{contact.lastName}</Table.Cell>
                                <Table.Cell {...commonCellProps}>{contact.phoneNumber}</Table.Cell>
                                <Table.Cell {...commonCellProps}>{contact.email}</Table.Cell>
                                <Table.Cell {...commonCellProps}>{contact.additionalInfo}</Table.Cell>
                                {extended && isFetchableContact(contact) && (
                                    <>
                                        {/* Poniższe właściwości mogą być undefined dla BaseContactDTOForContractor */}
                                        {"createdDatetime" in contact && (
                                            <Table.Cell {...commonCellProps}>
                                                {DateFormatter.formatDateTime(
                                                    (contact as FetchableContactDTO).createdDatetime
                                                )}
                                            </Table.Cell>
                                        )}
                                        {"createdByFirstName" in contact && (
                                            <Table.Cell {...commonCellProps}>
                                                <div>{(contact as FetchableContactDTO).createdByFirstName}</div>
                                                <div>{(contact as FetchableContactDTO).createdByLastName}</div>
                                            </Table.Cell>
                                        )}
                                        {"lastModifiedDatetime" in contact && (
                                            <Table.Cell {...commonCellProps}>
                                                {DateFormatter.formatDateTime(
                                                    (contact as FetchableContactDTO).lastModifiedDatetime
                                                )}
                                            </Table.Cell>
                                        )}
                                        {"modifiedByFirstName" in contact && (
                                            <Table.Cell {...commonCellProps}>
                                                <div>{(contact as FetchableContactDTO).modifiedByFirstName}</div>
                                                <div>{(contact as FetchableContactDTO).modifiedByLastName}</div>
                                            </Table.Cell>
                                        )}
                                    </>
                                )}
                                {extended && (
                                    <Table.Cell {...commonCellProps}>
                                        <HStack gap={1} alignContent="center">
                                            <EditContactDialog
                                                fetchContacts={fetchContacts}
                                                contactId={contact.id!}
                                            />
                                            <Button
                                                colorPalette="red"
                                                size="2xs"
                                                onClick={() => handleDeleteClick(contact.id!)}
                                            >
                                                {t("delete", {ns: "common"})}
                                            </Button>
                                        </HStack>
                                    </Table.Cell>
                                )}
                            </Table.Row>
                        ))}
                    </Table.Body>
                </Table.Root>
            </Table.ScrollArea>

            <ConfirmModal
                isOpen={open}
                onClose={onClose}
                onConfirm={confirmDelete}
                title={t("deleteConfirmation.title", {ns: "common"})}
                message={t("deleteConfirmation.message", {ns: "common"})}
                confirmText={t("delete", {ns: "common"})}
                cancelText={t("cancel", {ns: "common"})}
            />
        </Box>
    );
};

export default ContactTable;
