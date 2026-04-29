package game_model;

import factory.GameEventFactory;
import model.GameEvent;
import model.RobotMovedEvent;
import model.TargetChangedEvent;
import observer.Observeable;
import observer.RobotModelObserver;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static math_tools.GameMathTools.*;
import static math_tools.GameMathTools.angleTo;

public class RobotModel implements Observeable<RobotModelObserver> {
    private volatile double m_robotPositionX = 100;
    private volatile double m_robotPositionY = 100;
    private volatile double m_robotDirection = 0;

    private volatile int m_targetPositionX = 150;
    private volatile int m_targetPositionY = 100;

    private static final double maxVelocity = 0.1;
    private static final double maxAngularVelocity = 0.001;

    private final List<RobotModelObserver> observers = new ArrayList<>();

    public void move(double velocity, double angularVelocity, double duration)
    {
        velocity = applyLimits(velocity, 0, maxVelocity);
        angularVelocity = applyLimits(angularVelocity, -maxAngularVelocity, maxAngularVelocity);
        double newX = m_robotPositionX + velocity / angularVelocity *
                (Math.sin(m_robotDirection  + angularVelocity * duration) -
                        Math.sin(m_robotDirection));
        if (!Double.isFinite(newX))
        {
            newX = m_robotPositionX + velocity * duration * Math.cos(m_robotDirection);
        }
        double newY = m_robotPositionY - velocity / angularVelocity *
                (Math.cos(m_robotDirection  + angularVelocity * duration) -
                        Math.cos(m_robotDirection));
        if (!Double.isFinite(newY))
        {
            newY = m_robotPositionY + velocity * duration * Math.sin(m_robotDirection);
        }
        m_robotPositionX = newX;
        m_robotPositionY = newY;
        double newDirection = asNormalizedRadians(m_robotDirection + angularVelocity * duration);
        m_robotDirection = newDirection;
    };



    public void update()
    {
        double distance = distance(m_targetPositionX, m_targetPositionY,
                m_robotPositionX, m_robotPositionY);
        if (distance < 0.5)
        {
            return;
        }
        double velocity = maxVelocity;
        double angleToTarget = angleTo(m_robotPositionX, m_robotPositionY, m_targetPositionX, m_targetPositionY);
        double angularVelocity = 0;
        if (angleToTarget > m_robotDirection)
        {
            angularVelocity = maxAngularVelocity;
        }
        if (angleToTarget < m_robotDirection)
        {
            angularVelocity = -maxAngularVelocity;
        }
        move(velocity, angularVelocity, 10);
        notifyObservers(GameEventFactory.createRobotMoved(m_robotPositionX, m_robotPositionY, m_robotDirection));
    }

    public void setTargetPosition(Point p)
    {
        m_targetPositionX = p.x;
        m_targetPositionY = p.y;
        notifyObservers(GameEventFactory.createTargetChanged(m_targetPositionX, m_targetPositionY));
    }

    @Override
    public void attach(RobotModelObserver observer) {
        observers.add(observer);
        // начальное стостояние
        observer.onModelUpdateEvent(
                GameEventFactory.createRobotMoved(m_robotPositionX, m_robotPositionY, m_robotDirection)
        );

        observer.onModelUpdateEvent(
                GameEventFactory.createTargetChanged(m_targetPositionX, m_targetPositionY)
        );
    }

    @Override
    public void deattach(RobotModelObserver observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(GameEvent event) {
        for (RobotModelObserver observer : observers){
            observer.onModelUpdateEvent(event);
        }
    }
}
