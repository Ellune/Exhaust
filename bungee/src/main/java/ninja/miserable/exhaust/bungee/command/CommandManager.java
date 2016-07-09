package ninja.miserable.exhaust.bungee.command;

import com.google.common.base.Joiner;
import com.sk89q.intake.CommandException;
import com.sk89q.intake.CommandMapping;
import com.sk89q.intake.InvalidUsageException;
import com.sk89q.intake.InvocationCommandException;
import com.sk89q.intake.argument.Namespace;
import com.sk89q.intake.util.auth.AuthorizationException;
import com.sk89q.intake.util.auth.Authorizer;
import ninja.miserable.exhaust.bungee.provider.BungeeModule;
import ninja.miserable.exhaust.bungee.provider.core.CommandSenderProvider;
import ninja.miserable.exhaust.bungee.provider.core.ProxiedPlayerProvider;
import ninja.miserable.exhaust.bungee.provider.core.ProxiedPlayerSenderProvider;
import ninja.miserable.exhaust.command.AbstractCommandManager;
import ninja.miserable.exhaust.command.AbstractDefaultExecutor;
import ninja.miserable.exhaust.command.CommandExecutor;
import ninja.miserable.exhaust.command.ParameterContainer;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;

import java.util.Collections;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;

public class CommandManager extends AbstractCommandManager {

    private static final CommandExecutor<CommandSender> NULL = new CommandExecutor<CommandSender>() {
        @Override
        public boolean onCommand(CommandSender sender, String name, String[] args) {
            return false;
        }
    };

    /** A reference to the BungeeCord {@link Plugin}. */
    private final Plugin plugin;
    private CommandExecutor<CommandSender> executor;
    /** The default command executor available to use. */
    private DefaultExecutor defaultExecutor;

    public CommandManager(@Nonnull final Plugin plugin) {
        this(plugin, NULL);
        this.setExecutor(new CommandExecutor<CommandSender>() {
            @Override
            public boolean onCommand(CommandSender sender, String name, String[] args) {
                return CommandManager.this.getDefaultExecutor().onCommand(sender, name, args);
            }
        });
    }

    public CommandManager(@Nonnull final Plugin plugin, @Nonnull final CommandExecutor<CommandSender> executor) {
        super(new ParameterContainer<>(
            CommandSender.class,
            ProxiedPlayer.class,
            ProxyServer.class,
            CommandSenderProvider.INSTANCE,
            ProxiedPlayerSenderProvider.INSTANCE,
            ProxiedPlayerProvider.INSTANCE,
            plugin.getProxy()
        ));

        checkNotNull(executor, "executor");
        this.plugin = checkNotNull(plugin, "plugin");

        if (executor != NULL) {
            this.setExecutor(executor);
        }

        this.injector.install(new BungeeModule());
        this.builder.setAuthorizer(new Authorizer() {
            @Override
            public boolean testPermission(Namespace namespace, String permission) {
                return checkNotNull(namespace.get(CommandSender.class)).hasPermission(permission);
            }
        });
    }

    private void setExecutor(CommandExecutor<CommandSender> executor) {
        this.executor = executor;
    }

    /**
     * Once all of our commands have been registered, register all commands as dynamic commands with Bukkit.
     */
    @Override
    public final void build() {
        PluginManager pm = this.plugin.getProxy().getPluginManager();
        for (CommandMapping command : this.dispatcher.getCommands()) {
            pm.registerCommand(this.plugin, new DynamicCommand(this.executor, command.getAllAliases()[0], command.getAllAliases()));
        }
    }

    @Override
    public AbstractDefaultExecutor<CommandSender> getDefaultExecutor() {
        if (this.defaultExecutor == null) {
            this.defaultExecutor = new DefaultExecutor(this);
        }

        return this.defaultExecutor;
    }

    /**
     * A default command executor, to be used if the user of the command manager does not wish to use their own.
     */
    public final class DefaultExecutor extends AbstractDefaultExecutor<CommandSender> {

        /** A reference to the command manager. */
        private final CommandManager manager;

        public DefaultExecutor(CommandManager manager) {
            this.manager = manager;
        }

        /**
         * Execute a command.
         *
         * @param sender The command sender.
         * @param name An name of the command.
         * @param args The raw arguments, in String[] format.
         * @return true
         */
        @Override
        public boolean onCommand(CommandSender sender, String name, String[] args) {
            Namespace namespace = new Namespace();
            namespace.put(CommandSender.class, sender);

            try {
                this.manager.dispatcher().call(createArgString(args, name), namespace, Collections.<String>emptyList());
            } catch (AuthorizationException e) {
                sender.sendMessage(
                    new ComponentBuilder("You don't have permission.")
                        .color(ChatColor.RED)
                        .create()
                );
            } catch (InvocationCommandException e) {
                if (e.getCause() instanceof NumberFormatException) {
                    sender.sendMessage(
                        new ComponentBuilder("Number expected, string received instead.")
                            .color(ChatColor.RED)
                            .create()
                    );

                    return true;
                }

                e.printStackTrace();

                sender.sendMessage(
                    new ComponentBuilder("An unexpected error occurred.")
                        .color(ChatColor.RED)
                        .create()
                );
            } catch (CommandException e) {
                if (e instanceof InvalidUsageException) {
                    InvalidUsageException ue = (InvalidUsageException) e;
                    String message = ue.getMessage();
                    sender.sendMessage(
                        new ComponentBuilder(message != null ? message : "The command was not used properly (no more help available)")
                            .color(ChatColor.RED)
                            .create()
                    );

                    sender.sendMessage(
                        new ComponentBuilder("Usage: /" + Joiner.on(" ").join(ue.getAliasStack()))
                            .color(ChatColor.RED)
                            .create()
                    );

                    return true;
                }

                e.printStackTrace();
                sender.sendMessage(
                    new ComponentBuilder("An unexpected error occurred.")
                        .color(ChatColor.RED)
                        .create()
                );
            }

            return true;
        }
    }
}
