package ninja.miserable.exhaust.bungee.provider.core;

import com.sk89q.intake.argument.ArgumentException;
import com.sk89q.intake.argument.ArgumentParseException;
import com.sk89q.intake.argument.CommandArgs;
import com.sk89q.intake.parametric.Provider;
import com.sk89q.intake.parametric.ProvisionException;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.annotation.Nullable;

public class ProxiedPlayerProvider implements Provider<ProxiedPlayer> {

    public static final ProxiedPlayerProvider INSTANCE = new ProxiedPlayerProvider();

    @Override
    public boolean isProvided() {
        return false;
    }

    @Nullable
    @Override
    public ProxiedPlayer get(CommandArgs arguments, List<? extends Annotation> modifiers) throws ArgumentException, ProvisionException {
        ProxiedPlayer proxiedPlayer = getPlayer(arguments.next());

        if (proxiedPlayer == null) {
            throw new ArgumentParseException("Player not found.");
        }

        return proxiedPlayer;
    }

    @Override
    public List<String> getSuggestions(String prefix) {
        List<String> suggestions = new ArrayList<>();

        for (ProxiedPlayer proxiedPlayer : ProxyServer.getInstance().getPlayers()) {
            String pName = proxiedPlayer.getName();

            if (pName.startsWith(Pattern.quote(prefix))) {
                suggestions.add(pName);
            }
        }

        return suggestions;
    }

    private static ProxiedPlayer getPlayer(String input) {
        ProxiedPlayer found = null;
        String lowerCase = input.toLowerCase();
        int delta = Integer.MAX_VALUE;

        for (ProxiedPlayer proxiedPlayer : ProxyServer.getInstance().getPlayers()) {
            String name = proxiedPlayer.getName();

            if (name.toLowerCase().startsWith(lowerCase)) {
                int curDelta = name.length() - lowerCase.length();
                if (curDelta < delta) {
                    found = proxiedPlayer;
                    delta = curDelta;
                }

                if (curDelta == 0) {
                    break;
                }
            }
        }

        return found;
    }

}
