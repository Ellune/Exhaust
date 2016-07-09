package ninja.miserable.exhaust.bukkit;

import org.bukkit.plugin.java.JavaPlugin;

public final class Exhaust extends JavaPlugin {

    /** The singleton instance of this plugin. */
    private static Exhaust exhaust;

    public Exhaust() {
        exhaust = this;
    }

    @Override
    public final void onEnable() {
    }

    @Override
    public final void onDisable() {
        exhaust = null;
    }

    public static Exhaust get() {
        return exhaust;
    }
}
