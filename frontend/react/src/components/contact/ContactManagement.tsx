import React from "react";
import {deleteContractorById} from "@/services/contractor-service.ts";
import Pagination from "@/components/shared/Pagination.tsx";
import {Flex} from "@chakra-ui/react";
import {getContactsByFilter} from "@/services/contact-service.ts";
import AddContactDialog from "@/components/contact/AddContactDialog.tsx";
import ContactFilterForm from "@/components/contact/ContactFilterForm.tsx";
import ContactLayout from "@/components/contact/ContactLayout.tsx";
import GenericContactTable from "@/components/contact/GenericContactTable.tsx";
import {useDataManagement} from "@/hooks/useDataManagement.ts";


const ContactManagement: React.FC = () => {
    const {
        items: contacts,
        currentPage,
        totalPages,
        rowsPerPage,
        sortField,
        sortDirection,
        handlers: {
            onPageChange,
            onRowsPerPageChange,
            onSortChange,
            onFilterSubmit,
            onDelete
        }

    } = useDataManagement(
        params =>
            getContactsByFilter(params).then(res => ({
                data: res.contacts,
                totalPages: res.totalPages,
            })),
        deleteContractorById,
    );

    return (
        <ContactLayout
            filters={<ContactFilterForm onSubmit={onFilterSubmit}/>}
            addContactButton={
                <Flex justifyContent={"center"} gap={2}>
                    <AddContactDialog fetchContacts={() => onPageChange(0)}/>
                </Flex>
            }
            table={
                <GenericContactTable
                    contacts={contacts}
                    onDelete={onDelete}
                    fetchContacts={() => onPageChange(0)}
                    onSortChange={onSortChange}
                    sortField={sortField}
                    sortDirection={sortDirection}
                    extended={true}
                />}
            pagination={
                <Pagination
                    currentPage={currentPage}
                    totalPages={totalPages}
                    rowsPerPage={rowsPerPage}
                    onPageChange={onPageChange}
                    onRowsPerPageChange={onRowsPerPageChange}
                />
            }
        />
    );
};

export default ContactManagement;