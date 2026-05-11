package observer;

import model.GameEvent;

public interface GameModelObserver {
    void onModelUpdateEvent(GameEvent state);
}
