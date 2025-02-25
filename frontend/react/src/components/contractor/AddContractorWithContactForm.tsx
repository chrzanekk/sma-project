// import {useTranslation} from "react-i18next";
// import React, {useState} from "react";
// import {AddContractorDTO} from "@/types/contractor-types.ts";
// import {AddContactDTO} from "@/types/contact-types.ts";
// import {addContractor} from "@/services/contractor-service.ts";
// import {
//     StepsCompletedContent,
//     StepsContent,
//     StepsItem,
//     StepsList,
//     StepsNextTrigger,
//     StepsPrevTrigger,
//     StepsRoot,
// } from "@/components/ui/steps"
// import AddContractorForm from "@/components/contractor/AddContractorForm.tsx";
// import {Button, Group} from "@chakra-ui/react";
//
//
// interface AddContractorWithContactFormProps {
//     onSuccess: () => void;
// }
//
// const AddContractorWithContactForm: React.FC<AddContractorWithContactFormProps> = ({onSuccess}) => {
//     const {t} = useTranslation(['common', 'contractors']);
//     const [addContractorDto, setAddContractorDto] = useState<AddContractorDTO | null>(null);
//     const [selectedContactDto, setSelectedContactDto] = useState<AddContactDTO | null>(null);
//
//     const handleContractorData = (data: AddContractorDTO) => {
//         setAddContractorDto(data);
//     }
//
//     const handleContactData = (data: AddContactDTO) => {
//         setSelectedContactDto(data);
//     }
//
//     const handleFinalSubmit = async () => {
//         if(!addContractorDto) return;
//         const payload = {
//             ...addContractorDto,
//             contacts: selectedContactDto ? [selectedContactDto] :[]
//         };
//         try {
//             await addContractor(payload);
//             onSuccess();
//         } catch (err) {
//             console.error('Błąd podczas dodawania kontrahenta z kontaktem', err);
//         }
//     };
//
//     return (
//         <StepsRoot defaultStep={0} count={3}>
//             <StepsList>
//                 //todo translation
//                 <StepsItem index={0} title="Dane kontrahenta" />
//                 <StepsItem index={1} title="Kontakt" />
//                 <StepsItem index={2} title="Podsumowanie" />
//             </StepsList>
//             <StepsContent index={0}>
//                 <AddContractorForm onSuccess={handleContractorData}/>
//             </StepsContent>
//             <StepsContent index={1}>
//                 <ContactFormWithSearch onSuccess={handleContactData}/>
//             </StepsContent>
//             <StepsContent index={2}>
//                 <div>
//                     <h3>Podsumowanie</h3>
//                     <p>
//                         <strong>Dane kontrahenta:</strong> {addContractorDto ? addContractorDto.name : 'Brak danych'}
//                     </p>
//                     <p>
//                         <strong>Wybrany kontakt:</strong>{' '}
//                         {selectedContactDto ? `${selectedContactDto.firstName} ${selectedContactDto.lastName}` : 'Brak'}
//                     </p>
//                 </div>
//                 <Button onClick={handleFinalSubmit}>Zapisz</Button>
//             </StepsContent>
//             <StepsCompletedContent>
//                 Wszystkie kroki zakończone!
//             </StepsCompletedContent>
//             <Group>
//                 <StepsPrevTrigger asChild>
//                     <Button variant="outline" size="sm">
//                         Poprzedni
//                     </Button>
//                 </StepsPrevTrigger>
//                 <StepsNextTrigger asChild>
//                     <Button variant="outline" size="sm">
//                         Następny
//                     </Button>
//                 </StepsNextTrigger>
//             </Group>
//         </StepsRoot>
//     )
// }
//
// export default AddContractorWithContactForm;