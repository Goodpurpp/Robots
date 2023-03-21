package robots.localisation;

import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class RobotsLocalisation {
    private static final ResourceBundle resourceBundle;

    static {
        ResourceBundle tmp;
        try {
            tmp = ResourceBundle.getBundle("Localisation", Locale.getDefault());
        } catch (MissingResourceException e) {
            tmp = ResourceBundle.getBundle("Localisation_en_US");
        }
        resourceBundle = tmp;
    }

    private RobotsLocalisation() {
        throw new IllegalStateException();
    }

    public static String getString(String key) {
        return resourceBundle.getString(key);
    }

    public static Object getObject(String key) {
        return resourceBundle.getObject(key);
    }

    public static String[] getStrings(String key) {
        return resourceBundle.getStringArray(key);
    }
}
