import React, {useState} from 'react';
import {Button, Flex, Grid, GridItem, Heading} from '@chakra-ui/react';
import RoleManagement from '@/components/role/RoleManagement';
import UserManagement from '@/components/user/UserManagement';
import {themeColors} from "@/theme/theme-colors.ts";
import {getAdminPanelMenuItems} from "@/layout/admin-panel-menu-items.ts";
import {useTranslation} from "react-i18next";
import {MenuContent, MenuItem, MenuRoot, MenuTrigger,} from "@/components/ui/menu"

export type AdminPanelView = 'roles' | 'users';

const AdminPanel: React.FC = () => {
    const {t} = useTranslation('adminPanelMenu');
    const [activeView, setActiveView] = useState<AdminPanelView>('users');

    const renderActiveView = () => {
        switch (activeView) {
            case 'roles':
                return <RoleManagement/>;
            case 'users':
                return <UserManagement/>;
            default:
                return <Heading size="md" textAlign="center">Invalid view</Heading>;
        }
    };

    const adminMenuItems = getAdminPanelMenuItems(setActiveView);

    return (
        <Grid
            templateRows="6% 94%"
            bgColor={themeColors.bgColorLight()}
            gap={1}
            px={1}
            py={1}
        >
            {/* Wiersz 1: Tytuł i menu wyboru */}
            <GridItem
                w="100%"
                p={1}
                bg={themeColors.bgColor()}
                borderRadius="lg"
                color="white"
            >
                <Flex
                    justifyContent="space-between"
                    alignSelf="center"
                >
                    <Heading size="sm"
                             fontSize="clamp(12px, 2vw, 14px)"
                             alignSelf="center"
                             color={themeColors.fontColor()}>
                        {t('adminPanel')}
                    </Heading>
                    <MenuRoot>
                        <MenuTrigger asChild>
                            <Button
                                bg={themeColors.bgColor()}
                                p={2}
                                color={themeColors.fontColor()}
                                _hover={{
                                    textDecoration: 'none',
                                    bg: themeColors.highlightBgColor(),
                                    color: themeColors.popoverBgColor()
                                }}
                                variant="outline"
                                size="2xs"
                                fontSize="clamp(10px, 1.5vw, 12px)"
                                textAlign="center"
                                w="auto" // Szerokość automatyczna
                                h="100%" // Dostosowanie wysokości do rodzica
                                maxH="40px"
                            >
                                {t("selectView")}
                            </Button>
                        </MenuTrigger>
                        <MenuContent bgColor={themeColors.bgColor()}>
                            {adminMenuItems.map((item, index) => (
                                <MenuItem
                                    key={item.label || index}
                                    bg={themeColors.bgColor()}
                                    rounded={'md'}
                                    p={[2, 3]}
                                    _hover={{
                                        textDecoration: 'none',
                                        bg: themeColors.highlightBgColor(),
                                        color: themeColors.popoverBgColor(),
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
                bg={themeColors.bgColor()}
                borderRadius="lg"
                overflowY={"auto"}
            >
                {renderActiveView()}
            </GridItem>
        </Grid>
    );
};

export default AdminPanel;
