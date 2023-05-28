package robots.gui.game;

import robots.gui.common.Pair;
import robots.gui.game.entity.Robot;
import robots.gui.game.entity.Target;
import robots.gui.mouse.MouseTracker;
import robots.gui.mouse.RobotsMouseEvent;
import robots.gui.mouse.RobotsMouseListener;
import robots.math.RobotsMathKt;

import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Collectors;

import javax.swing.JPanel;

import static robots.math.RobotsMathKt.isIntersectingLines;

public class GameVisualizer extends JPanel implements RobotsMouseListener {
    private final Timer timer = new Timer("events generator", true);
    private final List<Pair<Point, Point>> lines = new ArrayList<>();

    private volatile Dimension dimension = this.getSize();

    private final Target target = new Target(150, 100);
    private final Robot robot = new Robot();


    public GameVisualizer(MouseTracker tracker) {
        this.timer.schedule(new TimerTask() {
            @Override
            public void run() {
                GameVisualizer.this.onRedrawEvent();
            }
        }, 0, 50);
        this.timer.schedule(new TimerTask() {
            @Override
            public void run() {
                GameVisualizer.this.robot.onModelUpdateEvent(target, dimension, lines);
            }
        }, 0, 10);
        this.addComponentListener(new GameVisualizerAdapter());
        this.setDoubleBuffered(true);
        tracker.registerListener(this);
    }

    protected void onRedrawEvent() {
        EventQueue.invokeLater(this::repaint);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;
        this.drawRobot(g2d, robot);
        this.drawTarget(g2d, target);
        this.drawLines(g2d, lines);
    }

    private void drawRobot(Graphics2D g, Robot robot) {
        int robotCenterX = (int) Math.round(robot.getRobotPositionX());
        int robotCenterY = (int) Math.round(robot.getRobotPositionY());
        AffineTransform t = AffineTransform.getRotateInstance(robot.getRobotDirection(), robotCenterX, robotCenterY);
        g.setTransform(t);
        g.setColor(Color.MAGENTA);
        GameVisualizer.fillOval(g, robotCenterX, robotCenterY, 30, 10);
        g.setColor(Color.BLACK);
        GameVisualizer.drawOval(g, robotCenterX, robotCenterY, 30, 10);
        g.setColor(Color.WHITE);
        GameVisualizer.fillOval(g, robotCenterX + 10, robotCenterY, 5, 5);
        g.setColor(Color.BLACK);
        GameVisualizer.drawOval(g, robotCenterX + 10, robotCenterY, 5, 5);
    }

    private static void fillOval(Graphics g, int centerX, int centerY, int diam1, int diam2) {
        g.fillOval(centerX - diam1 / 2, centerY - diam2 / 2, diam1, diam2);
    }

    private static void drawOval(Graphics g, int centerX, int centerY, int diam1, int diam2) {
        g.drawOval(centerX - diam1 / 2, centerY - diam2 / 2, diam1, diam2);
    }

    private void drawTarget(Graphics2D g, Target target) {
        AffineTransform t = AffineTransform.getRotateInstance(0, 0, 0);
        g.setTransform(t);
        g.setColor(Color.GREEN);
        fillOval(g, target.getTargetPositionX(), target.getTargetPositionY(), 5, 5);
        g.setColor(Color.BLACK);
        drawOval(g, target.getTargetPositionX(), target.getTargetPositionY(), 5, 5);
    }

    private void drawLines(Graphics2D g, List<Pair<Point, Point>> lines) {
        g.setColor(Color.magenta);
        for (Pair<Point, Point> line : lines) {
            g.drawLine(line.first().x,
                    line.first().y,
                    line.second().x ,
                    line.second().y);
        }
    }

    @Override
    public void onReleaseChange(RobotsMouseEvent e) {
        if (lines.size() > 0) {
            List<Pair<Point, Point>> copyLines = new ArrayList<>(lines);
            copyLines.add(new Pair<>(e.f(), e.s()));
            Map<Pair<Point, Point>, Set<Pair<Point, Point>>> intersectingLines = new HashMap<>();
            for (Pair<Point, Point> line : copyLines) {
                for (Pair<Point, Point> line1 : copyLines) {
                    if (line == line1) {
                        continue;
                    }

                    if (isIntersectingLines(line, line1)) {
                        intersectingLines.compute(line, (k, v) -> v == null ? new HashSet<>(List.of(line1)) : add(v, line1));
                    }
                }
            }
            Set<Set<Pair<Point, Point>>> i = intersectingLines.entrySet()
                                                              .stream()
                                                              .filter(el -> el.getValue()
                                                                              .size() >= 2)
                                                              .map(this::flatten)
                                                              .collect(Collectors.toSet());
            List<Set<Pair<Point, Point>>> figures = i.stream()
                                                     .filter(this::isFormFigure)
                                                     .toList();

            if (figures.isEmpty()) {
                lines.add(new Pair<>(e.f(), e.s()));
            }
        } else {
            lines.add(new Pair<>(e.f(), e.s()));
        }
        EventQueue.invokeLater(this::repaint);
    }

    @Override
    public void onClickChange(RobotsMouseEvent e) {
        target.setTargetPosition(e.f());
        repaint();
    }

    private Set<Pair<Point, Point>> add(Set<Pair<Point, Point>> set, Pair<Point, Point> el) {
        set.add(el);
        return set;
    }

    private Set<Pair<Point, Point>> flatten(Entry<Pair<Point, Point>, Set<Pair<Point, Point>>> entry) {
        entry.getValue()
             .add(entry.getKey());
        return entry.getValue();
    }

    private boolean isFormFigure(Set<Pair<Point, Point>> set) {
        for (Pair<Point, Point> a : set) {
            for (Pair<Point, Point> b : set) {
                if (a == b) {
                    continue;
                }

                boolean isIntersecting = isIntersectingLines(a, b);
                if (!isIntersecting) {
                    return false;
                }
            }
        }
        return true;
    }

    private class GameVisualizerAdapter extends ComponentAdapter {
        @Override
        public void componentResized(ComponentEvent e) {
            Dimension newSize = GameVisualizer.this.getSize();
            double xScalar = RobotsMathKt.findScalar(dimension.width, newSize.width);
            double yScalar = RobotsMathKt.findScalar(dimension.height, newSize.height);
            target.scale(xScalar, yScalar);
            robot.scalePosition(xScalar, yScalar);
            dimension = newSize;
        }
    }
}
