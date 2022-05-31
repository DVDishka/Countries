package dvdcraft.countries.executorsHandlers;

import dvdcraft.countries.common.Classes.Country;
import dvdcraft.countries.common.CommonVariables;
import it.unimi.dsi.fastutil.Pair;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.scoreboard.Score;

public class EventHandler implements Listener {

    @org.bukkit.event.EventHandler
    public void deathEvent(PlayerDeathEvent event) {
        Player player = event.getPlayer();
        for (Pair<Country, Country> countryPair : CommonVariables.wars) {
            if (Country.getCountry(player.getName()) == countryPair.first() ||
            Country.getCountry(player.getName()) == countryPair.second()) {
                Score score = Bukkit.getScoreboardManager().getMainScoreboard().getObjective("War").getScore(player);
                score.setScore(score.getScore() - 1);
                if (score.getScore() == 0) {
                    score.resetScore();
                    player.banPlayer("War is over for you");
                }
            }
        }
    }
}
