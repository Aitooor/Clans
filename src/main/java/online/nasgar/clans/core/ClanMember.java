package online.nasgar.clans.core;

import online.nasgar.clans.lang.Lang;
import org.bukkit.Bukkit;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

@SerializableAs("ClanMember")
public class ClanMember implements Cloneable, ConfigurationSerializable {
    private final UUID uuid;
    private String name;
    private Date joinDate;
    private boolean clanPvp;
    private String status;
    private Clan clan;
    private boolean clanChatToggled;

    public ClanMember(Player p, Clan c) {
        this.uuid = p.getUniqueId();
        this.joinDate = new Date();
        this.status = "member";
        this.name = p.getName();
        this.clanPvp = false;
        this.clan = c;
        Clans.addMember(this);
    }

    public ClanMember(UUID u, String name, Clan c) {
        this.uuid = u;
        this.joinDate = new Date();
        this.status = "member";
        this.name = name;
        this.clanPvp = false;
        this.clan = c;
        Clans.addMember(this);
    }

    public static ClanMember deserialize(Map<String, Object> map, Clan clanRef) {
        UUID uuid = UUID.fromString((String) map.get("uuid"));
        String name = (String) map.get("name");
        //UUID clanId = UUID.fromString((String)map.get("clan"));
        String status = (String) map.get("status");
        boolean clanpvp = Boolean.parseBoolean((String) map.get("clanpvp"));
        Date joinDate = new Date(Long.parseLong((String) map.get("joindate")));
        ClanMember result = new ClanMember(uuid, name, clanRef);
        result.setStatus(status);
        result.setClanPvp(clanpvp);
        result.setJoinDate(joinDate);
        return result;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(Date joinDate) {
        this.joinDate = joinDate;
    }

    public boolean isClanPvp() {
        return clanPvp;
    }

    public void setClanPvp(boolean clanPvp) {
        this.clanPvp = clanPvp;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Clan getClan() {
        return clan;
    }

    public void setClan(Clan clan) {
        this.clan = clan;
    }

    public boolean isClanChatToggled() {
        return this.clanChatToggled;
    }

    public void setClanChatToggled(boolean value) {
        this.clanChatToggled = value;
    }

    public void toggleChat() {
        setClanChatToggled(!isClanChatToggled());
        getPlayer().sendMessage(Lang.getLang("clan_chat_toggled_" + (isClanChatToggled() ? "on" : "off")));
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(this.uuid);
    }

    public void setAsLeader() {
        this.status = "leader";
        clan.setLeader(this);
    }

    public boolean isLeader() {
        return status.equals("leader");
    }

    public boolean isModerator() {
        return this.status.equals("moderator");
    }

    public void setModerator() {
        this.status = "moderator";
    }

    public void setMember() {
        this.status = "member";
    }

    @Override
    public @NotNull Map<String, Object> serialize() {
        LinkedHashMap<String, Object> result = new LinkedHashMap<String, Object>();
        result.put("name", name);
        result.put("uuid", uuid.toString());
        result.put("status", status);
        result.put("joindate", "" + joinDate.getTime());
        result.put("clanpvp", Boolean.toString(clanPvp));
        result.put("clan", clan.getId().toString());
        return result;
    }
}
