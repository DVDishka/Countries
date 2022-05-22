package dvdcraft.countries.executorsHandlers;

import dvdcraft.countries.common.Classes.Country;
import dvdcraft.countries.common.Classes.Territory;
import dvdcraft.countries.common.CommonVariables;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
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
                if (CommonVariables.requests.containsKey(player.getName())) {
                    flag += 2;
                }
                if (country == null) {
                    flag -= 3;
                }
                if (flag == 0) {
                    return Arrays.asList("create", "get", "status", "leave");
                }
                if (flag == 1) {
                    return Arrays.asList("create", "get", "status", "leave", "edit");
                }
                if (flag == 2) {
                    return Arrays.asList("create", "get", "status", "leave", "reply");
                }
                if (flag == 3) {
                    return Arrays.asList("create", "get", "status", "leave", "edit", "reply");
                }
                if (flag == -3) {
                    return Arrays.asList("create", "get");
                }
                if (flag == -1) {
                    return Arrays.asList("create", "get", "reply");
                }
            }

            if (args.length == 2) {
                if (args[0].equals("edit")) {
                    return Arrays.asList("color", "member", "territory", "delete", "leader");
                } else if (args[0].equals("get")) {
                    return Arrays.asList("countries");
                } else if (args[0].equals("reply")) {
                    return Arrays.asList("yes", "no");
                } else {
                    return Arrays.asList("");
                }
            }

            if (args.length == 3) {
                if (args[1].equals("territory") || args[1].equals("member")) {
                    return Arrays.asList("add", "remove");
                } else if (args[1].equals("color") || args[1].equals("leader")) {
                    return Arrays.asList("set");
                } else {
                    return Arrays.asList("");
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
                } else if (args[1].equals("member") && args[2].equals("remove")) {
                    ArrayList<String> list = new ArrayList<>();
                    Country country = Country.getCountry(sender.getName());
                    if (country == null) {
                        list.add("");
                        return list;
                    }
                    for (String member : country.getMembers()) {
                        if (!member.equals(sender.getName())){
                            list.add(member);
                        }
                    }
                    return list;
                } else if (args[1].equals("territory") && args[2].equals("remove")) {
                    ArrayList<String> list = new ArrayList<>();
                    Country country = Country.getCountry(sender.getName());
                    if (country == null) {
                        list.add("");
                        return list;
                    }
                    for (Territory territory : country.getTerritories()) {
                        list.add(String.valueOf(territory.getFromX()) + " " + String.valueOf(territory.getFromZ()) + " " +
                                String.valueOf(territory.getToX()) + " " + String.valueOf(territory.getToZ()));
                    }
                    return list;
                } else if (args[1].equals("color") && args[2].equals("set")) {
                    return Arrays.asList("RED", "BLUE", "BLACK", "AQUA", "GREEN", "WHITE", "YELLOW", "GRAY", "GOLD", "DARK_PURPLE", "LIGHT_PURPLE", "DARK_AQUA", "DARK_GREEN", "DARK_RED", "DARK_GRAY", "DARK_BLUE");
                } else {
                    return Arrays.asList("");
                }
            }
            if (args.length > 4) {
                return Arrays.asList("");
            }
        }
        return null;
    }
}
