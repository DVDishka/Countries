package dvdcraft.countries.executorsHandlers;

import dvdcraft.countries.common.Classes.Country;
import dvdcraft.countries.common.Classes.Territory;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CountryTabCompleter implements org.bukkit.command.TabCompleter {

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {

        if (command.getName().equals("country")) {

            if (args.length == 0) {
                return Arrays.asList("country");
            }

            if (args.length == 1) {
                return Arrays.asList("color", "create", "get", "member", "status", "territory");
            }

            if (args.length == 2) {
                if (args[0].equals("territory") | args[0].equals("member")) {
                    return Arrays.asList("add", "remove");
                } else if (args[0].equals("get")) {
                    return null;
                } else if (args[0].equals("color")) {
                    return Arrays.asList("set");
                } else {
                    return Arrays.asList("");
                }
            }
            if (args.length == 3) {
                if (args[0].equals("member") && args[1].equals("add")) {
                    return null;
                } else if (args[0].equals("member") && args[1].equals("remove")) {
                    ArrayList<String> list = new ArrayList<String>();
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
                } else if (args[0].equals("territory") && args[1].equals("remove")) {
                    ArrayList<String> list = new ArrayList<String>();
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
                } else if (args[0].equals("color") && args[1].equals("set")) {
                    return Arrays.asList("RED", "BLUE", "BLACK", "AQUA", "GREEN", "WHITE", "YELLOW", "GRAY", "GOLD", "DARK_PURPLE", "LIGHT_PURPLE", "DARK_AQUA", "DARK_GREEN", "DARK_RED", "DARK_GRAY", "DARK_BLUE");
                } else {
                    return Arrays.asList("");
                }
            }
            if (args.length > 3) {
                return Arrays.asList("");
            }
        }
        return null;
    }
}
