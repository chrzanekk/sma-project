package pl.com.chrzanowski.scma.util;

import org.springframework.context.i18n.LocaleContextHolder;
import pl.com.chrzanowski.scma.domain.enumeration.Language;

import java.util.Locale;

public class LanguageUtil {

    public static Language getCurrentLanguage() {
        return prepareLanguageFrom(LocaleContextHolder.getLocale());
    }

    public static Language prepareLanguageFrom(Locale locale) {
        return Language.valueOf(locale.getLanguage().toUpperCase());
    }
}
