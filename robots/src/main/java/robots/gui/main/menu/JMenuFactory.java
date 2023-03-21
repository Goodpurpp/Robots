package robots.gui.main.menu;

import lombok.RequiredArgsConstructor;
import robots.localisation.RobotsLocalisation;
import robots.log.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;

@RequiredArgsConstructor
public class JMenuFactory {
    private final Window window;

    public JMenu createLookAndFeelMenu() {
        JMenu lookAndFeelMenu = new JMenu(RobotsLocalisation.getString("menu.view.name"));
        lookAndFeelMenu.setMnemonic(KeyEvent.VK_V);
        lookAndFeelMenu.getAccessibleContext().setAccessibleDescription(RobotsLocalisation.getString("menu.view.description"));
        addJMenuItem(lookAndFeelMenu, RobotsLocalisation.getString("menu.view.items.system_schema"), KeyEvent.VK_S, (event) -> {
            setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            window.invalidate();
        });
        addJMenuItem(lookAndFeelMenu, RobotsLocalisation.getString("menu.view.items.universal_schema"), KeyEvent.VK_S, (event) -> {
            setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            window.invalidate();
        });
        return lookAndFeelMenu;
    }

    private void addJMenuItem(JMenu menu, String itemText, int itemKeyEventCode, ActionListener actionListener) {
        JMenuItem menuItem = new JMenuItem(itemText, itemKeyEventCode);
        menuItem.addActionListener(actionListener);
        menu.add(menuItem);
    }

    private void setLookAndFeel(String className) {
        try {
            UIManager.setLookAndFeel(className);
            SwingUtilities.updateComponentTreeUI(window);
        } catch (ClassNotFoundException | InstantiationException
                 | IllegalAccessException | UnsupportedLookAndFeelException ignored) {

        }
    }

    public JMenu createTestMenu() {
        JMenu testMenu = new JMenu(RobotsLocalisation.getString("menu.test.name"));
        testMenu.setMnemonic(KeyEvent.VK_T);
        testMenu.getAccessibleContext().setAccessibleDescription(RobotsLocalisation.getString("menu.test.description"));
        addJMenuItem(testMenu, RobotsLocalisation.getString("menu.test.items.log_message"), KeyEvent.VK_S, (event) -> Logger.debug(RobotsLocalisation.getString("log.message.test")));
        return testMenu;
    }

    public JMenu createExitMenu() {
        JMenu exitMenu = new JMenu(RobotsLocalisation.getString("menu.exit.name"));
        exitMenu.setMnemonic(KeyEvent.VK_ESCAPE);
        exitMenu.getAccessibleContext().setAccessibleDescription(RobotsLocalisation.getString("menu.exit.description"));
        addJMenuItem(exitMenu, RobotsLocalisation.getString("menu.exit.items.exit"), KeyEvent.VK_S, (event) -> window.dispatchEvent(createClosingEvent(window)));
        return exitMenu;
    }

    private WindowEvent createClosingEvent(Window window) {
        return new WindowEvent(window, WindowEvent.WINDOW_CLOSING);
    }

}
