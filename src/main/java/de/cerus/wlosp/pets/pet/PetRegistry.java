package de.cerus.wlosp.pets.pet;

import org.bukkit.entity.LivingEntity;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;
import java.util.stream.Collectors;

public class PetRegistry {

    private final Set<Pet<?>> registeredPets = new HashSet<>();

    public void registerPetImplementations(Pet<?>... pet) {
        registeredPets.addAll(Arrays.asList(pet));
    }

    public void unregisterPetImplementation(Pet<?> pet) {
        registeredPets.remove(pet);
    }

    public void unregisterAllPetImplementations(JavaPlugin plugin) {
        new ArrayList<>(registeredPets).stream().filter(pet -> pet.getPlugin() == plugin)
                .forEach(registeredPets::remove);
    }

    public Optional<Pet<?>> getPetImplementation(Class<? extends LivingEntity> entityClass) {
        return registeredPets.stream().filter(pet -> pet.getEntityClass() == entityClass).findAny();
    }

    public Set<Pet<?>> getPetImplementationsOfPlugin(JavaPlugin plugin) {
        return registeredPets.stream().filter(pet -> pet.getPlugin() == plugin).collect(Collectors.toSet());
    }

}
