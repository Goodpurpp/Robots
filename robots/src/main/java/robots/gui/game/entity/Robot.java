package robots.gui.game.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import robots.gui.common.Pair;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.awt.*;
import java.util.Set;

import static robots.math.RobotsMathKt.angleTo;
import static robots.math.RobotsMathKt.applyLimits;
import static robots.math.RobotsMathKt.asNormalizedRadians;
import static robots.math.RobotsMathKt.distance;
import static robots.math.RobotsMathKt.isIntersecting;

@RequiredArgsConstructor
public class Robot {
    private static final double maxVelocity = 0.1;
    private static final double maxAngularVelocity = 0.001;

    @Getter
    private volatile double robotPositionX = 100;
    @Getter
    private volatile double robotPositionY = 100;
    @Getter
    private volatile double robotDirection = 0;
    private final double radius = 10;

    public void onModelUpdateEvent(Target target, Dimension dimension, List<Pair<Point, Point>> lines) {
        double targetX = target.getTargetPositionX();
        double targetY = target.getTargetPositionY();
        double distance = distance(targetX, targetY, robotPositionX, robotPositionY);
        if (distance < 0.5) {
            return;
        }
        double angleToTarget = angleTo(robotPositionX, robotPositionY, targetX, targetY);
        double angularVelocity = 0;
        if (angleToTarget > robotDirection) {
            angularVelocity = maxAngularVelocity;
        } else if (angleToTarget < robotDirection) {
            angularVelocity = -maxAngularVelocity;
        }
        if (isCrossed(lines)) {
            ...
        } else {
            moveRobot(maxVelocity, angularVelocity, 10, dimension, lines);
        }
    }

    private void moveRobot(double velocity, double angularVelocity, double duration, Dimension dimension, List<Pair<Point, Point>> lines) {
        velocity = applyLimits(velocity, 0, maxVelocity);
        angularVelocity = applyLimits(angularVelocity, -maxAngularVelocity, maxAngularVelocity);
        double newX = robotPositionX + velocity / angularVelocity * (Math.sin(robotDirection + angularVelocity * duration) - Math.sin(robotDirection));
        if (!Double.isFinite(newX)) {
            newX = robotPositionX + velocity * duration * Math.cos(robotDirection);
        }
        double newY = robotPositionY - velocity / angularVelocity * (Math.cos(robotDirection + angularVelocity * duration) - Math.cos(robotDirection));
        if (!Double.isFinite(newY)) {
            newY = robotPositionY + velocity * duration * Math.sin(robotDirection);
        }
        boolean isCrossed = false;
        robotPositionX = applyLimits(newX, 0, dimension.width);
        robotPositionY = applyLimits(newY, 0, dimension.height);

        double newDirection = asNormalizedRadians(robotDirection + angularVelocity * duration + bounceAngle(newX, newY));
        robotDirection = newDirection;
    }

    private Pair<Double, Double> cross(double x1, double y1, double x2, double y2, double x3, double y3,
                                       double x4, double y4) {
        double resultX;
        double resultY;
        double n;
        if (y2 - y1 > Double.MIN_NORMAL) {
            double q = (x2 - x1) / (y1 - y2);
            double sn = (x3 - x4) + (y3 - y4) * q;
            double fn = (x3 - x1) + (y3 - y1) * q;
            n = fn / sn;
        } else {
            n = (y3 - y1) / (y3 - y4);
        }
        resultX = x3 + (x4 - x3) * n;
        resultY = y3 + (y4 - y3) * n;
        return new Pair<>(resultX, resultY);
    }

    private double bounceAngle(double robotNewX, double robotNewY) {
        if (Double.compare(robotNewX, robotPositionX) != 0) {
            return robotDirection - Math.PI;
        } else if (Double.compare(robotNewY, robotPositionY) != 0) {
            return robotDirection - Math.PI / 2;
        }
        return 0;
    }

    public void scalePosition(double xScalar, double yScalar) {
        double newX = Math.round(robotPositionX * xScalar);
        double newY = Math.round(robotPositionY * yScalar);
        robotPositionX = newX;
        robotPositionY = newY;
    }

    private boolean isCrossed(List<Pair<Point, Point>> lines) {
        boolean isCrossed = false;
        for (Pair<Point, Point> currentLine : lines) {
            for (int i = -1; i < 2; i += 2) {
                for (int j = -1; j < 2; j += 2) {
                    if (isIntersecting(new Pair<>(new Pair<>(robotPositionX + radius * i, robotPositionY + radius * j), new Pair<>(robotPositionX, robotPositionY)), currentLine)) {
                        isCrossed = true;
                        break;
                    }
                }
            }
        }
        return isCrossed;
    }

    private void replaceDirection(List<Pair<Point, Point>> lines) {
        Set<Pair<Pair<Double, Double>, Pair<Double, Double>>> crossedLines = new HashSet<>();
        boolean isCrossed = false;
        for (Pair<Point, Point> currentLine : lines) {
            for (int i = -1; i < 2; i += 2) {
                for (int j = -1; j < 2; j += 2) {
                    var line = new Pair<>(new Pair<>(robotPositionX + radius * i, robotPositionY + radius * j), new Pair<>(robotPositionX, robotPositionY));
                    if (isIntersecting(line, currentLine)) {
                        crossedLines.add(line);
                    }
                }
            }
        }
    }
}
