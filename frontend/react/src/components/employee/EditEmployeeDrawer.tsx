import React from "react";
import {useTranslation} from "react-i18next";
import {useThemeColors} from "@/theme/theme-colors.ts";
import {Box, Button, DrawerContext, Heading} from "@chakra-ui/react";
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
import EditEmployeeForm from "@/components/employee/EditEmployeeForm.tsx";

interface EditEmployeeDrawerProps {
    fetchEmployees: () => void;
    employeeId: number;
}


const EditEmployeeDrawer: React.FC<EditEmployeeDrawerProps> = ({fetchEmployees, employeeId}) => {
    const {t} = useTranslation('employees');
    const themeColors = useThemeColors();

    return (
        <Box>
            <DrawerRoot size={"lg"} placement={'end'}>
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
                <DrawerContent bg={themeColors.bgColorSecondary}
                               offset={"4"}
                               borderRadius={"md"}>
                    <DrawerContext>
                        {(store) => (
                            <Box>
                                <DrawerCloseTrigger/>
                                <DrawerHeader>
                                    <Heading size={"xl"} color={themeColors.fontColor}>
                                        {t("employees:edit")}
                                    </Heading>
                                </DrawerHeader>
                                <DrawerBody>
                                    <EditEmployeeForm
                                        onSuccess={() => {
                                            fetchEmployees();
                                            store.setOpen(false);
                                        }}
                                        employeeId={employeeId}/>
                                </DrawerBody>
                                <DrawerFooter>
                                    <DrawerActionTrigger asChild>
                                        <Button
                                            colorPalette="red"
                                            onClick={() => store.setOpen(false)}
                                        >
                                            {t("close", {ns: "common"})}
                                        </Button>
                                    </DrawerActionTrigger>
                                </DrawerFooter>
                            </Box>
                        )}
                    </DrawerContext>
                </DrawerContent>
            </DrawerRoot>
        </Box>
    )
}

export default EditEmployeeDrawer;