package online.nasgar.clans.commands.player;

import online.nasgar.clans.Clans;
import online.nasgar.clans.commands.ACommand;
import online.nasgar.clans.commands.CommandException;
import online.nasgar.clans.core.ClanMember;
import online.nasgar.clans.lang.Lang;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ColorCommand extends ACommand {
    public ColorCommand(CommandSender sender, String[] args, Clans plugin) {
        super(sender, args, plugin);
    }

    @Override
    public boolean execute() throws Exception {
        if (!consoleExecutable && !(sender instanceof Player)) {
            throw new CommandException(Lang.getLang("must_be_player"));
        }
        Player p = (Player) sender;
        if (args.length < 2) {
            throw new CommandException(Lang.getLang("not_enough_args"));
        }
        ClanMember member = online.nasgar.clans.core.Clans.getMember(p.getUniqueId());
        if (member == null) {
            throw new CommandException(Lang.getLang("not_in_clan"));
        }
        if (!member.isLeader() && !member.isModerator()) {
            throw new CommandException(Lang.getLang("not_leader_or_moderator"));
        }
        String color = args[1];
        if (Lang.checkHex(color)) {
            member.getClan().setColor(color);
            p.sendMessage(Lang.getLang("color_set"));
        }
        else {
            p.sendMessage(Lang.getLang("color_format"));
        }
        return false;
    }

    @Override
    public boolean executeAsync() throws CommandException {
        return false;
    }
}
