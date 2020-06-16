package de.cerus.wlosp.pets.model;

import de.cerus.wlosp.pets.pet.PetFlag;
import org.bukkit.entity.EntityType;

import java.util.Objects;

public class CustomPetModel {

    private final String name;
    private final EntityType entityType;
    private final PetFlag[] flags;
    //TODO: More stuff

    public CustomPetModel(String name, EntityType entityType, PetFlag[] flags) throws NullPointerException {
        this.name = Objects.requireNonNull(name);
        this.entityType = Objects.requireNonNull(entityType);
        this.flags = flags == null ? new PetFlag[0] : flags;
    }

    public String getName() {
        return name;
    }

    public EntityType getEntityType() {
        return entityType;
    }

    public PetFlag[] getFlags() {
        return flags;
    }

}
