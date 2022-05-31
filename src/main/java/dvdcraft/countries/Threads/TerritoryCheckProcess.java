package dvdcraft.countries.Threads;

import dvdcraft.countries.common.CommonVariables;
import dvdcraft.countries.common.Classes.Country;
import dvdcraft.countries.common.Classes.Territory;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class TerritoryCheckProcess extends Thread {

    public TerritoryCheckProcess(String name) {
        super(name);
    }

    public void run() {
        while (true) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                for (Country country : CommonVariables.countries) {
                    for (Territory territory : country.getTerritories()) {
                        Location location = player.getLocation();
                        if (location.getBlockX() >= territory.getFromX() && location.getBlockX() <= territory.getToX() &&
                                location.getBlockZ() >= territory.getFromZ() && location.getBlockZ() <= territory.getToZ()) {
                            player.sendActionBar(country.getChatColor() + country.getName() + " territory");
                        }
                    }
                }
            }
            try {
                sleep(1000L);
            } catch (Exception e) {
                CommonVariables.logger.warning("Thread exception");
            }
        }
    }
}
