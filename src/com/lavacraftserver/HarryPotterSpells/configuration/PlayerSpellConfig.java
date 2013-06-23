package com.lavacraftserver.HarryPotterSpells.configuration;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Level;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginManager;

import com.lavacraftserver.HarryPotterSpells.HPS;
import com.lavacraftserver.HarryPotterSpells.Jobs.EnableJob;

/**
 * A class that manages the {@code PlayerSpellConfig.yml} file. <br>
 * This file is used to store all the spells that players know.
 */
public class PlayerSpellConfig extends CustomConfiguration implements EnableJob {

    /**
     * Constructs a new {@link PlayerSpellConfig} without copying any defaults
     * @param file where to store the custom configuration
     */
    public PlayerSpellConfig(File file) {
        super(file);
    }

    /**
     * Constructs a new {@link CustomConfiguration}, copying defaults from an {@link InputStream}
     * @param file where to store the custom configuration
     * @param stream an input stream to copy default configuration from
     */
    public PlayerSpellConfig(File file, InputStream stream) {
        super(file, stream);
    }

	/**
	 * The current version specifying the format of the {@code PlayerSpellConfig.yml}
	 */
	public static final double CURRENT_VERSION = 0.5d;

	/**
	 * A utility function that is meant to be used instead of {@link FileConfiguration#getStringList(String)}. <br>
	 * This is needed because the Bukkit version returns {@code null} when no String list is found.
	 * @param string the path to the String list
	 * @return the string list at that location or an empty {@link ArrayList}
	 */
	public List<String> getStringListOrEmpty(String string) {
	    return get().getStringList(string) == null ? new ArrayList<String>() : get().getStringList(string);
	}

	@Override
	public void onEnable(PluginManager pm) {
	    Double version = get().getDouble("VERSION_DO_NOT_EDIT", -1d) == -1d ? null : get().getDouble("VERSION_DO_NOT_EDIT", -1d);

	    if(version == null || version < CURRENT_VERSION) {
	        // STORE UPDATES HERE

	        if(version == null) { // Updating from unformatted version to version 0.4
	            HPS.PM.log(Level.INFO, HPS.Localisation.getTranslation("pscOutOfDate"));

	            Map<String, List<String>> newConfig = new HashMap<String, List<String>>(), lists = new HashMap<String, List<String>>();
	            Set<String> css = new HashSet<String>();

	            for(String s : get().getKeys(false)) // This seemingly stupid code is to avoid a ConcurrencyModificationException (google it)
	                css.add(s);

	            for(String s : css)
	                lists.put(s, get().getStringList(s));

	            for(String cs : css) {
	                List<String> list = new ArrayList<String>();
	                for(String spellString : get().getStringList(cs)) {
	                    if(spellString.equals("AlarteAscendare")) {
                            list.add("Alarte Ascendare");
	                    } else if(spellString.equals("AvadaKedavra")) {
	                        list.add("Avada Kedavra");
	                    } else if(spellString.equals("FiniteIncantatem")) {
                            list.add("Finite Incantatem");
	                    } else if(spellString.equals("MagnaTonitrus")) {
                            list.add("Magna Tonitrus");
                        } else if(spellString.equals("PetrificusTotalus")) {
                            list.add("Petrificus Totalus");
                        } else if(spellString.equals("TimeSpell")) {
	                        list.add("Time");
	                    } else if(spellString.equals("TreeSpell")) {
                            list.add("Tree");
	                    } else if(spellString.equals("WingardiumLeviosa")) {
                            list.add("Wingardium Leviosa");
	                    } else {
	                        list.add(spellString);
	                    }
	                }

	                newConfig.put(cs, list);
	            }

	            for(Entry<String, List<String>> ent : newConfig.entrySet())
	                get().set(ent.getKey(), ent.getValue());

	            get().set("VERSION_DO_NOT_EDIT", 0.4d);
	            save();

	            HPS.PM.log(Level.INFO, HPS.Localisation.getTranslation("pscUpdated", "0.4"));
	        }
	    }
	}

}