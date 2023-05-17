package robots.gui.common;

import robots.localisation.LocalisationChangeable;

import javax.swing.*;
import java.awt.*;
import java.nio.file.Path;

public abstract class RobotsJFrame extends JFrame
        implements RobotsSerializable<RobotsJFrameState>, LocalisationChangeable {
    public RobotsJFrame(Path serializedPath) throws HeadlessException {
        super();
        this.addWindowListener(new RobotsWindowAdapter(this, serializedPath));
    }
}
