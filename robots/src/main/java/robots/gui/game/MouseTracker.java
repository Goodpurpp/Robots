package robots.gui.game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class MouseTracker {
    private final List<MouseListener> listeners = new ArrayList<>();
    private final MouseAdapter mouseAdapter = new MouseAdapter();

    public void registerListener(MouseListener listener) {
        synchronized (listeners) {
            if (listener instanceof JPanel frame) {
                listeners.add(listener);
                frame.addMouseListener(mouseAdapter);
            }
        }
    }

    public void unregisterListener(MouseListener listener) {
        synchronized (listeners) {
            listeners.remove(listener);
        }
    }

    private void invokeListeners(robots.gui.game.MouseEvent event) {
        listeners.forEach(l -> l.onClickChange(event));
    }


    private class MouseAdapter extends java.awt.event.MouseAdapter {
        private Point first;
        private final CooldownSkillTimer skillTimer = new CooldownSkillTimer();

        @Override
        public void mousePressed(MouseEvent e) {
            System.out.println(skillTimer.isDown());
            first = e.getPoint();
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            Point second = e.getPoint();
            if (skillTimer.isDown()) {
                skillTimer.update();
                MouseTracker.this.invokeListeners(new robots.gui.game.MouseEvent((Point) first.clone(), (Point) second.clone()));
            }
        }
    }
}
