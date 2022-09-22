package ru.dvdishka.countries.executorsHandlers;

import ru.dvdishka.countries.Classes.Country;
import ru.dvdishka.countries.Classes.Member;
import ru.dvdishka.countries.Classes.Permission;
import ru.dvdishka.countries.Classes.Rank;
import ru.dvdishka.countries.common.CommonVariables;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class CountryTabCompleter implements org.bukkit.command.TabCompleter {

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {

        if (command.getName().equals("country")) {

            if (args.length == 1) {
                Player player = Bukkit.getPlayer(sender.getName());
                Country country = Country.getCountry(player.getName());
                int flag = 0;
                if (country != null && country.getCountryLeader() != null && country.getCountryLeader().equals(player.getName())) {
                    flag = 1;
                }
                if (country == null) {
                    flag -= 3;
                }
                if (flag == 0) {
                    return Arrays.asList("create", "get", "status", "leave", "territory");
                }
                if (flag == 1) {
                    return Arrays.asList("create", "get", "status", "leave", "edit", "territory");
                }
                if (flag == -3) {
                    return Arrays.asList("create", "get", "territory");
                }
            }

            if (args.length == 2) {
                if (args[0].equals("edit")) {
                    return Arrays.asList("color", "member", "delete", "leader", "friendlyFire", "rank");
                } else if (args[0].equals("get")) {
                    return Arrays.asList("countries", "members");
                } else if (args[0].equals("territory")) {
                    return Arrays.asList("set", "get", "delete", "edit");
                } else if (args[0].equals("create")) {
                    return Arrays.asList("name");
                } else {
                    return Arrays.asList();
                }
            }

            if (args.length == 3) {
                if (args[1].equals("member")) {
                    return Arrays.asList("add", "remove", "rank");
                } else if (args[1].equals("color") || args[1].equals("leader")) {
                    return Arrays.asList("set");
                } else if (args[1].equals("war")) {
                    ArrayList<String> list = new ArrayList<>();
                    for (Country country : CommonVariables.countries) {
                        if (country != Country.getCountry(sender.getName())) {
                            list.add(country.getName());
                        }
                    }
                    return list;
                } else if (args[1].equals("friendlyFire")) {
                    return Arrays.asList("enable", "disable");
                } else if (args[1].equals("rank")) {

                    ArrayList<String> tabCompletions = new ArrayList<>();
                    Country senderCountry = Country.getCountry(sender.getName());

                    if (senderCountry != null) {

                        Member senderMember = senderCountry.getMember(sender.getName());

                        if (senderMember.hasPermission(Permission.CREATE_RANK)) {
                            tabCompletions.add("create");
                        }

                        if (senderMember.hasPermission(Permission.REMOVE_RANK)) {
                            tabCompletions.add("remove");
                        }

                        if (senderMember.hasPermission(Permission.EDIT_RANK)) {

                            for (Rank rank : senderCountry.getRanks()) {

                                tabCompletions.add(rank.getName());
                            }
                        }
                    }

                    tabCompletions.sort(Comparator.naturalOrder());

                    return tabCompletions;

                } else if (args[0].equals("territory") && args[1].equals("set")) {
                    return Arrays.asList("fromX");
                } else if (args[0].equals("territory") && args[1].equals("edit")) {
                    return Arrays.asList("hidden");
                } else if (args[0].equals("get") && args[1].equals("members")) {

                    ArrayList<String> tabCompletions = new ArrayList<>();

                    for (Country country : CommonVariables.countries) {

                        tabCompletions.add(country.getName());
                    }

                    tabCompletions.sort(Comparator.naturalOrder());

                    return tabCompletions;

                } else {

                    return Arrays.asList();
                }
            }

            if (args.length == 4) {

                if (args[1].equals("member") && args[2].equals("add") || args[1].equals("leader") && args[2].equals("set")) {
                    ArrayList<String> list = new ArrayList<>();
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        if (!player.getName().equals(sender.getName())) {
                            list.add(player.getName());
                        }
                    }
                    return list;
                } else if (args[1].equals("member") && args[2].equals("rank")) {
                    return null;
                } else if (args[1].equals("member") && args[2].equals("remove")) {
                    ArrayList<String> list = new ArrayList<>();
                    Country country = Country.getCountry(sender.getName());
                    if (country == null) {
                        return list;
                    }
                    for (String member : country.getStringMembers()) {
                        if (!member.equals(sender.getName())) {
                            list.add(member);
                        }
                    }
                    return list;
                } else if (args[1].equals("color") && args[2].equals("set")) {
                    return Arrays.asList("RED", "BLUE", "BLACK", "AQUA", "GREEN", "WHITE", "YELLOW", "GRAY", "GOLD", "DARK_PURPLE", "LIGHT_PURPLE", "DARK_AQUA", "DARK_GREEN", "DARK_RED", "DARK_GRAY", "DARK_BLUE");
                } else if (args[0].equals("territory") && args[1].equals("set")) {
                    return Arrays.asList("fromZ");
                } else if (args[0].equals("edit") && args[1].equals("rank") && args[2].equals("create")) {
                    return Arrays.asList("rankName");
                } else if (args[0].equals("edit") && args[1].equals("rank") && args[2].equals("remove")) {

                    ArrayList<String> tabCompletions = new ArrayList<>();

                    Country senderCountry = Country.getCountry(sender.getName());

                    if (senderCountry == null) {

                        return Arrays.asList();
                    }

                    Member senderMember = senderCountry.getMember(sender.getName());

                    if (senderMember.hasPermission(Permission.REMOVE_RANK)) {

                        for (Rank rank : senderCountry.getRanks()) {

                            tabCompletions.add(rank.getName());
                        }
                    }

                    tabCompletions.sort(Comparator.naturalOrder());

                    return tabCompletions;

                } else if (args[0].equals("territory") && args[1].equals("edit") && args[2].equals("hidden")) {
                    return Arrays.asList("enable", "disable");
                } else {
                    return Arrays.asList();
                }
            }
            if (args.length == 5) {
                if (args[0].equals("territory") && args[1].equals("set")) {
                    return Arrays.asList("toX");
                } else if (args[1].equals("war")) {
                    ArrayList<String> list = new ArrayList<>();
                    for (Country country : CommonVariables.countries) {
                        if (country != Country.getCountry(sender.getName())) {
                            list.add(country.getName());
                        }
                    }
                    return list;
                } else if (args[1].equals("member") && args[2].equals("rank")) {

                    ArrayList<String> tabCompletions = new ArrayList<>();
                    Country senderCountry = Country.getCountry(sender.getName());

                    if (senderCountry != null) {

                        Member senderMember = senderCountry.getMember(sender.getName());

                        if (senderMember.hasPermission(Permission.SET_PLAYER_RANK)) {

                            for (Rank rank : senderCountry.getRanks()) {

                                tabCompletions.add(rank.getName());
                            }
                        }
                    }

                    tabCompletions.sort(Comparator.naturalOrder());

                    return tabCompletions;
                } else {
                    return Arrays.asList();
                }
            }
            if (args.length == 6) {
                if (args[0].equals("territory") && args[1].equals("set")) {
                    return Arrays.asList("toZ");
                } else {
                    return Arrays.asList();
                }
            }
            if (args.length >= 7) {
                return Arrays.asList();
            }
        }
        return null;
    }
}
