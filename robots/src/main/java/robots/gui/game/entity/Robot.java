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
        return Math.max(a, c) <= Math.min(b, d);
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
        Pair<Double, Double> point = new Pair<>(targetX,targetY);
        if (!targetIsOpen(lines, targetX, targetY, robotPositionX, robotPositionY)) {
            point = isIntersect(lines, targetX, targetY, robotPositionX, robotPositionY, point);
        }
        double c = ((point.second() - robotPositionY) / (point.first() - robotPositionX));
        double arctg = RobotsMathKt.angleTo(robotPositionX, robotPositionY, point.first(), point.second());
        robotDirection = arctg;
        robotPositionX = applyLimits(newX, 0, dimension.width);
        robotPositionY = applyLimits(newY, 0, dimension.height);
    }


    private double bounceAngle(double robotNewX, double robotNewY) {
        if (Double.compare(robotNewX, robotPositionX) != 0) {
            return robotDirection - Math.PI;
        } else if (Double.compare(robotNewY, robotPositionY) != 0) {
            return robotDirection - Math.PI / 2;
        }
        return 0;
    }
    private boolean targetIsOpen(List<Pair<Point, Point>> lines, double targetX, double targetY, double robotX, double robotY) {
        for (var line : lines) {
            if (intersect(new Pair<>(targetX, targetY), new Pair<>(robotX, robotY), new Pair<>((double) line.first().x, (double) line.first().y), new Pair<>(
                    (double) line.second().x, (double) line.second().y))) {
                return false;
            }
        }
        return true;
    }

    private Pair<Double, Double> isIntersect(List<Pair<Point, Point>> lines, double targetX, double targetY, double robotX, double robotY,Pair<Double,Double> currentPoint) {
        List<Pair<Pair<Double, Double>, Pair<Double, Double>>> correctLines = new ArrayList<>();
        for (var line : lines) {
            if (intersect(new Pair<>(targetX, targetY), new Pair<>(robotX, robotY), new Pair<>((double) line.first().x, (double) line.first().y), new Pair<>(
                    (double) line.second().x, (double) line.second().y))) {
                correctLines.add(new Pair<>(new Pair<>((double) line.first().x, (double) line.first().y), new Pair<>((double) line.second().x, (double) line.second().y)));
            }
        }
        double minLength = Double.MIN_VALUE;
        Pair<Double, Double> minPoint;
        if (correctLines.size() > 0) {
            minPoint = correctLines.get(0)
                                   .first();
        } else {
            minPoint = new Pair<>(robotX - 1, robotY );
        }
        for (var line : correctLines) {
            double firstLength = getLength(line.first().first(), line.first().second(), targetX, targetY);
            double secondLength = getLength(line.second().first(), line.second().second(), targetX, targetY);
            if (minLength < firstLength) {
                System.out.println(minLength);
                minPoint = line.first();
                minLength = firstLength;
            }
            if (minLength < secondLength) {
                System.out.println(minLength);
                minPoint = line.second();
                minLength = secondLength;
            }
        }
        return minPoint;
    }

    private double getLength(double x1, double y1, double targetX, double targetY) {
        return Math.sqrt(Math.pow(targetX - x1, 2) + Math.pow(targetY - y1, 2));
    }

    public void scalePosition(double xScalar, double yScalar) {
        double newX = Math.round(robotPositionX * xScalar);
        double newY = Math.round(robotPositionY * yScalar);
        robotPositionX = newX;
        robotPositionY = newY;
    }

}