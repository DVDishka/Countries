package dvdcraft.countries.executorsHandlers;

import com.destroystokyo.paper.Title;
import dvdcraft.countries.common.Classes.Country;
import dvdcraft.countries.common.CommonVariables;
import it.unimi.dsi.fastutil.Pair;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;

public class EventHandler implements Listener {

    @org.bukkit.event.EventHandler
    public void deathEvent(PlayerDeathEvent event) {
        Player player = event.getPlayer();
        for (Pair<Country, Country> countryPair : CommonVariables.wars) {
            if (Country.getCountry(player.getName()) == countryPair.first() ||
            Country.getCountry(player.getName()) == countryPair.second()) {
                try {
                    Score score = Bukkit.getScoreboardManager().getMainScoreboard().getObjective("War_" + countryPair.first().getName() +
                            "_vs_" + countryPair.second().getName()).getScore(player);
                    score.setScore(score.getScore() - 1);
                    if (score.getScore() == 0) {
                        score.resetScore();
                    }
                    boolean flag = false;
                    Objective objective = Bukkit.getScoreboardManager().getMainScoreboard().getObjective("War_" +
                            countryPair.first().getName() + "_vs_" + countryPair.second().getName());
                    for (String member : countryPair.first().getMembers()) {
                        OfflinePlayer offlineMember = Bukkit.getOfflinePlayer(member);
                        try {
                            Score memberScore = objective.getScore(offlineMember);
                            if (memberScore.getScore() != 0) {
                                flag = true;
                            }
                        } catch (Exception e) {
                            continue;
                        }
                    }
                    if (!flag) {
                        for (String member : countryPair.first().getMembers()) {
                            Player loosePlayer = Bukkit.getPlayer(member);
                            if (loosePlayer != null) {
                                loosePlayer.sendTitle(Title.builder().title(ChatColor.RED + "You Lose!").build());
                            }
                        }
                        for (String member : countryPair.second().getMembers()) {
                            Player winPlayer = Bukkit.getPlayer(member);
                            if (winPlayer != null) {
                                winPlayer.sendTitle(Title.builder().title(ChatColor.GOLD + "You Won!").build());
                            }
                        }
                        CommonVariables.wars.remove(countryPair);
                        objective.unregister();
                        return;
                    }
                    flag = false;
                    for (String member : countryPair.second().getMembers()) {
                        OfflinePlayer offlineMember = Bukkit.getOfflinePlayer(member);
                        try {
                            Score memberScore = objective.getScore(offlineMember);
                            if (memberScore.getScore() != 0) {
                                flag = true;
                            }
                        } catch (Exception e) {
                            continue;
                        }
                    }
                    if (!flag) {
                        for (String member : countryPair.first().getMembers()) {
                            Player loosePlayer = Bukkit.getPlayer(member);
                            if (loosePlayer != null) {
                                loosePlayer.sendTitle(Title.builder().title(ChatColor.GOLD + "You Won!").build());
                            }
                        }
                        for (String member : countryPair.second().getMembers()) {
                            Player winPlayer = Bukkit.getPlayer(member);
                            if (winPlayer != null) {
                                winPlayer.sendTitle(Title.builder().title(ChatColor.RED + "You Lose!").build());
                            }
                        }
                        CommonVariables.wars.remove(countryPair);
                        objective.unregister();
                        return;
                    }
                } catch (Exception e) {
                    return;
                }
            }
        }
    }

    @org.bukkit.event.EventHandler
    public void playerQuitEvent(PlayerQuitEvent event) {
        for (Pair<Country, Country> countryPair : CommonVariables.wars) {
            try {
                if (Country.getCountry(event.getPlayer().getName()).getName().equals(countryPair.first().getName()) ||
                        Country.getCountry(event.getPlayer().getName()).getName().equals(countryPair.second().getName())) {
                    Score score = Bukkit.getScoreboardManager().getMainScoreboard().getObjective("War_" +
                            countryPair.first().getName() + "_vs_" + countryPair.second().getName()).getScore(event.getPlayer());
                    score.resetScore();
                    boolean flag = false;
                    Objective objective = Bukkit.getScoreboardManager().getMainScoreboard().getObjective("War_" +
                            countryPair.first().getName() + "_vs_" + countryPair.second().getName());
                    for (String member : countryPair.first().getMembers()) {
                        OfflinePlayer offlineMember = Bukkit.getOfflinePlayer(member);
                        try {
                            Score memberScore = objective.getScore(offlineMember);
                            if (memberScore.getScore() != 0) {
                                flag = true;
                            }
                        } catch (Exception e) {
                            continue;
                        }
                    }
                    if (!flag) {
                        for (String member : countryPair.first().getMembers()) {
                            Player loosePlayer = Bukkit.getPlayer(member);
                            if (loosePlayer != null) {
                                loosePlayer.sendTitle(Title.builder().title(ChatColor.RED + "You Lose!").build());
                            }
                        }
                        for (String member : countryPair.second().getMembers()) {
                            Player winPlayer = Bukkit.getPlayer(member);
                            if (winPlayer != null) {
                                winPlayer.sendTitle(Title.builder().title(ChatColor.GOLD + "You Won!").build());
                            }
                        }
                        CommonVariables.wars.remove(countryPair);
                        objective.unregister();
                        return;
                    }
                    flag = false;
                    for (String member : countryPair.second().getMembers()) {
                        OfflinePlayer offlineMember = Bukkit.getOfflinePlayer(member);
                        try {
                            Score memberScore = objective.getScore(offlineMember);
                            if (memberScore.getScore() != 0) {
                                flag = true;
                            }
                        } catch (Exception e) {
                            continue;
                        }
                    }
                    if (!flag) {
                        for (String member : countryPair.first().getMembers()) {
                            Player loosePlayer = Bukkit.getPlayer(member);
                            if (loosePlayer != null) {
                                loosePlayer.sendTitle(Title.builder().title(ChatColor.GOLD + "You Won!").build());
                            }
                        }
                        for (String member : countryPair.second().getMembers()) {
                            Player winPlayer = Bukkit.getPlayer(member);
                            if (winPlayer != null) {
                                winPlayer.sendTitle(Title.builder().title(ChatColor.RED + "You Lose!").build());
                            }
                        }
                        CommonVariables.wars.remove(countryPair);
                        objective.unregister();
                        return;
                    }
                }
            } catch (Exception e) {
                return;
            }
        }
    }
}
