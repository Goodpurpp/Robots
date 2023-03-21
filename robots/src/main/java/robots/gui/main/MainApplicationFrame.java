package robots.gui.main;

import java.awt.*;
import java.awt.event.WindowEvent;

import javax.swing.*;

import robots.gui.common.RobotsWindowAdapter;
import robots.gui.game.GameWindow;
import robots.gui.log.LogWindow;
import robots.gui.main.menu.JMenuFactory;
import robots.localisation.RobotsLocalisation;
import robots.log.Logger;

public class MainApplicationFrame extends JFrame {
    private final JDesktopPane desktopPane = new JDesktopPane();

    public MainApplicationFrame() {
        this.setContentPane(desktopPane);
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new RobotsWindowAdapter(this));

        int inset = 50;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setBounds(inset, inset, screenSize.width - inset * 2, screenSize.height - inset * 2);

        addWindow(createLogWindow());

        GameWindow gameWindow = new GameWindow();
        gameWindow.setSize(400, 400);
        addWindow(gameWindow);

        setJMenuBar(generateMenuBar());
    }

    @Override
    protected void processWindowEvent(final WindowEvent windowEvent) {
        super.processWindowEvent(windowEvent);
        if (windowEvent.getID() == WindowEvent.WINDOW_CLOSED) {
            JInternalFrame[] frames = desktopPane.getAllFrames();
            for (JInternalFrame frame : frames) {
                frame.setVisible(false);
                frame.dispose();
            }
            System.exit(0);
        }
    }

    protected LogWindow createLogWindow() {
        LogWindow logWindow = new LogWindow(Logger.getDefaultLogSource());
        logWindow.setLocation(10, 10);
        logWindow.setSize(300, 800);
        setMinimumSize(logWindow.getSize());
        logWindow.pack();
        Logger.debug(RobotsLocalisation.getString("log.message.start"));
        return logWindow;
    }

    protected void addWindow(JInternalFrame frame) {
        desktopPane.add(frame);
        frame.setVisible(true);
    }

    private JMenuBar generateMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenuFactory jMenuFactory = new JMenuFactory(this);
        menuBar.add(jMenuFactory.createLookAndFeelMenu());
        menuBar.add(jMenuFactory.createTestMenu());
        menuBar.add(jMenuFactory.createExitMenu());
        return menuBar;
    }
}
