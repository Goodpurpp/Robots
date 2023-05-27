package robots.gui.common;

import robots.localisation.LocalisationChangeable;

import javax.swing.*;
import java.beans.PropertyVetoException;
import java.nio.file.Path;

public abstract class RobotsJInternalFrame extends JInternalFrame
        implements RobotsSerializable<RobotsJInternalFrameState>, LocalisationChangeable {

    public RobotsJInternalFrame(String title, boolean resizable, boolean closable, boolean maximizable, boolean iconifiable, Path serializedPath) {
        super(title, resizable, closable, maximizable, iconifiable);
        this.addInternalFrameListener(new RobotsInternalFrameAdapter(this, serializedPath));
        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        this.addPropertyChangeListener("localisation", new RobotsLocaleChangeAdapter(this));
    }

    @Override
    public RobotsJInternalFrameState writeWindowState() {
        return new RobotsJInternalFrameState(this.getSize(), this.isMaximum, this.isIcon);
    }

    @Override
    public void readWindowState(RobotsJInternalFrameState state) {
        if (state == null) {
            this.setLocation(10, 10);
            this.setSize(300, 800);
            this.pack();
            return;
        }

        this.setSize(state.getDimension());
        try {
            setIcon(state.isIcon());
            setMaximum(state.isMaximized());
        } catch (PropertyVetoException e) {
            throw new RuntimeException(e);
        }
    }
}
