package observer;

import model.GameEvent;

public interface Observeable<T>{
    void attach(T observer);
    void deattach(T observer);
    void notifyObservers(GameEvent event);
}
