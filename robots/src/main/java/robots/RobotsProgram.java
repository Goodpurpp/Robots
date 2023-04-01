package robots;

import robots.gui.main.MainApplicationFrame;
import robots.gui.common.PathEnum;
import robots.localisation.RobotsLocalisation;

import java.awt.Frame;
import java.io.File;
import java.util.Locale;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class RobotsProgram {

    static {
        Locale.setDefault(Locale.UK);
    }

    public static void main(String[] args) {
        preInit();
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(() -> {
            MainApplicationFrame frame = new MainApplicationFrame();
            frame.pack();
            frame.setVisible(true);
            frame.setExtendedState(Frame.MAXIMIZED_BOTH);
        });
    }

    private static void preInit() {
        File baseJsonDir = PathEnum.BASE_JSON_PATH.getPath().toFile();

        if (!baseJsonDir.exists()) {

            if (!baseJsonDir.mkdir()) {
                throw new IllegalStateException(RobotsLocalisation.getString("error.create.json-dir"));
            }

        }

    }
}
