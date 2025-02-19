import {useTranslation} from "react-i18next";
import {
    DrawerActionTrigger,
    DrawerBackdrop,
    DrawerBody,
    DrawerCloseTrigger,
    DrawerContent,
    DrawerFooter,
    DrawerHeader,
    DrawerRoot,
    DrawerTrigger
} from "@/components/ui/drawer.tsx";
import {Button, DrawerContext, Heading} from "@chakra-ui/react";
import {themeColors} from "@/theme/theme-colors.ts";
import {FaTimes} from "react-icons/fa";
import React from "react";
import EditContactForm from "@/components/contact/EditContactForm.tsx";


interface EditContractorDrawerProps {
    fetchContacts: () => void;
    contactId: number;
}

const EditContactDrawer: React.FC<EditContractorDrawerProps> = ({fetchContacts, contactId}) => {
    const {t} = useTranslation(['common', 'contacts']);

    return (
        <>
            <DrawerRoot size={"lg"} placement={"end"}>
                <DrawerBackdrop/>
                <DrawerTrigger asChild>
                    <Button
                        colorPalette="green"
                        size={"2xs"}
                        p={1}
                    >
                        {t('data', {ns: "common"})}
                    </Button>
                </DrawerTrigger>
                <DrawerContent bg={themeColors.bgColorSecondary()}
                               offset={"2"}
                               borderRadius={"md"}>
                    <DrawerContext>
                        {(store) => (
                            <>
                                <DrawerCloseTrigger/>
                                <DrawerHeader>
                                    <Heading size={"xl"} color={themeColors.fontColor()}>
                                        {t("contractors:edit")}
                                    </Heading>
                                </DrawerHeader>
                                <DrawerBody>
                                    <EditContactForm
                                        onSuccess={() => {
                                            fetchContacts();
                                            store.setOpen(false);
                                        }}
                                        contactId={contactId}
                                    />
                                </DrawerBody>
                                <DrawerFooter>
                                    <DrawerActionTrigger asChild>
                                        <Button
                                            colorPalette="red"
                                            onClick={() => store.setOpen(false)}
                                        >
                                            <FaTimes/>
                                            {t("close", {ns: "common"})}
                                        </Button>
                                    </DrawerActionTrigger>
                                </DrawerFooter>
                            </>

                        )}
                    </DrawerContext>
                </DrawerContent>
            </DrawerRoot>
        </>
    )
}

export default EditContactDrawer;