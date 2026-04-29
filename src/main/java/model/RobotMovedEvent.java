package model;

public class RobotMovedEvent implements GameEvent {
    private final double x;
    private final double y;
    private final double direction;

    public RobotMovedEvent(double x, double y, double direction) {
        this.x = x;
        this.y = y;
        this.direction = direction;
    }

    @Override
    public String getType() {
        return "ROBOT_MOVED";
    }

    public double getX() { return x; }
    public double getY() { return y; }
    public double getDirection() { return direction; }
}