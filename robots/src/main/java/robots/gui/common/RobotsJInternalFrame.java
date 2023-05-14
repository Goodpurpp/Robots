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

    @Override
    public RobotsJInternalFrameState writeWindowState() {
        return new RobotsJInternalFrameState(this.getSize(), this.isMaximum, this.isIcon);
    }

    @Override
    public void readWindowState(RobotsJInternalFrameState state) {
        if (state == null) {
            this.setSize(400, 400);
            return;
        }

        this.setSize(state.getDimension());
        this.isIcon = state.isIcon();
        this.isMaximum = state.isMaximized();
    }
}
