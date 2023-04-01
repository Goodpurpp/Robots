package robots.gui.common;

import robots.localisation.LocalisationChangeable;

import javax.swing.*;
import java.awt.*;

public abstract class RobotsJFrame extends JFrame
        implements RobotsSerializable<RobotsJFrameState>, LocalisationChangeable {
    public RobotsJFrame() throws HeadlessException {
    }

    public RobotsJFrame(GraphicsConfiguration gc) {
        super(gc);
    }

    public RobotsJFrame(String title) throws HeadlessException {
        super(title);
    }

    public RobotsJFrame(String title, GraphicsConfiguration gc) {
        super(title, gc);
    }
}
