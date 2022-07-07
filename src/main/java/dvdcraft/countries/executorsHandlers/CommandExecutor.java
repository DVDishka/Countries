package dvdcraft.countries.executorsHandlers;

import com.destroystokyo.paper.Title;
import dvdcraft.countries.Threads.TimerProcess;
import dvdcraft.countries.common.Classes.Owner;
import dvdcraft.countries.common.CommonVariables;
import dvdcraft.countries.common.Classes.Country;
import dvdcraft.countries.common.Classes.Territory;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;

public class CommandExecutor implements org.bukkit.command.CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (args.length < 1) {
            return false;
        }
        String commandName = args[0];
        Player player = Bukkit.getPlayer(sender.getName());
        if (player == null) {
            CommonVariables.logger.warning("Something went wrong!");
        }

        if (commandName.equals("create") && args.length == 2) {
            if (Country.getCountry(player.getName()) != null) {
                sender.sendMessage(ChatColor.RED + "If you are in the country you cannot create a new one!");
                return true;
            }
            for (Country country : CommonVariables.countries) {
                if (country.getName().equals(args[1])) {
                    sender.sendMessage(ChatColor.RED + "A country with this name already exists!");
                    return true;
                }
            }
            Country country = new Country(args[1]);
            country.addMember(player.getName());
            country.setLeader(player.getName());
            player.sendTitle(Title.builder().title(country.getChatColor() + country.getName()).subtitle(ChatColor.GOLD +
                            "has been created").build());
            CommonVariables.countries.add(country);
            return true;
        }

        if (commandName.equals("edit") && args.length == 4 && args[1].equals("member") && args[2].equals("add")) {
            Player member = Bukkit.getPlayer(args[3]);
            if (member == null) {
                sender.sendMessage(ChatColor.RED + "There is no player with that name!");
                return true;
            } else {
                Country country = Country.getCountry(player.getName());
                if (country != null) {
                    if (!player.getName().equals(country.getCountryLeader())) {
                        sender.sendMessage(ChatColor.RED + "You are not the leader of the country!");
                        return true;
                    } else if (Country.getCountry(member.getName()) != null) {
                        sender.sendMessage(ChatColor.RED + "This player is already in the country!");
                        return true;
                    } else {
                        for (HashSet<Country> countryHashSet : CommonVariables.wars) {
                            for (Country warCountry : countryHashSet) {
                                if (warCountry == country) {
                                    sender.sendMessage(ChatColor.RED + "You can not invite someone while your country is in the war!");
                                    return true;
                                }
                            }
                        }
                        sender.sendMessage(member.getName() + " has been invited to " + country.getName());
                        member.sendTitle(Title.builder().title(ChatColor.LIGHT_PURPLE + "New Invite").build());
                        member.sendMessage(country.getChatColor() + "You has been invited to " + country.getName() +
                                " by " + player.getName() + " </country reply yes> or </country reply no>");
                        CommonVariables.requests.put(member.getName(), country);
                        return true;
                    }
                } else {
                    sender.sendMessage(ChatColor.RED + "You are not the leader of the country!");
                    return true;
                }
            }
        }

        if (commandName.equals("edit") && args.length == 4 && args[1].equals("member") && args[2].equals("remove")) {
            Country country = Country.getCountry(player.getName());
            Country memberCountry = Country.getCountry(args[3]);
            if (country != null) {
                if (country != memberCountry) {
                    sender.sendMessage(ChatColor.RED + args[3] + " is not in your country!");
                    return true;
                } else {
                    String leader = country.getCountryLeader();
                    if (leader.equals(player.getName())) {
                        country.removeMember(args[3]);
                        for (String member : country.getMembers()) {
                            if (Bukkit.getPlayer(member) != null) {
                                Bukkit.getPlayer(member).sendTitle(Title.builder().title(country.getChatColor() +
                                        country.getName()).subtitle(ChatColor.RED + args[3] +
                                        " has been kicked from " + country.getName()).build());
                            }
                        }
                        if (Bukkit.getPlayer(args[3]) != null) {
                            Bukkit.getPlayer(args[3]).sendTitle(Title.builder().title(country.getChatColor() + country.getName())
                                    .subtitle(ChatColor.RED + "You has been kicked from " + country.getName()).build());
                        }
                        return true;
                    } else {
                        sender.sendMessage(ChatColor.RED + "You are not the leader of the country!");
                        return true;
                    }
                }
            } else {
                sender.sendMessage(ChatColor.RED + "You are not the leader of the country!");
                return true;
            }
        }

        if (commandName.equals("get") && args.length == 2 && args[1].equals("countries")) {
            if (CommonVariables.countries.isEmpty()) {
                sender.sendMessage(ChatColor.RED + "There are no countries yet!");
                return true;
            } else {
                for (Country country : CommonVariables.countries) {
                    String answer = "";
                    answer += "Country name: ";
                    answer += country.getName() + "\n";
                    answer += "Country leader: ";
                    answer += country.getCountryLeader() + "\n";
                    answer += "Members: ";
                    for (String member : country.getMembers()) {
                        answer += member + " ";
                    }
                    answer += "\n";
                    answer += "Territories: ";
                    for (Territory territory : country.getTerritories()) {
                        if (!territory.isHidden()) {
                            answer += "from X: " + territory.getFromX() +
                                    " Z: " + territory.getFromZ();
                            answer += " to X: " + territory.getToX() +
                                    " Z: " + territory.getToZ();
                            answer += "\n";
                        }
                    }
                    sender.sendMessage(country.getChatColor() + answer);
                }
                return true;
            }
        }

        if (commandName.equals("status")) {
            Country country = Country.getCountry(player.getName());
            if (country == null) {
                sender.sendMessage(ChatColor.RED + "You are not in the country!");
                return true;
            }
            String answer = "";
            answer += "Country name: ";
            answer += country.getName() + "\n";
            answer += "Country leader: ";
            answer += country.getCountryLeader() + "\n";
            answer += "Members: ";
            for (String member : country.getMembers()) {
                answer += member + " ";
            }
            answer += "\n";
            answer += "Territories: ";
            for (Territory territory : country.getTerritories()) {
                if (!territory.isHidden()) {
                    answer += "from X: " + territory.getFromX() +
                            " Z: " + territory.getFromZ();
                    answer += " to X: " + territory.getToX() +
                            " Z: " + territory.getToZ();
                    answer += "\n";
                }
            }
            sender.sendMessage(country.getChatColor() + answer);
            return true;
        }

        if (commandName.equals("territory") && args.length == 6 && args[1].equals("set")) {
            int fromX = 0;
            int toX = 0;
            int fromZ = 0;
            int toZ = 0;
            try {
                fromX = Integer.parseInt(args[2]);
                fromZ = Integer.parseInt(args[3]);
                toX = Integer.parseInt(args[4]);
                toZ = Integer.parseInt(args[5]);
            } catch (Exception e) {
                sender.sendMessage("All arguments must be a number");
                return true;
            }
            int square = Math.abs(toX - fromX) * Math.abs(toZ - fromZ);
            if (square > 250_000) {
                sender.sendMessage(ChatColor.RED + "Your territory must be <= 250.000 blocks!");
                return true;
            }
            for (Owner checkOwner : CommonVariables.owners.values()) {
                Territory checkTerritory = checkOwner.getTerritory();
                if (fromX >= checkTerritory.getFromX() && fromX <= checkTerritory.getToX() &&
                        fromZ >= checkTerritory.getFromZ() && fromZ <= checkTerritory.getToZ() ||
                        toX >= checkTerritory.getFromX() && toX <= checkTerritory.getToX() &&
                        toZ >= checkTerritory.getFromZ() && toZ <= checkTerritory.getToZ() ||

                        fromX >= checkTerritory.getFromX() && fromX <= checkTerritory.getToX() &&
                        toZ >= checkTerritory.getFromZ() && toZ <= checkTerritory.getToZ() ||
                        fromX >= checkTerritory.getFromX() && fromX <= checkTerritory.getToX() &&
                        toZ >= checkTerritory.getFromZ() && toZ <= checkTerritory.getToZ() ||

                        checkTerritory.getFromX() >= fromX && checkTerritory.getFromX() <= toX &&
                        checkTerritory.getFromZ() >= fromZ && checkTerritory.getFromZ() <= toZ ||
                        checkTerritory.getToX() >= fromX && checkTerritory.getToX() <= toX &&
                        checkTerritory.getToZ() >= fromZ && checkTerritory.getToZ() <= toZ &&

                        !checkOwner.getName().equals(sender.getName())) {

                    sender.sendMessage(ChatColor.RED + "this territory belongs to another player");
                    return true;
                }
            }
            Territory territory = new Territory(fromX, fromZ, toX, toZ, false);
            if (CommonVariables.owners.containsKey(player.getName())) {
                Owner owner = CommonVariables.owners.get(player.getName());
                owner.setTerritory(territory);
            } else {
                Owner owner = new Owner(player.getName(), territory);
                CommonVariables.owners.put(player.getName(), owner);
            }
            player.sendTitle(Title.builder().title(ChatColor.GREEN + "Territory")
                    .subtitle(ChatColor.GOLD + "has been set").build());
            return true;
        }

        if (commandName.equals("territory") && args.length == 2 && args[1].equals("delete")) {
            if (CommonVariables.owners.containsKey(player.getName())) {
                CommonVariables.owners.remove(player.getName());
                player.sendTitle(Title.builder().title(ChatColor.GREEN + "Territory")
                        .subtitle(ChatColor.RED + "has been deleted").build());
            } else {
                sender.sendMessage(ChatColor.RED + "You have not any territory!");
            }
            return true;
        }

        if (commandName.equals("edit") && args.length == 4 && args[1].equals("color") && args[2].equals("set")) {
            Country country = Country.getCountry(player.getName());
            if (country == null) {
                sender.sendMessage(ChatColor.RED + "You are not in the country!");
                return true;
            }
            if (!country.getCountryLeader().equals(player.getName())) {
                sender.sendMessage("You are not the leader of the country!");
                return true;
            }
            String colorName = args[3];
            if (country.setColor(colorName)) {
                for (String member : country.getMembers()) {
                    if (Bukkit.getPlayer(member) != null) {
                        Bukkit.getPlayer(member).sendTitle(Title.builder().title(country.getChatColor() +
                                country.getName()).subtitle(country.getChatColor() + "new color").build());
                    }
                }
            } else {
                sender.sendMessage(ChatColor.RED + "Wrong color!");
            }
            return true;
        }

        if (commandName.equals("edit") && args.length == 2 && args[1].equals("delete")) {
            Country country = Country.getCountry(player.getName());
            if (country == null) {
                sender.sendMessage(ChatColor.RED + "You are not in the country!");
                return true;
            }
            String leader = country.getCountryLeader();
            if (!player.getName().equals(leader)) {
                sender.sendMessage(ChatColor.RED + "You are not the leader of the country!");
                return true;
            }
            CommonVariables.teams.get(country.getName()).unregister();
            CommonVariables.teams.remove(country.getName());
            CommonVariables.countries.remove(country);
            for (String member : country.getMembers()) {
                Player memberPlayer = Bukkit.getPlayer(member);
                if (memberPlayer != null) {
                    memberPlayer.sendTitle(Title.builder().title(country.getChatColor() + country.getName())
                            .subtitle(ChatColor.RED + "has been deleted").build());
                }
            }
            return true;
        }

        if (commandName.equals("leave")) {
            Country country = Country.getCountry(player.getName());
            if (country == null) {
                sender.sendMessage(ChatColor.RED + "You are not in the country!");
                return true;
            }
            String leader = country.getCountryLeader();
            if (player.getName().equals(leader)) {
                sender.sendMessage(ChatColor.RED + "You are the leader of the country, so you need to set new leader" +
                        " before leaving!");
                return true;
            }
            for (HashSet<Country> countryHashSet : CommonVariables.wars) {
                for (Country warCountry : countryHashSet) {
                    if (warCountry == country){
                        sender.sendMessage(ChatColor.RED + "You can not leave while your country is in the war!");
                        return true;
                    }
                }
            }
            country.removeMember(player.getName());
            CommonVariables.teams.get(country.getName()).removePlayer(Bukkit.getPlayer(player.getName()));
            Bukkit.getPlayer(sender.getName()).sendTitle(Title.builder().title(country.getChatColor() +
                    country.getName()).subtitle(ChatColor.RED + "you left " + country.getName()).build());
            for (String member : country.getMembers()) {
                Player memberPlayer = Bukkit.getPlayer(member);
                if (memberPlayer != null && !memberPlayer.equals(player)) {
                    memberPlayer.sendTitle(Title.builder().title(country.getChatColor() + country.getName())
                            .subtitle(ChatColor.RED + player.getName() + " left " + country.getName()).build());
                }
            }
            return true;
        }

        if (commandName.equals("edit") && args.length == 4 && args[1].equals("leader") && args[2].equals("set")) {
            Country country = Country.getCountry(player.getName());
            if (country == null) {
                sender.sendMessage(ChatColor.RED + "You are not in the country!");
                return true;
            }
            String leader = country.getCountryLeader();
            if (!player.getName().equals(leader)) {
                sender.sendMessage(ChatColor.RED + "You are not the leader of the country!");
                return true;
            }
            Player newLeaderPlayer = Bukkit.getPlayer(args[3]);
            if (newLeaderPlayer == null) {
                sender.sendMessage(ChatColor.RED + "There is no player with that name!");
                return true;
            }
            Country newLeaderCountry = Country.getCountry(newLeaderPlayer.getName());
            if (newLeaderCountry != country) {
                sender.sendMessage(ChatColor.RED + "Player is not in your country!");
                return true;
            }
            country.setLeader(newLeaderPlayer.getName());
            newLeaderPlayer.sendTitle(Title.builder().title(country.getChatColor() + country.getName())
                    .subtitle(ChatColor.GOLD + "now you are " + country.getName() + " leader").build());
            for (String member : country.getMembers()) {
                if (Bukkit.getPlayer(member) != null) {
                    Bukkit.getPlayer(member).sendTitle(Title.builder().title(country.getChatColor() + country.getName())
                            .subtitle(ChatColor.GOLD + newLeaderCountry.getName() + " is leader now").build());
                }
            }
            sender.sendMessage("New leader has been set");
            return true;
        }

        if (commandName.equals("reply") && args.length == 2 && args[1].equals("yes")) {
            try {
                Country request = CommonVariables.requests.get(player.getName());
                CommonVariables.requests.remove(player.getName());
                request.addMember(player.getName());
                Bukkit.getPlayer(sender.getName()).sendTitle(Title.builder().title(ChatColor.LIGHT_PURPLE +
                        "Invite").subtitle(ChatColor.GOLD + "now you are member of " + request.getName()).build());
                return true;
            } catch (Exception e) {
                sender.sendMessage(ChatColor.RED + "You have not any invitations!");
                return true;
            }
        }

        if (commandName.equals("reply") && args.length == 2 && args[1].equals("no")) {
            try {
                Country request = CommonVariables.requests.get(player.getName());
                CommonVariables.requests.remove(player.getName());
                Bukkit.getPlayer(sender.getName()).sendTitle(Title.builder().title(ChatColor.LIGHT_PURPLE +
                        "Invite").subtitle(ChatColor.RED + "You declined the invitation").build());
                return true;
            } catch (Exception e) {
                sender.sendMessage(ChatColor.RED + "You have not any invitations!");
                return true;
            }
        }

        if (commandName.equals("event") && args.length >= 4 && args[1].equals("war")) {
            HashSet<Country> thisWarCountryHashSet = new HashSet<>();
            Country country = Country.getCountry(player.getName());
            if (country == null) {
                sender.sendMessage(ChatColor.RED + "You are not in the country!");
                return true;
            }
            if (!player.getName().equals(country.getCountryLeader())) {
                sender.sendMessage(ChatColor.RED + "You are not the leader of the country!");
                return true;
            }
            thisWarCountryHashSet.add(Country.getCountry(player.getName()));
            for (int i = 2; i < args.length - 1; i++) {
                if (Country.getCountryByName(args[i]) != null) {
                    thisWarCountryHashSet.add(Country.getCountryByName(args[i]));
                } else {
                    sender.sendMessage(ChatColor.RED + "There is no country with that name!");
                    return true;
                }
            }
            int time = 0;
            try {
                time = Integer.parseInt(args[args.length - 1]);
            } catch (Exception e) {
                sender.sendMessage(ChatColor.RED + "Time must be a number!");
                return true;
            }
            if (time < 1) {
                sender.sendMessage(ChatColor.RED + "Time must be >= 1!");
                return true;
            }
            if (time > 10) {
                sender.sendMessage(ChatColor.RED + "Time must be <= 10!");
                return true;
            }
            for (HashSet<Country> countryHashSet : CommonVariables.wars) {
                for (Country warCountry : countryHashSet) {
                    for (Country checkCountry : thisWarCountryHashSet) {
                        if (warCountry == checkCountry) {
                            sender.sendMessage(ChatColor.RED + checkCountry.getName() + " is already in the war!");
                            return true;
                        }
                    }
                }
            }
            for (Country warCountry : thisWarCountryHashSet) {
                int playersCounter = 0;
                boolean isLeaderOnline = false;
                for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                    if (Country.getCountry(onlinePlayer.getName()) == warCountry) {
                        playersCounter++;
                        if (warCountry.getCountryLeader().equals(onlinePlayer.getName())) {
                            isLeaderOnline = true;
                        }
                    }
                }
                if (!isLeaderOnline) {
                    sender.sendMessage(ChatColor.RED + "Leader of opposite country is offline!");
                    return true;
                }
            }
            CommonVariables.wars.add(thisWarCountryHashSet);
            time *= 3600;
            Thread timerThread = new TimerProcess(time, thisWarCountryHashSet);
            timerThread.start();
            for (Country warCountry : thisWarCountryHashSet) {
                for (String member : warCountry.getMembers()) {
                    if (Bukkit.getPlayer(member) != null) {
                        Bukkit.getPlayer(member).sendTitle(Title.builder().title(ChatColor.RED + "War")
                                .subtitle(ChatColor.RED + country.getName() + "declared war").build());
                    }
                }
            }
            return true;
        }

        if (commandName.equals("territory") && args.length == 2 && args[1].equals("get")) {
            if (CommonVariables.owners.containsKey(player.getName())) {
                String answer = "";
                Territory territory = CommonVariables.owners.get(player.getName()).getTerritory();
                answer += "from X: " + territory.getFromX() +
                        " Z: " + territory.getFromZ();
                answer += " to X: " + territory.getToX() +
                        " Z: " + territory.getToZ();
                sender.sendMessage(answer);
                return true;
            } else {
                sender.sendMessage(ChatColor.RED + "You have not any territory!");
                return true;
            }
        }

        if (commandName.equals("edit") && args.length == 3 && args[1].equalsIgnoreCase("friendlyFire") && args[2].equals("enable")) {
            Country country = Country.getCountry(player.getName());
            if (country == null) {
                sender.sendMessage(ChatColor.RED + "You are not in the country!");
                return true;
            }
            if (!country.getCountryLeader().equals(player.getName())) {
                sender.sendMessage(ChatColor.RED + "You are not the leader of the country!");
                return true;
            }
            CommonVariables.teams.get(country.getName()).setAllowFriendlyFire(true);
            for (String member : country.getMembers()) {
                if (Bukkit.getPlayer(member) != null) {
                    Bukkit.getPlayer(member).sendTitle(Title.builder().title(country.getChatColor() + country.getName())
                            .subtitle(ChatColor.RED + "friendly fire has been enabled").build());
                }
            }
            return true;
        }

        if (commandName.equals("edit") && args.length == 3 && args[1].equalsIgnoreCase("friendlyFire") && args[2].equals("disable")) {
            Country country = Country.getCountry(player.getName());
            if (country == null) {
                sender.sendMessage(ChatColor.RED + "You are not in the country!");
                return true;
            }
            if (!country.getCountryLeader().equals(player.getName())) {
                sender.sendMessage(ChatColor.RED + "You are not the leader of the country!");
                return true;
            }
            CommonVariables.teams.get(country.getName()).setAllowFriendlyFire(false);
            for (String member : country.getMembers()) {
                if (Bukkit.getPlayer(member) != null) {
                    Bukkit.getPlayer(member).sendTitle(Title.builder().title(country.getChatColor() + country.getName())
                            .subtitle(ChatColor.GOLD + "friendly fire has been disabled").build());
                }
            }
            return true;
        }

        if (commandName.equals("territory") && args.length == 4 && args[1].equals("edit") && args[2].equals("hidden") && args[3].equals("enable")) {
            Territory territory = CommonVariables.owners.get(player.getName()).getTerritory();
            if (territory == null) {
                sender.sendMessage(ChatColor.RED + "You have no territory!");
                return false;
            }
            territory.setHidden(true);
            player.sendTitle(Title.builder().title(ChatColor.GREEN + "Territory")
                    .subtitle(ChatColor.GOLD + "is hidden now").build());
            return true;
        }

        if (commandName.equals("territory") && args.length == 4 && args[1].equals("edit") && args[2].equals("hidden") && args[3].equals("disable")) {
            Territory territory = CommonVariables.owners.get(player.getName()).getTerritory();
            if (territory == null) {
                sender.sendMessage(ChatColor.RED + "You have no territory!");
                return false;
            }
            territory.setHidden(false);
            player.sendTitle(Title.builder().title(ChatColor.GREEN + "Territory")
                    .subtitle(ChatColor.RED + "is not hidden now").build());
            return true;
        }

        sender.sendMessage(ChatColor.RED + "Unknown command or invalid arguments!");
        return false;
    }
}
