// 'use client'
//
// import React from 'react'
// import {
//     Box,
//     Collapsible,
//     Flex,
//     HStack,
//     Icon,
//     Image,
//     Link,
//     LinkBox,
//     LinkOverlay,
//     MenuItem,
//     Stack,
//     Text,
//     useDisclosure,
//     VStack
// } from '@chakra-ui/react'
// import {FiChevronDown} from "react-icons/fi";
// import {getNavItems} from './nav-items.ts';
// import {getUserMenuItems} from './user-menu-items.ts';
// import LanguageSwitcher from "@/layout/LanguageSwitcher.tsx";
// import {themeColors} from '@/theme/theme-colors.ts';
// import {useTranslation} from "react-i18next";
// import useUser from "@/hooks/UseUser.tsx";
// import {MenuContent, MenuRoot, MenuTrigger} from "@/components/ui/menu.tsx";
// import {Button} from "@/components/ui/button.tsx";
// import {PopoverContent, PopoverRoot, PopoverTrigger,} from "@/components/ui/popover.tsx";
// import {FaBars, FaChevronRight} from "react-icons/fa6";
//
// interface NavItem {
//     label: string
//     subLabel?: string
//     children?: Array<NavItem>
//     href?: string
// }
//
// const Navbar: React.FC = () => {
//     const {onToggle} = useDisclosure();
//     const userMenuItems = getUserMenuItems();
//     const {user} = useUser();
//     const {t} = useTranslation('navbar');
//
//     return (
//         <Box bg={themeColors.bgColor()} px={4}>
//             <Flex h={16} alignItems={'center'} justifyContent={'space-between'}>
//                 {/* Hamburger Icon */}
//                 <Button
//                     size={'md'}
//                     aria-label={'Toggle Navigation'}
//                     display={{md: 'none'}}
//                     onClick={onToggle}
//                 >
//                     <Box as="span">{<FaBars/>}</Box>
//                 </Button>
//
//                 {/* Logo and Desktop Navigation */}
//                 <HStack gap={8} alignItems={'center'}>
//                     <Box>
//                         <Link href="/dashboard">
//                             <Image
//                                 borderRadius="full"
//                                 boxSize="75px"
//                                 src="/img/sma-logo.png"
//                                 alt="S.M.A."
//                                 cursor="pointer"
//                             />
//                         </Link>
//                     </Box>
//                     <HStack as={'nav'} gap={1} display={{base: 'none', md: 'flex'}}>
//                         <DesktopNav/>
//                     </HStack>
//                 </HStack>
//
//                 {/* User Menu */}
//                 <Flex alignItems={'center'}>
//                     <MenuRoot>
//                         <MenuTrigger asChild>
//                             <Button
//                                 py={2}
//                                 transition="all 0.3s"
//                                 _focus={{boxShadow: 'none'}}
//                                 rounded={'md'}
//                                 p={2}
//                                 _hover={{
//                                     border: '1px solid white',
//                                     textDecoration: 'none',
//                                     bg: themeColors.highlightBgColor(),
//                                     color: themeColors.popoverBgColor()
//                                 }}
//                             >
//                                 <HStack>
//                                     <VStack
//                                         display={{base: 'none', md: 'flex'}}
//                                         alignItems="flex-start"
//                                         gap="1px"
//                                         ml="2">
//                                         <Text fontSize="small" color={themeColors.fontColor()}
//                                         >{t('loggedAs')}</Text>
//                                         <Text fontSize="small" color={themeColors.fontColor()}
//                                         >{user ? `${user.firstName} ${user.lastName}` : 'Guest'}</Text>
//                                     </VStack>
//                                     <Box display={{base: 'none', md: 'flex'}}>
//                                         <FiChevronDown/>
//                                     </Box>
//                                 </HStack>
//                             </Button>
//                         </MenuTrigger>
//                         <MenuContent bg={themeColors.bgColor()} p={1}>
//                             {userMenuItems.map((item) => (
//                                 <MenuItem
//                                     key={item.label}
//                                     bg={themeColors.bgColor()}
//                                     rounded={'md'}
//                                     color={themeColors.fontColor()}
//                                     p={2}
//                                     _hover={{
//                                         textDecoration: 'none',
//                                         bg: themeColors.highlightBgColor(),
//                                         color: themeColors.popoverBgColor(),
//                                         border: '1px solid white'
//                                     }}
//                                     value={item.label}
//                                     valueText={item.label}
//                                     closeOnSelect={true}
//                                     onClick={item.onClick ? item.onClick : undefined}
//                                 >
//                                     {item.label}
//                                 </MenuItem>
//                             ))}
//                         </MenuContent>
//                     </MenuRoot>
//                     <LanguageSwitcher/>
//                 </Flex>
//             </Flex>
//
//             {/* Mobile Navigation */}
//             <Collapsible.Root>
//                 <Collapsible.Trigger/>
//                 <Collapsible.Content>
//                     <MobileNav/>
//                 </Collapsible.Content>
//             </Collapsible.Root>
//         </Box>
//     )
// }
//
// const DesktopNav: React.FC = () => {
//     const navItems = getNavItems();
//     return (
//         <Stack direction={'row'} gap={4}>
//             {navItems.map((navItem) => (
//                 <Box key={navItem.label}>
//                     <PopoverRoot>
//                         <PopoverTrigger asChild>
//                             <Box>
//                                 <LinkBox
//                                     p={2}
//                                     fontSize={'sm'}
//                                     fontWeight={500}
//                                     color={themeColors.fontColor()}
//                                     rounded={'lg'}
//                                     _hover={{
//                                         border: '1px solid white',
//                                         textDecoration: 'none',
//                                         bg: themeColors.highlightBgColor(),
//                                         color: themeColors.popoverBgColor()
//                                     }}>
//                                     <LinkOverlay href={navItem.href ?? '#'}>
//                                         <Text>{navItem.label}</Text>
//                                     </LinkOverlay>
//                                 </LinkBox>
//                             </Box>
//                         </PopoverTrigger>
//
//                         {navItem.children && (
//                             <PopoverContent
//                                 border="1px solid white"
//                                 boxShadow={'xl'}
//                                 bg={themeColors.bgColor()}
//                                 color={themeColors.fontColor()}
//                                 p={2}
//                                 rounded={'xl'}
//                                 minW={'sm'}
//                                 mt={3}>
//                                 <Stack>
//                                     {navItem.children.map((child) => (
//                                         <DesktopSubNav key={child.label} {...child} />
//                                     ))}
//                                 </Stack>
//                             </PopoverContent>
//                         )}
//                     </PopoverRoot>
//                 </Box>
//             ))}
//         </Stack>
//     )
// }
//
// const DesktopSubNav: React.FC<NavItem> = ({label, href, subLabel}) => {
//     return (
//         <LinkBox
//             role={'group'}
//             display={'block'}
//             p={1}
//             rounded={'md'}
//             bg={themeColors.bgColor()}
//             _hover={{
//                 border: '1px solid white',
//                 textDecoration: 'none',
//                 bg: themeColors.highlightBgColor(),
//                 color: themeColors.popoverBgColor()
//             }}
//         >
//             <LinkOverlay href={href}>
//                 <Stack direction={'row'} align={'center'}>
//                     <Box>
//                         <Text fontWeight={500}>
//                             {label}
//                         </Text>
//                         <Text fontSize={'xs'}>{subLabel}</Text>
//                     </Box>
//                 </Stack>
//             </LinkOverlay>
//             <Icon w={5} h={5} as={FaChevronRight}/>
//         </LinkBox>
//     )
// }
//
// const MobileNav: React.FC = () => {
//     const navItems = getNavItems();
//     return (
//         <Stack bg={themeColors.bgColor()} p={4} display={{md: 'none'}}>
//             {navItems.map((navItem) => (
//                 <MobileNavItem key={navItem.label} {...navItem} />
//             ))}
//         </Stack>
//     )
// }
//
// const MobileNavItem: React.FC<NavItem> = ({label, children, href}) => {
//     const {onToggle} = useDisclosure()
//
//     return (
//         <Stack gap={4} onClick={children && onToggle}>
//             <LinkBox py={2} _hover={{textDecoration: 'none'}}>
//                 <LinkOverlay href={href ?? '#'}>
//                     <Text fontWeight={600} color={themeColors.fontColor()}>
//                         {label}
//                     </Text>
//                 </LinkOverlay>
//             </LinkBox>
//             <Collapsible.Root>
//                 <Collapsible.Trigger asChild>
//                     <Button>MENU</Button>
//                 </Collapsible.Trigger>
//                 <Collapsible.Content>
//                     <Stack pl={4} borderLeft={1} borderStyle={'solid'}
//                            borderColor={themeColors.borderColor()}>
//                         {children &&
//                             children.map((child) => (
//                                 <Link as="a"
//                                       key={child.label}
//                                       py={2}
//                                       borderBottom={1}
//                                       borderStyle={'solid'}
//                                       borderColor={themeColors.borderColor()}
//                                       href={child.href}>
//                                     <Text fontWeight={700} color={themeColors.fontColorChildMenu()}>
//                                         {child.label}
//                                     </Text>
//                                 </Link>
//                             ))}
//                     </Stack>
//                 </Collapsible.Content>
//             </Collapsible.Root>
//         </Stack>
//     )
// }
//
// export default Navbar
'use client'

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

interface NavItem {
    label: string
    subLabel?: string
    children?: Array<NavItem>
    href?: string
}

const Navbar: React.FC = () => {
    const {onToggle} = useDisclosure();
    const userMenuItems = getUserMenuItems();
    const {user} = useUser();
    const {t} = useTranslation('navbar');

    return (
        <Box bg={themeColors.bgColor()} px={4}>
            <Flex h={16} alignItems={'center'} justifyContent={'space-between'}>
                {/* Hamburger Icon */}
                <Button
                    size={'md'}
                    aria-label={'Toggle Navigation'}
                    display={{md: 'none'}}
                    onClick={onToggle}
                >
                    <FaBars/>
                </Button>

                {/* Logo and Desktop Navigation */}
                <HStack gap={8} alignItems={'center'}>
                    <Box>
                        <Link href="/dashboard">
                            <Image
                                borderRadius="full"
                                boxSize="75px"
                                src="/img/sma-logo.png"
                                alt="S.M.A."
                                cursor="pointer"
                            />
                        </Link>
                    </Box>
                    <HStack as={'nav'} gap={1} display={{base: 'none', md: 'flex'}}>
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
                                p={2}
                            >
                                <HStack>
                                    <VStack
                                        display={{base: 'none', md: 'flex'}}
                                        alignItems="flex-start"
                                        gap="1px"
                                        ml="2">
                                        <Text fontSize="small" color={themeColors.fontColor()}>
                                            {t('loggedAs')}
                                        </Text>
                                        <Text fontSize="small" color={themeColors.fontColor()}>
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
                                    value={item.label} // Dodanie wymaganego 'value'
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
                                >
                                    <Link href={item.href} style={{ textDecoration: 'none', display: 'block', width: '100%' }}>
                                        {item.label}
                                    </Link>
                                </MenuItem>

                            ))}
                        </MenuContent>
                    </MenuRoot>
                    <LanguageSwitcher/>
                </Flex>
            </Flex>

            {/* Mobile Navigation */}
            <Collapsible.Root>
                <Collapsible.Trigger asChild>
                    <Button>MENU</Button>
                </Collapsible.Trigger>
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
        <Stack direction={'row'} gap={4}>
            {navItems.map((navItem) => (
                <PopoverRoot key={navItem.label}>
                    <PopoverTrigger asChild>
                        <Box
                            p={2}
                            fontSize={'sm'}
                            fontWeight={500}
                            color={themeColors.fontColor()}
                            rounded={'lg'}
                            _hover={{
                                border: '1px solid white',
                                textDecoration: 'none',
                                bg: themeColors.highlightBgColor(),
                                color: themeColors.popoverBgColor()
                            }}
                        >
                            <Link href={navItem.href ?? '#'}>{navItem.label}</Link>
                        </Box>
                    </PopoverTrigger>

                    {navItem.children && (
                        <PopoverContent
                            border="1px solid white"
                            boxShadow={'xl'}
                            bg={themeColors.bgColor()}
                            color={themeColors.fontColor()}
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
                        <Text fontWeight={500}>{label}</Text>
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
    const {onToggle} = useDisclosure()

    return (
        <Stack gap={4} onClick={children && onToggle}>
            <Box py={2} _hover={{textDecoration: 'none'}}>
                <Link href={href ?? '#'}>
                    <Text fontWeight={600} color={themeColors.fontColor()}>
                        {label}
                    </Text>
                </Link>
            </Box>
            {children && (
                <Collapsible.Root>
                    <Collapsible.Trigger asChild>
                        <Button size="sm">Toggle</Button>
                    </Collapsible.Trigger>
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
