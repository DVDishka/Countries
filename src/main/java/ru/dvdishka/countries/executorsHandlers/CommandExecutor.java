package ru.dvdishka.countries.executorsHandlers;

import javafx.util.Pair;
import org.bukkit.inventory.meta.SkullMeta;
import ru.dvdishka.countries.Threads.TimerProcess;
import ru.dvdishka.countries.common.CommonVariables;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import ru.dvdishka.countries.Classes.*;

import java.util.*;

public class CommandExecutor implements org.bukkit.command.CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (args.length < 1) {
            return false;
        }
        String commandName = args[0];

        if (commandName.equals("reply") && args.length == 3 && args[1].equals("yes")) {

            try {

                Country request = CommonVariables.requests.get(args[2]);

                CommonVariables.requests.remove(args[2]);
                request.addMember(args[2]);

                Bukkit.getPlayer(args[2]).sendTitle(ChatColor.LIGHT_PURPLE + "Invite",
                        ChatColor.GOLD + "now you are member of " + request.getName());

                Bukkit.getPlayer(args[2]).playSound(
                        Bukkit.getPlayer(args[2]).getLocation(),
                        Sound.ENTITY_PLAYER_LEVELUP,
                        50,
                        1);

                for (String member : request.getStringMembers()) {

                    Player memberPlayer = Bukkit.getPlayer(member);

                    if (memberPlayer != null) {

                        memberPlayer.sendTitle(request.getChatColor() + request.getName(),
                               ChatColor.GOLD + args[2] + " joined the country");
                    }
                }
                return true;

            } catch (Exception e) {
                return false;
            }
        }

        Player senderPlayer = Bukkit.getPlayer(sender.getName());

        if (senderPlayer == null) {

            CommonVariables.logger.warning("Something went wrong!");
        }

        if (commandName.equals("create") && args.length == 2) {

            if (Country.getCountry(senderPlayer.getName()) != null) {

                sender.sendMessage(ChatColor.RED + "If you are in the country you cannot create a new one!");
                return false;
            }

            for (Country country : CommonVariables.countries) {

                if (country.getName().equals(args[1])) {

                    sender.sendMessage(ChatColor.RED + "A country with this name already exists!");
                    return false;
                }

            }

            Country country = new Country(args[1]);

            country.addMember(senderPlayer.getName());
            country.setLeader(senderPlayer.getName());

            senderPlayer.sendTitle(country.getChatColor() + country.getName(),
                    ChatColor.GOLD + "has been created");

            CommonVariables.countries.add(country);

            return true;
        }

        if (commandName.equals("edit") && args.length == 4 && args[1].equals("member") && args[2].equals("add")) {

            Player memberPlayer = Bukkit.getPlayer(args[3]);

            if (memberPlayer == null) {

                sender.sendMessage(ChatColor.RED + "There is no player with that name!");
                return false;
            }
            Country senderCountry = Country.getCountry(senderPlayer.getName());

            if (senderCountry != null) {

                Member senderMember = senderCountry.getMember(senderPlayer.getName());

                if (!senderMember.hasPermission(Permission.INVITE_MEMBER)) {

                    sender.sendMessage(ChatColor.RED + "You do not have permission to invite player to your country!");
                    return false;

                } else if (Country.getCountry(memberPlayer.getName()) != null) {

                    sender.sendMessage(ChatColor.RED + "This player is already in the country!");
                    return false;

                } else {

                    for (HashSet<Country> countryHashSet : CommonVariables.wars) {

                        for (Country warCountry : countryHashSet) {

                            if (warCountry == senderCountry) {
                                sender.sendMessage(ChatColor.RED + "You can not invite someone while your country is in the war!");
                                return false;
                            }
                        }
                    }

                    BaseComponent[] component = new ComponentBuilder("You has been invited to " +
                            senderCountry.getName() + "\n")
                            .color(net.md_5.bungee.api.ChatColor.GOLD)
                            .append("[ACCEPT]   ").
                            color(net.md_5.bungee.api.ChatColor.GREEN)
                            .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND,
                                    "/country reply yes " + memberPlayer.getName()))
                            .append("[DECLINE]")
                            .color(net.md_5.bungee.api.ChatColor.RED)
                            .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND,
                                    "/country reply no " + memberPlayer.getName()))
                            .create();

                    memberPlayer.spigot().sendMessage(component);
                    memberPlayer.sendTitle(ChatColor.LIGHT_PURPLE + "New Invite", "");

                    CommonVariables.requests.put(memberPlayer.getName(), senderCountry);

                    return true;
                }
            } else {
                sender.sendMessage(ChatColor.RED + "You are not in the country!");
                return false;
            }
        }

        if (commandName.equals("edit") && args.length == 4 && args[1].equals("member") && args[2].equals("remove")) {

            Country senderCountry = Country.getCountry(senderPlayer.getName());
            Country memberCountry = Country.getCountry(args[3]);

            if (senderCountry != null) {

                if (senderCountry != memberCountry) {

                    sender.sendMessage(ChatColor.RED + args[3] + " is not in your country!");
                    return true;

                } else {

                    Member senderMember = senderCountry.getMember(senderPlayer.getName());
                    Member memberMember = memberCountry.getMember(args[3]);

                    if (senderMember.hasPermission(Permission.REMOVE_MEMBER)) {

                        if (senderMember.getRank() == null ||
                                memberMember.getRank() != null &&
                                        !senderMember.getRank().getPermissions()
                                                .containsAll(memberMember.getRank().getPermissions())) {

                            sender.sendMessage(ChatColor.RED + "Your rank is lower than " + args[3] + "`s rank!");
                            return false;
                        }

                        senderCountry.removeMember(args[3]);

                        for (String member : senderCountry.getStringMembers()) {

                            if (Bukkit.getPlayer(member) != null) {

                                Bukkit.getPlayer(member).sendTitle(senderCountry.getChatColor() + senderCountry.getName(),
                                        ChatColor.RED + args[3] + " has been kicked from " + senderCountry.getName());
                            }
                        }
                        if (Bukkit.getPlayer(args[3]) != null) {

                            Bukkit.getPlayer(args[3]).sendTitle(senderCountry.getChatColor() + senderCountry.getName(),
                                    ChatColor.RED + "You has been kicked from " + senderCountry.getName());
                        }
                        return false;

                    } else {

                        sender.sendMessage(ChatColor.RED + "You do not have permission to kick players!");
                        return false;
                    }
                }
            } else {

                sender.sendMessage(ChatColor.RED + "You are not in the country!");
                return false;
            }
        }

        if (commandName.equals("get") && args.length == 2 && args[1].equals("countries")) {

            if (CommonVariables.countries.isEmpty()) {

                sender.sendMessage(ChatColor.RED + "There are no countries yet!");
                return false;
            } else {

                for (Country country : CommonVariables.countries) {
                    String answer = "";
                    answer += "Country name: ";
                    answer += country.getName() + "\n";
                    answer += "Country leader: ";
                    answer += country.getCountryLeader() + "\n";
                    answer += "Members: ";
                    for (String member : country.getStringMembers()) {
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

            Country country = Country.getCountry(senderPlayer.getName());

            if (country == null) {

                sender.sendMessage(ChatColor.RED + "You are not in the country!");
                return false;
            }

            String answer = "";
            answer += "Country name: ";
            answer += country.getName() + "\n";
            answer += "Country leader: ";
            answer += country.getCountryLeader() + "\n";
            answer += "Members: ";
            for (String member : country.getStringMembers()) {
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
                return false;
            }
            int square = Math.abs(toX - fromX) * Math.abs(toZ - fromZ);
            if (square > 250_000) {
                sender.sendMessage(ChatColor.RED + "Your territory must be <= 250.000 blocks!");
                return false;
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
                    return false;
                }
            }
            Territory territory = new Territory(fromX, fromZ, toX, toZ, false);
            if (CommonVariables.owners.containsKey(senderPlayer.getName())) {
                Owner owner = CommonVariables.owners.get(senderPlayer.getName());
                owner.setTerritory(territory);
            } else {
                Owner owner = new Owner(senderPlayer.getName(), territory);
                CommonVariables.owners.put(senderPlayer.getName(), owner);
            }
            senderPlayer.sendTitle(ChatColor.GREEN + "Territory",
                    ChatColor.GOLD + "has been set");

            return true;
        }

        if (commandName.equals("territory") && args.length == 2 && args[1].equals("delete")) {

            if (CommonVariables.owners.containsKey(senderPlayer.getName())) {

                CommonVariables.owners.remove(senderPlayer.getName());
                senderPlayer.sendTitle(ChatColor.GREEN + "Territory",
                        ChatColor.RED + "has been deleted");
            } else {

                sender.sendMessage(ChatColor.RED + "You have not any territory!");
            }
            return true;
        }

        if (commandName.equals("edit") && args.length == 4 && args[1].equals("color") && args[2].equals("set")) {

            Country country = Country.getCountry(senderPlayer.getName());

            if (country == null) {
                sender.sendMessage(ChatColor.RED + "You are not in the country!");
                return false;
            }

            if (!country.getCountryLeader().equals(senderPlayer.getName())) {
                sender.sendMessage("You are not the leader of the country!");
                return false;
            }

            String colorName = args[3];

            if (country.setColor(colorName)) {

                for (String member : country.getStringMembers()) {

                    if (Bukkit.getPlayer(member) != null) {

                        Bukkit.getPlayer(member).sendTitle(country.getChatColor() + country.getName(),
                                country.getChatColor() + "new color");
                    }
                }
            } else {
                sender.sendMessage(ChatColor.RED + "Wrong color!");
            }

            return true;
        }

        if (commandName.equals("edit") && args.length == 2 && args[1].equals("delete")) {

            Country senderCountry = Country.getCountry(senderPlayer.getName());

            if (senderCountry == null) {
                sender.sendMessage(ChatColor.RED + "You are not in the country!");
                return false;
            }

            Member senderMember = senderCountry.getMember(senderPlayer);

            if (!senderMember.hasPermission(Permission.DELETE_COUNTRY)) {

                sender.sendMessage(ChatColor.RED + "You do not have permission to delete the country!");
                return false;
            }

            CommonVariables.teams.get(senderCountry.getName()).unregister();
            CommonVariables.teams.remove(senderCountry.getName());
            CommonVariables.countries.remove(senderCountry);

            for (String member : senderCountry.getStringMembers()) {

                Player memberPlayer = Bukkit.getPlayer(member);

                if (memberPlayer != null) {

                    memberPlayer.sendTitle(senderCountry.getChatColor() + senderCountry.getName(),
                            ChatColor.RED + "has been deleted");
                }
            }

            return true;
        }

        if (commandName.equals("leave")) {

            Country senderCountry = Country.getCountry(senderPlayer.getName());

            if (senderCountry == null) {
                sender.sendMessage(ChatColor.RED + "You are not in the country!");
                return false;
            }

            String leader = senderCountry.getCountryLeader();

            if (senderPlayer.getName().equals(leader)) {

                sender.sendMessage(ChatColor.RED + "You are the leader of the country, so you need to set new leader" +
                        " before leaving!");
                return false;
            }
            for (HashSet<Country> countryHashSet : CommonVariables.wars) {

                for (Country warCountry : countryHashSet) {

                    if (warCountry == senderCountry){

                        sender.sendMessage(ChatColor.RED + "You can not leave while your country is in the war!");
                        return false;
                    }
                }
            }

            senderCountry.removeMember(senderPlayer.getName());
            CommonVariables.teams.get(senderCountry.getName()).removePlayer(Bukkit.getPlayer(senderPlayer.getName()));
            Bukkit.getPlayer(sender.getName()).sendTitle(senderCountry.getChatColor() + senderCountry.getName(),
                    ChatColor.RED + "you left " + senderCountry.getName());

            for (String member : senderCountry.getStringMembers()) {

                Player memberPlayer = Bukkit.getPlayer(member);

                if (memberPlayer != null && !memberPlayer.equals(senderPlayer)) {

                    memberPlayer.sendTitle(senderCountry.getChatColor() + senderCountry.getName(),
                            ChatColor.RED + senderPlayer.getName() + " left " + senderCountry.getName());
                }
            }

            return true;
        }

        if (commandName.equals("edit") && args.length == 4 && args[1].equals("leader") && args[2].equals("set")) {

            Country country = Country.getCountry(senderPlayer.getName());

            if (country == null) {
                sender.sendMessage(ChatColor.RED + "You are not in the country!");
                return false;
            }

            String leader = country.getCountryLeader();

            if (!senderPlayer.getName().equals(leader)) {
                sender.sendMessage(ChatColor.RED + "You are not the leader of the country!");
                return false;
            }

            Player newLeaderPlayer = Bukkit.getPlayer(args[3]);

            if (newLeaderPlayer == null) {
                sender.sendMessage(ChatColor.RED + "There is no player with that name!");
                return false;
            }

            Country newLeaderCountry = Country.getCountry(newLeaderPlayer.getName());

            if (newLeaderCountry != country) {

                sender.sendMessage(ChatColor.RED + "Player is not in your country!");
                return false;
            }

            country.setLeader(newLeaderPlayer.getName());

            newLeaderPlayer.sendTitle(country.getChatColor() + country.getName(),
                    ChatColor.GOLD + "now you are " + country.getName() + " leader");

            for (String member : country.getStringMembers()) {

                if (Bukkit.getPlayer(member) != null) {

                    Bukkit.getPlayer(member).sendTitle(country.getChatColor() + country.getName(),
                            ChatColor.GOLD + newLeaderCountry.getName() + " is leader now");
                }
            }

            sender.sendMessage("New leader has been set");

            return true;
        }

        if (commandName.equals("reply") && args.length == 3 && args[1].equals("no")) {
            try {

                Country request = CommonVariables.requests.get(args[2]);

                if (request == null) {

                    return true;
                }

                CommonVariables.requests.remove(args[2]);

                Bukkit.getPlayer(args[2]).sendTitle(ChatColor.LIGHT_PURPLE + "Invite",
                        ChatColor.RED + "You declined the invitation");

                return true;

            } catch (Exception e) {

                return false;
            }
        }

        if (commandName.equals("event") && args.length >= 4 && args[1].equals("war") && false) {

            HashSet<Country> thisWarCountryHashSet = new HashSet<>();
            Country senderCountry = Country.getCountry(senderPlayer.getName());

            if (senderCountry == null) {

                sender.sendMessage(ChatColor.RED + "You are not in the country!");
                return false;
            }

            if (!senderPlayer.getName().equals(senderCountry.getCountryLeader())) {

                sender.sendMessage(ChatColor.RED + "You are not the leader of the country!");
                return false;
            }

            thisWarCountryHashSet.add(Country.getCountry(senderPlayer.getName()));

            for (int i = 2; i < args.length - 1; i++) {

                if (Country.getCountryByName(args[i]) != null) {

                    thisWarCountryHashSet.add(Country.getCountryByName(args[i]));
                } else {

                    sender.sendMessage(ChatColor.RED + "There is no country with that name!");
                    return false;
                }
            }

            int time = 0;

            try {

                time = Integer.parseInt(args[args.length - 1]);
            } catch (Exception e) {

                sender.sendMessage(ChatColor.RED + "Time must be a number!");
                return false;
            }

            if (time < 1) {

                sender.sendMessage(ChatColor.RED + "Time must be >= 1!");
                return false;
            }

            if (time > 10) {

                sender.sendMessage(ChatColor.RED + "Time must be <= 10!");
                return false;
            }
            for (HashSet<Country> countryHashSet : CommonVariables.wars) {

                for (Country warCountry : countryHashSet) {

                    for (Country checkCountry : thisWarCountryHashSet) {

                        if (warCountry == checkCountry) {

                            sender.sendMessage(ChatColor.RED + checkCountry.getName() + " is already in the war!");
                            return false;
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
                    return false;
                }
            }
            CommonVariables.wars.add(thisWarCountryHashSet);

            time *= 3600;

            Thread timerThread = new TimerProcess(time, thisWarCountryHashSet);
            timerThread.start();

            for (Country warCountry : thisWarCountryHashSet) {

                for (String member : warCountry.getStringMembers()) {

                    if (Bukkit.getPlayer(member) != null) {

                        Bukkit.getPlayer(member).sendTitle(ChatColor.RED + "War",
                                ChatColor.RED + senderCountry.getName() + "declared war");
                    }
                }
            }
            return true;
        }

        if (commandName.equals("territory") && args.length == 2 && args[1].equals("get")) {

            if (CommonVariables.owners.containsKey(senderPlayer.getName())) {

                String answer = "";
                Territory territory = CommonVariables.owners.get(senderPlayer.getName()).getTerritory();
                answer += "from X: " + territory.getFromX() +
                        " Z: " + territory.getFromZ();
                answer += " to X: " + territory.getToX() +
                        " Z: " + territory.getToZ();
                sender.sendMessage(answer);
                return true;
            } else {

                sender.sendMessage(ChatColor.RED + "You have not any territory!");
                return false;
            }
        }

        if (commandName.equals("edit") && args.length == 3 && args[1].equalsIgnoreCase("friendlyFire") && args[2].equals("enable")) {

            Country country = Country.getCountry(senderPlayer.getName());

            if (country == null) {

                sender.sendMessage(ChatColor.RED + "You are not in the country!");
                return false;
            }

            Member senderMember = country.getMember(senderPlayer);

            if (!senderMember.hasPermission(Permission.FRIENDLY_FIRE)) {

                sender.sendMessage(ChatColor.RED + "You do not have permission to edit friendlyFire settings!");
                return false;
            }

            CommonVariables.teams.get(country.getName()).setAllowFriendlyFire(true);

            for (String member : country.getStringMembers()) {

                if (Bukkit.getPlayer(member) != null) {

                    Bukkit.getPlayer(member).sendTitle(country.getChatColor() + country.getName(),
                            ChatColor.RED + "friendly fire has been enabled");
                }
            }
            return true;
        }

        if (commandName.equals("edit") && args.length == 3 && args[1].equalsIgnoreCase("friendlyFire") && args[2].equals("disable")) {

            Country country = Country.getCountry(senderPlayer.getName());

            if (country == null) {

                sender.sendMessage(ChatColor.RED + "You are not in the country!");
                return false;
            }

            Member senderMember = country.getMember(senderPlayer);

            if (!senderMember.hasPermission(Permission.FRIENDLY_FIRE)) {

                sender.sendMessage(ChatColor.RED + "You do not have permission to edit friendlyFire settings!");
                return false;
            }

            CommonVariables.teams.get(country.getName()).setAllowFriendlyFire(false);

            for (String member : country.getStringMembers()) {

                if (Bukkit.getPlayer(member) != null) {

                    Bukkit.getPlayer(member).sendTitle(country.getChatColor() + country.getName(),
                            ChatColor.GOLD + "friendly fire has been disabled");
                }
            }

            return true;
        }

        if (commandName.equals("territory") && args.length == 4 && args[1].equals("edit") && args[2].equals("hidden") && args[3].equals("enable")) {

            Territory territory = CommonVariables.owners.get(senderPlayer.getName()).getTerritory();

            if (territory == null) {

                sender.sendMessage(ChatColor.RED + "You have no territory!");
                return false;
            }

            territory.setHidden(true);

            senderPlayer.sendTitle(ChatColor.GREEN + "Territory",
                   ChatColor.GOLD + "is hidden now");

            return true;
        }

        if (commandName.equals("territory") && args.length == 4 && args[1].equals("edit") && args[2].equals("hidden") && args[3].equals("disable")) {

            Territory territory = CommonVariables.owners.get(senderPlayer.getName()).getTerritory();

            if (territory == null) {

                sender.sendMessage(ChatColor.RED + "You have no territory!");
                return false;
            }

            territory.setHidden(false);
            senderPlayer.sendTitle(ChatColor.GREEN + "Territory",
                    ChatColor.RED + "is not hidden now");

            return true;
        }

        if (commandName.equals("edit") && args.length == 4 && args[1].equals("rank") && args[2].equals("create")) {

            String newRankName = args[3];
            Rank newRank = new Rank(newRankName);
            Country senderCountry = Country.getCountry(senderPlayer.getName());

            if (senderCountry == null) {

                sender.sendMessage(ChatColor.RED + "You are not in the country!");
                return false;
            }

            Member playerMember = senderCountry.getMember(senderPlayer);

            if (!playerMember.hasPermission(Permission.CREATE_RANK)) {

                sender.sendMessage(ChatColor.RED + "You do not have permission to create a rank!");
                return false;
            }

            senderCountry.addRank(newRank);

            senderPlayer.sendTitle(senderCountry.getChatColor() + senderCountry.getName(),
                    ChatColor.GOLD + newRankName + " rank has been created");

            return true;
        }

        if (commandName.equals("edit") && args.length == 5 && args[1].equals("member") && args[2].equals("rank")) {

            String memberName = args[3];
            Country memberCountry = Country.getCountry(memberName);
            Country senderCountry = Country.getCountry(senderPlayer.getName());
            String rankName = args[4];

            if (senderCountry == null) {

                sender.sendMessage(ChatColor.RED + "You are not in the country!");
                return false;
            }

            Member senderMember = senderCountry.getMember(senderPlayer);

            if (!senderMember.hasPermission(Permission.SET_PLAYER_RANK)) {

                sender.sendMessage(ChatColor.RED + "You do not have permission to set ranks!");
                return false;
            }

            if (!memberCountry.equals(senderCountry)) {

                sender.sendMessage(ChatColor.RED + "This player is not in your country!");
                return false;
            }

            Rank setRank = senderCountry.getRank(rankName);

            if (setRank == null) {

                sender.sendMessage(ChatColor.RED + "There is no rank with that name!");
                return false;
            }

            if (!senderCountry.isLeader(senderPlayer) && senderMember.getRank() != null &&
                    !senderMember.getRank().getPermissions().containsAll(setRank.getPermissions())) {

                sender.sendMessage(ChatColor.RED + "The rank you want to set for a player has more permissions than yours!");
                return false;
            }

            memberCountry.setRank(memberName, setRank);

            for (Member member : memberCountry.getMembers()) {

                Player player = Bukkit.getPlayer(member.getName());

                if (player != null) {

                    player.sendTitle(memberCountry.getChatColor() + memberCountry.getName(),
                            ChatColor.GOLD + memberName + " now has a rank " + rankName);
                }
            }

            return true;
        }

        if (commandName.equals("edit") && args.length == 3 && args[1].equals("rank")) {

            String rankName = args[2];
            Country senderCountry = Country.getCountry(senderPlayer.getName());

            if (senderCountry == null) {

                sender.sendMessage(ChatColor.RED + "You are not in the country!");
                return false;
            }

            if (!senderCountry.containsRank(rankName)) {

                sender.sendMessage(ChatColor.RED + "There is no rank with that name!");
                return false;
            }

            Rank rank = senderCountry.getRank(rankName);

            if (!senderCountry.getMember(senderPlayer).hasPermission(Permission.EDIT_RANK)) {

                sender.sendMessage(ChatColor.RED + "You do not have permission to edit ranks!");
                return false;
            }

            Member senderMember = senderCountry.getMember(senderPlayer);

            if (!senderCountry.isLeader(senderPlayer) && senderMember.getRank() == null ||
                    !senderCountry.isLeader(senderPlayer) && senderMember.getRank().getPriority() <= rank.getPriority()) {

                sender.sendMessage(ChatColor.RED + "You can not edit a rank that has higher or equal priority!");
                return false;
            }

            Inventory permissionMenu = Bukkit.createInventory(null, Permission.values().length, rankName);

            int index = 0;
            for (Permission permission : Permission.values()) {

                ItemStack indicator;
                ItemMeta indicatorMeta;

                if (rank.hasPermission(permission)) {

                    indicator = new ItemStack(Material.LIME_WOOL);
                    indicatorMeta = indicator.getItemMeta();
                    indicatorMeta.setDisplayName(ChatColor.GREEN + permission.getName());
                    ArrayList<String> list = new ArrayList<>();
                    list.add(ChatColor.RED + "[CLICK] to disable");
                    indicatorMeta.setLore(list);
                } else {

                    indicator = new ItemStack(Material.RED_WOOL);
                    indicatorMeta = indicator.getItemMeta();
                    indicatorMeta.setDisplayName(ChatColor.RED + permission.getName());
                    ArrayList<String> list = new ArrayList<>();
                    list.add(ChatColor.GREEN + "[CLICK] to enable");
                    indicatorMeta.setLore(list);
                }

                indicator.setItemMeta(indicatorMeta);
                permissionMenu.setItem(index, indicator);

                index++;
            }

            CommonVariables.rankEdit.put(senderPlayer.getName(), permissionMenu);
            senderPlayer.openInventory(permissionMenu);

            return true;
        }

        if (commandName.equals("edit") && args.length == 4 && args[1].equals("rank") && args[2].equals("remove")) {

            Country senderCountry = Country.getCountry(senderPlayer.getName());

            if (senderCountry == null) {

                sender.sendMessage(ChatColor.RED + "You are not in the country!");
                return false;
            }

            Member senderMember = senderCountry.getMember(senderPlayer);

            if (!senderMember.hasPermission(Permission.REMOVE_RANK)) {

                sender.sendMessage(ChatColor.RED + "You do not have permission to remove rank!");
                return false;
            }

            Rank removeRank = senderCountry.getRank(args[3]);

            if (removeRank == null) {

                sender.sendMessage(ChatColor.RED + "There is no rank with that name!");
                return false;
            }

            if (!senderCountry.isLeader(senderPlayer) && senderMember.getRank() == null ||
                    !senderCountry.isLeader(senderPlayer) && senderMember.getRank().getPriority() <= removeRank.getPriority()) {

                sender.sendMessage(ChatColor.RED + "You can not remove a rank that has higher or equal priority!");
                return false;
            }

            senderCountry.removeRank(removeRank);

            senderPlayer.sendTitle(senderCountry.getChatColor() + senderCountry.getName(),
                    ChatColor.RED + "Rank " + removeRank.getName() + " has been removed");

            return true;
        }

        if (commandName.equals("edit") && args.length == 4 && args[1].equals("rank") && args[3].equals("icon")) {

            Country senderCountry = Country.getCountry(senderPlayer.getName());

            if (senderCountry == null) {

                sender.sendMessage(ChatColor.RED + "You are not in the country!");
                return false;
            }

            Member senderMember = senderCountry.getMember(senderPlayer);

            if (!senderMember.hasPermission(Permission.EDIT_RANK)) {

                sender.sendMessage(ChatColor.RED + "You do not have permission to edit rank!");
                return false;
            }

            Rank editRank = senderCountry.getRank(args[2]);

            if (editRank == null) {

                sender.sendMessage(ChatColor.RED + "There is no rank with that name!");
                return false;
            }

            if (!senderCountry.isLeader(senderPlayer) && senderMember.getRank() == null ||
                    !senderCountry.isLeader(senderPlayer) && senderMember.getRank().getPriority() <= editRank.getPriority()) {

                sender.sendMessage(ChatColor.RED + "You can not edit a rank that has higher or equal priority!");
                return false;
            }

            int iconsAmount = Material.values().length - 2;
            if (iconsAmount == 0) {
                sender.sendMessage(ChatColor.RED + "There are no icons yet");
                return false;
            }

            ArrayList<Inventory> icons = new ArrayList<>();

            int i = 1;
            while (iconsAmount > 0) {

                Inventory page = Bukkit.createInventory(null, 54, ChatColor.GOLD + "Rank icons " + i);

                page.setItem(45, CommonVariables.prevPage);
                page.setItem(53, CommonVariables.nextPage);
                icons.add(page);
                iconsAmount -= 52;
                i++;
            }

            int stackIndex = 0;
            int inventoryIndex = 0;

            for (Material icon : Material.values()) {
                if (!icon.equals(Material.LIGHT_GRAY_STAINED_GLASS_PANE) && !icon.equals(Material.AIR)) {

                    if (stackIndex == 45) {

                        stackIndex++;
                    }

                    if (stackIndex == 53) {

                        stackIndex = 0;
                        inventoryIndex++;
                    }

                    icons.get(inventoryIndex).setItem(stackIndex, new ItemStack(icon));
                    stackIndex++;

                    if (icons.get(inventoryIndex).getItem(stackIndex - 1) == null) {
                        stackIndex--;
                    }
                }
            }

            int pagesAmount = icons.size();

            for (int page = 0; page < pagesAmount; page++) {

                boolean isClear = true;

                for (ItemStack item : icons.get(page)) {

                    if (item != null && !item.equals(CommonVariables.nextPage) && !item.equals(CommonVariables.prevPage)) {

                        isClear = false;
                        break;
                    }
                }
                if (isClear) {

                    icons.remove(page);

                    pagesAmount--;
                    page--;
                }
            }

            stackIndex = 0;

            Inventory lastPage = icons.get(icons.size() - 1);

            for (ItemStack itemStack : lastPage) {

                if (itemStack == null) {

                    lastPage.setItem(stackIndex, new ItemStack(Material.LIGHT_GRAY_STAINED_GLASS_PANE));
                }

                stackIndex++;
            }

            icons.set(icons.size() - 1, lastPage);
            senderPlayer.openInventory(icons.get(0));

            CommonVariables.iconMenu = icons;
            CommonVariables.iconRankChoose.put(senderPlayer.getName(), new Pair<>(senderCountry.getName(), editRank.getName()));

            return true;
        }

        if (commandName.equals("get") && args.length == 3 && args[1].equals("members")) {

            Country country = Country.getCountryByName(args[2]);

            if (country == null) {

                sender.sendMessage(ChatColor.RED + "There is no country with that name!");
                return false;
            }

            ArrayList<Inventory> memberMenus = new ArrayList<>();

            int memberAmount = country.getMembers().size();
            int pageCounter = 1;
            int memberIndex = 1;

            Inventory memberMenu = Bukkit.createInventory(null, 54,
                    ChatColor.GOLD + country.getName() + " " + 1);

            ArrayList<Member> countryMembers = new ArrayList<>(country.getMembers());

            for (int i = 0; i < countryMembers.size(); i++) {

                for (int j = i; j < countryMembers.size(); j++) {

                    int firstMemberPriority;
                    int secondMemberPriority;

                    if (countryMembers.get(i).getRank() == null) {

                        firstMemberPriority = 0;

                    } else {

                        firstMemberPriority = countryMembers.get(i).getRank().getPriority();
                    }

                    if (countryMembers.get(j).getRank() == null) {

                        secondMemberPriority = 0;

                    } else {

                        secondMemberPriority = countryMembers.get(j).getRank().getPriority();
                    }

                    if (firstMemberPriority < secondMemberPriority) {

                        Collections.swap(countryMembers, i, j);

                    } else if (firstMemberPriority == secondMemberPriority) {

                        if (countryMembers.get(i).getRank() == null || countryMembers.get(j).getRank() == null) {

                            Collections.swap(countryMembers, i, j);

                        } else {

                            if (countryMembers.get(i).getRank().getName().compareTo(countryMembers.get(j).getName()) > 0) {

                                Collections.swap(countryMembers, i, j);
                            }
                        }
                    }
                }
            }

            for (Member member : countryMembers) {

                memberAmount--;

                ItemStack playerHead = new ItemStack(Material.PLAYER_HEAD);
                SkullMeta playerHeadMeta = (SkullMeta) playerHead.getItemMeta();

                playerHeadMeta.setOwningPlayer(Bukkit.getOfflinePlayer(member.getName()));

                playerHeadMeta.setDisplayName(member.getName());

                if (member.getRank() == null) {

                    playerHeadMeta.setLore(Arrays.asList("No Rank"));

                } else {

                    playerHeadMeta.setLore(Arrays.asList(member.getRank().getName()));
                }

                playerHead.setItemMeta(playerHeadMeta);

                if (memberIndex == 46) {

                    memberIndex++;
                    memberAmount++;
                }

                memberMenu.setItem(memberIndex - 1, playerHead);
                memberIndex++;

                if (memberIndex == 54 || memberAmount <= 0) {

                    memberMenu.setItem(45, CommonVariables.prevPage);
                    memberMenu.setItem(53, CommonVariables.nextPage);

                    memberIndex = 1;
                    pageCounter++;
                    memberMenus.add(memberMenu);
                    memberMenu = Bukkit.createInventory(null, 54,
                            ChatColor.GOLD + country.getName() + " " + pageCounter);
                }
            }

            int itemIndex = 0;

            for (ItemStack item : memberMenus.get(memberMenus.size() - 1)) {

                if (item == null) {

                    memberMenus.get(memberMenus.size() - 1).setItem(itemIndex, new ItemStack(Material.LIGHT_GRAY_STAINED_GLASS_PANE));
                }

                itemIndex++;
            }

            CommonVariables.memberMenu.put(country.getName(), memberMenus);
            CommonVariables.memberMenuRequest.put(senderPlayer.getName(), country.getName());

            senderPlayer.openInventory(memberMenus.get(0));

            return true;
        }

        if (commandName.equals("get") && args.length == 3 && args[1].equals("ranks")) {

            Country country = Country.getCountryByName(args[2]);

            if (country == null) {

                sender.sendMessage(ChatColor.RED + "There is no country with that name!");
                return false;
            }

            ArrayList<Rank> countryRanks = country.getRanks();

            if (countryRanks.size() == 0) {

                sender.sendMessage(ChatColor.RED + "This country has no ranks!");
                return false;
            }

            for (int i = 0; i < countryRanks.size(); i++) {

                for (int j = i; j < countryRanks.size(); j++) {

                    if (countryRanks.get(i).getPriority() < countryRanks.get(j).getPriority()) {

                        Collections.swap(countryRanks, i, j);

                    } else if (countryRanks.get(i).getPriority() == countryRanks.get(j).getPriority() &&
                            countryRanks.get(i).getName().compareTo(countryRanks.get(j).getName()) > 0) {

                        Collections.swap(countryRanks, i, j);
                    }
                }
            }

            ArrayList<Inventory> rankMenus = new ArrayList<>();

            int rankAmount = country.getRanks().size();
            int pageCounter = 1;
            int rankIndex = 1;

            Inventory rankMenu = Bukkit.createInventory(null, 54,
                    ChatColor.GOLD + country.getName() + " ranks " + 1);

            for (Rank rank : countryRanks) {

                rankAmount--;

                if (rank.getIcon() == null) {

                    rank.setIcon("BARRIER");
                }

                ItemStack icon = new ItemStack(Material.valueOf(rank.getIcon()));
                ItemMeta iconMeta = icon.getItemMeta();

                iconMeta.setDisplayName(rank.getName());

                iconMeta.setLore(Arrays.asList(ChatColor.LIGHT_PURPLE + "Priority: " + rank.getPriority()));

                icon.setItemMeta(iconMeta);

                rankMenu.setItem(rankIndex - 1, icon);
                rankIndex++;

                if (rankIndex == 46) {

                    rankIndex++;
                    rankAmount++;
                }

                if (rankIndex == 54 || rankAmount <= 0) {

                    rankMenu.setItem(45, CommonVariables.prevPage);
                    rankMenu.setItem(53, CommonVariables.nextPage);

                    rankIndex = 1;
                    pageCounter++;
                    rankMenus.add(rankMenu);
                    rankMenu = Bukkit.createInventory(null, 54,
                            ChatColor.GOLD + country.getName() + " ranks " + pageCounter);
                }
            }

            int itemIndex = 0;

            for (ItemStack item : rankMenus.get(rankMenus.size() - 1)) {

                if (item == null) {

                    rankMenus.get(rankMenus.size() - 1).setItem(itemIndex, new ItemStack(Material.LIGHT_GRAY_STAINED_GLASS_PANE));
                }

                itemIndex++;
            }

            CommonVariables.rankMenu.put(country.getName(), rankMenus);
            CommonVariables.rankMenuRequest.put(senderPlayer.getName(), country.getName());

            senderPlayer.openInventory(rankMenus.get(0));

            return true;
        }

        if (commandName.equals("edit") && args.length == 5 && args[1].equals("rank") && args[3].equals("priority")) {

            Country senderCountry = Country.getCountry(senderPlayer.getName());

            if (senderCountry == null) {

                senderPlayer.sendMessage(ChatColor.RED + "You are not in the country");
                return false;
            }

            Member senderMember = senderCountry.getMember(senderPlayer);
            Rank editRank = senderCountry.getRank(args[2]);

            if (editRank == null) {

                sender.sendMessage(ChatColor.RED + "There is no rank with that name!");
                return false;
            }

            if (!senderMember.hasPermission(Permission.EDIT_RANK)) {

                sender.sendMessage(ChatColor.RED + "You do not have permission to edit rank!");
                return false;
            }

            if (!senderCountry.isLeader(senderPlayer) && senderMember.getRank() == null ||
                    !senderCountry.isLeader(senderPlayer) && senderMember.getRank().getPriority() <= editRank.getPriority()) {

                sender.sendMessage(ChatColor.RED + "You can not edit a rank that has higher or equal priority!");
                return false;
            }

            int newPriority;

            try {

                newPriority = Integer.parseInt(args[4]);

            } catch (Exception e) {

                sender.sendMessage(ChatColor.RED + "Priority must be a number!");
                return false;
            }

            senderCountry.getRank(args[2]).setPriority(newPriority);

            senderCountry.updateRank(senderCountry.getRank(args[2]));

            senderPlayer.sendTitle(senderCountry.getChatColor() + senderCountry.getName(),
                    ChatColor.GOLD + String.valueOf(newPriority) + " priority has been set to rank " + editRank.getName());

            return true;
        }

        sender.sendMessage(ChatColor.RED + "Unknown command or invalid arguments!");
        return false;
    }
}
