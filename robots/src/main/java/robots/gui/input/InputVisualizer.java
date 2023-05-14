package robots.gui.input;

import lombok.SneakyThrows;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Timer;

public class InputVisualizer extends JPanel {
    private final Timer timer = new Timer("events generator", true);

    private final JTextArea inputContent;
    private final JButton button;
    private final InputCompiler compiler;

    public InputVisualizer() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        inputContent = new JTextArea();
        compiler = new InputCompiler();
        button = new JButton("save");
        ActionListener buttonListener = new ButtonListener();
        button.addActionListener(buttonListener);
        add(generateMenuBar(buttonListener));
        add(inputContent);
    }

    private JMenuBar generateMenuBar(ActionListener buttonListener) {
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(saveMenu(buttonListener));
        return menuBar;
    }

    public JMenu saveMenu(ActionListener actionListener) {
        JMenu menu = new JMenu("name");
        addJMenuItem(menu, "save", KeyEvent.VK_S, actionListener);
        return menu;
    }

    private static void addJMenuItem(JMenu menu, String itemText, int itemKeyEventCode, ActionListener actionListener) {
        JMenuItem menuItem = new JMenuItem(itemText, itemKeyEventCode);
        menuItem.addActionListener(actionListener);
        menu.add(menuItem);
    }

    public class ButtonListener implements ActionListener {

        @SneakyThrows
        @Override
        public void actionPerformed(ActionEvent e) {
            InputVisualizer.this.compiler.process(InputVisualizer.this.inputContent.getText());
        }
    }
}
