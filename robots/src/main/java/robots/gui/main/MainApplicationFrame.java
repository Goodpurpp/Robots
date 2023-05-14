package robots.gui.main;

import java.awt.event.WindowEvent;
import java.beans.PropertyVetoException;

import javax.swing.*;

import lombok.Getter;
import robots.gui.common.Pair;
import robots.gui.common.RobotsJFrame;
import robots.gui.common.RobotsJFrameState;
import robots.gui.common.RobotsLocaleChangeAdapter;
import robots.gui.game.GameWindow;
import robots.gui.input.InputWindow;
import robots.gui.log.LogWindow;
import robots.gui.main.menu.JMenuFactory;
import robots.gui.common.PathEnum;
import robots.localisation.RobotsLocalisation;
import robots.log.Logger;

public class MainApplicationFrame extends RobotsJFrame {
    @Getter
    private final JDesktopPane desktopPane = new JDesktopPane();

    public MainApplicationFrame() {
        super(PathEnum.MAIN_APPLICATION_FRAME_JSON.getPath());
        this.setContentPane(desktopPane);
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.addPropertyChangeListener("localisation", new RobotsLocaleChangeAdapter(this));

        this.addWindow(createLogWindow());

        GameWindow gameWindow = new GameWindow();
        this.addWindow(gameWindow);

        InputWindow inputWindow = new InputWindow();
        addWindow(inputWindow);

        this.setJMenuBar(generateMenuBar());
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
            RobotsLocalisation.save();
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

    protected void removeWindow(JInternalFrame frame) {
        desktopPane.remove(frame);
        frame.setVisible(false);
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

    @Override
    public void changeLocalisation() {
        this.setJMenuBar(generateMenuBar());
    }
}
