package robots.gui.game.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.awt.*;

@Getter
@AllArgsConstructor
public class Target {
    private volatile int targetPositionX;
    private volatile int targetPositionY;

    public void setTargetPosition(Point p) {
        targetPositionX = p.x;
        targetPositionY = p.y;
    }

    public void scale(double xScalar, double yScalar) {
        int newX = (int) Math.round(targetPositionX * xScalar);
        targetPositionX = newX;
        int newY = (int) Math.round(targetPositionY * yScalar);
        targetPositionY = newY;
    }
}
