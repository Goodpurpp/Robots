package robots.gui.game;

import robots.gui.common.RobotsInternalFrameAdapter;
import robots.localisation.RobotsLocalisation;

import java.awt.BorderLayout;

import javax.swing.*;

public class GameWindow extends JInternalFrame {
    private final GameVisualizer visualizer;

    public GameWindow() {
        super(RobotsLocalisation.getString("game.field.name"), true, true, true, true);
        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        this.addInternalFrameListener(new RobotsInternalFrameAdapter(this));
        this.visualizer = new GameVisualizer();
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(visualizer, BorderLayout.CENTER);
        this.getContentPane().add(panel);
        this.pack();
    }
}
