import React from 'react'
import {Box, Collapsible, Flex, HStack, Image, Link, Stack, Text, useDisclosure, VStack} from '@chakra-ui/react'
import {FiChevronDown} from "react-icons/fi";
import {getNavItems} from './nav-items.ts';
import {useUserMenuItems} from './user-menu-items.ts';
import LanguageSwitcher from "@/layout/LanguageSwitcher.tsx";
import {useThemeColors} from '@/theme/theme-colors.ts';
import {useTranslation} from "react-i18next";
import useUser from "@/hooks/UseUser.tsx";
import {MenuContent, MenuItem, MenuRoot, MenuTrigger} from "@/components/ui/menu.tsx";
import {Button} from "@/components/ui/button.tsx";
import {FaBars} from "react-icons/fa6";
import {ThemeToggle} from "@/layout/ThemeToggle.tsx";
import {NavItem} from "@/types/nav-item-types.ts";
import CompanySelector from "@/layout/CompanySelector.tsx";
import {useCompany} from "@/hooks/useCompany";


const Navbar: React.FC = () => {
    const {open, onToggle} = useDisclosure();
    const themeColors = useThemeColors();
    const { user } = useUser();
    const {selectedCompany} = useCompany();
    const userHasCompanies = user?.companies && user.companies.length > 0;
    const showNav = userHasCompanies && selectedCompany;

    return (
        <Box bg={themeColors.bgColorPrimary} h="50px">
            <Flex h="50px" alignItems="center" justifyContent="space-between" p={2}>
                {/* Hamburger Icon */}
                <Button
                    size="sm"
                    aria-label="Toggle Navigation"
                    display={{md: 'none'}}
                    onClick={onToggle}
                >
                    <FaBars/>
                </Button>

                {/* Logo and Desktop Navigation */}
                <HStack gap={1}>
                    <HStack>
                        <Link href="/dashboard">
                            <Image
                                borderRadius="full"
                                boxSize="50px"
                                src="/img/sma-logo.png"
                                alt="S.M.A."
                                cursor="pointer"
                            />
                        </Link>
                    </HStack>
                    <HStack as="nav" display={{base: 'none', md: 'flex'}}>
                        {showNav && <DesktopNav />}
                    </HStack>
                </HStack>

                {/* User Menu */}
                <HStack display={{base: 'none', md: 'flex'}}>
                    <CompanySelector/>
                    <UserMenu/>
                    <LanguageSwitcher/>
                    <ThemeToggle/>
                </HStack>
            </Flex>

            {/* Mobile Navigation */}
            <Collapsible.Root open={open} onOpenChange={onToggle}>
                <Collapsible.Trigger/>
                <Collapsible.Content>
                    {showNav && <MobileNav />}
                    <MobileNav/>
                    <UserMenuMobile/>
                </Collapsible.Content>
            </Collapsible.Root>
        </Box>
    );
};

const DesktopNav: React.FC = () => {
    const navItems = getNavItems();
    const themeColors = useThemeColors();

    return (
        <Stack direction="row" gap={2}>
            {navItems.map((navItem) => {

                if (navItem.children && navItem.children.length > 0) {
                    return (
                        <MenuRoot key={navItem.label}>
                            <MenuTrigger asChild>
                                <Button
                                    p={2}
                                    fontSize="sm"
                                    color={themeColors.fontColor}
                                    bg={themeColors.buttonBgColor}
                                    fontWeight={500}
                                    rounded="lg"
                                    lineHeight="1.5"
                                    _hover={{
                                        textDecoration: 'none',
                                        bg: themeColors.highlightBgColor,
                                        color: themeColors.fontColorHover
                                    }}
                                    onClick={navItem.onClick}
                                >
                                    {navItem.label}
                                    <FiChevronDown style={{marginLeft: 4}}/>
                                </Button>
                            </MenuTrigger>
                            <MenuContent bg={themeColors.bgColorSecondary} mt={2} p={1}>
                                {navItem.children.map((child) => (
                                    <MenuItem
                                        key={child.label}
                                        value={child.value}
                                        bg={themeColors.bgColorSecondary}
                                        color={themeColors.fontColor}
                                        rounded="md"
                                        p={2}
                                        _hover={{
                                            textDecoration: 'none',
                                            bg: themeColors.highlightBgColor,
                                            color: themeColors.fontColorHover
                                        }}
                                        onClick={child.onClick}
                                    >
                                        {child.label}
                                    </MenuItem>
                                ))}
                            </MenuContent>
                        </MenuRoot>
                    );
                }

                return (
                    <Button
                        key={navItem.label}
                        p={2}
                        fontSize="sm"
                        color={themeColors.fontColor}
                        bg={themeColors.buttonBgColor}
                        fontWeight={500}
                        rounded="lg"
                        lineHeight="1.5"
                        _hover={{
                            textDecoration: 'none',
                            bg: themeColors.highlightBgColor,
                            color: themeColors.fontColorHover
                        }}
                        onClick={navItem.onClick}
                    >
                        {navItem.label}
                    </Button>
                );
            })}
        </Stack>
    );
};

const UserMenu: React.FC = () => {
    const userMenuItems = useUserMenuItems();
    const {user} = useUser();
    const {t} = useTranslation('navbar');
    const themeColors = useThemeColors();

    return (
        <Flex alignItems="center">
            <MenuRoot>
                <MenuTrigger asChild>
                    <Button
                        bg={themeColors.buttonBgColor}
                        ml={4}
                        color={themeColors.fontColor}
                        _hover={{
                            textDecoration: 'none',
                            bg: themeColors.highlightBgColor,
                            color: themeColors.fontColorHover
                        }}
                        transition="all 0.3s"
                        variant="solid"
                        size="sm"
                        p={2}
                    >
                        <HStack>
                            <VStack
                                display={{base: 'none', md: 'flex'}}
                                alignItems="flex-start"
                                gap="0px"
                                ml="2"
                            >
                                <Text fontSize="xs" fontWeight="bold" lineHeight="1.2">
                                    {t('loggedAs')}
                                </Text>
                                <Text fontSize="xs" lineHeight="1.2">
                                    {user ? `${user.firstName} ${user.lastName}` : 'Guest'}
                                </Text>
                            </VStack>
                            <Box display={{base: 'none', md: 'flex'}}>
                                <FiChevronDown/>
                            </Box>
                        </HStack>
                    </Button>
                </MenuTrigger>
                <MenuContent bg={themeColors.bgColorSecondary} mt={2} p={1}>
                    {userMenuItems.map((item) => (
                        <MenuItem
                            key={item.label}
                            value={item.value}
                            bg={themeColors.bgColorSecondary}
                            color={themeColors.fontColor}
                            rounded="md"
                            p={2}
                            _hover={{
                                textDecoration: 'none',
                                bg: themeColors.highlightBgColor,
                                color: themeColors.fontColorHover
                            }}
                            onClick={item.onClick}
                        >
                            {item.label}
                        </MenuItem>
                    ))}
                </MenuContent>
            </MenuRoot>
        </Flex>
    );
};

const UserMenuMobile: React.FC = () => {
    const userMenuItems = useUserMenuItems();
    const {t} = useTranslation('navbar');
    const themeColors = useThemeColors();

    return (
        <MenuRoot>
            <MenuTrigger asChild>
                <Button
                    size="sm"
                    variant="outline"
                    display={{md: 'none'}}
                    aria-label={t('userMenu')}
                >
                    <FiChevronDown/>
                </Button>
            </MenuTrigger>
            <MenuContent
                bg={themeColors.bgColorSecondary}
                mt={2}
                p={1}
                rounded="md"
                shadow="md"
            >
                {userMenuItems.map((item) => (
                    <MenuItem
                        key={item.label}
                        value={item.value}
                        bg={themeColors.bgColorSecondary}
                        color={themeColors.fontColor}
                        rounded="md"
                        p={2}
                        _hover={{
                            textDecoration: 'none',
                            bg: themeColors.highlightBgColor,
                            color: themeColors.fontColorHover
                        }}
                        onClick={item.onClick}
                    >
                        {item.label}
                    </MenuItem>
                ))}
            </MenuContent>
        </MenuRoot>
    );
};

const MobileNav: React.FC = () => {
    const navItems = getNavItems();
    const themeColors = useThemeColors();

    return (
        <Stack
            bg={themeColors.bgColorPrimary}
            p={4}
            display={{md: 'none'}}
            gap={4}
        >
            {/* Navigation Menu */}
            <Box>
                {navItems.map((navItem, index) => (
                    <React.Fragment key={navItem.label}>
                        <MobileNavItem {...navItem} />
                        {index < navItems.length - 1 && (
                            <Box
                                borderBottom="1px solid"
                                borderColor={themeColors.borderColor}
                                my={2}
                            />
                        )}
                    </React.Fragment>
                ))}
            </Box>
        </Stack>
    );
};

const MobileNavItem: React.FC<NavItem> = ({label, children, href, onClick}) => {
    const {open, onToggle} = useDisclosure();
    const themeColors = useThemeColors();

    return (
        <Stack gap={4}>
            <Box
                py={2}
                textAlign="center"
                cursor={onClick ? "pointer" : "default"}
                _hover={{
                    textDecoration: "none",
                    bg: themeColors.highlightBgColor,
                    color: themeColors.fontColorHover,
                }}
                onClick={onClick ? onClick : children ? onToggle : undefined}
            >
                {href && !onClick ? (
                    <Link href={href}>
                        <Text fontWeight={600} color={themeColors.fontColor}>
                            {label}
                        </Text>
                    </Link>
                ) : (
                    <Text fontWeight={600} color={themeColors.fontColor}>
                        {label}
                    </Text>
                )}
            </Box>

            {children && open && (
                <Stack
                    pl={4}
                    borderLeft={1}
                    borderStyle="solid"
                    borderColor={themeColors.borderColor}
                >
                    {children.map((child) => (
                        <MobileNavItem key={child.label} {...child} />
                    ))}
                </Stack>
            )}
        </Stack>
    );
};

export default Navbar;
