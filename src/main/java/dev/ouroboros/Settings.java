package dev.ouroboros;

import net.minecraftforge.common.config.Configuration;

import java.io.File;

public final class Settings {
    public static final class Module {
        public final String name, key;
        public boolean enabled;

        Module(String name, String key, boolean enabled) {
            this.name = name;
            this.key = key;
            this.enabled = enabled;
        }
    }

    public static final Module INVENTORY = new Module("Inventory", "inventory", true);
    public static final Module ARMOR = new Module("Armor + Hand", "armor", true);
    public static final Module FPS = new Module("FPS", "fps", true);
    public static final Module CPS = new Module("CPS", "cps", true);
    public static final Module COORDS = new Module("Coordinates", "coords", true);
    public static final Module[] ALL = {INVENTORY, ARMOR, FPS, CPS, COORDS};

    private static Configuration cfg;

    private Settings() {}

    public static void load(File file) {
        cfg = new Configuration(file);
        for (Module m : ALL) {
            m.enabled = cfg.getBoolean(m.key, "modules", m.enabled, "Show " + m.name);
        }
        if (cfg.hasChanged()) cfg.save();
    }

    public static void save() {
        if (cfg == null) return;
        for (Module m : ALL) {
            cfg.get("modules", m.key, m.enabled).set(m.enabled);
        }
        cfg.save();
    }
}
