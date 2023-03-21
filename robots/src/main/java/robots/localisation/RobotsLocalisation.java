package robots.localisation;

import java.util.Locale;
import java.util.Map;

public class RobotsLocalisation {
    private static final Map<Locale, Localisation> localisations;
    private static final EnLocalisation enLocalisation = new EnLocalisation();

    static {
        localisations = Map.of(
                new Locale("ru", "RU"), new RuLocalisation(),
                Locale.UK, enLocalisation
        );
    }

    private RobotsLocalisation() {
        throw new IllegalStateException();
    }

    public static Localisation getLocalisation() {
        return localisations.getOrDefault(Locale.getDefault(), enLocalisation);
    }
}
