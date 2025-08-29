import { Global } from "@emotion/react";

export const GlobalStyles = () => (
    <Global
        styles={`
      :root {
        --bgColorPrimary: #e2e8f0;
        --bgColorSecondary: #f7fafc;
        --buttonBgColor: #e2e8f0;
        --popoverBgColor: #ffffff;
        --highlightBgColor: #86efac;
        --borderColor: #e2e8f0;
        --fontColor: #4a5568;
        --fontColorHover: #6b7280;
        --fontColorChildMenu: #6b7280;
      }

      .dark {
        --bgColorPrimary: #374151;
        --bgColorSecondary: #4b5563;
        --buttonBgColor: #374151;
        --popoverBgColor: #e5e7eb;
        --highlightBgColor: #116932;
        --borderColor: #4b5563;
        --fontColor: #d1d5db;
        --fontColorHover: #9ca3af;
        --fontColorChildMenu: #e5e7eb;
      }
    `}
    />
);
