package dvdcraft.countries.executorsHandlers;

import dvdcraft.countries.Threads.TimerProcess;
import dvdcraft.countries.common.CommonVariables;
import dvdcraft.countries.common.Classes.Country;
import dvdcraft.countries.common.Classes.Territory;
import it.unimi.dsi.fastutil.Pair;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.boss.BossBar;
import org.jetbrains.annotations.NotNull;

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
            sender.sendMessage("Country " + args[1] + " has been created!");
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
                        sender.sendMessage(member.getName() + " has been invited to " + country.getName());
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
                        sender.sendMessage(args[3] + " has been removed from " + country.getName());
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
                        answer += "from X: " + territory.getFromX() +
                                " Z: " + territory.getFromZ();
                        answer += " to X: " + territory.getToX() +
                                " Z: " + territory.getToZ();
                        answer += "\n";
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
                answer += "from X: " + territory.getFromX() +
                        " Z: " + territory.getFromZ();
                answer += " to X: " + territory.getToX() +
                        " Z: " + territory.getToZ();
                answer += "\n";
            }
            sender.sendMessage(country.getChatColor() + answer);
            return true;
        }

        if (commandName.equals("edit") && args.length == 7 && args[1].equals("territory") && args[2].equals("add")) {
            int fromX = 0;
            int toX = 0;
            int fromZ = 0;
            int toZ = 0;
            try {
                fromX = Integer.parseInt(args[3]);
                fromZ = Integer.parseInt(args[4]);
                toX = Integer.parseInt(args[5]);
                toZ = Integer.parseInt(args[6]);
            } catch (Exception e) {
                sender.sendMessage("All arguments must be a number");
                return true;
            }
            Country country = Country.getCountry(player.getName());
            if (country == null) {
                sender.sendMessage(ChatColor.RED + "You are not in the country!");
                return true;
            }
            if (!country.getCountryLeader().equals(player.getName())) {
                sender.sendMessage("You are not the leader of the country!");
                return true;
            }
            if (!(country.getTerritories().size() < country.getMembers().size())) {
                sender.sendMessage(ChatColor.RED + "Country can not have more territories then members");
                return true;
            }
            for (Country checkCountry : CommonVariables.countries) {
                for (Territory checkTerritory : checkCountry.getTerritories()) {
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
                            checkTerritory.getToZ() >= fromZ && checkTerritory.getToZ() <= toZ ||

                            checkTerritory.getFromX() >= fromX && checkTerritory.getFromX() <= toX &&
                            checkTerritory.getToZ() >= fromZ && checkTerritory.getToZ() <= toZ ||
                            checkTerritory.getFromX() >= fromX && checkTerritory.getFromX() <= toX &&
                            checkTerritory.getToZ() >= fromZ && checkTerritory.getToZ() <= toZ) {

                        sender.sendMessage(ChatColor.RED + "this territory belongs to another country");
                        return true;
                    }
                }
            }
            Territory territory = new Territory(fromX, fromZ, toX, toZ);
            country.addTerritory(territory);
            sender.sendMessage("Territory was added to your country");
            return true;
        }

        if (commandName.equals("edit") && args.length == 7 && args[1].equals("territory") && args[2].equals("remove")) {
            int fromX = 0;
            int toX = 0;
            int fromZ = 0;
            int toZ = 0;
            try {
                fromX = Integer.parseInt(args[3]);
                fromZ = Integer.parseInt(args[4]);
                toX = Integer.parseInt(args[5]);
                toZ = Integer.parseInt(args[6]);
            } catch (Exception e) {
                sender.sendMessage("All arguments must be a number");
                return true;
            }
            Country country = Country.getCountry(player.getName());
            if (country == null) {
                sender.sendMessage(ChatColor.RED + "You are not in the country!");
                return true;
            }
            if (!country.getCountryLeader().equals(player.getName())) {
                sender.sendMessage("You are not the leader of the country!");
                return true;
            }
            for (Territory territory : country.getTerritories()) {
                if (territory.getFromX() == fromX && territory.getToX() == toX &&
                territory.getFromZ() == fromZ && territory.getToZ() == toZ) {
                    country.removeTerritory(territory);
                    sender.sendMessage("Territory has been removed");
                    return true;
                }
            }
            sender.sendMessage(ChatColor.RED + "It is not territory of your country!");
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
                sender.sendMessage("Country color has been set");
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
            sender.sendMessage("Country was deleted!");
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
                sender.sendMessage(ChatColor.RED + "You are the leader of the country, so you need to set new leader before leaving!");
                return true;
            }
            country.removeMember(player.getName());
            CommonVariables.teams.get(country.getName()).removePlayer(Bukkit.getPlayer(player.getName()));
            sender.sendMessage("You left the country!");
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
            sender.sendMessage("New leader has been set");
            return true;
        }

        if (commandName.equals("reply") && args.length == 2 && args[1].equals("yes")) {
            try {
                Country request = CommonVariables.requests.get(player.getName());
                CommonVariables.requests.remove(player.getName());
                request.addMember(player.getName());
                sender.sendMessage(request.getChatColor() + "You has been added to " + request.getName());
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
                sender.sendMessage(request.getChatColor() + "You declined the invitation to " + request.getName());
                return true;
            } catch (Exception e) {
                sender.sendMessage(ChatColor.RED + "You have not any invitations!");
                return true;
            }
        }

        if (commandName.equals("event") && args.length == 4 && args[1].equals("war")) {
            Country country = Country.getCountry(player.getName());
            Country oppositeCountry = Country.getCountryByName(args[2]);
            if (country == null) {
                sender.sendMessage(ChatColor.RED + "You are not in the country!");
                return true;
            }
            if (!player.getName().equals(country.getCountryLeader())) {
                sender.sendMessage(ChatColor.RED + "You are not the leader of the country!");
                return true;
            }
            if (oppositeCountry == null) {
                sender.sendMessage(ChatColor.RED + "There is no country with that name!");
                return true;
            }
            int time = 0;
            try {
                time = Integer.parseInt(args[3]);
            } catch (Exception e) {
                sender.sendMessage(ChatColor.RED + "Time must be a number!");
                return true;
            }
            int playersCounter = 0;
            boolean isLeaderOnline = false;
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                if (Country.getCountry(onlinePlayer.getName()) == oppositeCountry) {
                    playersCounter++;
                    if (oppositeCountry.getCountryLeader().equals(onlinePlayer.getName())) {
                        isLeaderOnline = true;
                    }
                }
            }
            if (!isLeaderOnline || playersCounter < 2) {
                sender.sendMessage(ChatColor.RED + "There are too few members of opposite country!");
                return true;
            }
            CommonVariables.wars.add(Pair.of(country, oppositeCountry));
            time *= 3600;
            Thread timerThread = new TimerProcess(time, country, oppositeCountry);
            timerThread.start();
            return true;
        }
        sender.sendMessage(ChatColor.RED + "Unknown command or invalid arguments!");
        return false;
    }
}
