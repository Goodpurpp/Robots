package robots.gui.utils;

import robots.localisation.RobotsLocalisation;

import javax.swing.*;
import java.awt.*;

public class GuiUtils {
    private GuiUtils() {
        throw new IllegalStateException();
    }

    public static int askUserForCloseComponentAndWaitAnswer(Component component) {
        return JOptionPane.showOptionDialog(
                component,
                RobotsLocalisation.getString("exit.message"),
                RobotsLocalisation.getString("exit.title"),
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                new String[]{RobotsLocalisation.getString("exit.yes"), RobotsLocalisation.getString("exit.no")},
                RobotsLocalisation.getString("exit.no")
        );
    }
}
