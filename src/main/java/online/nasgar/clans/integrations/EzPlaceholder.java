package online.nasgar.clans.integrations;

import org.bukkit.entity.Player;

import online.nasgar.clans.Clans;
import online.nasgar.clans.core.Clan;
import online.nasgar.clans.core.ClanMember;
import online.nasgar.clans.lang.Lang;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.md_5.bungee.api.ChatColor;

public class EzPlaceholder extends PlaceholderExpansion {
	
    private Clans plugin;


    public EzPlaceholder(Clans plugin){
        this.plugin = plugin;
    }
    
    @Override
    public boolean persist(){
        return true;
    }


    public boolean canRegister(){
        return true;
    }


    @Override
    public String getAuthor(){
        return plugin.getDescription().getAuthors().toString();
    }


    @Override
    public String getIdentifier(){
        return "clans";
    }


    @Override
    public String getVersion(){
        return plugin.getDescription().getVersion();
    }


    @Override
    public String onPlaceholderRequest(Player player, String identifier){

        if(player == null){
            return "";
        }
        
        //%clans_clan_name%
        else if(identifier.equals("clan_name")){
			ClanMember member = online.nasgar.clans.core.Clans.getMember(player.getUniqueId());
			if (member != null) {
				Clan clan = member.getClan();
				return Lang.colorString(clan.getName());
			}
			else {
				return "";
			}
        }

	    // %clans_clan_tag%
		else if(identifier.equals("clan_tag")){
	    	ClanMember member = online.nasgar.clans.core.Clans.getMember(player.getUniqueId());
			if (member != null) {
				Clan clan = member.getClan();
				return Lang.colorString(clan.getTag());
			}
			else {
				return "";
			}
	    }
	    
	 // %clans_clan_online%
	    else if(identifier.equals("clan_online")){
	    	ClanMember member = online.nasgar.clans.core.Clans.getMember(player.getUniqueId());
			if (member != null) {
				Clan clan = member.getClan();
				return clan.getOnlineList().size()+"";
			}
			else {
				return "";
			}
	    }
	    
	 // %clans_clan_size%
	    else if(identifier.equals("clan_size")){
	    	ClanMember member = online.nasgar.clans.core.Clans.getMember(player.getUniqueId());
			if (member != null) {
				Clan clan = member.getClan();
				return clan.getMembers().size()+"";
			}
			else {
				return "";
			}
	    }
	    
	 // %clans_clan_maxsize%
	    else if(identifier.equals("clan_maxsize")){
	    	ClanMember member = online.nasgar.clans.core.Clans.getMember(player.getUniqueId());
			if (member != null) {
				Clan clan = member.getClan();
				return online.nasgar.clans.core.Clans.getMaxMembers(clan.getLevel())+"";
			}
			else {
				return "";
			}
	    }
	    
	 // %clans_clan_leader%
	    else if(identifier.equals("clan_leader")){
	    	ClanMember member = online.nasgar.clans.core.Clans.getMember(player.getUniqueId());
			if (member != null) {
				Clan clan = member.getClan();
				return clan.getLeader().getName();
			}
			else {
				return "";
			}
	    }
	    
	 // %clans_clan_bank%
	    else if(identifier.equals("clan_bank")){
	    	ClanMember member = online.nasgar.clans.core.Clans.getMember(player.getUniqueId());
			if (member != null) {
				Clan clan = member.getClan();
				return clan.getBank()+"";
			}
			else {
				return "";
			}
	    }
        
     // %clans_clan_maxbank%
	    else if(identifier.equals("clan_maxbank")){
	    	ClanMember member = online.nasgar.clans.core.Clans.getMember(player.getUniqueId());
			if (member != null) {
				Clan clan = member.getClan();
				return online.nasgar.clans.core.Clans.getMaxBank(clan.getLevel())+"";
			}
			else {
				return "";
			}
	    }
        
     // %clans_clan_level%
	    else if(identifier.equals("clan_level")){
	    	ClanMember member = online.nasgar.clans.core.Clans.getMember(player.getUniqueId());
			if (member != null) {
				Clan clan = member.getClan();
				return clan.getLevel()+"";
			}
			else {
				return "";
			}
	    }
        
     // %clans_clan_pvp%
	    else if(identifier.equals("clan_pvp")){
	    	ClanMember member = online.nasgar.clans.core.Clans.getMember(player.getUniqueId());
			if (member != null) {
				if (member.isClanPvp()) {
					return ChatColor.translateAlternateColorCodes('&', "&a+");
				}
				else {
					return ChatColor.translateAlternateColorCodes('&', "&c-");
				}
			}
			else {
				return "";
			}
	    }

		// %clans_clan_color%
		else if(identifier.equals("clan_color")) {
			ClanMember member = online.nasgar.clans.core.Clans.getMember(player.getUniqueId());
			if (member != null) {
				String color = member.getClan().getColor();
				if (!color.isEmpty())
					return String.format("{%s}◆", member.getClan().getColor());
				else
					return "◆";
			}
			else {
				return "";
			}
		}
        
        return null;
    }
}
