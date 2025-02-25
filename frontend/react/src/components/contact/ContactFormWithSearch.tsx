// import {AddContactDTO, AddContactFormValues, ContactDTO} from "@/types/contact-types.ts";
// import {useState} from "react";
// import {getContactsByFilter} from "@/services/contact-service.ts";
//
// interface ContatFormWithSearchProps {
//     onSuccess: (values: AddContactDTO) => void;
// }
//
// const ContactFormWithSearch: React.FC<ContatFormWithSearchProps> = ({onSuccess}) => {
//     const [selectedContact, setSelectedContact] = useState<AddContactFormValues | null>(null);
//     const [searchTerm, setSerchTerm] = useState('');
//     const [searchResults, setSearchResults] = useState<ContactDTO[]>([]);
//
//     const handleSearch = async () => {
//         try {
//             const result = await getContactsByFilter({name: searchTerm});
//             setSearchResults(result.contacts)
//         } catch (err) {
//             console.error('Bład wyszukiwania kontaktu', err);
//         }
//     };
//     const handleSelectContact = (contact: AddContactFormValues) => {
//         const contactToAdd = {...contact};
//         setSelectedContact(contactToAdd);
//         setSearchResults([]);
//     }
//
//     const handleResetContact = () => {
//         setSelectedContact(null);
//     }
//
// }