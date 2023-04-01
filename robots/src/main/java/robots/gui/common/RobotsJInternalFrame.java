package robots.gui.common;

import javax.swing.*;

public abstract class RobotsJInternalFrame extends JInternalFrame implements RobotsSerializable<RobotsJInternalFrameState> {
    public RobotsJInternalFrame() {
    }

    public RobotsJInternalFrame(String title) {
        super(title);
    }

    public RobotsJInternalFrame(String title, boolean resizable) {
        super(title, resizable);
    }

    public RobotsJInternalFrame(String title, boolean resizable, boolean closable) {
        super(title, resizable, closable);
    }

    public RobotsJInternalFrame(String title, boolean resizable, boolean closable, boolean maximizable) {
        super(title, resizable, closable, maximizable);
    }

    public RobotsJInternalFrame(String title, boolean resizable, boolean closable, boolean maximizable, boolean iconifiable) {
        super(title, resizable, closable, maximizable, iconifiable);
    }
}
