package de.cerus.wlosp.pets.pet;

import org.bukkit.World;
import org.bukkit.entity.LivingEntity;

import java.util.UUID;

public class PetEntityData<T extends LivingEntity> {

    private final UUID playerUuid;
    private final UUID petEntityUuid;
    private final World world;
    private final Pet<T> pet;

    public PetEntityData(UUID playerUuid, UUID petEntityUuid, World world, Pet<T> pet) {
        this.playerUuid = playerUuid;
        this.petEntityUuid = petEntityUuid;
        this.world = world;
        this.pet = pet;
    }

    public UUID getPlayerUuid() {
        return playerUuid;
    }

    public UUID getPetEntityUuid() {
        return petEntityUuid;
    }

    public World getWorld() {
        return world;
    }

    public Pet<T> getPet() {
        return pet;
    }

}
