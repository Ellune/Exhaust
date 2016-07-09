package ninja.miserable.exhaust.bukkit.provider;

import com.sk89q.intake.argument.ArgumentException;
import com.sk89q.intake.argument.ArgumentParseException;
import com.sk89q.intake.argument.CommandArgs;
import com.sk89q.intake.parametric.Provider;
import com.sk89q.intake.parametric.ProvisionException;
import org.bukkit.Bukkit;
import org.bukkit.World;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.annotation.Nullable;

public class WorldProvider implements Provider<World> {

    @Override
    public boolean isProvided() {
        return false;
    }

    @Nullable
    @Override
    public World get(CommandArgs arguments, List<? extends Annotation> modifiers) throws ArgumentException, ProvisionException {
        World world = Bukkit.getWorld(arguments.next());

        if (world == null) {
            throw new ArgumentParseException("World not found.");
        }

        return world;
    }

    @Override
    public List<String> getSuggestions(String prefix) {
        List<String> suggestions = new ArrayList<>();

        for (World world : Bukkit.getWorlds()) {
            String name = world.getName();

            if (name.startsWith(Pattern.quote(prefix))) {
                suggestions.add(name);
            }
        }

        return suggestions;
    }

}
