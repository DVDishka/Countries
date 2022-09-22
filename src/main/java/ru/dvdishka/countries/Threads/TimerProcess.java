package ru.dvdishka.countries.Threads;

import ru.dvdishka.countries.Classes.Country;
import ru.dvdishka.countries.common.CommonVariables;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

import java.util.HashSet;

public class TimerProcess extends Thread {

    private int time;
    private HashSet<Country> warCountries;

    public TimerProcess(int time, HashSet<Country> warCountries) {
        this.time = time;
        this.warCountries = warCountries;
    }

    public void run() {

        BossBar bossBar = Bukkit.getServer().createBossBar("", BarColor.RED, BarStyle.SEGMENTED_10);
        bossBar.setVisible(true);
        final int fullTime = time;
        int stage = 1;
        try {
            while (time > 0) {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    for (Country warCountry : warCountries) {
                        if (!bossBar.getPlayers().contains(player) && Country.getCountry(player.getName()) == warCountry) {
                            bossBar.addPlayer(player);
                        }
                    }
                }
                int hours = time / 3600;
                int minutes = (time - (hours * 3600)) / 60;
                int seconds = time % 60;
                bossBar.setTitle(hours + ":" + minutes + ":" + seconds);
                time--;
                if (time % (fullTime / 10) == 0) {
                    if (stage >= 0.1) {
                        stage -= 0.1;
                        bossBar.setProgress(stage);
                    }
                }
                //sleep(1000);
            }
        } catch (Exception e) {
            CommonVariables.logger.warning(ChatColor.RED + "Something went wrong in Timer Process");
        }
        bossBar.removeAll();
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
        String scoreboardName = "War_";
        for (Country warCountry : warCountries) {
            scoreboardName += warCountry.getName() + "_vs_";
        }
        scoreboardName = scoreboardName.substring(0, scoreboardName.length() - 5);
        Objective objective = scoreboard.registerNewObjective(scoreboardName, "dummy", "War");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        for (Country warCountry : warCountries) {
            for (String member : warCountry.getStringMembers()) {
                if (Bukkit.getOfflinePlayer(member).isOnline()) {
                    Score score = objective.getScore(Bukkit.getOfflinePlayer(member));
                    score.setScore(3);
                }
            }
        }
    }
}
