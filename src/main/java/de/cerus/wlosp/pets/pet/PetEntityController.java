package de.cerus.wlosp.pets.pet;

import de.cerus.wlosp.pets.task.PetFollowTask;
import de.cerus.wlosp.pets.task.PetTeleportTask;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

public class PetEntityController implements Listener {

    private final Map<UUID, PetEntityData<?>> petEntityDataMap = new HashMap<>();
    private final BukkitTask teleportTask;
    private final BukkitTask followTask;

    public PetEntityController(JavaPlugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);

        this.teleportTask = getTeleportTask(plugin).runTaskTimer(plugin, 0, 4);
        this.followTask = getFollowTask(plugin).runTaskTimer(plugin, 0, 4);
    }

    private BukkitRunnable getTeleportTask(JavaPlugin plugin) {
        return new PetTeleportTask(plugin, this);
    }

    private BukkitRunnable getFollowTask(JavaPlugin plugin) {
        return new PetFollowTask(plugin, this);
    }

    public <T extends LivingEntity> boolean spawnPet(Player player, Pet<T> pet) {
        if (hasPetSpawned(player)) {
            return false;
        }

        LivingEntity entity = player.getWorld().spawn(player.getLocation(), pet.getEntityClass());
        entity.setCustomName(player.getDisplayName() + "§r's pet");
        entity.setCustomNameVisible(true);

        petEntityDataMap.put(entity.getUniqueId(), new PetEntityData(player.getUniqueId(),
                entity.getUniqueId(), entity.getWorld(), pet));

        pet.petSpawned((T) entity, player);
        return true;
    }

    public <T extends LivingEntity> boolean despawnPet(Player player) {
        if (!hasPetSpawned(player)) {
            return false;
        }

        PetEntityData<T> data = getData(player);
        LivingEntity petEntity = data.getWorld().getLivingEntities().stream()
                .filter(livingEntity -> livingEntity.getUniqueId().equals(data.getPetEntityUuid()))
                .findAny().orElse(null);
        if (petEntity == null) {
            return false;
        }

        data.getPet().petDespawned((T) petEntity, player);

        petEntityDataMap.remove(petEntity.getUniqueId());
        petEntity.remove();
        return true;
    }

    public boolean hasPetSpawned(Player player) {
        return petEntityDataMap.values().stream()
                .anyMatch(petEntityData -> petEntityData.getPlayerUuid().equals(player.getUniqueId()));
    }

    private <T extends LivingEntity> PetEntityData<T> getData(Player player) {
        return (PetEntityData<T>) petEntityDataMap.values().stream()
                .filter(petEntityData -> petEntityData.getPlayerUuid().equals(player.getUniqueId()))
                .findAny().orElse(null);
    }

    @EventHandler
    private void onEntityTarget(EntityTargetLivingEntityEvent event) {
        Entity entity = event.getEntity();
        if (!petEntityDataMap.containsKey(entity.getUniqueId())) {
            return;
        }

        PetEntityData<?> data = petEntityDataMap.get(entity.getUniqueId());
        if ((data.getPet().getFlagInteger() & PetFlag.PEACEFUL.getFlag()) == 0) {
            return;
        }

        event.setTarget(null);
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (!petEntityDataMap.containsKey(event.getEntity().getUniqueId())) {
            return;
        }

        event.setCancelled(true);
    }

    public void shutdown() {
        for (Map.Entry<UUID, PetEntityData<?>> entry : new HashSet<>(petEntityDataMap.entrySet())) {
            Player player = Bukkit.getPlayer(entry.getValue().getPlayerUuid());
            despawnPet(player);
        }

        teleportTask.cancel();
        followTask.cancel();
    }

    public Map<UUID, PetEntityData<?>> getPetEntityDataMap() {
        return Collections.unmodifiableMap(petEntityDataMap);
    }

}
