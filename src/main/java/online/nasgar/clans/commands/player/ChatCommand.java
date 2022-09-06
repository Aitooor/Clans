package online.nasgar.clans.commands.player;

import online.nasgar.clans.Clans;
import online.nasgar.clans.commands.ACommand;
import online.nasgar.clans.commands.CommandException;
import online.nasgar.clans.commands.CommandManager;
import online.nasgar.clans.core.ClanMember;
import online.nasgar.clans.lang.Lang;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ChatCommand extends ACommand {

    protected static final boolean consoleExecutable = false;

    public ChatCommand(CommandSender sender, String[] args, Clans plugin) {
        super(sender, args, plugin);
    }

    @Override
    public boolean execute() throws Exception {
        Player p = (Player) sender;
        ClanMember member = online.nasgar.clans.core.Clans.getMember(p.getUniqueId());

        if (!consoleExecutable && !(sender instanceof Player)) {
            throw new CommandException(Lang.getLang("must_be_player"));
        }

        if (member == null) {
            throw new CommandException(Lang.getLang("not_in_clan"));
        }

        if (args.length < 2) {
            member.toggleChat();
            return true;
        }

        StringBuilder msg = new StringBuilder();
        for (int i = 1; i < args.length; i++) {
            msg.append(args[i]).append(" ");
        }
        msg = new StringBuilder(CommandManager.stripColor(msg.toString()).trim());
        member.getClan().clanMessage(member, msg.toString());
        return false;
    }

    @Override
    public boolean executeAsync() throws CommandException {
        return false;
    }

}
