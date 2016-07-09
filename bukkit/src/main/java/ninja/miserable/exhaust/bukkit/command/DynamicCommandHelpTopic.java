/*
 * WorldEdit
 * Copyright (C) 2012 sk89q <http://www.sk89q.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package ninja.miserable.exhaust.bukkit.command;

import com.sk89q.intake.CommandMapping;
import com.sk89q.intake.argument.Namespace;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.help.HelpTopic;
import org.bukkit.help.HelpTopicFactory;

/**
 * @author zml2008
 */
public class DynamicCommandHelpTopic extends HelpTopic {

    /** The dynamic command that this help topic is for. */
    protected final DynamicCommand command;

    public DynamicCommandHelpTopic(DynamicCommand command) {
        this.command = command;
        this.name = "/" + command.getName();

        this.shortText = command.getDescription();

        StringBuilder fullText = new StringBuilder();
        fullText.append(ChatColor.BOLD).append(ChatColor.GOLD).append("Usage: ").append(ChatColor.WHITE).append(command.getUsage());
        fullText.append("\n");

        if (command.getAliases().size() > 0) {
            fullText.append(ChatColor.BOLD).append(ChatColor.GOLD).append("Aliases: ").append(ChatColor.WHITE);
            boolean first = true;
            for (String alias : command.getAliases()) {
                if (!first) {
                    fullText.append(", ");
                }

                first = false;
                fullText.append(alias);
            }

            fullText.append("\n");
        }

        fullText.append(command.getDescription());

        this.fullText = fullText.toString();
    }

    @Override
    public boolean canSee(CommandSender sender) {
        Namespace namespace = new Namespace();
        namespace.put(CommandSender.class, sender);

        CommandMapping command = this.command.getManager().dispatcher().get(this.command.getName());
        return command != null && command.getCallable().testPermission(namespace);
    }

    @Override
    public String getFullText(CommandSender sender) {
        if (this.fullText == null || this.fullText.length() == 0) {
            return this.getShortText();
        } else {
            return this.fullText;
        }
    }

    public static class Factory implements HelpTopicFactory<DynamicCommand> {

        @Override
        public HelpTopic createTopic(DynamicCommand command) {
            return new DynamicCommandHelpTopic(command);
        }
    }
}
