package robots.gui.game;

import robots.gui.common.RobotsInternalFrameAdapter;
import robots.gui.common.RobotsJInternalFrame;
import robots.gui.common.RobotsJInternalFrameState;
import robots.gui.common.PathEnum;
import robots.gui.common.RobotsLocaleChangeAdapter;
import robots.localisation.RobotsLocalisation;

import java.awt.BorderLayout;

import javax.swing.*;

public class GameWindow extends RobotsJInternalFrame {
    private final GameVisualizer visualizer;

    public GameWindow() {
        super(RobotsLocalisation.getString("game.field.name"), true, true, true, true);
        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        this.addInternalFrameListener(new RobotsInternalFrameAdapter(this, PathEnum.GAME_WINDOW_JSON_PATH.getPath()));
        this.addPropertyChangeListener("localisation", new RobotsLocaleChangeAdapter(this));
        this.visualizer = new GameVisualizer();
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(visualizer, BorderLayout.CENTER);
        this.getContentPane().add(panel);
        this.pack();
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

    @Override
    public void changeLocalisation() {
        this.setTitle(RobotsLocalisation.getString("game.field.name"));
    }
}
