package storage;

import model.WindowState;
import java.util.Map;

public interface WindowStateStorage {
    void save(Map<String, WindowState> data);
    Map<String, WindowState> load();
}