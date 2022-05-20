package dvdcraft.countries.common.Classes;

import dvdcraft.countries.common.CommonVariables;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

import java.io.Serializable;
import java.util.HashSet;

public class Country implements Serializable {
    private String name;
    private String leader;
    private HashSet<String> members = new HashSet<String>();
    private HashSet<Territory> territories = new HashSet<Territory>();
    private ChatColor chatColor;
    private Team team;

    public Country(String name) {
        this.name = name;
        this.chatColor = ChatColor.WHITE;
        ScoreboardManager scoreboardManager = Bukkit.getScoreboardManager();
        this.team = scoreboardManager.getMainScoreboard().registerNewTeam(name);
        this.team.setPrefix(name + " ");
    }

    public Country(String name, Player leader, HashSet<String> members, HashSet<Territory> territories, ChatColor chatColor) {
        this.name = name;
        this.leader = leader.getName();
        this.members = members;
        this.territories = territories;
        this.chatColor = chatColor;
    }

    public void addMember(String name) {
        this.members.add(name);
        this.team.addPlayer(Bukkit.getPlayer(name));
    }

    public void addTerritory(Territory territory) {
        this.territories.add(territory);
    }

    public void removeMember(String name) {
        this.members.remove(name);
        this.team.removePlayer(Bukkit.getPlayer(name));
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
                chatColor = ChatColor.RED;
                this.team.setColor(chatColor);
                return true;
            case "BLUE":
                chatColor = ChatColor.BLUE;
                this.team.setColor(chatColor);
                return true;
            case "BLACK":
                chatColor = ChatColor.BLACK;
                this.team.setColor(chatColor);
                return true;
            case "AQUA":
                chatColor = ChatColor.AQUA;
                this.team.setColor(chatColor);
                return true;
            case "GREEN":
                chatColor = ChatColor.GREEN;
                this.team.setColor(chatColor);
                return true;
            case "WHITE":
                chatColor = ChatColor.WHITE;
                this.team.setColor(chatColor);
                return true;
            case "YELLOW":
                chatColor = ChatColor.YELLOW;
                this.team.setColor(chatColor);
                return true;
            case "GRAY":
                chatColor = ChatColor.GRAY;
                this.team.setColor(chatColor);
                return true;
            case "GOLD":
                chatColor = ChatColor.GOLD;
                this.team.setColor(chatColor);
                return true;
            case "DARK_PURPLE":
                chatColor = ChatColor.DARK_PURPLE;
                this.team.setColor(chatColor);
                return true;
            case "LIGHT_PURPLE":
                chatColor = ChatColor.LIGHT_PURPLE;
                this.team.setColor(chatColor);
                return true;
            case "DARK_AQUA":
                chatColor = ChatColor.DARK_AQUA;
                this.team.setColor(chatColor);
                return true;
            case "DARK_BLUE":
                chatColor = ChatColor.DARK_BLUE;
                this.team.setColor(chatColor);
                return true;
            case "DARK_GRAY":
                chatColor = ChatColor.DARK_GRAY;
                this.team.setColor(chatColor);
                return true;
            case "DARK_GREEN":
                chatColor = ChatColor.DARK_GREEN;
                this.team.setColor(chatColor);
                return true;
            case "DARK_RED":
                chatColor = ChatColor.DARK_RED;
                this.team.setColor(chatColor);
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
