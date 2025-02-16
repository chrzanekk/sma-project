import {ContractorDTO} from "@/types/contractor-types.ts";
import {useAuth} from "@/context/AuthContext.tsx";
import {useTranslation} from "react-i18next";
import {Button, Table, Text, useDisclosure} from "@chakra-ui/react";
import React, {useState} from "react";
import {useTheme} from "next-themes";
import {Field} from "@/components/ui/field.tsx";
import DateFormatter from "@/utils/date-formatter.ts";
import ConfirmModal from "@/components/shared/ConfirmModal.tsx";
import classNames from "classnames";
import '@/theme/css/global-table-styles.css';
import '@/theme/css/contractor-table-styles.css';


interface Props {
    contractors: ContractorDTO[];
    onDelete: (id: number) => void;
    fetchContractors: () => void;
}

const ContractorTable: React.FC<Props> = ({contractors, onDelete, fetchContractors}) => {
    const {t} = useTranslation(['common', 'contractors']);
    const {user: currentUser} = useAuth();
    const {open, onOpen, onClose} = useDisclosure();
    const [selectedContractorId, setSelectedContractorId] = useState<number | null>(null);
    const {theme} = useTheme();

    const tableClass = classNames('custom-table', 'contractor-table', {'dark-theme': theme === 'dark'});

    const handleDeleteClick = (id: number) => {
        setSelectedContractorId(id);
        onOpen();
    };

    const confirmDelete = () => {
        if (selectedContractorId !== null) {
            onDelete(selectedContractorId);
        }
        onClose();
    };

    if (!contractors || contractors.length === 0) {
        return (
            <Field alignContent={"center"}>
                <Text fontSize={20}>{t('dataNotFound')}</Text>
            </Field>)
    }

    return (
        <>
            <Table.Root className={tableClass}>
                <Table.Header>
                    <Table.Row>
                        <Table.ColumnHeader>ID</Table.ColumnHeader>
                        <Table.ColumnHeader>{t('contractors:name')}</Table.ColumnHeader>
                        <Table.ColumnHeader>{t('contractors:taxNumber')}</Table.ColumnHeader>
                        <Table.ColumnHeader>{t('contractors:address')}</Table.ColumnHeader>
                        <Table.ColumnHeader>{t('contractors:customer')}</Table.ColumnHeader>
                        <Table.ColumnHeader>{t('contractors:supplier')}</Table.ColumnHeader>
                        <Table.ColumnHeader>{t('contractors:scaffoldingUser')}</Table.ColumnHeader>
                        <Table.ColumnHeader>{t('createDate')}</Table.ColumnHeader>
                        <Table.ColumnHeader>{t('createdBy')}</Table.ColumnHeader>
                        <Table.ColumnHeader>{t('lastModifiedDate')}</Table.ColumnHeader>
                        <Table.ColumnHeader>{t('lastModifiedBy')}</Table.ColumnHeader>
                        <Table.ColumnHeader>{t('edit')}</Table.ColumnHeader>
                    </Table.Row>
                </Table.Header>
                <Table.Body>
                    {contractors.map((contractor) => (
                        <Table.Row key={contractor.id}>
                            <Table.Cell>{contractor.id}</Table.Cell>
                            <Table.Cell>{contractor.name}</Table.Cell>
                            <Table.Cell>{contractor.taxNumber}</Table.Cell>
                            <Table.Cell>
                                    <div>
                                        {contractor.street} {contractor.buildingNo}
                                        {contractor.apartmentNo && contractor.apartmentNo.trim() !== ""
                                            ? "/" + contractor.apartmentNo
                                            : ""},{" "}
                                        {contractor.postalCode} {contractor.city},{" "}
                                        {contractor.country && typeof contractor.country === "object"
                                            ? contractor.country.name
                                            : contractor.country || ""}
                                    </div>
                            </Table.Cell>
                            <Table.Cell>{contractor.customer ? t("common:yes") : t("common:no")}</Table.Cell>
                            <Table.Cell>{contractor.supplier ? t("common:yes") : t("common:no")}</Table.Cell>
                            <Table.Cell>{contractor.scaffoldingUser ? t("common:yes") : t("common:no")}</Table.Cell>
                            <Table.Cell>{DateFormatter.formatDateTime(contractor.createdDatetime!)}</Table.Cell>
                            <Table.Cell>{`${contractor.createdByFirstName} ${contractor.createdByLastName}`}</Table.Cell>
                            <Table.Cell>{DateFormatter.formatDateTime(contractor.lastModifiedDatetime!)}</Table.Cell>
                            <Table.Cell>{`${contractor.modifiedByFirstName} ${contractor.modifiedByLastName}`}</Table.Cell>
                            <Table.Cell>
                                {/*<HStack gap={1} alignContent={"center"}>*/}
                                {/*    <EditContractorDrawer*/}
                                {/*        fetchUsers={fetchContractors}*/}
                                {/*        contractorId={contractor.id!}/>*/}
                                {/*</HStack>*/}
                                <Button
                                    colorPalette="red"
                                    size={"2xs"}
                                    onClick={() => handleDeleteClick(contractor.id!)}
                                    disabled={currentUser?.id === contractor.id}>
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
                title={t("deleteConfirmation.title", {ns: "common"})}
                message={t("deleteConfirmation.message", {ns: "common"})}
                confirmText={t("delete", {ns: "common"})}
                cancelText={t("cancel", {ns: "common"})}
            />
        </>
    )

}


export default ContractorTable;