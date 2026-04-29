package observer;

import model.GameEvent;

public interface RobotModelObserver {
    void onModelUpdateEvent(GameEvent state);
}
