package robots.gui.game.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import robots.gui.common.Pair;
import robots.math.RobotsMathKt;

import java.util.*;
import java.awt.*;
import java.util.List;

import static robots.math.RobotsMathKt.angleTo;
import static robots.math.RobotsMathKt.applyLimits;
import static robots.math.RobotsMathKt.distance;

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
        moveRobot(maxVelocity, angularVelocity, 10, dimension, lines, targetX, targetY);
    }

    private double area(Pair<Double, Double> a, Pair<Double, Double> b, Pair<Double, Double> c) {
        return (b.first() - a.first()) * (c.second() - a.second()) - (b.second() - a.second()) * (c.first() - a.first());
    }

    private boolean intersect_1(double a, double b, double c, double d) {
        double t;
        if (a > b) {
            t = b;
            b = a;
            a = t;
        }
        if (c > d) {
            t = d;
            d = c;
            c = t;
        }
        return Math.max(a, c) < Math.min(b, d);
    }

    private boolean intersect(Pair<Double, Double> a, Pair<Double, Double> b, Pair<Double, Double> c, Pair<Double, Double> d) {
        return intersect_1(a.first(), b.first(), c.first(), d.first())
                && intersect_1(a.second(), b.second(), c.second(), d.second())
                && area(a, b, c) * area(a, b, d) <= 0.1
                && area(c, d, a) * area(c, d, b) <= 0.1;
    }

    private void moveRobot(double velocity, double angularVelocity, double duration, Dimension dimension, List<Pair<Point, Point>> lines, double targetX, double targetY) {
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
        Pair<Double, Double> point = findNextPoint(lines, targetX, targetY, robotPositionX, robotPositionY, dimension);
        robotDirection = RobotsMathKt.angleTo(robotPositionX, robotPositionY, point.first(), point.second());
        robotPositionX = applyLimits(newX, 0, dimension.width);
        robotPositionY = applyLimits(newY, 0, dimension.height);
    }

    private boolean targetIsOpen(List<Pair<Point, Point>> lines, double targetX, double targetY, double robotX, double robotY) {
        for (var line : lines) {
            if ((Double.compare(targetX, line.first().x) == 0 && Double.compare(targetY, line.first().y) == 0) ||
                (Double.compare(targetX, line.second().x) == 0 && Double.compare(targetY, line.second().y) == 0)) {
                continue;
            }
            if (intersect(new Pair<>(targetX, targetY), new Pair<>(robotX, robotY),
                    new Pair<>((double) line.first().x, (double) line.first().y),
                    new Pair<>((double) line.second().x, (double) line.second().y))) {
                return false;
            }
        }
        return true;
    }

    private Pair<Double, Double> findNextPoint(List<Pair<Point, Point>> lines, double targetX, double targetY, double currX, double currY, Dimension dimension) {
        if (targetIsOpen(lines, targetX, targetY, currX, currY)) {
            return new Pair<>(targetX, targetY);
        }

        List<Pair<Double, Double>> correctPoints = new ArrayList<>();
        for (var line : lines) {
            if (targetIsOpen(lines, line.first().x, line.first().y, currX, currY) && isNotOnBorder(line.first(), dimension)) {
                correctPoints.add(new Pair<>((double)line.first().x, (double)line.first().y));
            }
            if (targetIsOpen(lines, line.second().x, line.second().y, currX, currY) && isNotOnBorder(line.second(), dimension)) {
                correctPoints.add(new Pair<>((double)line.second().x, (double)line.second().y));
            }
        }
        double minLength = Double.MAX_VALUE;
        Pair<Double, Double> minPoint = null;
        for (var point : correctPoints) {
            double length = distance(point.first(), point.second(), targetX, targetY);
            if (minLength > length) {
                minPoint = point;
                minLength = length;
            }
        }
        return minPoint;
    }

    private boolean isNotOnBorder(Point point, Dimension dimension) {
        return Double.compare(point.x, 0) > 0 && Double.compare(point.y, 0) > 0 &&
                Double.compare(point.x, dimension.width) < 0 &&
                Double.compare(point.y, dimension.height) < 0;
    }

    public void scalePosition(double xScalar, double yScalar) {
        double newX = Math.round(robotPositionX * xScalar);
        double newY = Math.round(robotPositionY * yScalar);
        robotPositionX = newX;
        robotPositionY = newY;
    }
}