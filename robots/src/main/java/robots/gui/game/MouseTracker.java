package robots.gui.game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class MouseTracker {
    private final List<RobotsMouseListener> listeners = new ArrayList<>();
    private final RobotsMouseAdapter mouseAdapter = new RobotsMouseAdapter();

    public void registerListener(RobotsMouseListener listener) {
        synchronized (listeners) {
            listeners.add(listener);
            if (listener instanceof JPanel frame) {
                frame.addMouseListener(mouseAdapter);
            }
        }
    }

    public void unregisterListener(RobotsMouseListener listener) {
        synchronized (listeners) {
            listeners.remove(listener);
        }
    }

    private void invokeListeners(RobotsMouseEvent event) {
        listeners.forEach(l -> l.onClickChange(event));
    }


    private class RobotsMouseAdapter extends java.awt.event.MouseAdapter {
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
                MouseTracker.this.invokeListeners(new RobotsMouseEvent((Point) first.clone(), (Point) second.clone()));
            }
        }
    }
}
