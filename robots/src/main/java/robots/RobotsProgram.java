package robots;

import robots.gui.common.GsonHelper;
import robots.gui.common.Localisation;
import robots.gui.main.MainApplicationFrame;
import robots.gui.common.PathEnum;
import robots.localisation.RobotsLocalisation;

import java.awt.Frame;
import java.io.File;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class RobotsProgram {

    public static void main(String[] args) {
        preInit();
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        UIManager.getDefaults().addResourceBundle("Localisation");
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

        } else {
            GsonHelper<Localisation> gsonHelper = new GsonHelper<>();
            Localisation localisation = gsonHelper.loadFromJson(PathEnum.LOCALISATION_JSON_PATH.getPath(), Localisation.class);

            if (localisation != null) {
                RobotsLocalisation.changeLocalisation(localisation.getLocalisation());
            }

        }

    }
}
