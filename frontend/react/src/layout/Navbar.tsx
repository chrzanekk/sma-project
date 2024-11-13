'use client'

import React from 'react'
import {
    Avatar,
    Box,
    Collapse,
    Flex,
    HStack,
    Icon,
    IconButton,
    Image,
    Link,
    Menu,
    MenuButton,
    MenuDivider,
    MenuItem,
    MenuList,
    Popover,
    PopoverContent,
    PopoverTrigger,
    Stack,
    Text,
    useColorModeValue,
    useDisclosure,
    VStack
} from '@chakra-ui/react'
import {ChevronRightIcon, CloseIcon, HamburgerIcon,} from '@chakra-ui/icons'
import {FiChevronDown} from "react-icons/fi";
import {getNavItems} from './navItems';
import {getUserMenuItems} from './userMenuItems';
import LanguageSwitcher from "@/layout/LanguageSwitcher.tsx";

// Typy dla elementów nawigacyjnych
interface NavItem {
    label: string
    subLabel?: string
    children?: Array<NavItem>
    href?: string
}

const Navbar: React.FC = () => {
    const {isOpen, onToggle} = useDisclosure();
    const userMenuItems = getUserMenuItems();

    return (
        <Box bg={useColorModeValue('gray.100', 'gray.900')} px={4}>
            <Flex h={16} alignItems={'center'} justifyContent={'space-between'}>
                {/* Hamburger Icon */}
                <IconButton
                    size={'md'}
                    icon={isOpen ? <CloseIcon/> : <HamburgerIcon/>}
                    aria-label={'Toggle Navigation'}
                    display={{md: 'none'}}
                    onClick={onToggle}
                />

                {/* Logo and Desktop Navigation */}
                <HStack spacing={8} alignItems={'center'}>
                    <Box>
                        <Image
                            borderRadius='full'
                            boxSize='75px'
                            src='/img/sma-logo.png'
                            alt='S.M.A.'
                        />
                    </Box>
                    <HStack as={'nav'} spacing={4} display={{base: 'none', md: 'flex'}}>
                        <DesktopNav/>
                    </HStack>
                </HStack>

                {/* User Menu */}
                <Flex alignItems={'center'}>
                    <Menu>
                        <MenuButton py={2} transition="all 0.3s" _focus={{boxShadow: 'none'}}>
                            <HStack>
                                <Avatar
                                    size={'md'}
                                    src={
                                        'https://images.unsplash.com/photo-1493666438817-866a91353ca9?ixlib=rb-0.3.5&q=80&fm=jpg&crop=faces&fit=crop&h=200&w=200&s=b616b2c5b373a80ffc9636ba24f7a4a9'
                                    }
                                />
                                <VStack
                                    display={{base: 'none', md: 'flex'}}
                                    alignItems="flex-start"
                                    spacing="1px"
                                    ml="2">
                                    <Text fontSize="md">ZALOGOWANY JAKO:</Text>
                                    <Text fontSize="md">TU BEDZIE USERNAME</Text>
                                </VStack>
                                <Box display={{base: 'none', md: 'flex'}}>
                                    <FiChevronDown/>
                                </Box>
                            </HStack>
                        </MenuButton>
                        <MenuList
                            bg={useColorModeValue('white', 'gray.900')}
                            borderColor={useColorModeValue('gray.200', 'gray.700')}>
                            {userMenuItems.map((item) => (
                                <MenuItem key={item.label}>
                                    <Link href={item.href} _hover={{textDecoration: 'none'}}>
                                        {item.label}
                                    </Link>
                                </MenuItem>
                            ))}
                            <MenuDivider/>
                        </MenuList>
                    </Menu>
                    <LanguageSwitcher/>
                </Flex>
            </Flex>

            {/* Mobile Navigation */}
            <Collapse in={isOpen} animateOpacity>
                <MobileNav/>
            </Collapse>
        </Box>
    )
}

const DesktopNav: React.FC = () => {
    const linkColor = useColorModeValue('gray.600', 'gray.200')
    const linkHoverColor = useColorModeValue('gray.800', 'white')
    const popoverContentBgColor = useColorModeValue('white', 'gray.800')
    const navItems = getNavItems();
    return (
        <Stack direction={'row'} spacing={4}>
            {navItems.map((navItem) => (
                <Box key={navItem.label}>
                    <Popover trigger={'hover'} placement={'bottom-start'}>
                        <PopoverTrigger>
                            <Box
                                as="a"
                                p={2}
                                href={navItem.href ?? '#'}
                                fontSize={'sm'}
                                fontWeight={500}
                                color={linkColor}
                                _hover={{
                                    textDecoration: 'none',
                                    color: linkHoverColor,
                                }}>
                                {navItem.label}
                            </Box>
                        </PopoverTrigger>

                        {navItem.children && (
                            <PopoverContent
                                border={0}
                                boxShadow={'xl'}
                                bg={popoverContentBgColor}
                                p={4}
                                rounded={'xl'}
                                minW={'sm'}>
                                <Stack>
                                    {navItem.children.map((child) => (
                                        <DesktopSubNav key={child.label} {...child} />
                                    ))}
                                </Stack>
                            </PopoverContent>
                        )}
                    </Popover>
                </Box>
            ))}
        </Stack>
    )
}

const DesktopSubNav: React.FC<NavItem> = ({label, href, subLabel}) => {
    return (
        <Box
            as="a"
            href={href}
            role={'group'}
            display={'block'}
            p={2}
            rounded={'md'}
            _hover={{bg: useColorModeValue('pink.50', 'gray.900')}}>
            <Stack direction={'row'} align={'center'}>
                <Box>
                    <Text transition={'all .3s ease'} _groupHover={{color: 'pink.400'}} fontWeight={500}>
                        {label}
                    </Text>
                    <Text fontSize={'sm'}>{subLabel}</Text>
                </Box>
                <Icon color={'pink.400'} w={5} h={5} as={ChevronRightIcon}/>
            </Stack>
        </Box>
    )
}

const MobileNav: React.FC = () => {
    const navItems = getNavItems();
    return (
        <Stack bg={useColorModeValue('white', 'gray.800')} p={4} display={{md: 'none'}}>
            {navItems.map((navItem) => (
                <MobileNavItem key={navItem.label} {...navItem} />
            ))}
        </Stack>
    )
}

const MobileNavItem: React.FC<NavItem> = ({label, children, href}) => {
    const {isOpen, onToggle} = useDisclosure()

    return (
        <Stack spacing={4} onClick={children && onToggle}>
            <Box py={2} as="a" href={href ?? '#'} _hover={{textDecoration: 'none'}}>
                <Text fontWeight={600} color={useColorModeValue('gray.600', 'gray.200')}>
                    {label}
                </Text>
            </Box>

            <Collapse in={isOpen} animateOpacity>
                <Stack pl={4} borderLeft={1} borderStyle={'solid'}
                       borderColor={useColorModeValue('gray.200', 'gray.700')}>
                    {children &&
                        children.map((child) => (
                            <Box as="a" key={child.label} py={2} href={child.href}>
                                {child.label}
                            </Box>
                        ))}
                </Stack>
            </Collapse>
        </Stack>
    )
}

export default Navbar
