package robots.gui.main;

import java.awt.*;
import java.awt.event.WindowEvent;
import java.beans.PropertyVetoException;

import javax.swing.*;

import lombok.Getter;
import robots.gui.common.Pair;
import robots.gui.common.RobotsJFrame;
import robots.gui.common.RobotsJFrameState;
import robots.gui.common.RobotsLocaleChangedAdapter;
import robots.gui.common.RobotsWindowAdapter;
import robots.gui.game.GameWindow;
import robots.gui.log.LogWindow;
import robots.gui.main.menu.JMenuFactory;
import robots.gui.common.PathEnum;
import robots.localisation.RobotsLocalisation;
import robots.log.Logger;

public class MainApplicationFrame extends RobotsJFrame {
    @Getter
    private final JDesktopPane desktopPane = new JDesktopPane();

    public MainApplicationFrame() {
        this.setContentPane(desktopPane);
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new RobotsWindowAdapter(this, PathEnum.MAIN_APPLICATION_FRAME_JSON.getPath()));
        this.addPropertyChangeListener(new RobotsLocaleChangedAdapter(this));

        int inset = 50;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setBounds(inset, inset, screenSize.width - inset * 2, screenSize.height - inset * 2);

        addWindow(createLogWindow());

        GameWindow gameWindow = new GameWindow();
        addWindow(gameWindow);

        setJMenuBar(generateMenuBar());
    }

    @Override
    protected void processWindowEvent(final WindowEvent windowEvent) {
        super.processWindowEvent(windowEvent);
        if (windowEvent.getID() == WindowEvent.WINDOW_CLOSED) {
            JInternalFrame[] frames = desktopPane.getAllFrames();
            for (JInternalFrame frame : frames) {
                try {
                    frame.setClosed(true);
                } catch (PropertyVetoException e) {
                    frame.setVisible(false);
                    frame.dispose();
                }
            }
            System.exit(0);
        }
    }

    protected LogWindow createLogWindow() {
        LogWindow logWindow = new LogWindow(Logger.getDefaultLogSource());
        setMinimumSize(logWindow.getSize());
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
        menuBar.add(jMenuFactory.createLocalisationMenu());
        return menuBar;
    }

    @Override
    public RobotsJFrameState writeWindowState() {
        return new RobotsJFrameState(this.getSize(), new Pair<>(this.getX(), this.getY()));
    }

    @Override
    public void readWindowState(RobotsJFrameState state) {
        if (state == null) {
            return;
        }

        this.setSize(state.getDimension());
        this.setLocation(state.getLocation().first(), state.getLocation().second());
    }
}
