export interface MenuItemBase {
    label: string;
    onClick?: () => void;
    href?: string | "";
    children?: MenuItemBase[] | [];
    value: string;
}