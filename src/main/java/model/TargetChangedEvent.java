package model;

public class TargetChangedEvent implements GameEvent {
    private final int x;
    private final int y;

    public TargetChangedEvent(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String getType() {
        return "TARGET_CHANGED";
    }

    public int getX() { return x; }
    public int getY() { return y; }
}
