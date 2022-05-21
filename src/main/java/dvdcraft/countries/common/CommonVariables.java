package dvdcraft.countries.common;

import dvdcraft.countries.common.Classes.Country;
import org.bukkit.Bukkit;
import org.bukkit.scoreboard.Team;

import java.util.HashMap;
import java.util.HashSet;
import java.util.logging.Logger;

import static org.bukkit.Bukkit.getLogger;

public class CommonVariables {
    public static final Logger logger = getLogger();
    public static HashSet<Country> countries = new HashSet<>();
    public static HashMap<String, Team> teams = new HashMap<>();
    public static HashMap<String, Country> requests = new HashMap<>();

    public static void addTeam(String teamName) {
        try {
            CommonVariables.teams.put(teamName, Bukkit.getScoreboardManager().getMainScoreboard().registerNewTeam(teamName));
        } catch (Exception e) {
            CommonVariables.teams.put(teamName, Bukkit.getScoreboardManager().getMainScoreboard().getTeam(teamName));
        }
    }
}
