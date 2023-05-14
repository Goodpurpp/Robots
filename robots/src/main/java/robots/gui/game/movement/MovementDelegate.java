package robots.gui.game.movement;

import robots.gui.game.entity.Robot;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MovementDelegate {
    public void moveRobot(Robot robot) throws ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        Class<?> c = Class.forName("class0");
        Object o = c.getDeclaredConstructor().newInstance();
        Method callMethod = null;
        for (Method m : c.getMethods()) {
            if (m.getAnnotation(MoveAction.class) != null) {
                if (callMethod == null) {
                    callMethod = m;
                } else {
                    throw new IllegalArgumentException("more than 1 annotated method");
                }
            }
        }
        assert callMethod != null;
        callMethod.invoke(o);
    }
}
