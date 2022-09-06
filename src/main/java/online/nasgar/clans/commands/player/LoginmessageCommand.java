package online.nasgar.clans.commands.player;

import online.nasgar.clans.Clans;
import online.nasgar.clans.commands.ACommand;
import online.nasgar.clans.commands.CommandException;
import online.nasgar.clans.core.ClanMember;
import online.nasgar.clans.lang.Lang;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LoginmessageCommand extends ACommand {
    public LoginmessageCommand(CommandSender sender, String[] args, Clans plugin) {
        super(sender, args, plugin);
    }

    @Override
    public boolean execute() throws Exception {
        if (!consoleExecutable && !(sender instanceof Player)) {
            throw new CommandException(Lang.getLang("must_be_player"));
        }
        Player p = (Player) sender;
        ClanMember member = online.nasgar.clans.core.Clans.getMember(p.getUniqueId());
        if (member == null) {
            throw new CommandException(Lang.getLang("not_in_clan"));
        }
        if (!member.isLeader() && !member.isModerator()) {
            throw new CommandException(Lang.getLang("not_leader_or_moderator"));
        }
        if (args.length < 2) {
            member.getClan().setLoginMessage("");
            p.sendMessage(Lang.getLang("loginmessage_removed"));
        } else {
            String message = "";
            for (int i = 1; i < args.length; i++) {
                if (i == args.length - 1)
                    message += args[i];
                else
                    message += args[i] + " ";
            }
            message = Lang.colorString(message);
            if (message.length() > online.nasgar.clans.core.Clans.maxLoginMessageLength) {
                throw new CommandException(String.format(Lang.getLang("loginmessage_toolong"), online.nasgar.clans.core.Clans.maxLoginMessageLength + ""));
            }
            member.getClan().setLoginMessage(message);
            p.sendMessage(Lang.getLang("loginmessage_set") + message);
        }
        return false;
    }

    @Override
    public boolean executeAsync() throws CommandException {
        return false;
    }
}
