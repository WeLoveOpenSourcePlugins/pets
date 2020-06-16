package de.cerus.wlosp.pets.pet;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class Pet<T extends LivingEntity> {

    private final JavaPlugin plugin;
    private final String name;
    private final Class<? extends LivingEntity> entityClass;
    private PetFlag[] flags;

    public Pet(JavaPlugin plugin, Class<T> entityClass, String name) {
        this(plugin, name, entityClass, new PetFlag[0]);
    }

    protected Pet(JavaPlugin plugin, String name, Class<T> entityClass, PetFlag[] flags) {
        this.plugin = plugin;
        this.name = name;
        this.entityClass = entityClass;
        this.flags = flags;
    }

    public abstract void petSpawned(T spawnedEntity, Player owner);

    public abstract void petDespawned(T spawnedEntity, Player owner);

    public void setFlags(PetFlag[] flags) {
        this.flags = flags;
    }

    public String getName() {
        return name;
    }

    public Class<? extends LivingEntity> getEntityClass() {
        return entityClass;
    }

    public JavaPlugin getPlugin() {
        return plugin;
    }

    public PetFlag[] getFlags() {
        return flags;
    }

    public int getFlagInteger() {
        int i = 0;
        for (PetFlag flag : flags) {
            i = i | flag.getFlag();
        }
        return i;
    }
}
