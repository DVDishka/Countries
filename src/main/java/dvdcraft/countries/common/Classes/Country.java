package dvdcraft.countries.common.Classes;

import dvdcraft.countries.common.CommonVariables;
import org.bukkit.entity.Player;

import java.io.Serializable;
import java.util.HashSet;

public class Country implements Serializable {
    private String name;
    private String leader;
    private HashSet<String> members = new HashSet<String>();
    private HashSet<Territory> territories = new HashSet<Territory>();

    public Country(String name) {
        this.name = name;
    }

    public Country(String name, Player leader, HashSet<String> members, HashSet<Territory> territories) {
        this.name = name;
        this.leader = leader.getName();
        this.members = members;
        this.territories = territories;
    }

    public void addMember(String name) {
        this.members.add(name);
    }

    public void addTerritory(Territory territory) {
        this.territories.add(territory);
    }

    public void removeMember(String name) {
        this.members.remove(name);
    }

    public void removeTerritory(Territory territory) {
        this.territories.remove(territory);
    }

    public void setLeader(String leader) {
        this.leader = leader;
    }

    public String getName() {
        return this.name;
    }

    public static Country getCountry(String player) {
        for (Country country : CommonVariables.countries) {
            for (String member : country.members) {
                if (member.equals(player)) {
                    return country;
                }
            }
        }
        return null;
    }

    public String getCountryLeader() {
        return this.leader;
    }

    public HashSet<String> getMembers() {
        return this.members;
    }

    public HashSet<Territory> getTerritories() {
        return this.territories;
    }


}
