package online.nasgar.clans.commands.player;

import online.nasgar.clans.Clans;
import online.nasgar.clans.commands.ACommand;
import online.nasgar.clans.commands.CommandException;
import online.nasgar.clans.core.ClanMember;
import online.nasgar.clans.lang.Lang;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PlayerCommand extends ACommand {
    public PlayerCommand(CommandSender sender, String[] args, Clans plugin) {
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
        ClanMember member = online.nasgar.clans.core.Clans.getMemberByName(args[1]);
        if (member == null) {
            throw new CommandException(Lang.getLang("player_not_found"));
        }
        String tag = member.getClan().getTag();
        String name = member.getClan().getName();
        String message = String.format(Lang.getLang("in_clan"), member.getName(), Lang.colorString(tag), Lang.colorString(name));
        p.sendMessage(message);
        return false;
    }

    @Override
    public boolean executeAsync() throws CommandException {
        return false;
    }
}
