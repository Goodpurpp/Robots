package robots.gui.main;

import robots.common.Pair;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.List;

public class JMenuFactory {
    public JMenu createJMenu(String name, int mnemonicKey, String description, List<Pair<String, ActionListener>> items) {
        JMenu menu = new JMenu(name);
        menu.setMnemonic(mnemonicKey);
        menu.getAccessibleContext().setAccessibleDescription(description);
        for (Pair<String, ActionListener> item : items) {
            addJMenuItem(menu, item.first(), KeyEvent.VK_S, item.second());
        }
        return menu;
    }

    private static void addJMenuItem(JMenu menu, String itemText, int itemKeyEventCode, ActionListener actionListener) {
        JMenuItem menuItem = new JMenuItem(itemText, itemKeyEventCode);
        menuItem.addActionListener(actionListener);
        menu.add(menuItem);
    }
}
