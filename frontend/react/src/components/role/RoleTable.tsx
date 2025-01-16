import classNames from "classnames";
import ConfirmModal from "@/components/shared/ConfirmModal.tsx";
import React, {useState} from "react";
import {Button, Table, Text, useDisclosure} from "@chakra-ui/react";
import {RoleDTO} from "@/types/role-types.ts";
import {useTranslation} from "react-i18next";
import DateFormatter from "@/utils/date-formatter.ts";
import {Field} from "@/components/ui/field.tsx";
import {useTheme} from "next-themes";


interface Props {
    roles: RoleDTO[];
    onDelete: (id: number) => void;
}

const RoleTable: React.FC<Props> = ({roles, onDelete}) => {
    const {t} = useTranslation('auth')
    const {open, onOpen, onClose} = useDisclosure();
    const [selectedRoleId, setSelectedRoleId] = useState<number | null>(null);
    const {theme} = useTheme();

    const tableClass = classNames('custom-table', 'role-table', {
        'dark-theme': theme === 'dark'
    });


    const handleDeleteClick = (id: number) => {
        setSelectedRoleId(id);
        onOpen();
    }

    const confirmDelete = () => {
        if (selectedRoleId !== null) {
            onDelete(selectedRoleId);
        }
        onClose();
    }

    if (!roles || roles.length === 0) {
        return (
            <Field alignContent={"center"}>
                <Text fontSize={20}>{t('dataNotFound', {ns: "common"})}</Text>
            </Field>)
    }

    return (
        <>
            <Table.Root className={tableClass}>
                <Table.Header>
                    <Table.Row>
                        <Table.ColumnHeader>ID</Table.ColumnHeader>
                        <Table.ColumnHeader>{t('shared.roleName')}</Table.ColumnHeader>
                        <Table.ColumnHeader>{t('createDate', {ns: "common"})}</Table.ColumnHeader>
                        <Table.ColumnHeader>{t('lastModifiedDate', {ns: "common"})}</Table.ColumnHeader>
                        <Table.ColumnHeader>{t('delete', {ns: "common"})}</Table.ColumnHeader>
                    </Table.Row>
                </Table.Header>
                <Table.Body>
                    {roles.map((role) => (
                        <Table.Row key={role.id}>
                            <Table.Cell>{role.id}</Table.Cell>
                            <Table.Cell>{role.name}</Table.Cell>
                            <Table.Cell>{DateFormatter.formatDateTime(role.createdDatetime!)}</Table.Cell>
                            <Table.Cell>{DateFormatter.formatDateTime(role.lastModifiedDatetime!)}</Table.Cell>
                            <Table.Cell>
                                <Button colorPalette="red" size={"xs"}
                                        onClick={() => handleDeleteClick(role.id!)}>
                                    {t('delete', {ns: "common"})}
                                </Button>
                            </Table.Cell>
                        </Table.Row>
                    ))}
                </Table.Body>
            </Table.Root>

            <ConfirmModal
                isOpen={open}
                onClose={onClose}
                onConfirm={confirmDelete}
                title={t('deleteConfirmation.title', {ns: "common"})}
                message={t('deleteConfirmation.message', {ns: "common"})}
                confirmText={t('delete', {ns: "common"})}
                cancelText={t('cancel', {ns: "common"})}
            />
        </>
    );


};
export default RoleTable;
