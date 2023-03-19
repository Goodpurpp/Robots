package robots.gui;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.*;

import robots.log.Logger;

public class MainApplicationFrame extends JFrame {
    private final JDesktopPane desktopPane = new JDesktopPane();

    public MainApplicationFrame() {
        this.setContentPane(desktopPane);
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new MainApplicationFrameAdapter());

        int inset = 50;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setBounds(inset, inset, screenSize.width - inset * 2, screenSize.height - inset * 2);

        LogWindow logWindow = createLogWindow();
        addWindow(logWindow);

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
        Logger.debug("Протокол работает");
        return logWindow;
    }

    protected void addWindow(JInternalFrame frame) {
        desktopPane.add(frame);
        frame.setVisible(true);
    }

    private JMenuBar generateMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu lookAndFeelMenu = createLookAndFeelMenu();
        JMenu testMenu = createTestMenu();
        JMenuItem exitMenu = createExitMenu();
        menuBar.add(lookAndFeelMenu);
        menuBar.add(testMenu);
        menuBar.add(exitMenu);
        return menuBar;
    }

    private JMenu createLookAndFeelMenu() {
        JMenu lookAndFeelMenu = new JMenu("Режим отображения");
        lookAndFeelMenu.setMnemonic(KeyEvent.VK_V);
        lookAndFeelMenu.getAccessibleContext().setAccessibleDescription("Управление режимом отображения приложения");
        addJMenuItem(lookAndFeelMenu, "Системная схема", KeyEvent.VK_S, (event) -> {
            setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            this.invalidate();
        });
        addJMenuItem(lookAndFeelMenu, "Универсальная схема", KeyEvent.VK_S, (event) -> {
            setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            this.invalidate();
        });
        return lookAndFeelMenu;
    }

    private JMenu createTestMenu() {
        JMenu testMenu = new JMenu("Тесты");
        testMenu.setMnemonic(KeyEvent.VK_T);
        testMenu.getAccessibleContext().setAccessibleDescription("Тестовые команды");
        addJMenuItem(testMenu, "Сообщение в лог", KeyEvent.VK_S, (event) -> Logger.debug("Новая строка"));
        return testMenu;
    }

    private JMenu createExitMenu() {
        JMenu exitMenu = new JMenu("Выйти");
        exitMenu.setMnemonic(KeyEvent.VK_ESCAPE);
        exitMenu.getAccessibleContext().setAccessibleDescription("Выход из приложения");
        addJMenuItem(exitMenu, "Выйти из приложения", KeyEvent.VK_S, (event) -> this.dispatchEvent(createClosingEvent(this)));
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

    private final class MainApplicationFrameAdapter extends WindowAdapter {
        @Override
        public void windowClosing(WindowEvent event) {
            int answer = GuiUtils.askUserForCloseComponentAndWaitAnswer(MainApplicationFrame.this);

            switch (answer) {
                case JOptionPane.YES_OPTION -> {
                    MainApplicationFrame.this.setVisible(false);
                    MainApplicationFrame.this.dispose();
                }
                case JOptionPane.NO_OPTION -> {

                }
            }
        }
    }
}
