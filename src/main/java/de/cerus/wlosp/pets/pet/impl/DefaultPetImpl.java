package de.cerus.wlosp.pets.pet.impl;

import de.cerus.wlosp.pets.pet.Pet;
import de.cerus.wlosp.pets.pet.PetFlag;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class DefaultPetImpl<T extends LivingEntity> extends Pet<T> {

    public DefaultPetImpl(JavaPlugin plugin, Class<T> entityClass) {
        this(plugin, entityClass, new PetFlag[]{PetFlag.PEACEFUL,
                PetFlag.FOLLOW_OWNER, PetFlag.TELEPORT_WHEN_AWAY});
    }

    public DefaultPetImpl(JavaPlugin plugin, Class<T> entityClass, PetFlag[] flags) {
        super(plugin, entityClass.getSimpleName().split("\\.")[0], entityClass, flags);
    }

    @Override
    public void petSpawned(T spawnedEntity, Player owner) {
        owner.sendMessage("§aYour pet was spawned!");
    }

    @Override
    public void petDespawned(T spawnedEntity, Player owner) {
        owner.sendMessage("§aYour pet was despawned!");
    }

}
