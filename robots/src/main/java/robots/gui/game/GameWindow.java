package robots.gui.game;

import robots.gui.common.RobotsJInternalFrame;
import robots.gui.common.PathEnum;
import robots.gui.mouse.MouseTracker;
import robots.localisation.RobotsLocalisation;

import java.awt.BorderLayout;

import javax.swing.*;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;

public class GameWindow extends RobotsJInternalFrame {
    private final GameVisualizer visualizer;
    private final MouseTracker mouseTracker = new MouseTracker();

    public GameWindow() {
        super(RobotsLocalisation.getString("game.field.name"), true, true, true, true, PathEnum.GAME_WINDOW_JSON_PATH.getPath());
        this.visualizer = new GameVisualizer(mouseTracker);
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(visualizer, BorderLayout.CENTER);
        this.getContentPane().add(panel);
        addInternalFrameListener(new GameWindowListener());
        this.pack();
    }

    @Override
    public void changeLocalisation() {
        this.setTitle(RobotsLocalisation.getString("game.field.name"));
    }

    private class GameWindowListener extends InternalFrameAdapter {
        @Override
        public void internalFrameClosed(InternalFrameEvent e) {
            GameWindow.this.mouseTracker.unregisterListener(GameWindow.this.visualizer);
        }
    }
}
