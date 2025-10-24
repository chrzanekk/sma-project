import React, {useState} from 'react';
import {Button, Flex, Grid, GridItem, Heading} from '@chakra-ui/react';
import RoleManagement from '@/components/role/RoleManagement';
import UserManagement from '@/components/user/UserManagement';
import {useThemeColors} from "@/theme/theme-colors.ts";
import {getAdminPanelMenuItems} from "@/components/admin/admin-panel-menu-items.ts";
import {useTranslation} from "react-i18next";
import {MenuContent, MenuItem, MenuRoot, MenuTrigger,} from "@/components/ui/menu"
import CompanyManagement from "@/components/company/CompanyManagement.tsx";
import PositionManagement from "@/components/position/PositionManagement.tsx";

export type AdminPanelView = 'roles' | 'users' | 'companies' | 'positions';

const AdminPanel: React.FC = () => {
    const {t} = useTranslation('adminPanelMenu');
    const [activeView, setActiveView] = useState<AdminPanelView>('users');
    const themeColors = useThemeColors();

    const renderActiveView = () => {
        switch (activeView) {
            case 'roles':
                return <RoleManagement/>;
            case 'users':
                return <UserManagement/>;
            case 'companies':
                return <CompanyManagement/>;
            case 'positions':
                return <PositionManagement/>;
            default:
                return <Heading size="md" textAlign="center">Invalid view</Heading>;
        }
    };

    const adminMenuItems = getAdminPanelMenuItems(t, setActiveView);

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
                mt={-1}
            >
                {renderActiveView()}
            </GridItem>
        </Grid>
    );
};

export default AdminPanel;
