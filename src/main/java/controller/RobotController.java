package controller;

import game_model.RobotModel;
import gui.GameVisualizer;

import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;


public class RobotController {
    private final RobotModel model;
    private final Timer moveTimer;

    public RobotController(RobotModel model, GameVisualizer visualizer) {
        this.model = model;

        moveTimer = new Timer("model-updater", true);
        moveTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                model.update();
            }
        }, 0, 10);

    }

    public void onClick(Point p) {
        model.setTargetPosition(p);
    }
}