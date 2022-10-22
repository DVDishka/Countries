package ru.dvdishka.countries.executorsHandlers;

import javafx.util.Pair;
import org.bukkit.inventory.meta.SkullMeta;
import ru.dvdishka.countries.Classes.Country;
import ru.dvdishka.countries.Classes.Member;
import ru.dvdishka.countries.Classes.Permission;
import ru.dvdishka.countries.Classes.Rank;
import ru.dvdishka.countries.common.CommonVariables;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

public class EventHandler implements Listener {

    @org.bukkit.event.EventHandler
    public void deathEvent(PlayerDeathEvent event) {

        Player player = event.getEntity();

        for (HashSet<Country> countryHashSet : CommonVariables.wars) {
            for (Country warCountry : countryHashSet) {
                if (Country.getCountry(player.getName()) == warCountry) {
                    try {
                        String scoreboardName = "War_";
                        for (Country countryInWar : countryHashSet) {
                            scoreboardName += countryInWar.getName() + "_vs_";
                        }
                        scoreboardName = scoreboardName.substring(0, scoreboardName.length() - 5);
                        Score score = Bukkit.getScoreboardManager().getMainScoreboard().getObjective(scoreboardName)
                                .getScore(player);
                        if (score.getScore() > 0) {
                            score.setScore(score.getScore() - 1);
                        }
                        if (score.getScore() == 0) {
                            score.setScore(0);
                        }
                        int liveCountryCounter = 0;
                        Objective objective = Bukkit.getScoreboardManager().getMainScoreboard().getObjective(scoreboardName);
                        for (Country country : countryHashSet) {
                            boolean flag = false;
                            for (String member : country.getStringMembers()) {
                                OfflinePlayer offlineMember = Bukkit.getOfflinePlayer(member);
                                Score memberScore = objective.getScore(offlineMember);
                                try {
                                    if (memberScore.getScore() > 0) {
                                        flag = true;
                                    }
                                } catch (Exception ignored) {}
                            }
                            if (flag) {
                                liveCountryCounter++;
                            } else {
                                for (String member : country.getStringMembers()) {
                                    Player loosePlayer = Bukkit.getPlayer(member);
                                    if (loosePlayer != null) {
                                        loosePlayer.sendTitle(ChatColor.RED + "You Lose!",
                                                "");
                                    }
                                }
                            }
                        }
                        if (liveCountryCounter == 1) {
                            for (Country country : countryHashSet) {
                                boolean flag = false;
                                for (String member : country.getStringMembers()) {
                                    OfflinePlayer offlineMember = Bukkit.getOfflinePlayer(member);
                                    try {
                                        Score memberScore = objective.getScore(offlineMember);
                                        if (memberScore.getScore() > 0) {
                                            flag = true;
                                            liveCountryCounter++;
                                        }
                                    } catch (Exception ignored) {}
                                }
                                if (flag) {
                                    for (Country wonCountryCheck : countryHashSet)
                                        for (String member : wonCountryCheck.getStringMembers()) {
                                            Player loosePlayer = Bukkit.getPlayer(member);
                                            if (loosePlayer != null) {
                                                loosePlayer.sendTitle(ChatColor.RED + country.getName() + " Won!",
                                                        "");
                                            }
                                        }
                                    objective.unregister();
                                    CommonVariables.wars.remove(countryHashSet);
                                    return;
                                }
                            }
                        }
                    } catch (Exception e) {
                        return;
                    }
                }
            }
        }
    }

    @org.bukkit.event.EventHandler
    public void playerQuitEvent(PlayerQuitEvent event) {
        for (HashSet<Country> countryHashSet : CommonVariables.wars) {
            for (Country warCountry : countryHashSet) {
                try {
                    if (Country.getCountry(event.getPlayer().getName()).getName().equals(warCountry.getName())) {
                        String scoreboardName = "War_";
                        for (Country countryInWar : countryHashSet) {
                            scoreboardName += countryInWar.getName() + "_vs_";
                        }
                        scoreboardName = scoreboardName.substring(0, scoreboardName.length() - 5);
                        Objective objective = Bukkit.getScoreboardManager().getMainScoreboard().getObjective(scoreboardName);
                        Score score = objective.getScore(event.getPlayer());
                        score.setScore(0);
                        int liveCounryCounter = 0;
                        for (Country country : countryHashSet) {
                            boolean flag = false;
                            for (String member : country.getStringMembers()) {
                                OfflinePlayer offlineMember = Bukkit.getOfflinePlayer(member);
                                Score memberScore = objective.getScore(offlineMember);
                                try {
                                    if (memberScore.getScore() > 0) {
                                        flag = true;
                                    }
                                } catch (Exception e) {

                                }
                            }
                            if (flag) {
                                liveCounryCounter++;
                            } else {
                                for (String member : country.getStringMembers()) {
                                    Player loosePlayer = Bukkit.getPlayer(member);
                                    if (loosePlayer != null) {
                                        loosePlayer.sendTitle(ChatColor.RED + "You Lose!",
                                                "");
                                    }
                                }
                            }
                        }
                        if (liveCounryCounter == 1) {
                            for (Country country : countryHashSet) {
                                boolean flag = false;
                                for (String member : country.getStringMembers()) {
                                    OfflinePlayer offlineMember = Bukkit.getOfflinePlayer(member);
                                    try {
                                        Score memberScore = objective.getScore(offlineMember);
                                        if (memberScore.getScore() != 0) {
                                            flag = true;
                                            liveCounryCounter++;
                                        }
                                    } catch (Exception e) {
                                        continue;
                                    }
                                }
                                if (flag) {
                                    for (String member : country.getStringMembers()) {
                                        Player loosePlayer = Bukkit.getPlayer(member);
                                        if (loosePlayer != null) {
                                            loosePlayer.sendTitle(ChatColor.RED + "You Won!",
                                                    "");
                                        }
                                    }
                                    objective.unregister();
                                    CommonVariables.wars.remove(countryHashSet);
                                    return;
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    return;
                }
            }
        }
    }

    @org.bukkit.event.EventHandler
    public void onRankEditInventoryClick(InventoryClickEvent event) {

        if (CommonVariables.rankEdit.containsKey(event.getWhoClicked().getName())){

            if (event.getCurrentItem() == null ||
                    !event.getCurrentItem().getType().equals(Material.LIME_WOOL) &&
                    !event.getCurrentItem().getType().equals(Material.RED_WOOL)) {
                return;
            }

            event.setCancelled(true);

            Inventory permissionMenu = CommonVariables.rankEdit.get(event.getWhoClicked().getName());
            Country senderCountry = Country.getCountry(event.getWhoClicked().getName());
            Member senderMember = senderCountry.getMember(event.getWhoClicked().getName());
            Player senderPlayer = (Player) event.getWhoClicked();

            boolean isPermission = false;
            boolean isEnabled = false;

            String clickedPermissionName = "";

            for (Permission permission : Permission.values()) {

                if (event.getCurrentItem().getItemMeta().getDisplayName().endsWith(permission.getName())) {

                    clickedPermissionName = permission.getName();
                    isPermission = true;
                    break;
                }
            }

            if (!isPermission || event.getCurrentItem().getItemMeta().getLore() != null && event.getCurrentItem().getItemMeta().getLore().size() == 0) {

                return;
            }

            if (event.getCurrentItem().getItemMeta().getDisplayName().startsWith(ChatColor.GREEN.toString())) {

                isEnabled = true;
            }

            Permission clickedPermission = Permission.valueOf(clickedPermissionName);

            if (!senderMember.hasPermission(clickedPermission)) {
                senderPlayer.playSound(
                        senderPlayer.getLocation(),
                        Sound.BLOCK_ANVIL_PLACE,
                        50,
                        1);
                return;
            }

            String editRankName = event.getView().getTitle();

            Rank editRank = senderCountry.getRank(editRankName);

            if (isEnabled) {

                editRank.removePermission(clickedPermission);
            } else {

                editRank.addPermission(clickedPermission);
            }

            senderCountry.updateRank(editRank);

            ItemStack indicator;
            ItemMeta indicatorMeta;

            if (!isEnabled) {

                indicator = new ItemStack(Material.LIME_WOOL);
                indicatorMeta = indicator.getItemMeta();
                indicatorMeta.setDisplayName(ChatColor.GREEN + clickedPermissionName);
                ArrayList<String> list = new ArrayList<>();
                list.add(ChatColor.RED + "[CLICK] to disable");
                indicatorMeta.setLore(list);

            } else {

                indicator = new ItemStack(Material.RED_WOOL);
                indicatorMeta = indicator.getItemMeta();
                indicatorMeta.setDisplayName(ChatColor.RED + clickedPermissionName);
                ArrayList<String> list = new ArrayList<>();
                list.add(ChatColor.GREEN + "[CLICK] to enable");
                indicatorMeta.setLore(list);
            }

            senderPlayer.playSound(
                    senderPlayer.getLocation(),
                    Sound.ENTITY_PLAYER_LEVELUP,
                    50,
                    1);
            indicator.setItemMeta(indicatorMeta);
            event.setCurrentItem(indicator);
        }
    }

    @org.bukkit.event.EventHandler
    public void onMemberMenuClick(InventoryClickEvent event) {

        if (CommonVariables.memberMenuRequest.containsKey(event.getWhoClicked().getName())) {

            Player senderPlayer = (Player) event.getWhoClicked();
            int pageIndex = 0;

            for (Inventory memberMenuPage : CommonVariables.memberMenu
                    .get(CommonVariables.memberMenuRequest.get(event.getWhoClicked().getName()))) {

                if (event.getClickedInventory().equals(memberMenuPage)) {

                    event.setCancelled(true);

                    if (event.getCurrentItem() != null) {

                        if (event.getCurrentItem().equals(CommonVariables.prevPage)) {

                            if (pageIndex > 0) {

                                senderPlayer.openInventory(CommonVariables.memberMenu
                                        .get(CommonVariables.memberMenuRequest.get(event.getWhoClicked().getName()))
                                        .get(pageIndex - 1));
                                senderPlayer.playSound(
                                        senderPlayer.getLocation(),
                                        Sound.ITEM_BOOK_PAGE_TURN,
                                        50,
                                        1);
                            } else {

                                senderPlayer.playSound(
                                        senderPlayer.getLocation(),
                                        Sound.BLOCK_ANVIL_PLACE,
                                        50,
                                        1);
                            }
                        }

                        if (event.getCurrentItem().equals(CommonVariables.nextPage)) {

                            if (CommonVariables.memberMenu.get(CommonVariables.memberMenuRequest
                                    .get(senderPlayer.getName())).size() > pageIndex + 1) {

                                senderPlayer.openInventory(CommonVariables.memberMenu
                                        .get(CommonVariables.memberMenuRequest.get(event.getWhoClicked().getName()))
                                        .get(pageIndex + 1));
                                senderPlayer.playSound(
                                        senderPlayer.getLocation(),
                                        Sound.ITEM_BOOK_PAGE_TURN,
                                        50,
                                        1);
                            } else {

                                senderPlayer.playSound(
                                        senderPlayer.getLocation(),
                                        Sound.BLOCK_ANVIL_PLACE,
                                        50,
                                        1);
                            }
                        }
                    }

                    return;
                }

                pageIndex++;
            }
        }
    }

    @org.bukkit.event.EventHandler
    public void onRankMenuClick(InventoryClickEvent event) {

        if (CommonVariables.rankMenuRequest.containsKey(event.getWhoClicked().getName())) {

            Player senderPlayer = (Player) event.getWhoClicked();
            int pageIndex = 0;

            for (Inventory rankMenuPage : CommonVariables.rankMenu
                    .get(CommonVariables.rankMenuRequest.get(event.getWhoClicked().getName()))) {

                if (event.getClickedInventory().equals(rankMenuPage)) {

                    if (event.getCurrentItem() != null) {

                        if (event.getCurrentItem().equals(CommonVariables.prevPage)) {

                            if (pageIndex > 0) {

                                senderPlayer.openInventory(CommonVariables.rankMenu
                                        .get(CommonVariables.rankMenuRequest.get(event.getWhoClicked().getName()))
                                        .get(pageIndex - 1));
                                senderPlayer.playSound(
                                        senderPlayer.getLocation(),
                                        Sound.ITEM_BOOK_PAGE_TURN,
                                        50,
                                        1);
                            } else {

                                senderPlayer.playSound(
                                        senderPlayer.getLocation(),
                                        Sound.BLOCK_ANVIL_PLACE,
                                        50,
                                        1);
                            }

                            event.setCancelled(true);

                            return;
                        }

                        if (event.getCurrentItem().equals(CommonVariables.nextPage)) {

                            if (CommonVariables.rankMenu.get(CommonVariables.rankMenuRequest
                                    .get(senderPlayer.getName())).size() > pageIndex + 1) {

                                senderPlayer.openInventory(CommonVariables.rankMenu
                                        .get(CommonVariables.rankMenuRequest.get(event.getWhoClicked().getName()))
                                        .get(pageIndex + 1));
                                senderPlayer.playSound(
                                        senderPlayer.getLocation(),
                                        Sound.ITEM_BOOK_PAGE_TURN,
                                        50,
                                        1);
                            } else {

                                senderPlayer.playSound(
                                        senderPlayer.getLocation(),
                                        Sound.BLOCK_ANVIL_PLACE,
                                        50,
                                        1);
                            }

                            event.setCancelled(true);

                            return;
                        }

                        if (event.getCurrentItem().getType().equals(Material.LIGHT_GRAY_STAINED_GLASS_PANE)) {

                            event.setCancelled(true);

                            return;
                        }

                        String rankName = event.getCurrentItem().getItemMeta().getDisplayName();
                        Country country = Country.getCountryByName(CommonVariables.rankMenuRequest.get(senderPlayer.getName()));

                        ArrayList<Inventory> memberMenus = new ArrayList<>();

                        int memberAmount = 0;
                        int pageCounter = 1;
                        int memberIndex = 1;

                        for (Member member : country.getMembers()) {

                            if (member.getRank().getName().equals(rankName)) {

                                memberAmount++;
                            }
                        }

                        Inventory memberMenu = Bukkit.createInventory(null, 54,
                                ChatColor.GOLD + country.getName() + " " + rankName + "s " + pageCounter);

                        for (Member member : country.getMembers()) {

                            if (member.getRank().getName().equals(rankName)) {

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
                                            ChatColor.GOLD + country.getName() + " " + rankName + "s " + pageCounter + 1);
                                }
                            }
                        }

                        int itemIndex = 0;

                        if (memberMenus.size() == 0) {

                            event.setCancelled(true);

                            senderPlayer.playSound(
                                    senderPlayer.getLocation(),
                                    Sound.BLOCK_ANVIL_PLACE,
                                    50,
                                    1);

                            return;
                        }

                        for (ItemStack item : memberMenus.get(memberMenus.size() - 1)) {

                            if (item == null) {

                                memberMenus.get(memberMenus.size() - 1).setItem(itemIndex, new ItemStack(Material.LIGHT_GRAY_STAINED_GLASS_PANE));
                            }

                            itemIndex++;
                        }

                        CommonVariables.countryRankOwnersRequest.put(senderPlayer.getName(), memberMenus);

                        senderPlayer.openInventory(memberMenus.get(0));

                        senderPlayer.playSound(
                                senderPlayer.getLocation(),
                                Sound.ENTITY_PLAYER_LEVELUP,
                                50,
                                1);
                    }

                    return;
                }

                pageIndex++;
            }
        }
    }

    @org.bukkit.event.EventHandler
    public void onRankOwnersMenuClick(InventoryClickEvent event) {

        if (CommonVariables.countryRankOwnersRequest.containsKey(event.getWhoClicked().getName())) {

            Player senderPlayer = (Player) event.getWhoClicked();

            int pageNumber = 0;

            for (Inventory inventory : CommonVariables.countryRankOwnersRequest.get(event.getWhoClicked().getName())) {

                if (event.getClickedInventory().equals(inventory)) {

                    if (event.getCurrentItem() != null) {

                        event.setCancelled(true);

                        if (event.getCurrentItem().getType().equals(Material.LIGHT_GRAY_STAINED_GLASS_PANE)) {

                            senderPlayer.playSound(
                                    senderPlayer.getLocation(),
                                    Sound.BLOCK_ANVIL_PLACE,
                                    50,
                                    1);

                            event.setCancelled(true);

                            return;
                        }

                        if (event.getCurrentItem().equals(CommonVariables.prevPage)) {

                            if (pageNumber > 0) {

                                senderPlayer.playSound(
                                        senderPlayer.getLocation(),
                                        Sound.ITEM_BOOK_PAGE_TURN,
                                        50,
                                        1);

                                senderPlayer.openInventory(CommonVariables.iconMenu.get(pageNumber - 1));

                            } else {

                                senderPlayer.playSound(
                                        senderPlayer.getLocation(),
                                        Sound.BLOCK_ANVIL_PLACE,
                                        50,
                                        1);

                            }

                            event.setCancelled(true);

                            return;
                        }

                        if (event.getCurrentItem().equals(CommonVariables.nextPage)) {

                            if (pageNumber < CommonVariables.iconMenu.size() - 1) {

                                senderPlayer.playSound(
                                        senderPlayer.getLocation(),
                                        Sound.ITEM_BOOK_PAGE_TURN,
                                        50,
                                        1);

                                senderPlayer.openInventory(CommonVariables.iconMenu.get(pageNumber + 1));

                            } else {

                                senderPlayer.playSound(
                                        senderPlayer.getLocation(),
                                        Sound.BLOCK_ANVIL_PLACE,
                                        50,
                                        1);

                            }

                            event.setCancelled(true);

                            return;
                        }
                    }
                }

                pageNumber++;
            }
        }
    }

    @org.bukkit.event.EventHandler
    public void onRankIconMenuClick(InventoryClickEvent event) {

        Player senderPlayer = (Player) event.getWhoClicked();

        int pageNumber = 0;

        for (Inventory iconPage : CommonVariables.iconMenu) {

            if (event.getClickedInventory() != null && event.getClickedInventory().equals(iconPage)) {

                if (event.getCurrentItem() != null && !event.getCurrentItem().getType().equals(Material.AIR)) {

                    if (event.getCurrentItem().getType().equals(Material.LIGHT_GRAY_STAINED_GLASS_PANE)) {

                        senderPlayer.playSound(
                                senderPlayer.getLocation(),
                                Sound.BLOCK_ANVIL_PLACE,
                                50,
                                1);

                        event.setCancelled(true);

                        return;
                    }

                    if (event.getCurrentItem().equals(CommonVariables.prevPage)) {

                        if (pageNumber > 0) {

                            senderPlayer.playSound(
                                    senderPlayer.getLocation(),
                                    Sound.ITEM_BOOK_PAGE_TURN,
                                    50,
                                    1);

                            senderPlayer.openInventory(CommonVariables.iconMenu.get(pageNumber - 1));

                        } else {

                            senderPlayer.playSound(
                                    senderPlayer.getLocation(),
                                    Sound.BLOCK_ANVIL_PLACE,
                                    50,
                                    1);

                        }

                        event.setCancelled(true);

                        return;
                    }

                    if (event.getCurrentItem().equals(CommonVariables.nextPage)) {

                        if (pageNumber < CommonVariables.iconMenu.size() - 1) {

                            senderPlayer.playSound(
                                    senderPlayer.getLocation(),
                                    Sound.ITEM_BOOK_PAGE_TURN,
                                    50,
                                    1);

                            senderPlayer.openInventory(CommonVariables.iconMenu.get(pageNumber + 1));

                        } else {

                            senderPlayer.playSound(
                                    senderPlayer.getLocation(),
                                    Sound.BLOCK_ANVIL_PLACE,
                                    50,
                                    1);

                        }

                        event.setCancelled(true);

                        return;
                    }

                    Pair<String, String> countryRankPair = CommonVariables.iconRankChoose.get(event.getWhoClicked().getName());

                    if (Country.getCountryByName(countryRankPair.getKey()).getRank(countryRankPair.getValue()).getIcon() == null) {

                        Country.getCountryByName(countryRankPair.getKey()).getRank(countryRankPair.getValue())
                                .setIcon("BARRIER");
                    }

                    ItemStack icon = event.getCurrentItem();
                    ItemMeta iconMeta = icon.getItemMeta();

                    iconMeta.setDisplayName(countryRankPair.getValue());
                    icon.setItemMeta(iconMeta);

                    Country.getCountryByName(countryRankPair.getKey()).getRank(countryRankPair.getValue()).setIcon(icon.getType().name());

                    senderPlayer.playSound(
                            senderPlayer.getLocation(),
                            Sound.ENTITY_PLAYER_LEVELUP,
                            50,
                            1);

                    Country.getCountryByName(countryRankPair.getKey()).updateRank((Country
                            .getCountryByName(countryRankPair.getKey()).getRank(countryRankPair.getValue())));

                    event.setCancelled(true);

                    return;
                }
            }

            pageNumber++;
        }
    }
}
