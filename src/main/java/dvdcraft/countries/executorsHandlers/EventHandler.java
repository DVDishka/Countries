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

import java.util.HashSet;

public class EventHandler implements Listener {

    @org.bukkit.event.EventHandler
    public void deathEvent(PlayerDeathEvent event) {
        Player player = event.getPlayer();
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
                            score.resetScore();
                        }
                        int liveCountryCounter = 0;
                        Objective objective = Bukkit.getScoreboardManager().getMainScoreboard().getObjective(scoreboardName);
                        for (Country country : countryHashSet) {
                            boolean flag = false;
                            for (String member : country.getMembers()) {
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
                                for (String member : country.getMembers()) {
                                    Player loosePlayer = Bukkit.getPlayer(member);
                                    if (loosePlayer != null) {
                                        loosePlayer.sendTitle(Title.builder().title(ChatColor.RED + "You Lose!").build());
                                    }
                                }
                            }
                        }
                        if (liveCountryCounter == 1) {
                            for (Country country : countryHashSet) {
                                boolean flag = false;
                                for (String member : country.getMembers()) {
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
                                        for (String member : wonCountryCheck.getMembers()) {
                                            Player loosePlayer = Bukkit.getPlayer(member);
                                            if (loosePlayer != null) {
                                                loosePlayer.sendTitle(Title.builder().title(ChatColor.RED + country.getName()
                                                        + " Won!").build());
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
                        score.resetScore();
                        int liveCounryCounter = 0;
                        for (Country country : countryHashSet) {
                            boolean flag = false;
                            for (String member : country.getMembers()) {
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
                                for (String member : country.getMembers()) {
                                    Player loosePlayer = Bukkit.getPlayer(member);
                                    if (loosePlayer != null) {
                                        loosePlayer.sendTitle(Title.builder().title(ChatColor.RED + "You Lose!").build());
                                    }
                                }
                            }
                        }
                        if (liveCounryCounter == 1) {
                            for (Country country : countryHashSet) {
                                boolean flag = false;
                                for (String member : country.getMembers()) {
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
                                    for (String member : country.getMembers()) {
                                        Player loosePlayer = Bukkit.getPlayer(member);
                                        if (loosePlayer != null) {
                                            loosePlayer.sendTitle(Title.builder().title(ChatColor.RED + "You Won!").build());
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
}
