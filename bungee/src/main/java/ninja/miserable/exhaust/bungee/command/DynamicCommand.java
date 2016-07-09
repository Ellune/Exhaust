package ninja.miserable.exhaust.bungee.command;

import ninja.miserable.exhaust.command.CommandExecutor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class DynamicCommand extends Command {

    private final CommandExecutor<CommandSender> executor;

    public DynamicCommand(CommandExecutor<CommandSender> executor, String name, String... aliases) {
        super(name, null, aliases);
        this.executor = executor;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        this.executor.onCommand(sender, this.getName(), args);
    }
}
