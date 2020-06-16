package de.cerus.wlosp.pets.pet.impl;

import org.bukkit.DyeColor;
import org.bukkit.entity.Cat;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.concurrent.ThreadLocalRandom;

public class DogPetImpl extends DefaultPetImpl<Wolf> {

    public DogPetImpl(JavaPlugin plugin) {
        super(plugin, "dog", Wolf.class);
    }

    @Override
    public void petSpawned(Wolf entity, Player owner) {
        entity.setTamed(true);
        entity.setCollarColor(DyeColor.values()[ThreadLocalRandom.current().nextInt(0, DyeColor.values().length)]);

        super.petSpawned(entity, owner);
    }

}
