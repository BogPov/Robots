package factory;

import model.GameEvent;
import model.RobotMovedEvent;
import model.TargetChangedEvent;

public final class GameEventFactory {

    private GameEventFactory() {}

    public static GameEvent createRobotMoved(double x, double y, double direction) {
        return new RobotMovedEvent(x, y, direction);
    }

    public static GameEvent createTargetChanged(int x, int y) {
        return new TargetChangedEvent(x, y);
    }
}