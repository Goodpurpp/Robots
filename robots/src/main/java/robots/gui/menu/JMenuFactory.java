package robots.gui.menu;

import lombok.RequiredArgsConstructor;
import robots.localisation.Localisation;
import robots.localisation.Localisation.MenuItemLocalisation;
import robots.log.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;

@RequiredArgsConstructor
public class JMenuFactory {
    private final Localisation localisation;
    private final Window window;

    public JMenu createLookAndFeelMenu() {
        MenuItemLocalisation itemLocalisation = localisation.getMenuLocalisation().getItemsLocalisation().get(0);
        JMenu lookAndFeelMenu = new JMenu(itemLocalisation.getMenuName());
        lookAndFeelMenu.setMnemonic(KeyEvent.VK_V);
        lookAndFeelMenu.getAccessibleContext().setAccessibleDescription(itemLocalisation.getDescription());
        addJMenuItem(lookAndFeelMenu, itemLocalisation.getItemsName().get(0), KeyEvent.VK_S, (event) -> {
            setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            window.invalidate();
        });
        addJMenuItem(lookAndFeelMenu, itemLocalisation.getItemsName().get(0), KeyEvent.VK_S, (event) -> {
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
        MenuItemLocalisation itemLocalisation = localisation.getMenuLocalisation().getItemsLocalisation().get(1);
        JMenu testMenu = new JMenu(itemLocalisation.getMenuName());
        testMenu.setMnemonic(KeyEvent.VK_T);
        testMenu.getAccessibleContext().setAccessibleDescription(itemLocalisation.getDescription());
        addJMenuItem(testMenu, itemLocalisation.getItemsName().get(0), KeyEvent.VK_S, (event) -> Logger.debug("Новая строка"));
        return testMenu;
    }

    public JMenu createExitMenu() {
        MenuItemLocalisation itemLocalisation = localisation.getMenuLocalisation().getItemsLocalisation().get(2);
        JMenu exitMenu = new JMenu(itemLocalisation.getMenuName());
        exitMenu.setMnemonic(KeyEvent.VK_ESCAPE);
        exitMenu.getAccessibleContext().setAccessibleDescription(itemLocalisation.getDescription());
        addJMenuItem(exitMenu, itemLocalisation.getItemsName().get(0), KeyEvent.VK_S, (event) -> window.dispatchEvent(createClosingEvent(window)));
        return exitMenu;
    }

    private WindowEvent createClosingEvent(Window window) {
        return new WindowEvent(window, WindowEvent.WINDOW_CLOSING);
    }

}
