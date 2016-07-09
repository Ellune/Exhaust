package ninja.miserable.exhaust.example.command;

import com.sk89q.intake.Command;
import com.sk89q.intake.argument.CommandContext;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class DemoCommands {

    @Command(
        aliases = {"demo", "exhaust"},
        desc = "Exhaust demo command"
    )
    public void demo(final CommandSender sender, final CommandContext args) {
        sender.sendMessage(ChatColor.RED + "You gave me " + ChatColor.BOLD + args.argsLength() + ChatColor.RESET + ChatColor.RED + " arguments.");
        if (args.argsLength() > 1) {
            sender.sendMessage("    " + args.getJoinedStrings(0));
        }
    }
}
