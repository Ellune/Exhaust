package ninja.miserable.exhaust.bungee;

import net.md_5.bungee.api.plugin.Plugin;

public final class Exhaust extends Plugin {

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
