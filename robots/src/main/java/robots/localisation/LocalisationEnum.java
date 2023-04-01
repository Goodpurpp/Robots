package robots.localisation;

import lombok.Getter;
import robots.log.Logger;

import java.util.Locale;

public enum LocalisationEnum {
    RU(new Locale("ru", "RU")),
    EN(Locale.US);

    @Getter
    private final Locale locale;

    LocalisationEnum(Locale locale) {
        this.locale = locale;
    }

    public static LocalisationEnum valueOf(Locale locale) {
        for (LocalisationEnum e : LocalisationEnum.values()) {
            if (e.locale.equals(locale)) {
                return e;
            }
        }
        Logger.error("Не найдено локализации по локали");
        throw new IllegalStateException();
    }

    public static LocalisationEnum valueOf(Long ordinal) {
        for (LocalisationEnum e : LocalisationEnum.values()) {
            if (e.ordinal() == ordinal) {
                return e;
            }
        }
        Logger.error("Не найдено локализации по локали");
        throw new IllegalStateException();
    }
}
