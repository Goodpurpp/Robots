package robots.gui.game.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.awt.*;

import static robots.math.RobotsMathKt.angleTo;
import static robots.math.RobotsMathKt.applyLimits;
import static robots.math.RobotsMathKt.asNormalizedRadians;
import static robots.math.RobotsMathKt.distance;

@RequiredArgsConstructor
public class Robot {
    @Getter
    private volatile double robotPositionX = 100;
    @Getter
    private volatile double robotPositionY = 100;
    @Getter
    private volatile double robotDirection = 0;

    private static final double maxVelocity = 0.1;
    private static final double maxAngularVelocity = 0.001;

    public void onModelUpdateEvent(Target target, Dimension dimension) {
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

        moveRobot(maxVelocity, angularVelocity, 10, dimension);
    }

    private void moveRobot(double velocity, double angularVelocity, double duration, Dimension dimension) {
        velocity = applyLimits(velocity, 0, maxVelocity);
        angularVelocity = applyLimits(angularVelocity, -maxAngularVelocity, maxAngularVelocity);
        double newX = robotPositionX + velocity / angularVelocity *
                (Math.sin(robotDirection + angularVelocity * duration) -
                        Math.sin(robotDirection));
        if (!Double.isFinite(newX)) {
            newX = robotPositionX + velocity * duration * Math.cos(robotDirection);
        }
        double newY = robotPositionY - velocity / angularVelocity *
                (Math.cos(robotDirection + angularVelocity * duration) -
                        Math.cos(robotDirection));
        if (!Double.isFinite(newY)) {
            newY = robotPositionY + velocity * duration * Math.sin(robotDirection);
        }
        robotPositionX = applyLimits(newX, 0, dimension.width);
        robotPositionY = applyLimits(newY, 0, dimension.height);
        double newDirection = asNormalizedRadians(robotDirection + angularVelocity * duration + bounceAngle(newX, newY));
        robotDirection = newDirection;
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
}
