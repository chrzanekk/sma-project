import {useThemeColors} from "@/theme/theme-colors.ts";
import {Box, Button, DialogContext, Heading} from "@chakra-ui/react";
import {
    DialogActionTrigger,
    DialogBackdrop,
    DialogBody,
    DialogCloseTrigger,
    DialogContent,
    DialogFooter,
    DialogHeader,
    DialogRoot,
    DialogTrigger
} from "@/components/ui/dialog.tsx";
import {FaPlus} from "react-icons/fa";
import React from "react";
import {useTranslation} from "react-i18next";
import ConstructionSiteWithContractorsFormSteps
    from "@/components/constructionsite/ConstructionSiteWithContractorsFormSteps.tsx";

interface AddConstructionSiteDialogProps {
    fetchConstructionSites: () => void;
}

const AddConstructionSiteDialog: React.FC<AddConstructionSiteDialogProps> = ({fetchConstructionSites}) => {
    const {t} = useTranslation('constructionSites');
    const themeColors = useThemeColors();
    return (
        <Box>
            <DialogRoot size={"lg"} placement={"top"}>
                <DialogBackdrop/>
                <DialogTrigger asChild>
                    <Button
                        colorPalette="green"
                        size={"2xs"}
                        p={1}
                    ><FaPlus/>
                        {t('add')}
                    </Button>
                </DialogTrigger>
                <DialogContent bg={themeColors.bgColorSecondary} offset={"4"} borderRadius={"md"}>
                    <DialogContext>
                        {(store) => (
                            <Box>
                                <DialogCloseTrigger/>
                                <DialogHeader>
                                    <Heading size={"xl"} color={themeColors.fontColor}>
                                        {t("constructionSites:details")}
                                    </Heading>
                                </DialogHeader>
                                <DialogBody>
                                    <ConstructionSiteWithContractorsFormSteps
                                        onSuccess={() => {
                                            fetchConstructionSites();
                                            store.setOpen(false);
                                        }}
                                    />
                                </DialogBody>
                                <DialogFooter>
                                    <DialogActionTrigger asChild>
                                        <Button
                                            colorPalette="red"
                                            onClick={() => store.setOpen(false)}
                                        >
                                            {t("close", {ns: "common"})}
                                        </Button>
                                    </DialogActionTrigger>
                                </DialogFooter>
                            </Box>
                        )}
                    </DialogContext>
                </DialogContent>
            </DialogRoot>
        </Box>
    )
}

export default AddConstructionSiteDialog;