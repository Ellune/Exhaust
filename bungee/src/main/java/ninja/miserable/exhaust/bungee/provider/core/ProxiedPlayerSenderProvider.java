package ninja.miserable.exhaust.bungee.provider.core;

import com.google.common.collect.ImmutableList;
import com.sk89q.intake.argument.ArgumentException;
import com.sk89q.intake.argument.CommandArgs;
import com.sk89q.intake.parametric.Provider;
import com.sk89q.intake.parametric.ProvisionException;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.lang.annotation.Annotation;
import java.util.List;

import javax.annotation.Nullable;

public class ProxiedPlayerSenderProvider implements Provider<ProxiedPlayer> {

    public static final ProxiedPlayerSenderProvider INSTANCE = new ProxiedPlayerSenderProvider(CommandSenderProvider.INSTANCE);

    private final CommandSenderProvider commandSenderProvider;

    public ProxiedPlayerSenderProvider(CommandSenderProvider commandSenderProvider) {
        this.commandSenderProvider = commandSenderProvider;
    }

    @Override
    public boolean isProvided() {
        return true;
    }

    @Nullable
    @Override
    public ProxiedPlayer get(CommandArgs arguments, List<? extends Annotation> modifiers) throws ArgumentException, ProvisionException {
        CommandSender commandSender = commandSenderProvider.get(arguments, modifiers);

        if (!(commandSender instanceof ProxiedPlayer)) {
            throw new ProvisionException("Sender is not a player.");
        }

        return (ProxiedPlayer) commandSender;
    }

    @Override
    public List<String> getSuggestions(String prefix) {
        return ImmutableList.of();
    }

}
