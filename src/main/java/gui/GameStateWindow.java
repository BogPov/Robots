package gui;

import model.GameEvent;
import model.RobotMovedEvent;
import model.TargetChangedEvent;
import observer.GameModelObserver;

import javax.swing.*;
import java.awt.*;

public class GameStateWindow extends JInternalFrame implements GameModelObserver
{
    private final TextArea m_content;

    private volatile double m_robotX;
    private volatile double m_robotY;
    private volatile double m_robotDir;

    private volatile int m_targetX;
    private volatile int m_targetY;

    public GameStateWindow()
    {
        super("Состояние робота", true, true, true, true);

        m_content = new TextArea();
        m_content.setEditable(false);

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(m_content, BorderLayout.CENTER);

        getContentPane().add(panel);

        updateContent();
    }

    private void updateContent()
    {
        m_content.setText(
                "РОБОТ" + "\nx: " + String.format("%.2f", m_robotX)
                        + "\ny: " + String.format("%.2f", m_robotY)
                        + "\ndir: " + String.format("%.2f", m_robotDir) +
                        "\n\nЦЕЛЬ" + "\nx: " + m_targetX + "\ny: " + m_targetY
        );

        m_content.invalidate();
    }

    @Override
    public void onModelUpdateEvent(GameEvent event)
    {
        if (event instanceof RobotMovedEvent move)
        {
            m_robotX = move.getX();
            m_robotY = move.getY();
            m_robotDir = move.getDirection();
        }
        else if (event instanceof TargetChangedEvent target)
        {
            m_targetX = target.getX();
            m_targetY = target.getY();
        }

        EventQueue.invokeLater(this::updateContent);
    }
}