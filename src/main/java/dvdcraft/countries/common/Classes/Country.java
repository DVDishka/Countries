package dvdcraft.countries.common.Classes;

import dvdcraft.countries.common.CommonVariables;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.HashSet;

public class Country implements Serializable {
    private String name;
    private String leader;
    private HashSet<String> members = new HashSet<>();
    private ChatColor chatColor;

    public Country(String name) {
        this.name = name;
        this.chatColor = ChatColor.WHITE;
        CommonVariables.addTeam(name);
        CommonVariables.teams.get(name).setColor(chatColor);
        CommonVariables.teams.get(name).setPrefix(name + " ");
    }

    public Country(String name, Player leader, HashSet<String> members, ChatColor chatColor) {
        this.name = name;
        this.leader = leader.getName();
        this.members = members;
        this.chatColor = chatColor;
    }

    public void addMember(String name) {
        this.members.add(name);
        CommonVariables.teams.get(this.name).addPlayer(Bukkit.getPlayer(name));
    }

    public void removeMember(String name) {
        this.members.remove(name);
        CommonVariables.teams.get(this.name).removePlayer(Bukkit.getOfflinePlayer(name));
    }

    public void setLeader(String leader) {
        this.leader = leader;
    }

    public boolean setColor(String colorName) {
        switch (colorName) {
            case "RED":
                chatColor = ChatColor.RED;
                CommonVariables.teams.get(this.name).setColor(chatColor);
                return true;
            case "BLUE":
                chatColor = ChatColor.BLUE;
                CommonVariables.teams.get(this.name).setColor(chatColor);
                return true;
            case "BLACK":
                chatColor = ChatColor.BLACK;
                CommonVariables.teams.get(this.name).setColor(chatColor);
                return true;
            case "AQUA":
                chatColor = ChatColor.AQUA;
                CommonVariables.teams.get(this.name).setColor(chatColor);
                return true;
            case "GREEN":
                chatColor = ChatColor.GREEN;
                CommonVariables.teams.get(this.name).setColor(chatColor);
                return true;
            case "WHITE":
                chatColor = ChatColor.WHITE;
                CommonVariables.teams.get(this.name).setColor(chatColor);
                return true;
            case "YELLOW":
                chatColor = ChatColor.YELLOW;
                CommonVariables.teams.get(this.name).setColor(chatColor);
                return true;
            case "GRAY":
                chatColor = ChatColor.GRAY;
                CommonVariables.teams.get(this.name).setColor(chatColor);
                return true;
            case "GOLD":
                chatColor = ChatColor.GOLD;
                CommonVariables.teams.get(this.name).setColor(chatColor);
                return true;
            case "DARK_PURPLE":
                chatColor = ChatColor.DARK_PURPLE;
                CommonVariables.teams.get(this.name).setColor(chatColor);
                return true;
            case "LIGHT_PURPLE":
                chatColor = ChatColor.LIGHT_PURPLE;
                CommonVariables.teams.get(this.name).setColor(chatColor);
                return true;
            case "DARK_AQUA":
                chatColor = ChatColor.DARK_AQUA;
                CommonVariables.teams.get(this.name).setColor(chatColor);
                return true;
            case "DARK_BLUE":
                chatColor = ChatColor.DARK_BLUE;
                CommonVariables.teams.get(this.name).setColor(chatColor);
                return true;
            case "DARK_GRAY":
                chatColor = ChatColor.DARK_GRAY;
                CommonVariables.teams.get(this.name).setColor(chatColor);
                return true;
            case "DARK_GREEN":
                chatColor = ChatColor.DARK_GREEN;
                CommonVariables.teams.get(this.name).setColor(chatColor);
                return true;
            case "DARK_RED":
                chatColor = ChatColor.DARK_RED;
                CommonVariables.teams.get(this.name).setColor(chatColor);
                return true;
            default:
                return false;
        }
    }

    public String getName() {
        return this.name;
    }

    public ChatColor getChatColor() {
        return this.chatColor;
    }

    @Contract(pure = true)
    public static @Nullable Country getCountry(String player) {
        for (Country country : CommonVariables.countries) {
            for (String member : country.members) {
                if (member.equals(player)) {
                    return country;
                }
            }
        }
        return null;
    }

    public static @Nullable Country getCountryByName(String name) {
        for (Country country : CommonVariables.countries) {
            if (country.getName().equals(name)) {
                return country;
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
        HashSet<Territory> territories = new HashSet<>();
        for (Owner owner : CommonVariables.owners.values()) {
            if (Country.getCountry(owner.getName()) == this) {
                territories.add(owner.getTerritory());
            }
        }
        return territories;
    }
}
