package online.nasgar.clans.events;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import online.nasgar.clans.core.ClanMember;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import online.nasgar.clans.Clans;

public class ChatEvent implements Listener {
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerChat(AsyncChatEvent e) {
		Player p = e.getPlayer();
		ClanMember member = online.nasgar.clans.core.Clans.getMember(p.getUniqueId());

		PlainTextComponentSerializer plainSerializer = PlainTextComponentSerializer.plainText();
		String message = plainSerializer.serialize(e.message());

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
