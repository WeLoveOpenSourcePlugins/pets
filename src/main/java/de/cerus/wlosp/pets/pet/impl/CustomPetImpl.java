package de.cerus.wlosp.pets.pet.impl;

import de.cerus.wlosp.pets.pet.Pet;
import de.cerus.wlosp.pets.pet.PetFlag;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class CustomPetImpl extends Pet<LivingEntity> {

    public CustomPetImpl(JavaPlugin plugin, String name, Class<? extends LivingEntity> entityClass, PetFlag[] flags) {
        super(plugin, name, entityClass, flags);
    }

    @Override
    public void petSpawned(LivingEntity spawnedEntity, Player owner) {
        //TODO
    }

    @Override
    public void petDespawned(LivingEntity spawnedEntity, Player owner) {
        //TODO
    }
}
