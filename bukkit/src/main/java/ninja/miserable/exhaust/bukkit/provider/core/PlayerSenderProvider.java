package ninja.miserable.exhaust.bukkit.provider.core;

import com.google.common.collect.ImmutableList;
import com.sk89q.intake.argument.ArgumentException;
import com.sk89q.intake.argument.CommandArgs;
import com.sk89q.intake.parametric.Provider;
import com.sk89q.intake.parametric.ProvisionException;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.lang.annotation.Annotation;
import java.util.List;

import javax.annotation.Nullable;

public class PlayerSenderProvider implements Provider<Player> {

    public static final PlayerSenderProvider INSTANCE = new PlayerSenderProvider(CommandSenderProvider.INSTANCE);

    private final CommandSenderProvider commandSenderProvider;

    public PlayerSenderProvider(CommandSenderProvider commandSenderProvider) {
        this.commandSenderProvider = commandSenderProvider;
    }

    @Override
    public boolean isProvided() {
        return true;
    }

    @Nullable
    @Override
    public Player get(CommandArgs arguments, List<? extends Annotation> modifiers) throws ArgumentException, ProvisionException {
        CommandSender commandSender = commandSenderProvider.get(arguments, modifiers);

        if (!(commandSender instanceof Player)) {
            throw new ProvisionException("Sender is not a player.");
        }

        return (Player) commandSender;
    }

    @Override
    public List<String> getSuggestions(String prefix) {
        return ImmutableList.of();
    }
}
