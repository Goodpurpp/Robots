package robots.gui.game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class MouseTracker {
    private final List<MouseListener> listeners = new ArrayList<>();
    private final List<MouseClicked> mouseClickedListeners = new ArrayList<>();
    private Point first;
    private Point second;

    public MouseTracker() {
    }

    public void registerListener(MouseListener listener) {
        synchronized (listeners) {
            listeners.add(listener);
            if (listener instanceof JPanel frame) {
                frame.addMouseListener(new MouseAdapter());
            }
        }
    }

    public void unregisterListener(MouseListener listener) {
        synchronized (listeners) {
            listeners.remove(listener);
        }
    }

    private void invokeLister() {
        var event = new robots.gui.game.MouseEvent(this.first, this.second);
        listeners.forEach(l -> l.onClickChange(event));
    }

    private void invokeClick() {
        var event = new MouseClickedEvent();
        mouseClickedListeners.forEach(l -> l.mouseClicked(event));

    }


    private class MouseAdapter extends java.awt.event.MouseAdapter {

        private CooldownSkillTimer skillTimer = new CooldownSkillTimer();

        @Override
        public void mousePressed(MouseEvent e) {
            System.out.println(skillTimer.isDown());
            MouseTracker.this.first = e.getPoint();
//            System.out.println("____PRESSED____");
//            System.out.println(Double.toString(e.getX()) + " " + Double.toString(e.getY()));
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            MouseTracker.this.second = e.getPoint();
            if (skillTimer.isDown()) {
                skillTimer.update();
                MouseTracker.this.invokeLister();
            }
//            System.out.println("____RELEASED____");
//            System.out.println(Double.toString(e.getX()) + " " + Double.toString(e.getY()));
        }

    }

}
