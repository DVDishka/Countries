package dvdcraft.countries.common;

import dvdcraft.countries.common.Classes.Country;

import java.util.HashSet;
import java.util.logging.Logger;

import static org.bukkit.Bukkit.getLogger;

public class CommonVariables {
    public static final Logger logger = getLogger();
    public static HashSet <Country> countries = new HashSet<Country>();
}
