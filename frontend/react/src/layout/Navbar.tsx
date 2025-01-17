import React from 'react'
import {
    Box,
    Collapsible,
    Flex,
    HStack,
    Image,
    Link,
    MenuItem,
    Stack,
    Text,
    useDisclosure,
    VStack
} from '@chakra-ui/react'
import {FiChevronDown} from "react-icons/fi";
import {getNavItems} from './nav-items.ts';
import {getUserMenuItems} from './user-menu-items.ts';
import LanguageSwitcher from "@/layout/LanguageSwitcher.tsx";
import {themeColors} from '@/theme/theme-colors.ts';
import {useTranslation} from "react-i18next";
import useUser from "@/hooks/UseUser.tsx";
import {MenuContent, MenuRoot, MenuTrigger} from "@/components/ui/menu.tsx";
import {Button} from "@/components/ui/button.tsx";
import {PopoverContent, PopoverRoot, PopoverTrigger,} from "@/components/ui/popover.tsx";
import {FaBars} from "react-icons/fa6";
import {ThemeToggle} from "@/layout/ThemeToggle.tsx";

interface NavItem {
    label: string
    subLabel?: string
    children?: Array<NavItem>
    href?: string
}

const Navbar: React.FC = () => {
    const {open, onToggle} = useDisclosure();
    const userMenuItems = getUserMenuItems();
    const {user} = useUser();
    const {t} = useTranslation('navbar');

    return (
        <Box bg={themeColors.bgColor()} h={"50px"}>
            <Flex h={"50px"}
                  alignItems={'center'}
                  justifyContent={'space-between'}
                  p={2}>
                {/* Hamburger Icon */}
                <Button
                    size={'sm'}
                    aria-label={'Toggle Navigation'}
                    display={{md: 'none'}}
                    onClick={onToggle}
                >
                    <FaBars/>
                </Button>

                {/* Logo and Desktop Navigation */}
                <HStack gap={1}>
                    <Box>
                        <Link href="/dashboard">
                            <Image
                                borderRadius="full"
                                boxSize="50px"
                                src="/img/sma-logo.png"
                                alt="S.M.A."
                                cursor="pointer"
                            />
                        </Link>
                    </Box>
                    <HStack as={'nav'} display={{base: 'none', md: 'flex'}}>
                        <DesktopNav/>
                    </HStack>
                </HStack>

                {/* User Menu */}
                <Flex alignItems={'center'}>
                    <MenuRoot>
                        <MenuTrigger asChild>
                            <Button
                                bg={themeColors.bgColor()}
                                ml={4}
                                color={themeColors.fontColor()}
                                _hover={{
                                    textDecoration: 'none',
                                    bg: themeColors.highlightBgColor(),
                                    color: themeColors.popoverBgColor()
                                }}
                                transition="all 0.3s"
                                as={Button}
                                variant="outline"
                                size="sm"
                                p={1}
                            >
                                <HStack>
                                    <VStack
                                        display={{base: 'none', md: 'flex'}}
                                        alignItems="flex-start"
                                        gap="0px"
                                        ml="2">
                                        <Text fontSize="xs"
                                              fontWeight="bold"
                                              lineHeight="1.2">
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
                        <MenuContent bg={themeColors.bgColor()} p={1}>
                            {userMenuItems.map((item) => (
                                <MenuItem
                                    key={item.label}
                                    value={item.label}
                                    bg={themeColors.bgColor()}
                                    rounded="md"
                                    color={themeColors.fontColor()}
                                    p={2}
                                    _hover={{
                                        textDecoration: 'none',
                                        bg: themeColors.bgColorLight(),
                                        color: themeColors.popoverBgColor(),
                                        border: '1px solid white'
                                    }}
                                    as="div"
                                    onClick={item.onClick ? item.onClick : undefined}
                                >
                                    {item.href ? (
                                        <Link href={item.href}
                                              onClick={item.onClick ? (e) => e.preventDefault() : undefined}>
                                            {item.label}
                                        </Link>
                                    ) : (
                                        <Text>{item.label}</Text>
                                    )}
                                </MenuItem>

                            ))}
                        </MenuContent>
                    </MenuRoot>
                    <HStack>
                        <LanguageSwitcher/>
                        <ThemeToggle/>
                    </HStack>
                </Flex>
            </Flex>

            {/* Mobile Navigation */}
            <Collapsible.Root open={open} onOpenChange={onToggle}>
                <Collapsible.Trigger/>
                <Collapsible.Content>
                    <MobileNav/>
                </Collapsible.Content>
            </Collapsible.Root>
        </Box>
    )
}

const DesktopNav: React.FC = () => {
    const navItems = getNavItems();
    return (
        <Stack
            direction={'row'}
            gap={2}
        >
            {navItems.map((navItem) => (
                <PopoverRoot key={navItem.label}>
                    <PopoverTrigger asChild>
                        <Box
                            p={2}
                            fontSize={'sm'}
                            fontWeight={500}
                            rounded={'lg'}
                            lineHeight="1.5"
                            _hover={{
                                border: '1px solid white',
                                textDecoration: 'none',
                                bg: themeColors.highlightBgColor(),
                                color: themeColors.popoverBgColor()
                            }}
                        >
                            <Link
                                color={themeColors.fontColor()}
                                href={navItem.href ?? '#'}>{navItem.label}
                            </Link>
                        </Box>
                    </PopoverTrigger>

                    {navItem.children && (
                        <PopoverContent
                            border="1px solid white"
                            boxShadow={'xl'}
                            bg={themeColors.bgColor()}
                            color={themeColors.popoverBgColor()}
                            p={2}
                            rounded={'xl'}
                            minW={'sm'}
                            mt={3}
                        >
                            <Stack>
                                {navItem.children.map((child) => (
                                    <DesktopSubNav key={child.label} {...child} />
                                ))}
                            </Stack>
                        </PopoverContent>
                    )}
                </PopoverRoot>
            ))}
        </Stack>
    )
}

const DesktopSubNav: React.FC<NavItem> = ({label, href, subLabel}) => {
    return (
        <Box
            role={'group'}
            display={'block'}
            p={1}
            rounded={'md'}
            bg={themeColors.bgColor()}
            _hover={{
                border: '1px solid white',
                textDecoration: 'none',
                bg: themeColors.highlightBgColor(),
                color: themeColors.popoverBgColor()
            }}
        >
            <Link href={href}>
                <Stack direction={'row'} align={'center'}>
                    <Box>
                        <Text fontWeight={500} lineHeight="1.5">{label}</Text>
                        {subLabel && <Text fontSize={'xs'}>{subLabel}</Text>}
                    </Box>
                </Stack>
            </Link>
        </Box>
    )
}

const MobileNav: React.FC = () => {
    const navItems = getNavItems();
    return (
        <Stack bg={themeColors.bgColor()} p={4} display={{md: 'none'}}>
            {navItems.map((navItem) => (
                <MobileNavItem key={navItem.label} {...navItem} />
            ))}
        </Stack>
    )
}

const MobileNavItem: React.FC<NavItem> = ({label, children, href}) => {
    const {open, onToggle} = useDisclosure()

    return (
        <Stack gap={4} onClick={children && onToggle}>
            <Box py={2} textAlign="center" _hover={{textDecoration: 'none'}}>
                <Link href={href ?? '#'}>
                    <Text fontWeight={600} color={themeColors.fontColor()}>
                        {label}
                    </Text>
                </Link>
            </Box>
            {children && (
                <Collapsible.Root open={open} onOpenChange={onToggle}>
                    <Collapsible.Trigger/>
                    <Collapsible.Content>
                        <Stack pl={4} borderLeft={1} borderStyle={'solid'} borderColor={themeColors.borderColor()}>
                            {children.map((child) => (
                                <Link key={child.label} py={2} href={child.href} borderBottom={1}>
                                    <Text fontWeight={700} color={themeColors.fontColorChildMenu()}>
                                        {child.label}
                                    </Text>
                                </Link>
                            ))}
                        </Stack>
                    </Collapsible.Content>
                </Collapsible.Root>
            )}
        </Stack>
    )
}

export default Navbar;
