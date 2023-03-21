package robots.gui.utils;

import robots.localisation.Localisation;
import robots.localisation.RobotsLocalisation;

import javax.swing.*;
import java.awt.*;

public class GuiUtils {
    private static final String[] closingOptions;
    private static final String defaultOption;
    private static final String message;
    private static final String title;

    static {
        Localisation localisation = RobotsLocalisation.getLocalisation();
        closingOptions = new String[]{localisation.getExitLocalisation().getYes(), localisation.getExitLocalisation().getNo()};
        defaultOption = closingOptions[1];
        message = localisation.getExitLocalisation().getMessage();
        title = localisation.getExitLocalisation().getTitle();
    }

    private GuiUtils() {
        throw new IllegalStateException();
    }

    public static int askUserForCloseComponentAndWaitAnswer(Component component) {
        return JOptionPane.showOptionDialog(
                component,
                message,
                title,
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                closingOptions,
                defaultOption
        );
    }
}
