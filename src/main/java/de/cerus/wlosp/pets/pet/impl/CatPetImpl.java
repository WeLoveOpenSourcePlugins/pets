package de.cerus.wlosp.pets.pet.impl;

import org.bukkit.DyeColor;
import org.bukkit.entity.Cat;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.concurrent.ThreadLocalRandom;

public class CatPetImpl extends DefaultPetImpl<Cat> {

    public CatPetImpl(JavaPlugin plugin) {
        super(plugin, Cat.class);
    }

    @Override
    public void petSpawned(Cat entity, Player owner) {
        entity.setCatType(Cat.Type.values()[ThreadLocalRandom.current().nextInt(0, Cat.Type.values().length)]);
        entity.setCollarColor(DyeColor.values()[ThreadLocalRandom.current().nextInt(0, DyeColor.values().length)]);

        super.petSpawned(entity, owner);
    }

}
