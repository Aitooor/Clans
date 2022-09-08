package online.nasgar.clans.events;

import online.nasgar.clans.core.ClanMember;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatEvent implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerChat(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();
        ClanMember member = online.nasgar.clans.core.Clans.getMember(p.getUniqueId());

        String message = e.getMessage();

        if (member == null) return;

        if (member.isClanChatToggled()) {
            e.setCancelled(true);
            member.getClan().clanMessage(member, message);
        } else if (message.charAt(0) == ';') {
            e.setCancelled(true);
            member.getClan().clanMessage(message.substring(1));
        }
    }
}
