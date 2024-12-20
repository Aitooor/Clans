package online.nasgar.clans.db;

import online.nasgar.clans.Clans;
import online.nasgar.clans.core.Clan;
import online.nasgar.clans.core.ClanMember;
import online.nasgar.clans.lang.CustomConfig;

import java.util.*;
import java.util.Map.Entry;

public class DBYml implements IDBConnector {

    public CustomConfig clans;

    public DBYml(Clans plugin) {
        clans = new CustomConfig(plugin, "clans.yml");
        clans.reloadCustomConfig(false);
        clans.scheduleBackup(2000, 36000);
    }

    @Override
    public void loadClans() {
        List<HashMap<String, Object>> list = (List<HashMap<String, Object>>) clans.getCustomConfig(false).get("clans");
        Map<UUID, Clan> result = new HashMap<UUID, Clan>();
        if (list != null) {
            for (HashMap<String, Object> clanMap : list) {
                Clan clan = Clan.deserialize(clanMap);
                result.put(clan.getId(), clan);
            }
            refreshMembers(result);
            online.nasgar.clans.core.Clans.setClans(result);
        }
    }

    public void refreshMembers(Map<UUID, Clan> clans) {
        Iterator<Entry<UUID, Clan>> mapIterator = clans.entrySet().iterator();
        HashMap<UUID, ClanMember> members = new HashMap<UUID, ClanMember>();
        while (mapIterator.hasNext()) {
            Entry<UUID, Clan> mapElement = mapIterator.next();
            for (ClanMember m : mapElement.getValue().getMembers()) {
                members.put(m.getUuid(), m);
            }
        }
        online.nasgar.clans.core.Clans.setMembers(members);
    }

    @Override
    public boolean saveClans(Map<UUID, Clan> map) {
        if (map != null && !map.isEmpty()) {
            Iterator<Entry<UUID, Clan>> mapIterator = map.entrySet().iterator();
            ArrayList<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
            while (mapIterator.hasNext()) {
                Entry<UUID, Clan> mapElement = mapIterator.next();
                list.add(mapElement.getValue().serialize());
            }
            clans.getCustomConfig(false).set("clans", list);
            clans.saveCustomConfig();

            return true;
        }

        return false;
    }


}
