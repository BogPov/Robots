package storage;

import model.WindowState;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class YamlWindowStateStorage implements WindowStateStorage {

    private final String configPath;

    public YamlWindowStateStorage(String configPath) {
        this.configPath = configPath;
    }

    @Override
    public void save(Map<String, WindowState> data) {
        Map<String, Map<String, Object>> raw = new HashMap<>();

        for (Map.Entry<String, WindowState> e : data.entrySet()) {
            WindowState ws = e.getValue();

            Map<String, Object> m = new HashMap<>();
            m.put("x", ws.x);
            m.put("y", ws.y);
            m.put("width", ws.width);
            m.put("height", ws.height);
            m.put("icon", ws.icon);
            m.put("max", ws.max);

            raw.put(e.getKey(), m);
        }

        Yaml yaml = new Yaml();
        try (FileWriter writer = new FileWriter(configPath)) {
            yaml.dump(raw, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int getInt(Object o, int def) {
        if (o instanceof Number) {
            return ((Number) o).intValue();
        }
        return def;
    }

    private boolean getBoolean(Object o, boolean def) {
        if (o instanceof Boolean) {
            return (Boolean) o;
        }
        return def;
    }

    @Override
    public Map<String, WindowState> load() {
        File file = new File(configPath);

        if (!file.exists()) {
            return new HashMap<>();
        }

        Yaml yaml = new Yaml();

        try (FileInputStream in = new FileInputStream(file)) {

            Object obj = yaml.load(in);
            if (obj == null) {
                return new HashMap<>();
            }

            if (!(obj instanceof Map)) {
                return new HashMap<>();
            }

            Map<?, ?> raw = (Map<?, ?>) obj;
            Map<String, WindowState> result = new HashMap<>();

            for (Map.Entry<?, ?> e : raw.entrySet()) {

                if (!(e.getKey() instanceof String)) continue;
                if (!(e.getValue() instanceof Map)) continue;

                String name = (String) e.getKey();
                Map<?, ?> m = (Map<?, ?>) e.getValue();

                WindowState ws = new WindowState();

                ws.x = getInt(m.get("x"), 0);
                ws.y = getInt(m.get("y"), 0);
                ws.width = getInt(m.get("width"), 300);
                ws.height = getInt(m.get("height"), 300);
                ws.icon = getBoolean(m.get("icon"), false);
                ws.max = getBoolean(m.get("max"), false);

                result.put(name, ws);
            }

            return result;

        } catch (Exception e) {
            e.printStackTrace();
            return new HashMap<>();
        }
    }
}