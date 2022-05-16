package dvdcraft.countries.executors_handlers;

import dvdcraft.countries.common.CommonVariables;
import dvdcraft.countries.common.Country;
import dvdcraft.countries.common.Territory;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CommandExecutor implements org.bukkit.command.CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        String commandName = command.getName();
        Player player = Bukkit.getPlayer(sender.getName());
        if (player == null) {
            CommonVariables.logger.warning("Something went wrong!");
        }

        if (commandName.equals("createCountry") && args.length == 1) {
            if (Country.getCountry(player.getName()) != null) {
                sender.sendMessage(ChatColor.RED + "if you are in the country you cannot create a new one!");
                return true;
            }
            Country country = new Country(args[0]);
            country.addMember(player);
            country.setLeader(player.getName());
            sender.sendMessage("country " + args[0] + " has been created!");
            CommonVariables.countries.add(country);
            return true;
        }

        if (commandName.equals("addMember") && args.length == 1) {
            Player member = Bukkit.getPlayer(args[0]);
            if (member == null) {
                sender.sendMessage(ChatColor.RED + "There is no player with that name!");
                return true;
            } else {
                Country country = Country.getCountry(player.getName());
                if (country != null) {
                    if (!player.getName().equals(country.getCountryLeader())) {
                        sender.sendMessage(ChatColor.RED + "You are not the leader of the country!");
                        return true;
                    } else {
                        country.addMember(member);
                        sender.sendMessage(member.getName() + " has been added to " + country.getName());
                        return true;
                    }
                } else {
                    sender.sendMessage(ChatColor.RED + "You are not the leader of the country!");
                    return true;
                }
            }
        }

        if (commandName.equals("removeMember") && args.length == 1) {
            Country country = Country.getCountry(player.getName());
            Country memberCountry = Country.getCountry(args[0]);
            if (country != null) {
                if (country != memberCountry) {
                    sender.sendMessage(ChatColor.RED + args[0] + " is not in your country!");
                    return true;
                } else {
                    String leader = country.getCountryLeader();
                    if (leader.equals(player.getName())) {
                        country.removeMember(args[0]);
                        sender.sendMessage(args[0] + " has been removed to " + country.getName());
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

        if (commandName.equals("getCountry") && args.length == 1) {
            Player member = Bukkit.getPlayer(args[0]);
            if (member == null) {
                sender.sendMessage(ChatColor.RED + "There is no player with that name!");
                return true;
            }
            Country country = Country.getCountry(member.getName());
            if (country == null) {
                sender.sendMessage("This player has no country");
                return true;
            }
            sender.sendMessage("This player is in " + country.getName());
            return true;
        }

        if (commandName.equals("getStatus")) {
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
                        " Y: " + territory.getFromY() +
                        " Z: " + territory.getFromZ();
                answer += "to X: " + territory.getToX() +
                        " Y: " + territory.getToY() +
                        " Z: " + territory.getToX();
                answer += "\n";
            }
            sender.sendMessage(answer);
            return true;
        }
        return false;
    }
}
