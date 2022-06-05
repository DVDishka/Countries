package dvdcraft.countries.Threads;

import dvdcraft.countries.common.Classes.Country;
import dvdcraft.countries.common.CommonVariables;
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

public class TimerProcess extends Thread {

    private int time;
    private Country country;
    private Country oppositeCountry;

    public TimerProcess(int time, Country country, Country oppositeCountry) {
        this.time = time;
        this.country = country;
        this.oppositeCountry = oppositeCountry;
    }

    public void run() {
        BossBar bossBar = Bukkit.getServer().createBossBar("", BarColor.RED, BarStyle.SOLID);
        bossBar.setVisible(true);
        try {
            while (time > 0) {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (!bossBar.getPlayers().contains(player) && Country.getCountry(player.getName()) == this.country ||
                            !bossBar.getPlayers().contains(player) && Country.getCountry(player.getName()) == this.oppositeCountry) {
                        bossBar.addPlayer(player);
                    }
                }
                int hours = time / 3600;
                int minutes = (time - (hours * 3600)) / 60;
                int seconds = time % 60;
                bossBar.setTitle(hours + ":" + minutes + ":" + seconds);
                time--;
                //sleep(1000);
            }
        } catch (Exception e) {
            CommonVariables.logger.warning(ChatColor.RED + "Something went wrong in Timer Process");
        }
        bossBar.removeAll();
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
        Objective objective = scoreboard.registerNewObjective("War_" + country.getName() + "_vs_" + oppositeCountry.getName(),
                "dummy", "War");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        for (String member : country.getMembers()) {
            if (Bukkit.getOfflinePlayer(member).isOnline()) {
                Score score = objective.getScore(Bukkit.getOfflinePlayer(member));
                score.setScore(3);
            }
        }
        for (String member : oppositeCountry.getMembers()) {
            if (Bukkit.getOfflinePlayer(member).isOnline()) {
                Score score = objective.getScore(Bukkit.getOfflinePlayer(member));
                score.setScore(3);
            }
        }
    }
}
