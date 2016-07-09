package ninja.miserable.exhaust.example;

import ninja.miserable.exhaust.bukkit.command.CommandManager;
import ninja.miserable.exhaust.example.command.DemoCommands;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class DemoPlugin extends JavaPlugin {

    private CommandManager commandManager;

    @Override
    public void onEnable() {
        this.commandManager = new CommandManager(this);
        this.commandManager.builder()
            .registerMethods(new DemoCommands());
        this.commandManager.build();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String alias, String[] args) {
        return this.commandManager.getDefaultExecutor().onCommand(sender, command, alias, args);
    }
}
