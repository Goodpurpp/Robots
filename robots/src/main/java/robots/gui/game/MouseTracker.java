package robots.gui.game;

import robots.gui.game.entity.MouseClicked;
import robots.gui.game.entity.MouseListener;
import robots.log.LogChangeListener;

import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import javax.swing.plaf.basic.BasicListUI;
import javax.swing.plaf.basic.BasicListUI.MouseInputHandler;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.nio.DoubleBuffer;
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
        var event = new robots.gui.game.entity.MouseEvent(this.first, this.second);
        listeners.forEach(l -> l.onClickChange(event));
    }

    private void invokeClick() {
        var event = new MouseClickedEvent();
        mouseClickedListeners.forEach(l -> l.mouseClicked(event));

    }


    private class MouseAdapter extends java.awt.event.MouseAdapter {

        @Override
        public void mousePressed(MouseEvent e) {
            MouseTracker.this.first = e.getPoint();
            System.out.println("____PRESSED___");
            System.out.println(Double.toString(e.getX()) + " " + Double.toString(e.getY()));
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            MouseTracker.this.second = e.getPoint();
            MouseTracker.this.invokeLister();
            System.out.println("____RELEASED____");
            System.out.println(Double.toString(e.getX()) + " " + Double.toString(e.getY()));
        }

    }

}
