package ninja.miserable.exhaust.bukkit.provider.core;

import com.sk89q.intake.argument.ArgumentException;
import com.sk89q.intake.argument.ArgumentParseException;
import com.sk89q.intake.argument.CommandArgs;
import com.sk89q.intake.parametric.Provider;
import com.sk89q.intake.parametric.ProvisionException;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.annotation.Nullable;

public class PlayerProvider implements Provider<Player> {

    public static final PlayerProvider INSTANCE = new PlayerProvider();

    @Override
    public boolean isProvided() {
        return false;
    }

    @Nullable
    @Override
    public Player get(CommandArgs arguments, List<? extends Annotation> modifiers) throws ArgumentException, ProvisionException {
        Player player = Bukkit.getPlayer(arguments.next());

        if (player == null) {
            throw new ArgumentParseException("Player not found.");
        }

        return player;
    }

    @Override
    public List<String> getSuggestions(String prefix) {
        List<String> suggestions = new ArrayList<>();

        for (Player player : Bukkit.getOnlinePlayers()) {
            String pName = player.getName();

            if (pName.startsWith(Pattern.quote(prefix))) {
                suggestions.add(pName);
            }
        }

        return suggestions;
    }
}
