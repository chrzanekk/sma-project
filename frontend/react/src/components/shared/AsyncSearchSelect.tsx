// src/components/shared/AsyncSearchSelect.tsx
import {useMemo, useRef, useState} from "react";
import Select, {StylesConfig} from "react-select";
import {getSelectStyles} from "@/components/shared/formOptions"; // Twoje style bazowe
import {themeVars} from "@/theme/theme-colors";

export interface AsyncSearchSelectOption<T = any> {
    value: string | number;
    label: string;
    raw?: T; // pełny obiekt opcjonalnie
}

export interface AsyncSearchSelectProps<T = any> {
    loadOptions: (term: string) => Promise<AsyncSearchSelectOption<T>[]>;
    value?: AsyncSearchSelectOption<T> | null;
    onChange?: (option: AsyncSearchSelectOption<T> | null) => void;
    placeholder?: string;
    width?: string;
    bgColor?: string;
    disabled?: boolean;
    debounceMs?: number;
    minChars?: number;
    clearable?: boolean;
    noOptionsMessage?: string;
    size?: "xs" | "sm" | "md" | "lg";
}

const sizeToHeights = (size: "xs" | "sm" | "md" | "lg") => {
    switch (size) {
        case "xs":
            return {controlHeight: "24px", fontSize: "12px", padding: "0 6px"};
        case "sm":
            return {controlHeight: "30px", fontSize: "14px", padding: "0 8px"};
        case "md":
            return {controlHeight: "36px", fontSize: "16px", padding: "0 10px"};
        case "lg":
            return {controlHeight: "42px", fontSize: "18px", padding: "0 12px"};
        default:
            return {controlHeight: "30px", fontSize: "14px", padding: "0 8px"};
    }
};

const AsyncSearchSelect = <T, >({
                                    loadOptions,
                                    value = null,
                                    onChange,
                                    placeholder = "Search...",
                                    width,
                                    bgColor,
                                    disabled = false,
                                    debounceMs = 300,
                                    minChars = 2,
                                    clearable = true,
                                    noOptionsMessage = "No results",
                                    size = "sm",
                                }: AsyncSearchSelectProps<T>) => {
    const selectStyles = getSelectStyles();
    const [options, setOptions] = useState<AsyncSearchSelectOption<T>[]>([]);
    const [isLoading, setIsLoading] = useState(false);
    const debounceTimer = useRef<number | null>(null);
    const dims = sizeToHeights(size);

    const customSelectStyles: StylesConfig<any, false> = useMemo(() => ({
        ...selectStyles,
        control: (provided, state) => {
            const baseControl = selectStyles.control
                ? selectStyles.control(provided, state)
                : provided;
            return {
                ...baseControl,
                backgroundColor: bgColor ?? baseControl.backgroundColor,
                width: width || "auto",
                maxWidth: width,
                minHeight: dims.controlHeight,
                height: dims.controlHeight,
                fontSize: dims.fontSize,
            };
        },
        valueContainer: (provided) => ({
            ...provided,
            height: dims.controlHeight,
            padding: dims.padding,
        }),
        input: (provided) => ({
            ...provided,
            color: themeVars.fontColor,
        }),
        menu: (provided) => ({
            ...provided,
            fontSize: dims.fontSize,
            width: "100%",
            backgroundColor: themeVars.bgColorSecondary,
        }),
        singleValue: (provided) => ({
            ...provided,
            color: themeVars.fontColor,
        }),
        placeholder: (provided) => ({
            ...provided,
            color: themeVars.fontColor,
        }),
        menuList: (provided) => ({
            ...provided,
            backgroundColor: themeVars.bgColorSecondary, // tło listy
            paddingTop: 0,
            paddingBottom: 0,
        }),
        option: (provided, state) => ({
            ...provided,
            backgroundColor: state.isSelected
                ? themeVars.bgColorPrimary
                : themeVars.bgColorSecondary,
            color: state.isSelected
                ? themeVars.fontColor
                : themeVars.fontColor,
            cursor: "pointer",
            "&:hover": {
                backgroundColor: themeVars.highlightBgColor,
                color: themeVars.fontColorHover
            }
        }),
        noOptionsMessage: (base) => ({
            ...base,
            backgroundColor: themeVars.bgColorSecondary,
            color: themeVars.fontColor,
            paddingTop: 8,
            paddingBottom: 8,
        }),
    }), [selectStyles, bgColor, width, dims, themeVars.fontColor, themeVars.bgColorSecondary, themeVars.bgColorPrimary, themeVars.highlightBgColor]);

    const handleInputChange = (inputValue: string) => {
        // react-select wywołuje to przy każdej zmianie
        if (debounceTimer.current) {
            window.clearTimeout(debounceTimer.current);
        }
        if (inputValue.trim().length < minChars) {
            setOptions([]);
            setIsLoading(false);
            return inputValue;
        }
        setIsLoading(true);
        debounceTimer.current = window.setTimeout(async () => {
            try {
                const next = await loadOptions(inputValue.trim());
                setOptions(next ?? []);
            } finally {
                setIsLoading(false);
            }
        }, debounceMs);
        return inputValue;
    };

    const handleChange = (opt: any) => {
        onChange?.(opt || null);
    };

    return (
        <Select
            isDisabled={disabled}
            isClearable={clearable}
            placeholder={placeholder}
            value={value}
            onChange={handleChange}
            onInputChange={handleInputChange}
            options={options}
            styles={customSelectStyles}
            isLoading={isLoading}
            noOptionsMessage={() => noOptionsMessage}
            backspaceRemovesValue
            escapeClearsValue
            filterOption={() => true} // żeby nie dublować lokalnego filtrowania; bazujemy na wynikach loadOptions
        />
    );
};

export default AsyncSearchSelect;
