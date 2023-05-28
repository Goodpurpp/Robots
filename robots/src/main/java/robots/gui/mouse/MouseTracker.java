package robots.gui.mouse;

import robots.gui.game.CooldownSkillTimer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import static robots.math.RobotsMathKt.distance;

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
            if (listener instanceof JPanel frame) {
                frame.removeMouseListener(mouseAdapter);
            }
        }
    }

    private void invokeListeners(RobotsMouseEvent event) {
        synchronized (listeners) {
            listeners.forEach(l -> l.onReleaseChange(event));
        }
    }

    public void invokeClickListeners(RobotsMouseEvent event) {
        synchronized (listeners) {
            listeners.forEach(l -> l.onClickChange(event));
        }
    }


    private class RobotsMouseAdapter extends MouseAdapter {
        private Point first;
        private final CooldownSkillTimer skillTimer = new CooldownSkillTimer();

        @Override
        public void mouseClicked(MouseEvent e) {
            invokeClickListeners(new RobotsMouseEvent(e.getPoint(), null));
        }

        @Override
        public void mousePressed(MouseEvent e) {
            first = e.getPoint();
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            Point second = e.getPoint();
            if (skillTimer.isDown() && distance(first.x, first.y, second.x, second.y) > 1) {
                skillTimer.update();
                MouseTracker.this.invokeListeners(new RobotsMouseEvent((Point) first.clone(), (Point) second.clone()));
            }
        }
    }
}
