package ninja.miserable.exhaust.bungee.provider;

import com.sk89q.intake.parametric.AbstractModule;
import net.md_5.bungee.api.config.ServerInfo;

public class BungeeModule extends AbstractModule {

    @Override
    protected void configure() {
        this.bind(ServerInfo.class).toProvider(new ServerInfoProvider());
    }

}
