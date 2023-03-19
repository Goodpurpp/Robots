package robots.gui;

import robots.logic.GameVisualizer;

import java.awt.BorderLayout;

import javax.swing.*;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;

public class GameWindow extends JInternalFrame {
    private final GameVisualizer visualizer;

    public GameWindow() {
        super("Игровое поле", true, true, true, true);
        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        this.addInternalFrameListener(new GameWindowAdapter());
        visualizer = new GameVisualizer();
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(visualizer, BorderLayout.CENTER);
        getContentPane().add(panel);
        pack();
    }

    private final class GameWindowAdapter extends InternalFrameAdapter {
        @Override
        public void internalFrameClosing(InternalFrameEvent event) {
            int answer = GuiUtils.askUserForCloseComponentAndWaitAnswer(GameWindow.this);

            switch (answer) {
                case JOptionPane.YES_OPTION -> {
                    GameWindow.this.setVisible(false);
                    GameWindow.this.dispose();
                }
                case JOptionPane.NO_OPTION -> {

                }
            }
        }
    }
}
