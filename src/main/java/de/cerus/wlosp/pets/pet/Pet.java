package de.cerus.wlosp.pets.pet;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class Pet<T extends LivingEntity> implements Listener {

    private final JavaPlugin plugin;
    private final Class<? extends LivingEntity> entityClass;
    private PetFlag[] flags;

    public Pet(JavaPlugin plugin, Class<T> entityClass) {
        this(plugin, entityClass, new PetFlag[0]);
    }

    protected Pet(JavaPlugin plugin, Class<T> entityClass, PetFlag[] flags) {
        this.plugin = plugin;
        this.entityClass = entityClass;
        this.flags = flags;
        this.plugin.getServer().getPluginManager().registerEvents(this, this.plugin);
    }

    public abstract void petSpawned(T spawnedEntity, Player owner);

    public abstract void petDespawned(T spawnedEntity, Player owner);

    public void setFlags(PetFlag[] flags) {
        this.flags = flags;
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
}
