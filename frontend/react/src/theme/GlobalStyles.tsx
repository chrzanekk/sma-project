import { Global } from "@emotion/react";

export const GlobalStyles = () => (
    <Global
        styles={`
      :root {
        --bgColorPrimary: #d4d4d8;
        --bgColorSecondary: #f4f4f5;
        --buttonBgColor: #d4d4d8;
        --popoverBgColor: #ffffff;
        --highlightBgColor: #86efac;
        --borderColor: #e4e4e7;
        --fontColor: #52525b;
        --fontColorHover: #71717a;
        --fontColorChildMenu: #71717a;
      }

      .dark {
        --bgColorPrimary: #3f3f46;
        --bgColorSecondary: #52525b;
        --buttonBgColor: #3f3f46;
        --popoverBgColor: #e4e4e7;
        --highlightBgColor: #116932;
        --borderColor: #3f3f46;
        --fontColor: #d4d4d8;
        --fontColorHover: #a1a1aa;
        --fontColorChildMenu: #e4e4e7;
      }
    `}
    />
);
