package ninja.miserable.exhaust.bungee.provider;

import com.sk89q.intake.argument.ArgumentException;
import com.sk89q.intake.argument.ArgumentParseException;
import com.sk89q.intake.argument.CommandArgs;
import com.sk89q.intake.parametric.Provider;
import com.sk89q.intake.parametric.ProvisionException;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.annotation.Nullable;

public class ServerInfoProvider implements Provider<ServerInfo> {

    @Override
    public boolean isProvided() {
        return false;
    }

    @Nullable
    @Override
    public ServerInfo get(CommandArgs arguments, List<? extends Annotation> modifiers) throws ArgumentException, ProvisionException {
        ServerInfo serverInfo = ProxyServer.getInstance().getServerInfo(arguments.next());

        if (serverInfo == null) {
            throw new ArgumentParseException("Server not found.");
        }

        return serverInfo;
    }

    @Override
    public List<String> getSuggestions(String prefix) {
        List<String> suggestions = new ArrayList<>();

        for (String server : ProxyServer.getInstance().getServers().keySet()) {
            if (server.startsWith(Pattern.quote(prefix))) {
                suggestions.add(server);
            }
        }

        return suggestions;
    }

}
