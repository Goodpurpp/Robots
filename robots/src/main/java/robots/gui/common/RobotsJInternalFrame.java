package robots.gui.common;

import robots.localisation.LocalisationChangeable;

import javax.swing.*;
import java.nio.file.Path;

public abstract class RobotsJInternalFrame extends JInternalFrame
        implements RobotsSerializable<RobotsJInternalFrameState>, LocalisationChangeable {

    public RobotsJInternalFrame(String title, boolean resizable, boolean closable, boolean maximizable, boolean iconifiable, Path serializedPath) {
        super(title, resizable, closable, maximizable, iconifiable);
        this.addInternalFrameListener(new RobotsInternalFrameAdapter(this, serializedPath));
    }
}
