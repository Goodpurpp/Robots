package robots.gui.game;

import robots.gui.common.RobotsInternalFrameAdapter;
import robots.localisation.Localisation;

import java.awt.BorderLayout;

import javax.swing.*;

public class GameWindow extends JInternalFrame {
    private final GameVisualizer visualizer;

    public GameWindow(Localisation.GameLocalisation gameLocalisation) {
        super(gameLocalisation.getGameField(), true, true, true, true);
        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        this.addInternalFrameListener(new RobotsInternalFrameAdapter(this));
        visualizer = new GameVisualizer();
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(visualizer, BorderLayout.CENTER);
        getContentPane().add(panel);
        pack();
    }
}
