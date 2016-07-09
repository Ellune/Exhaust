/*
 * WorldEdit, a Minecraft world manipulation toolkit
 * Copyright (C) sk89q <http://www.sk89q.com>
 * Copyright (C) WorldEdit team and contributors
 *
 * This program is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package net.ellune.exhaust.bukkit.command;

import com.google.common.base.Joiner;
import com.sk89q.intake.CommandException;
import com.sk89q.intake.CommandMapping;
import com.sk89q.intake.InvalidUsageException;
import com.sk89q.intake.InvocationCommandException;
import com.sk89q.intake.argument.Namespace;
import com.sk89q.intake.util.auth.AuthorizationException;
import com.sk89q.intake.util.auth.Authorizer;
import net.ellune.exhaust.bukkit.provider.BukkitModule;
import net.ellune.exhaust.bukkit.provider.core.CommandSenderProvider;
import net.ellune.exhaust.bukkit.provider.core.PlayerProvider;
import net.ellune.exhaust.bukkit.provider.core.PlayerSenderProvider;
import net.ellune.exhaust.command.AbstractCommandManager;
import net.ellune.exhaust.command.AbstractDefaultExecutor;
import net.ellune.exhaust.command.ParameterContainer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Field;
import java.util.Collections;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * A Bukkit-oriented command manager.
 */
public class CommandManager extends AbstractCommandManager {

    /** A reference to the Bukkit {@link Plugin}. */
    private final Plugin plugin;
    /** The default command executor available to use. */
    private DefaultExecutor defaultExecutor;
    /** If there is an issue grabbing Bukkit's CommandMap, we create our own to use in our fallback command manager listener. */
    private CommandMap fallbackCommandMap;

    public CommandManager(@Nonnull final Plugin plugin) {
        super(new ParameterContainer<>(
            CommandSender.class,
            Player.class,
            Server.class,
            CommandSenderProvider.INSTANCE,
            PlayerSenderProvider.INSTANCE,
            PlayerProvider.INSTANCE,
            plugin.getServer()
        ));

        this.plugin = checkNotNull(plugin);
        this.injector.install(new BukkitModule());
        this.builder.setAuthorizer(new Authorizer() {
            @Override
            public boolean testPermission(Namespace namespace, String permission) {
                return checkNotNull(namespace.get(CommandSender.class), "Current sender not available.").hasPermission(permission);
            }
        });
    }

    /**
     * Once all of our commands have been registered, register all commands as dynamic commands with Bukkit.
     */
    @Override
    public final void build() {
        for (CommandMapping command : this.dispatcher.getCommands()) {
            DynamicCommand dynamic = new DynamicCommand(
                command.getAllAliases(),
                command.getDescription().getShortDescription(),
                command.getDescription().getUsage(),
                this.plugin, // owner
                this,
                this.plugin // plugin
            );
            this.getCommandMap().register(this.plugin.getDescription().getName(), dynamic);
        }
    }

    /**
     * If you don't wish to create your own executor, you may be lazy and use the default one.
     *
     * @return a new {@link DefaultExecutor}
     */
    @Override
    public DefaultExecutor getDefaultExecutor() {
        if (this.defaultExecutor == null) {
            this.defaultExecutor = new DefaultExecutor(this);
        }

        return this.defaultExecutor;
    }


    /** @author zml2008 */
    private CommandMap getCommandMap() {
        CommandMap map = getField(this.plugin.getServer().getPluginManager(), "commandMap");
        if (map == null) {
            if (this.fallbackCommandMap != null) {
                map = this.fallbackCommandMap;
            } else {
                this.plugin.getLogger().severe("Could not retrieve server CommandMap, using fallback instead!");
                this.fallbackCommandMap = map = new SimpleCommandMap(Bukkit.getServer());
                Bukkit.getServer().getPluginManager().registerEvents(new FallbackCommandManager(this.fallbackCommandMap), this.plugin);
            }
        }

        return map;
    }

    /** @author zml2008 */
    private static <T> T getField(Object from, String name) {
        Class<?> clazz = from.getClass();
        do {
            try {
                Field field = clazz.getDeclaredField(name);
                field.setAccessible(true);
                return (T) field.get(from);
            } catch (IllegalAccessException | NoSuchFieldException e) {
                // ignore
            }
        } while (clazz.getSuperclass() != Object.class && ((clazz = clazz.getSuperclass()) != null));
        return null;
    }

    /**
     * A default command executor, to be used if the user of the command manager does not wish to use their own.
     */
    public final class DefaultExecutor extends AbstractDefaultExecutor<CommandSender> implements CommandExecutor {

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
                sender.sendMessage(ChatColor.RED + "You don't have permission.");
            } catch (InvocationCommandException e) {
                if (e.getCause() instanceof NumberFormatException) {
                    sender.sendMessage(ChatColor.RED + "Number expected, string received instead.");
                    return true;
                }

                e.printStackTrace();
                sender.sendMessage(ChatColor.RED + "An unexpected error occurred.");
            } catch (InvalidUsageException e) {
                String message = e.getMessage();
                sender.sendMessage(ChatColor.RED + (message != null ? message : "The command was not used properly (no more help available)"));
                sender.sendMessage(ChatColor.RED + "Usage: /" + Joiner.on(" ").join(e.getAliasStack()));
                return true;
            } catch (CommandException e) {
                String message = e.getMessage();
                if (message == null) {
                    message = "An unexpected error occurred.";
                }
                sender.sendMessage(ChatColor.RED + message);
            }

            return true;
        }

        /**
         * Execute a command.
         *
         * @param sender The command sender.
         * @param command The command.
         * @param alias An alias of the command.
         * @param args The raw arguments, in String[] format.
         * @return true
         */
        @Override
        public boolean onCommand(CommandSender sender, Command command, String alias, String[] args) {
            return this.onCommand(sender, alias, args);
        }
    }
}
