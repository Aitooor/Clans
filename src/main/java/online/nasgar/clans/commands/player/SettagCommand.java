package online.nasgar.clans.commands.player;

import online.nasgar.clans.Clans;
import online.nasgar.clans.commands.ACommand;
import online.nasgar.clans.commands.CommandException;
import online.nasgar.clans.commands.CommandManager;
import online.nasgar.clans.core.ClanMember;
import online.nasgar.clans.lang.Lang;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SettagCommand extends ACommand {

    protected static final boolean consoleExecutable = false;

    public SettagCommand(CommandSender sender, String[] args, Clans plugin) {
        super(sender, args, plugin);
    }

    @Override
    public boolean execute() throws Exception {

        if (!consoleExecutable && !(sender instanceof Player)) {
            throw new CommandException(Lang.getLang("must_be_player"));
        }
        if (args.length < 2) {
            throw new CommandException(Lang.getLang("not_enough_args"));
        }
        Player p = (Player) sender;
        ClanMember member = online.nasgar.clans.core.Clans.getMember(p.getUniqueId());
        if (member == null) {
            throw new CommandException(Lang.getLang("not_in_clan"));
        }
        if (!member.isLeader() && !member.isModerator()) {
            throw new CommandException(Lang.getLang("not_leader_or_moderator"));
        }
        String clanTag = "";
        for (int i = 1; i < args.length; i++) {
            clanTag += args[i] + " ";
        }
        clanTag = clanTag.trim();
        if (!CommandManager.legalTag(clanTag)) {
            throw new CommandException(Lang.getLang("illegal_tag"));
        }
        member.getClan().setTag(clanTag);
        member.getClan().clanMessage(String.format(Lang.getLang("tag_changed"), Lang.colorString(clanTag)));

        return false;
    }

    @Override
    public boolean executeAsync() throws CommandException {
        // TODO Auto-generated method stub
        return false;
    }
}
