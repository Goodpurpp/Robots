package robots.gui.game;


import robots.gui.common.RobotsJInternalFrame;
import robots.gui.common.PathEnum;
import robots.localisation.RobotsLocalisation;

import java.awt.BorderLayout;

import javax.swing.*;

public class GameWindow extends RobotsJInternalFrame {
    private final GameVisualizer visualizer;
    private final MouseTracker mouseTracker = new MouseTracker();

    public GameWindow() {
        super(RobotsLocalisation.getString("game.field.name"), true, true, true, true, PathEnum.GAME_WINDOW_JSON_PATH.getPath());
        this.visualizer = new GameVisualizer(mouseTracker);
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(visualizer, BorderLayout.CENTER);
        this.getContentPane()
            .add(panel);
        this.pack();
    }

    @Override
    public void changeLocalisation() {
        this.setTitle(RobotsLocalisation.getString("game.field.name"));
    }
}
