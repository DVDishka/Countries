package dvdcraft.countries.Threads;

import dvdcraft.countries.common.Classes.Owner;
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
                for (Owner owner : CommonVariables.owners.values()) {
                    Territory territory = owner.getTerritory();
                    Location location = player.getLocation();
                    Country country = Country.getCountry(owner.getName());
                    if (location.getBlockX() >= territory.getFromX() && location.getBlockX() <= territory.getToX() &&
                            location.getBlockZ() >= territory.getFromZ() && location.getBlockZ() <= territory.getToZ() &&
                            location.getWorld() == Bukkit.getWorld("world")) {
                        if (country != null) {
                            player.sendActionBar(country.getChatColor() + country.getName() + " " + owner.getName());
                        } else {
                            player.sendActionBar(owner.getName());
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
