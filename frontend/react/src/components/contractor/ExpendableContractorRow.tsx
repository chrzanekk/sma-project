import {Box, Button, HStack, Table, Collapsible,Text} from "@chakra-ui/react";
import {FetchableContractorDTO} from "@/types/contractor-types.ts";
import React from "react";
import DateFormatter from "@/utils/date-formatter.ts";
import EditContractorDrawer from "@/components/contractor/EditContractorDrawer.tsx";
import EditContractorDialog from "@/components/contractor/EditContractorDialog.tsx";

const ExpandableContractorRow: React.FC<{
    contractor: FetchableContractorDTO;
    onDelete: (id: number) => void;
    fetchContractors: () => void;
    t: any;
}> = ({contractor, onDelete, fetchContractors, t}) => {
    return (
        <Collapsible.Root>
            {/* Główny wiersz tabeli */}
            <Table.Row key={contractor.id}>
                <Collapsible.Trigger>
                    <Table.Cell>{contractor.id}</Table.Cell>
                    <Table.Cell>{contractor.name}</Table.Cell>
                    <Table.Cell>{contractor.taxNumber}</Table.Cell>
                    <Table.Cell>
                        <div>
                            {contractor.street} {contractor.buildingNo}
                            {contractor.apartmentNo &&
                            contractor.apartmentNo.trim() !== ""
                                ? "/" + contractor.apartmentNo
                                : ""}
                            , {contractor.postalCode} {contractor.city},{" "}
                            {contractor.country &&
                            typeof contractor.country === "object"
                                ? contractor.country.name
                                : contractor.country || ""}
                        </div>
                    </Table.Cell>
                    <Table.Cell>{contractor.customer ? t("common:yes") : t("common:no")}</Table.Cell>
                    <Table.Cell>{contractor.supplier ? t("common:yes") : t("common:no")}</Table.Cell>
                    <Table.Cell>{contractor.scaffoldingUser ? t("common:yes") : t("common:no")}</Table.Cell>
                    <Table.Cell>{DateFormatter.formatDateTime(contractor.createdDatetime!)}</Table.Cell>
                    <Table.Cell>
                        <div>{contractor.createdByFirstName}</div>
                        <div>{contractor.createdByLastName}</div>
                    </Table.Cell>
                    <Table.Cell>{DateFormatter.formatDateTime(contractor.lastModifiedDatetime!)}</Table.Cell>
                    <Table.Cell>
                        <div>{contractor.modifiedByFirstName}</div>
                        <div>{contractor.modifiedByLastName}</div>
                    </Table.Cell>
                    <Table.Cell>
                        <HStack gap={1} alignContent={"center"}>
                            <EditContractorDrawer fetchContractors={fetchContractors} contractorId={contractor.id!}/>
                            <EditContractorDialog fetchContractors={fetchContractors} contractorId={contractor.id!}/>
                            <Button
                                colorPalette="red"
                                size={"2xs"}
                                onClick={() => onDelete(contractor.id!)}
                            >
                                {t("delete", {ns: "common"})}
                            </Button>
                        </HStack>
                    </Table.Cell>
                </Collapsible.Trigger>
            </Table.Row>
            {/* Rozwijany wiersz z listą kontaktów */}
            <Collapsible.Content>
                <Box bg="gray.50">
                    {contractor.contacts && contractor.contacts.length > 0 ? (
                        contractor.contacts.map((contact, index) => (
                            <Box key={index} borderBottom="1px solid" borderColor="gray.200" py={2}>
                                <Text fontWeight="bold">{contact.firstName}{" "}{contact.lastName}</Text>
                                <Text>{contact.email}{" "}{contact.phoneNumber}</Text>
                                <Text>{contact.additionalInfo}</Text>
                            </Box>
                        ))
                    ) : (
                        <Text>{t("noContacts", {ns: "contractors"})}</Text>
                    )}
                </Box>
            </Collapsible.Content>


        </Collapsible.Root>
    );
};

export default ExpandableContractorRow;