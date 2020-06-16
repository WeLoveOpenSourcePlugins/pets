package de.cerus.wlosp.pets.file;

import de.cerus.wlosp.pets.PetsPlugin;
import de.cerus.wlosp.pets.pet.impl.CustomPetImpl;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Set;

public class PetFileLoader {

    private final JavaPlugin plugin = JavaPlugin.getPlugin(PetsPlugin.class);
    private final File dir = new File(plugin.getDataFolder(), File.separator + "custom-pets");

    public PetFileLoader() {
        dir.mkdirs();
    }

    public Set<CustomPetImpl> loadPetFiles() {
        File[] files = dir.listFiles();
        if (files == null) {
            return new HashSet<>();
        }

        Set<CustomPetImpl> set = new HashSet<>();
        for (File file : files) {
            PetFile petFile = new PetFile(file);
            try {
                set.add(petFile.loadPetFromFile(plugin));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        return set;
    }

}
