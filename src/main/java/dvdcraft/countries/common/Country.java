package dvdcraft.countries.common;

import org.bukkit.entity.Player;

import java.io.Serializable;
import java.util.ArrayList;
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

    public void addMember(Player member) {
        this.members.add(member.getName());
    }

    public void removeMember(Player member) {
        this.members.remove(member);
    }

    public void removeMember(String name) {
        for (String member : members) {
            if (member.equals("name")) {
                this.members.remove(member);
            }
        }
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
