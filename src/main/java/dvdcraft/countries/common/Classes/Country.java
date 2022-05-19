package dvdcraft.countries.common.Classes;

import dvdcraft.countries.common.CommonVariables;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.entity.Player;

import java.io.Serializable;
import java.util.HashSet;

public class Country implements Serializable {
    private String name;
    private String leader;
    private HashSet<String> members = new HashSet<String>();
    private HashSet<Territory> territories = new HashSet<Territory>();
    private Color color;
    private ChatColor chatColor;

    public Country(String name) {
        this.name = name;
        this.color = Color.WHITE;
        this.chatColor = ChatColor.WHITE;
    }

    public Country(String name, Player leader, HashSet<String> members, HashSet<Territory> territories, Color color, ChatColor chatColor) {
        this.name = name;
        this.leader = leader.getName();
        this.members = members;
        this.territories = territories;
        this.color = color;
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

    public boolean setColor(String colorName) {
        switch (colorName) {
            case "RED":
                color = Color.RED;
                chatColor = ChatColor.RED;
                return true;
            case "BLUE":
                color = Color.BLUE;
                chatColor = ChatColor.BLUE;
                return true;
            case "BLACK":
                color = Color.BLACK;
                chatColor = ChatColor.BLACK;
                return true;
            case "AQUA":
                color = Color.AQUA;
                chatColor = ChatColor.AQUA;
                return true;
            case "GREEN":
                color = Color.GREEN;
                chatColor = ChatColor.GREEN;
                return true;
            case "WHITE":
                color = Color.WHITE;
                chatColor = ChatColor.WHITE;
                return true;
            case "YELLOW":
                color = Color.YELLOW;
                chatColor = ChatColor.YELLOW;
                return true;
            case "GRAY":
                color = Color.GRAY;
                chatColor = ChatColor.GRAY;
                return true;
            case "ORANGE":
                color = Color.ORANGE;
                chatColor = ChatColor.GOLD;
                return true;
            case "PURPLE":
                color = Color.PURPLE;
                chatColor = ChatColor.DARK_PURPLE;
                return true;
            default:
                return false;
        }
    }

    public String getName() {
        return this.name;
    }

    public Color getColor() {
        return this.color;
    }

    public ChatColor getChatColor() {
        return this.chatColor;
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
