package robots.gui.game;

import robots.gui.game.entity.Robot;
import robots.gui.game.entity.Target;
import robots.math.RobotsMathKt;

import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JPanel;

public class GameVisualizer extends JPanel {
    private final Timer timer = new Timer("events generator", true);

    private volatile Dimension dimension = this.getSize();

    private final Target target = new Target(150, 100);
    private final Robot robot = new Robot();

    public GameVisualizer() {
        this.timer.schedule(new TimerTask() {
            @Override
            public void run() {
                GameVisualizer.this.onRedrawEvent();
            }
        }, 0, 50);
        this.timer.schedule(new TimerTask() {
            @Override
            public void run() {
                GameVisualizer.this.robot.onModelUpdateEvent(target, dimension);
            }
        }, 0, 10);
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                GameVisualizer.this.target.setTargetPosition(e.getPoint());
                GameVisualizer.this.repaint();
            }
        });
        this.addComponentListener(new GameVisualizerAdapter());
        this.setDoubleBuffered(true);
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
