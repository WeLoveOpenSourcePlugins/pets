package de.cerus.wlosp.pets.task;

import de.cerus.wlosp.pets.pet.PetEntityController;
import de.cerus.wlosp.pets.pet.PetEntityData;
import de.cerus.wlosp.pets.pet.PetFlag;
import net.minecraft.server.v1_15_R1.EntityInsentient;
import net.minecraft.server.v1_15_R1.NavigationAbstract;
import net.minecraft.server.v1_15_R1.PathEntity;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftLivingEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Map;
import java.util.UUID;

public class PetFollowTask extends BukkitRunnable {

    private final JavaPlugin plugin;
    private final PetEntityController petEntityController;

    public PetFollowTask(JavaPlugin plugin, PetEntityController petEntityController) {
        this.plugin = plugin;
        this.petEntityController = petEntityController;
    }

    @Override
    public void run() {
        for (Map.Entry<UUID, PetEntityData<?>> entry : petEntityController.getPetEntityDataMap().entrySet()) {
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
    }

}
