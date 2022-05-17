package dvdcraft.countries.executorsHandlers;

import dvdcraft.countries.common.CommonVariables;
import dvdcraft.countries.common.Classes.Country;
import dvdcraft.countries.common.Classes.Territory;
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
                    } else if (Country.getCountry(member.getName()) != null) {
                        sender.sendMessage(ChatColor.RED + "This player is already in the country!");
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
                        " Z: " + territory.getFromZ();
                answer += " to X: " + territory.getToX() +
                        " Z: " + territory.getToZ();
                answer += "\n";
            }
            sender.sendMessage(answer);
            return true;
        }

        if (commandName.equals("addTerritory") && args.length == 4) {
            int fromX = 0;
            int toX = 0;
            int fromZ = 0;
            int toZ = 0;
            try {
                fromX = Integer.parseInt(args[0]);
                toX = Integer.parseInt(args[1]);
                fromZ = Integer.parseInt(args[2]);
                toZ = Integer.parseInt(args[3]);
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
                    fromZ >= checkTerritory.getFromZ() && fromZ <= checkTerritory.getToZ() |
                    toX >= checkTerritory.getFromX() && toX <= checkTerritory.getToX() &&
                    toZ >= checkTerritory.getFromZ() && toZ <= checkTerritory.getToZ() |

                    fromX >= checkTerritory.getFromX() && fromX <= checkTerritory.getToX() &&
                    toZ >= checkTerritory.getFromZ() && toZ <= checkTerritory.getToZ() |
                    fromX >= checkTerritory.getFromX() && fromX <= checkTerritory.getToX() &&
                    toZ >= checkTerritory.getFromZ() && toZ <= checkTerritory.getToZ() |


                    checkTerritory.getFromX() >= fromX && checkTerritory.getFromX() <= toX &&
                    checkTerritory.getFromZ() >= fromZ && checkTerritory.getFromZ() <= toZ |
                    checkTerritory.getToX() >= fromX && checkTerritory.getToX() <= toX &&
                    checkTerritory.getToZ() >= fromZ && checkTerritory.getToZ() <= toZ |

                    checkTerritory.getFromX() >= fromX && checkTerritory.getFromX() <= toX &&
                    checkTerritory.getToZ() >= fromZ && checkTerritory.getToZ() <= toZ |
                    checkTerritory.getFromX() >= fromX && checkTerritory.getFromX() <= toX &&
                    checkTerritory.getToZ() >= fromZ && checkTerritory.getToZ() <= toZ) {

                        sender.sendMessage(ChatColor.RED + "this territory belongs to another country");
                        return true;
                    }
                }
            }
            Territory territory = new Territory(fromX, toX, fromZ, toZ);
            country.addTerritory(territory);
            sender.sendMessage("Territory was added to your country");
            return true;
        }
        return false;
    }
}
