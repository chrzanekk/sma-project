import classNames from "classnames";
import ConfirmModal from "@/components/shared/ConfirmModal.tsx";
import React, {useState} from "react";
import {Button, Table, Tbody, Td, Text, Th, Thead, Tr, useDisclosure} from "@chakra-ui/react";
import {FaTrash} from "react-icons/fa6";
import {RoleDTO} from "@/types/role-types.ts";
import {useTranslation} from "react-i18next";
import DateFormatter from "@/utils/date-formatter.ts";

const tableClass = classNames('custom-table', 'role-table');

interface Props {
    roles: RoleDTO[];
    onDelete: (id: number) => void;
}

const RoleTable: React.FC<Props> = ({roles, onDelete}) => {
    const {t} = useTranslation('auth')
    const {isOpen, onOpen, onClose} = useDisclosure();
    const [selectedRoleId, setSelectedRoleId] = useState<number | null>(null);

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
        return <Text fontSize={20} align={"center"}>{t('dataNotFound', {ns: "common"})}</Text>
    }

    return (
        <>
            <Table className={tableClass}>
                <Thead>
                    <Tr>
                        <Th>ID</Th>
                        <Th>{t('shared.roleName')}</Th>
                        <Th>{t('createDate', {ns: "common"})}</Th>
                        <Th>{t('lastModifiedDate', {ns: "common"})}</Th>
                        <Th>{t('delete', {ns: "common"})}</Th>
                    </Tr>
                </Thead>
                <Tbody>
                    {roles.map((role) => (
                        <Tr key={role.id}>
                            <Td>{role.id}</Td>
                            <Td>{role.name}</Td>
                            <Td>{DateFormatter.formatDateTime(role.createdDatetime!)}</Td>
                            <Td>{DateFormatter.formatDateTime(role.lastModifiedDatetime!)}</Td>
                            <Td>
                                <Button leftIcon={<FaTrash/>} colorScheme="red" size={"xs"}
                                        onClick={() => handleDeleteClick(role.id!)}>
                                    {t('delete', {ns: "common"})}
                                </Button>
                            </Td>
                        </Tr>
                    ))}
                </Tbody>
            </Table>

            <ConfirmModal
                isOpen={isOpen}
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
