package de.cerus.wlosp.pets.task;

import de.cerus.wlosp.pets.pet.PetEntityController;
import de.cerus.wlosp.pets.pet.PetEntityData;
import de.cerus.wlosp.pets.pet.PetFlag;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Map;
import java.util.UUID;

public class PetTeleportTask extends BukkitRunnable {

    private final JavaPlugin plugin;
    private final PetEntityController petEntityController;

    public PetTeleportTask(JavaPlugin plugin, PetEntityController petEntityController) {
        this.plugin = plugin;
        this.petEntityController = petEntityController;
    }

    @Override
    public void run() {
        for (Map.Entry<UUID, PetEntityData<?>> entry : petEntityController.getPetEntityDataMap().entrySet()) {
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
    }

}
