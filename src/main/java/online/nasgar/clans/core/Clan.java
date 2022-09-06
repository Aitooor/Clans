package online.nasgar.clans.core;

import online.nasgar.clans.lang.Lang;
import online.nasgar.clans.logs.EzLogs;
import online.nasgar.clans.utils.InventoryUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.*;

@SerializableAs("Clan")
public class Clan implements Cloneable, ConfigurationSerializable {
    private final UUID id;
    private final Date creationDate;
    private String name;
    private String tag;
    private List<ClanMember> members;
    private ClanMember leader;
    private Location home;
    private boolean disbanded;
    private int bank;
    private int level;
    private Inventory storage;
    private String loginMessage;
    private String color;

    public Clan(String name) {
        super();
        this.name = name;
        this.id = UUID.randomUUID();
        this.tag = "";
        this.members = new ArrayList<ClanMember>();
        this.leader = null;
        this.creationDate = new Date();
        this.home = null;
        this.disbanded = false;
        this.bank = 0;
        this.level = 1;
        this.storage = Bukkit.createInventory(null, 9, Lang.getLang("clan_storage"));
        this.loginMessage = "";
        this.color = "";
    }

    public Clan(UUID id,
                String name,
                String tag,
                List<ClanMember> members,
                ClanMember leader,
                Date creationDate,
                Location home,
                int bank,
                int level,
                Inventory storage,
                String loginMessage,
                String color) {
        super();
        this.id = id;
        this.name = name;
        this.tag = tag == null ? "" : tag;
        this.members = members == null ? new ArrayList<>() : members;
        this.leader = leader;
        this.creationDate = creationDate;
        this.home = home;
        this.disbanded = false;
        this.bank = bank;
        this.level = level;
        this.storage = storage;
        this.loginMessage = loginMessage == null ? "" : loginMessage;
        this.color = color == null ? "" : color;
    }

    public static Clan deserialize(Map<String, Object> map) {
        UUID uuid = UUID.fromString((String) map.get("id"));
        String n = (String) map.get("name");
        String t = (String) map.get("tag");
        Date date = new Date(Long.parseLong((String) map.get("creationdate")));
        UUID leaderUUID = UUID.fromString((String) map.get("leader"));
        String loginMessage = (String) map.get("loginMessage");
        String color = (String) map.get("color");

        int bank = 0;
        Object bankRaw = map.get("bank");
        if (bankRaw != null) {
            bank = Integer.parseInt((String) bankRaw);
        }

        int level = 1;
        Object levelRaw = map.get("level");
        if (levelRaw != null) {
            levelRaw = Integer.parseInt((String) levelRaw);
            level = (int) levelRaw;
        }


        @SuppressWarnings("unchecked")
        List<Map<String, Object>> mapMembers = (List<Map<String, Object>>) map.get("members");
        List<ClanMember> memberList = new ArrayList<ClanMember>();
        Object home_o = map.get("home");
        Location clanHome = null;
        if (home_o instanceof Location) {
            clanHome = (Location) home_o;
        }
        String storageName = Lang.getLang("clan_storage");
        Inventory inv = Bukkit.createInventory(null, level == 2 ? 27 : level == 3 ? 54 : 9, storageName);
        ArrayList<ItemStack> stacks = (ArrayList<ItemStack>) map.get("storage");
        if (stacks != null) {
            for (int i = 0; i < stacks.size() && i < inv.getSize(); i++) {
                inv.setItem(i, stacks.get(i));
            }
        }

        Clan result = new Clan(uuid, n, t, null, null, date, clanHome, bank, level, inv, loginMessage, color);
        ClanMember leaderMember = null;
        for (Map<String, Object> m : mapMembers) {
            ClanMember mem = ClanMember.deserialize(m, result);
            memberList.add(mem);
            if (mem.getUuid().equals(leaderUUID)) {
                leaderMember = mem;
            }
        }
        result.setMembers(memberList);
        result.setLeader(leaderMember);
        return result;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public ClanMember getLeader() {
        return leader;
    }

    public void setLeader(ClanMember member) {
        if (members.contains(member)) {
            if (this.leader != null)
                this.leader.setStatus("member");

            this.leader = member;
            member.setStatus("leader");
        }
    }

    public String getLoginMessage() {
        return loginMessage;
    }

    public void setLoginMessage(String loginMessage) {
        this.loginMessage = loginMessage;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public UUID getId() {
        return id;
    }

    public List<ClanMember> getMembers() {
        return members;
    }

    public void setMembers(List<ClanMember> members) {
        this.members = members;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public Location getHome() {
        return home;
    }

    public void setHome(Location home) {
        this.home = home.clone();
    }

    public int clanSize() {
        return members.size();
    }

    public ClanMember addPlayer(Player p) {
        ClanMember member = new ClanMember(p, this);
        members.add(member);
        return member;
    }

    public boolean addMember(ClanMember clanMember) {
        if (clanMember.getUuid().equals(leader.getUuid()) ||
                members.stream().anyMatch(m -> m.getUuid().equals(clanMember.getUuid()))) {
            return false;
        }

        members.add(clanMember);
        return true;
    }

    public ClanMember getMemberByName(String name) {
        return members.stream().filter(m -> m.getName().equals(name)).findAny().orElse(null);
    }

    public boolean removeMember(ClanMember member) {
        int index = members.indexOf(member);
        if (index >= 0) {
            members.remove(index);
            Clans.removeMember(member);
            return true;
        }
        return false;
    }

    public boolean isDisbanded() {
        return disbanded;
    }

    public void setDisbanded(boolean disbanded) {
        this.disbanded = disbanded;
    }

    public int getBank() {
        return bank;
    }

    public void setBank(int bank) {
        this.bank = bank;
    }

    public void withdraw(int amt) {
        bank = amt > bank ? 0 : bank - amt;
    }

    public void deposit(int amt) {
        bank = bank + amt;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public Inventory getStorage() {
        return storage;
    }

    public String getMemberList() {
        StringBuilder result = new StringBuilder();
        for (ClanMember cm : members) {
            // isLeader	= &a&l | isModerator = &a | else = &r
            color = cm.isLeader() ? "&a&l" : cm.isModerator() ? "&a" : "&r";
            result.append(color).append(cm.getName()).append("&r, ");
        }
        return result.toString();
    }

    public String getOnlineString() {
        StringBuilder result = new StringBuilder();
        for (ClanMember cm : members) {
            if (cm.getPlayer() != null && cm.getPlayer().isOnline())
                result.append(cm.getName()).append(" ");
        }

        if (result.length() == 0)
            result = new StringBuilder(Lang.getLang("nobody"));

        return result.toString();
    }

    public List<String> getOnlineList() {
        List<String> result = new LinkedList<>();
        for (ClanMember cm : members) {
            if (cm.getPlayer() != null && cm.getPlayer().isOnline())
                result.add(cm.getName());
        }
        return result;
    }

    public void clanMessage(ClanMember cm, String msg) {
        for (ClanMember member : members) {
            Player p = member.getPlayer();
            if (p != null && p.isOnline())
                p.sendMessage(Lang.colorString(String.format(Clans.clanChatFormat, cm.getName(), msg)));
        }
    }

    public void clanMessage(String msg) {
        for (ClanMember member : members) {
            Player p = member.getPlayer();
            if (p != null && p.isOnline()) {
                p.sendMessage(Lang.colorString(msg));
            }
        }
    }

    public String shortEntry() {
        return String.format(Lang.getLang("list_clan"), name, tag);
    }

    public void upgrade() throws ClanException {
        int aimingLvl = level + 1;
        if (aimingLvl <= 3) {
            int cost = Clans.getClanCost(aimingLvl);
            if (cost <= bank) {
                clanMessage(Lang.getLang("clan_upgrade"));
                bank -= cost;
                EzLogs.logBalance(new ClanMember(null, "LVL" + aimingLvl, this), cost, "UPGRADE");
                upgradeStorage(aimingLvl);
                level = aimingLvl;
            } else {
                throw new ClanException(Lang.getLang("upgrade_cost") + cost + "$");
            }
        } else {
            throw new ClanException(Lang.getLang("upgrade_max"));
        }
    }

    public void closeStorageForAll() {
        List<HumanEntity> viewers = storage.getViewers();
        List<HumanEntity> viewersCopy = new LinkedList<>(viewers);
        for (HumanEntity viewer : viewersCopy)
            viewer.closeInventory();
    }

    private void upgradeStorage(int lvl) {
        Inventory newStorage = lvl == 2 ?
                Bukkit.createInventory(null, 27, Lang.getLang("clan_storage")) : lvl == 3 ?
                Bukkit.createInventory(null, 54, Lang.getLang("clan_storage")) : null;
        closeStorageForAll();

        if (newStorage != null)
            newStorage.setContents(storage.getContents());

        storage = newStorage;
    }

    @Override
    public @NotNull Map<String, Object> serialize() {
        LinkedHashMap<String, Object> result = new LinkedHashMap<String, Object>();
        result.put("id", id.toString());
        result.put("name", name);
        result.put("tag", tag);
        ArrayList<Map<String, Object>> memberMap = new ArrayList<>();
        for (ClanMember cm : members) {
            memberMap.add(cm.serialize());
        }
        result.put("members", memberMap);
        result.put("leader", leader.getUuid().toString());
        result.put("creationdate", "" + creationDate.getTime());
        result.put("home", home == null ? "null" : home);
        result.put("bank", "" + bank);
        result.put("level", "" + level);
        ItemStack[] stacks = storage.getContents();
        for (int i = 0; i < stacks.length; i++) {
            if (!InventoryUtils.testSavable(stacks[i])) {
                stacks[i] = null;
                storage.setItem(i, null);
            }
        }
        result.put("storage", stacks);
        result.put("color", color);
        result.put("loginMessage", loginMessage);
        return result;
    }
}
