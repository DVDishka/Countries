package ru.dvdishka.countries.Threads;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import ru.dvdishka.countries.Classes.Owner;
import ru.dvdishka.countries.common.CommonVariables;
import ru.dvdishka.countries.Classes.Country;
import ru.dvdishka.countries.Classes.Territory;
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

                            player.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                                    TextComponent.fromLegacyText(country.getChatColor() + country.getName() + " " + owner.getName()));
                        } else {

                            player.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                                    TextComponent.fromLegacyText(owner.getName()));
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
