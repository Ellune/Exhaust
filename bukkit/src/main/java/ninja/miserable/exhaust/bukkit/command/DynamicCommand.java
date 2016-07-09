package ninja.miserable.exhaust.bukkit.command;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginIdentifiableCommand;
import org.bukkit.plugin.Plugin;

import java.util.Arrays;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * A dynamically registered {@link org.bukkit.command.Command}.
 */
public class DynamicCommand extends org.bukkit.command.Command implements PluginIdentifiableCommand {

    protected final CommandExecutor owner;
    protected final CommandManager manager;
    protected final Plugin plugin;

    /** Register the {@link DynamicCommand} type with the Bukkit {@link HelpTopicFactory}. */
    static {
        Bukkit.getServer().getHelpMap().registerHelpTopicFactory(DynamicCommand.class, new DynamicCommandHelpTopic.Factory());
    }

    public DynamicCommand(String[] aliases, String description, String usage, CommandExecutor owner, CommandManager manager, Plugin plugin) {
        super(aliases[0], description, usage, Arrays.asList(aliases));

        checkNotNull(owner, "owner");
        checkNotNull(manager, "manager");
        checkNotNull(plugin, "plugin");

        this.owner = owner;
        this.plugin = plugin;
        this.manager = manager;
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        return this.owner.onCommand(sender, this, label, args);
    }

    @Override
    public Plugin getPlugin() {
        return this.plugin;
    }

    public CommandManager getManager() {
        return this.manager;
    }
}
