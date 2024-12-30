import React, {useState} from 'react';
import {Button, Flex, Grid, GridItem, Heading, Menu, MenuButton, MenuItem, MenuList,} from '@chakra-ui/react';
import {ChevronDownIcon} from '@chakra-ui/icons';
import RoleManagement from '@/components/role/RoleManagement';
import UserManagement from '@/components/user/UserManagement';
import {themeColors} from "@/theme/theme-colors.ts";
import {getAdminPanelMenuItems} from "@/layout/admin-panel-menu-items.ts";
import {useTranslation} from "react-i18next";

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
            templateRows="auto 1fr" // Pierwszy rząd na tytuł i menu, drugi elastyczny na widok
            bgColor={themeColors.bgColorLight()}
            height="100vh"
            gap={2}
            px={2}
            py={2}
        >
            {/* Wiersz 1: Tytuł i menu wyboru */}
            <GridItem
                w="100%"
                p={1}
                bg={themeColors.bgColor()}
                borderRadius="lg"
                color="white"
            >
                <Flex justifyContent="space-between" alignItems="center">
                    {/* Tytuł */}
                    <Heading size="sm" fontSize={14} color={themeColors.fontColor()}>
                        {t('adminPanel')}
                    </Heading>

                    {/* Menu wyboru widoku */}
                    <Menu>
                        <MenuButton
                            bg={themeColors.bgColor()}
                            color={themeColors.fontColor()}
                            _hover={{
                                textDecoration: 'none',
                                bg: themeColors.highlightBgColor(),
                                color: themeColors.popoverBgColor()
                            }}
                            as={Button}
                            rightIcon={<ChevronDownIcon/>}
                            variant="outline"
                            size="sm"
                            p={2}
                        >
                            {t("selectView")}
                        </MenuButton>
                        <MenuList bg={themeColors.bgColor()} p={1}>
                            {adminMenuItems.map((item, index) => (
                                <MenuItem
                                    bg={themeColors.bgColor()}
                                    rounded={'md'}
                                    color={themeColors.fontColor()}
                                    p={2}
                                    _hover={{
                                        textDecoration: 'none',
                                        bg: themeColors.highlightBgColor(),
                                        color: themeColors.popoverBgColor(),
                                        border: '1px solid white'
                                    }}
                                    key={index}
                                    onClick={item.onClick}
                                >
                                    {item.label}
                                </MenuItem>
                            ))}
                        </MenuList>
                    </Menu>
                </Flex>
            </GridItem>

            {/* Wiersz 2: Renderowany widok */}
            <GridItem
                w="100%"
                bg="white"
                borderRadius="lg"
            >
                {renderActiveView()}
            </GridItem>
        </Grid>
    );
};

export default AdminPanel;