package ru.dvdishka.countries.common;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import ru.dvdishka.countries.Classes.Country;
import ru.dvdishka.countries.Classes.Owner;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.logging.Logger;

import static org.bukkit.Bukkit.getLogger;

public class CommonVariables {

    public static final Logger logger = getLogger();

    public static HashSet<Country> countries = new HashSet<>();
    public static HashMap<String, Team> teams = new HashMap<>();
    public static HashMap<String, Country> requests = new HashMap<>();
    public static HashSet<HashSet<Country>> wars = new HashSet<>();
    public static HashMap<String, Owner> owners = new HashMap<>();
    public static HashMap<String, Inventory> rankEdit = new HashMap<>();
    public static HashMap<String, ArrayList<Inventory>> memberMenu = new HashMap<>();
    public static HashMap<String, String> memberMenuRequest = new HashMap<>();

    public static ItemStack prevPage;
    public static ItemStack nextPage;

    public static void addTeam(String teamName) {
        if (Bukkit.getScoreboardManager().getMainScoreboard().getTeam(teamName) != null) {
            CommonVariables.teams.put(teamName, Bukkit.getScoreboardManager().getMainScoreboard().getTeam(teamName));
        } else {
            CommonVariables.teams.put(teamName, Bukkit.getScoreboardManager().getMainScoreboard().registerNewTeam(teamName));
        }
    }
}
