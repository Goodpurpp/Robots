package robots.gui.main;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;

import javax.swing.*;

import robots.common.Pair;
import robots.gui.common.RobotsWindowAdapter;
import robots.gui.game.GameWindow;
import robots.gui.log.LogWindow;
import robots.localisation.Localisation;
import robots.localisation.Localisation.MenuItemLocalisation;
import robots.localisation.RobotsLocalisation;
import robots.log.Logger;

import java.util.List;

public class MainApplicationFrame extends JFrame {
    private final JDesktopPane desktopPane = new JDesktopPane();

    public MainApplicationFrame() {
        this.setContentPane(desktopPane);
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new RobotsWindowAdapter(this));

        int inset = 50;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setBounds(inset, inset, screenSize.width - inset * 2, screenSize.height - inset * 2);
        Localisation localisation = RobotsLocalisation.getLocalisation();

        addWindow(createLogWindow(localisation.getLogLocalisation()));

        GameWindow gameWindow = new GameWindow(localisation.getGameLocalisation());
        gameWindow.setSize(400, 400);
        addWindow(gameWindow);

        setJMenuBar(generateMenuBar(localisation.getMenuLocalisation()));
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

    protected LogWindow createLogWindow(Localisation.LogLocalisation localisation) {
        LogWindow logWindow = new LogWindow(Logger.getDefaultLogSource());
        logWindow.setLocation(10, 10);
        logWindow.setSize(300, 800);
        setMinimumSize(logWindow.getSize());
        logWindow.pack();
        Logger.debug(localisation.getLogProtocolStart());
        return logWindow;
    }

    protected void addWindow(JInternalFrame frame) {
        desktopPane.add(frame);
        frame.setVisible(true);
    }

    private JMenuBar generateMenuBar(Localisation.MenuLocalisation menuLocalisation) {
        JMenuBar menuBar = new JMenuBar();
        JMenuFactory menuFactory = new JMenuFactory();
        MenuItemLocalisation menuItemLocalisation = menuLocalisation.getItemsLocalisation().get(0);
        JMenu lookAndFeelMenu = menuFactory.createJMenu(
                menuItemLocalisation.getMenuName(),
                KeyEvent.VK_V,
                menuItemLocalisation.getDescription(),
                List.of(new Pair<>(menuItemLocalisation.getItemsName().get(0), (event) -> {
                            setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                            this.invalidate();
                        }),
                        new Pair<>(menuItemLocalisation.getItemsName().get(1), (event) -> {
                            setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
                            this.invalidate();
                        })
                )
        );
        menuItemLocalisation = menuLocalisation.getItemsLocalisation().get(1);
        JMenu testMenu = createTestMenu(menuLocalisation.getItemsLocalisation().get(1));
        menuItemLocalisation = menuLocalisation.getItemsLocalisation().get(1);
        JMenuItem exitMenu = createExitMenu(menuLocalisation.getItemsLocalisation().get(2));
        menuBar.add(lookAndFeelMenu);
        menuBar.add(testMenu);
        menuBar.add(exitMenu);
        return menuBar;
    }

    private JMenu createTestMenu(Localisation.MenuItemLocalisation itemLocalisation) {
        JMenu testMenu = new JMenu(itemLocalisation.getMenuName());
        testMenu.setMnemonic(KeyEvent.VK_T);
        testMenu.getAccessibleContext().setAccessibleDescription(itemLocalisation.getDescription());
        addJMenuItem(testMenu, itemLocalisation.getItemsName().get(0), KeyEvent.VK_S, (event) -> Logger.debug("Новая строка"));
        return testMenu;
    }

    private JMenu createExitMenu(Localisation.MenuItemLocalisation itemLocalisation) {
        JMenu exitMenu = new JMenu(itemLocalisation.getMenuName());
        exitMenu.setMnemonic(KeyEvent.VK_ESCAPE);
        exitMenu.getAccessibleContext().setAccessibleDescription(itemLocalisation.getDescription());
        addJMenuItem(exitMenu, itemLocalisation.getItemsName().get(0), KeyEvent.VK_S, (event) -> this.dispatchEvent(createClosingEvent(this)));
        return exitMenu;
    }

    private WindowEvent createClosingEvent(Window window) {
        return new WindowEvent(window, WindowEvent.WINDOW_CLOSING);
    }

    private void addJMenuItem(JMenu menu, String itemText, int itemKeyEventCode, ActionListener actionListener) {
        JMenuItem menuItem = new JMenuItem(itemText, itemKeyEventCode);
        menuItem.addActionListener(actionListener);
        menu.add(menuItem);
    }

    private void setLookAndFeel(String className) {
        try {
            UIManager.setLookAndFeel(className);
            SwingUtilities.updateComponentTreeUI(this);
        } catch (ClassNotFoundException | InstantiationException
                 | IllegalAccessException | UnsupportedLookAndFeelException ignored) {

        }
    }
}
