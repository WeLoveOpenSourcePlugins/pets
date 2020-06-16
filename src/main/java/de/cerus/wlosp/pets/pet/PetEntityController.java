package de.cerus.wlosp.pets.pet;

import net.minecraft.server.v1_15_R1.EntityInsentient;
import net.minecraft.server.v1_15_R1.NavigationAbstract;
import net.minecraft.server.v1_15_R1.PathEntity;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftZombie;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PetEntityController implements Listener {

    private final Map<UUID, PetEntityData<?>> petEntityDataMap = new HashMap<>();
    private final BukkitTask teleportTask;
    private final BukkitTask followTask;

    public PetEntityController(JavaPlugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);

        //TODO: Move tasks into separate classes
        this.teleportTask = getTeleportTask(plugin);
        this.followTask = getFollowTask(plugin);
    }

    private BukkitTask getTeleportTask(JavaPlugin plugin) {
        return plugin.getServer().getScheduler().runTaskTimer(plugin, () -> {
            for (Map.Entry<UUID, PetEntityData<?>> entry : petEntityDataMap.entrySet()) {
                if ((entry.getValue().getPet().getFlagInteger() & PetFlag.TELEPORT_WHEN_AWAY.getFlag()) == 0) {
                    continue;
                }

                LivingEntity livingEntity = entry.getValue().getWorld().getLivingEntities().stream()
                        .filter(ent -> ent.getUniqueId().equals(entry.getKey())).findAny().orElse(null);
                if (livingEntity == null) {
                    continue;
                }

                Player owner = plugin.getServer().getPlayer(entry.getValue().getPlayerUuid());
                if (owner == null) {
                    continue;
                }

                if (livingEntity.getLocation().distanceSquared(owner.getLocation()) < Math.pow(25, 2)) {
                    continue;
                }

                livingEntity.teleport(owner);
            }
        }, 0, 4);
    }

    private BukkitTask getFollowTask(JavaPlugin plugin) {
        return plugin.getServer().getScheduler().runTaskTimer(plugin, () -> {
            for (Map.Entry<UUID, PetEntityData<?>> entry : petEntityDataMap.entrySet()) {
                if ((entry.getValue().getPet().getFlagInteger() & PetFlag.FOLLOW_OWNER.getFlag()) == 0) {
                    continue;
                }

                LivingEntity livingEntity = entry.getValue().getWorld().getLivingEntities().stream()
                        .filter(ent -> ent.getUniqueId().equals(entry.getKey())).findAny().orElse(null);
                if (livingEntity == null) {
                    continue;
                }

                Player owner = plugin.getServer().getPlayer(entry.getValue().getPlayerUuid());
                if (owner == null) {
                    continue;
                }

                NavigationAbstract navigation = ((EntityInsentient) ((CraftLivingEntity) livingEntity).getHandle())
                        .getNavigation();

                if(livingEntity.getLocation().distanceSquared(owner.getLocation()) < Math.pow(3, 2)) {
                    navigation.c();
                    continue;
                }

                PathEntity pathEntity = navigation.a(((CraftEntity) owner).getHandle(), 0);
                navigation.a(pathEntity, 1.0);
                navigation.a(1.25);
            }
        }, 0, 4);
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
        if(!petEntityDataMap.containsKey(event.getEntity().getUniqueId())) {
            return;
        }

        event.setCancelled(true);
    }

    public void shutdown() {
        teleportTask.cancel();
    }

}
