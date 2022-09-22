package ru.dvdishka.countries.Classes;

import ru.dvdishka.countries.common.CommonVariables;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;

public class Country implements Serializable {

    private String name;
    private String leader;
    private HashSet<Member> members = new HashSet<>();
    private ChatColor chatColor;
    private ArrayList<Rank> ranks = new ArrayList<>();

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
        for (String member : members) {
            this.members.add(new Member(member));
        }
        this.chatColor = chatColor;
    }

    public void addMember(String name) {
        this.members.add(new Member(name));
        CommonVariables.teams.get(this.name).addPlayer(Bukkit.getPlayer(name));
    }

    public void addMember(Member member) {
        this.members.add(member);
        CommonVariables.teams.get(this.name).addPlayer(Bukkit.getPlayer(member.getName()));
    }

    public void removeMember(String name) {
        for (Member member : members) {
            if (member.getName().equals(name)) {
                members.remove(member);
                break;
            }
        }
        CommonVariables.teams.get(this.name).removePlayer(Bukkit.getOfflinePlayer(name));
    }

    public void removeMember(Member member) {
        this.members.remove(member);
        CommonVariables.teams.get(this.name).removePlayer(Bukkit.getOfflinePlayer(member.getName()));
    }

    public void addRank(Rank rank) {
        for (Rank checkRank : ranks) {
            if (checkRank.getName().equals(rank.getName())) {
                ranks.remove(checkRank);
                break;
            }
        }
        for (Member checkMember : members) {
            if (checkMember.getRank() != null && checkMember.getRank().equals(rank.getName())) {
                checkMember.setRank(rank);
            }
        }
        ranks.add(rank);
    }

    public void removeRank(Rank rank) {

        for (Member member : members) {

            if (member.getRank() == rank) {

                member.setRank(null);
            }
        }

        ranks.remove(rank);
    }

    public void removeRank(String name) {

        for (Member member : members) {

            if (member.getRank() != null && member.getRank().getName().equals(name)) {

                member.setRank(null);
            }
        }

        for (Rank rank : ranks) {
            if (rank.getName().equals(name)) {
                ranks.remove(rank);
                break;
            }
        }
    }

    public boolean containsRank(Rank rank) {
        for (Rank countryRank : ranks) {
            if (rank.getName().equals(countryRank.getName())) {
                return true;
            }
        }
        return false;
    }

    public boolean containsRank(String rank) {
        for (Rank countryRank : ranks) {
            if (rank.equals(countryRank.getName())) {
                return true;
            }
        }
        return false;
    }

    public boolean isLeader(Player player) {
        return this.leader.equals(player.getName());
    }

    public boolean isLeader(String player) {
        return this.leader.equals(player);
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

    public void setRank(String member, Rank rank) {

        for (Member checkMember : members) {
            if (checkMember.getName().equals(member)) {
                checkMember.setRank(rank);
            }
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

            for (Member member : country.members) {

                if (member.getName().equals(player)) {
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

    public HashSet<String> getStringMembers() {
        HashSet<String> stringMembers = new HashSet<>();
        for (Member member : members) {
            stringMembers.add(member.getName());
        }
        return stringMembers;
    }

    public HashSet<Member> getMembers() {
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

    public ArrayList<Rank> getRanks() {
        return this.ranks;
    }

    public Member getMember(String player) {
        for (Member member : members) {
            if (member.getName().equals(player)) {
                return member;
            }
        }
        return null;
    }

    public Member getMember(Player player) {
        for (Member member : members) {
            if (member.getName().equals(player.getName())) {
                return member;
            }
        }
        return null;
    }

    public Rank getRank(String rank) {

        for (Rank checkRank : ranks) {
            if (checkRank.getName().equals(rank)) {
                return checkRank;
            }
        }

        return null;
    }
}
