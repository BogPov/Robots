package service;

import model.WindowState;
import storage.WindowStateStorage;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

public class WindowStateService {

    private final WindowStateStorage storage;

    public WindowStateService(WindowStateStorage storage) {
        this.storage = storage;
    }

    public void save(JDesktopPane desktopPane) {
        Map<String, WindowState> data = new HashMap<>();

        for (JInternalFrame frame : desktopPane.getAllFrames()) {
            if (frame.getName() == null) continue;

            WindowState state = new WindowState();
            state.x = frame.getX();
            state.y = frame.getY();
            state.width = frame.getWidth();
            state.height = frame.getHeight();
            state.icon = frame.isIcon();
            state.max = frame.isMaximum();

            data.put(frame.getName(), state);
        }

        storage.save(data);
    }

    public void load(JDesktopPane desktopPane) {
        Map<String, WindowState> data = storage.load();

        for (JInternalFrame frame : desktopPane.getAllFrames()) {
            WindowState state = data.get(frame.getName());
            if (state == null) continue;

            frame.setBounds(state.x, state.y, state.width, state.height);

            try {
                if (state.icon) frame.setIcon(true);
                if (state.max) frame.setMaximum(true);
            } catch (Exception ignored) {}
        }
    }
}