import React, {useState} from 'react';
import {Box, Button, Flex, Grid, GridItem, Heading, Spinner, Text} from '@chakra-ui/react';
import RoleManagement from '@/components/role/RoleManagement';
import UserManagement from '@/components/user/UserManagement';
import {useThemeColors} from "@/theme/theme-colors.ts";
import {useTranslation} from "react-i18next";
import {MenuContent, MenuItem, MenuRoot, MenuTrigger,} from "@/components/ui/menu"
import CompanyManagement from "@/components/company/CompanyManagement.tsx";
import PositionManagement from "@/components/position/PositionManagement.tsx";
import ResourcePermissionManagement from "@/components/admin/ResourcePermissionManagement.tsx";
import {useResourcePermissions} from "@/context/ResourcePermissionContext.tsx";
import {useAdminPanelMenu} from "@/hooks/useAdminPanelMenu.ts";

export type AdminPanelView = 'roles' | 'users' | 'companies' | 'positions' | 'permissions';

const AdminPanel: React.FC = () => {
    const {t} = useTranslation('adminPanelMenu');
    const themeColors = useThemeColors();
    const {canAccessResource, loading} = useResourcePermissions();

    // ✅ Don't set default view - let user choose
    const [activeView, setActiveView] = useState<AdminPanelView | null>(null);

    // ✅ Use custom hook for menu items
    const adminMenuItems = useAdminPanelMenu(t, (view) => setActiveView(view));

    // Show loading while fetching permissions
    if (loading) {
        return (
            <Box display="flex" justifyContent="center" alignItems="center" minH="400px">
                <Spinner size="xl"/>
            </Box>
        );
    }

    // No access to any admin resource
    if (adminMenuItems.length === 0) {
        return (
            <Box p={8} textAlign="center">
                <Heading size="md" color={themeColors.fontColor}>
                    {t('noAccess', 'You do not have access to any admin panels')}
                </Heading>
            </Box>
        );
    }


    const renderActiveView = () => {
        if (!activeView) {
            return (
                <Box
                    display="flex"
                    flexDirection="column"
                    justifyContent="center"
                    alignItems="center"
                    minH="200px"
                    p={8}
                >
                    <Heading
                        size="lg"
                        mb={4}
                        color={themeColors.fontColor}
                        textAlign="center"
                    >
                        {t('selectViewMessage', 'Wybierz widok z menu')}
                    </Heading>
                    <Text color={themeColors.fontColor}
                    >
                        {t('selectViewHint', 'Użyj przycisku "Wybierz widok" aby wybrać panel administracyjny')}
                    </Text>
                </Box>
            );
        }

        // ✅ Check permission before rendering view
        switch (activeView) {
            case 'roles':
                return canAccessResource('ROLE_MANAGEMENT') ? (
                    <RoleManagement/>
                ) : null;
            case 'users':
                return canAccessResource('USER_MANAGEMENT') ? (
                    <UserManagement/>
                ) : null;
            case 'companies':
                return canAccessResource('COMPANY_MANAGEMENT') ? (
                    <CompanyManagement/>
                ) : null;
            case 'positions':
                return canAccessResource('POSITION_MANAGEMENT') ? (
                    <PositionManagement/>
                ) : null;
            case 'permissions':
                return canAccessResource('RESOURCE_MANAGEMENT') ? (
                    <ResourcePermissionManagement/>
                ) : null;
            default:
                return (
                    <Heading size="md" textAlign="center" color={themeColors.fontColor}>
                        {t('invalidView', 'Invalid view')}
                    </Heading>
                );
        }
    };

    return (
        <Grid
            height={"100%"}
            bgColor={themeColors.bgColorSecondary}
            gap={1}
            px={1}
            py={1}
        >
            {/* Wiersz 1: Tytuł i menu wyboru */}
            <GridItem
                w={"100%"}
                p={1}
                bg={themeColors.bgColorPrimary}
                borderRadius={"lg"}
                color={"white"}
            >
                <Flex
                    justifyContent={"space-between"}
                >
                    <Heading size={"lg"}
                             fontSize={"16px"}
                             textAlign={"center"}
                             color={themeColors.fontColor}>
                        {t('adminPanel')}
                    </Heading>
                    <MenuRoot>
                        <MenuTrigger asChild>
                            <Button
                                bg={themeColors.bgColorPrimary}
                                p={2}
                                color={themeColors.fontColor}
                                _hover={{
                                    textDecoration: 'none',
                                    bg: themeColors.highlightBgColor,
                                    color: themeColors.popoverBgColor
                                }}
                                variant="outline"
                                size="2xs"
                                fontSize="clamp(10px, 1.5vw, 12px)"
                                textAlign="center"
                                w="auto" // Szerokość automatyczna
                                h="auto" // Dostosowanie wysokości do rodzica
                                maxH="40px"
                            >
                                {t("selectView")}
                            </Button>
                        </MenuTrigger>
                        <MenuContent
                            bgColor={themeColors.bgColorSecondary}
                        >
                            {adminMenuItems.map((item, index) => (
                                <MenuItem
                                    key={item.label || index}
                                    bg={themeColors.bgColorSecondary}
                                    rounded={'md'}
                                    color={themeColors.fontColor}
                                    p={[2, 3]}
                                    _hover={{
                                        textDecoration: 'none',
                                        bg: themeColors.highlightBgColor,
                                        color: themeColors.popoverBgColor,
                                        border: '1px solid white'
                                    }}
                                    value={item.label}
                                    valueText={item.label}
                                    onClick={item.onClick}
                                    closeOnSelect={true}
                                >
                                    {item.label}
                                </MenuItem>
                            ))}
                        </MenuContent>
                    </MenuRoot>
                </Flex>
            </GridItem>

            <GridItem
                w="100%"
                bg={themeColors.bgColorPrimary}
                borderRadius="lg"
                overflowY={"auto"}
            >
                {renderActiveView()}
            </GridItem>
        </Grid>
    );
};

export default AdminPanel;
