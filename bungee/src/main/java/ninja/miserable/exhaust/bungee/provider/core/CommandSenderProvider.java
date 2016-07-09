package ninja.miserable.exhaust.bungee.provider.core;

import com.google.common.collect.ImmutableList;
import com.sk89q.intake.argument.ArgumentException;
import com.sk89q.intake.argument.CommandArgs;
import com.sk89q.intake.parametric.Provider;
import com.sk89q.intake.parametric.ProvisionException;
import net.md_5.bungee.api.CommandSender;

import java.lang.annotation.Annotation;
import java.util.List;

import javax.annotation.Nullable;

public class CommandSenderProvider implements Provider<CommandSender> {

    public static final CommandSenderProvider INSTANCE = new CommandSenderProvider();

    @Override
    public boolean isProvided() {
        return true;
    }

    @Nullable
    @Override
    public CommandSender get(CommandArgs arguments, List<? extends Annotation> modifiers) throws ArgumentException, ProvisionException {
        CommandSender commandSender = arguments.getNamespace().get(CommandSender.class);

        if (commandSender == null) {
            throw new ProvisionException("Sender is not set on the namespace.");
        }

        return commandSender;
    }

    @Override
    public List<String> getSuggestions(String prefix) {
        return ImmutableList.of();
    }

}
