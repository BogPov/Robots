package gui;

import controller.RobotController;
import game_model.RobotModel;
import observer.RobotModelObserver;

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JInternalFrame;
import javax.swing.JPanel;

public class GameWindow extends JInternalFrame
{
    RobotModel model;
    private final GameVisualizer m_visualizer;

    public GameWindow() 
    {
        super("Игровое поле", true, true, true, true);
        model = new RobotModel();
        m_visualizer = new GameVisualizer(model);
        RobotController controller = new RobotController(model, m_visualizer);
        m_visualizer.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                controller.onClick(e.getPoint());
            }
        });
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(m_visualizer, BorderLayout.CENTER);
        getContentPane().add(panel);
        pack();
    }

    public void setNewModelObserver(RobotModelObserver observer){
        model.attach(observer);
    }
}
