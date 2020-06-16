package de.cerus.wlosp.pets;

import de.cerus.wlosp.pets.command.PetCommand;
import de.cerus.wlosp.pets.listener.PlayerListener;
import de.cerus.wlosp.pets.pet.PetEntityController;
import de.cerus.wlosp.pets.pet.PetRegistry;
import de.cerus.wlosp.pets.pet.impl.CatPetImpl;
import de.cerus.wlosp.pets.pet.impl.ZombiePetImpl;
import org.bukkit.plugin.java.JavaPlugin;

public class PetsPlugin extends JavaPlugin {

    private PetRegistry petRegistry;
    private PetEntityController petEntityController;

    @Override
    public void onEnable() {
        petRegistry = new PetRegistry();
        petEntityController = new PetEntityController(this);

        petRegistry.registerPetImplementations(
                new ZombiePetImpl(this),
                new CatPetImpl(this)
        );

        getServer().getPluginManager().registerEvents(new PlayerListener(petEntityController), this);
        getCommand("pet").setExecutor(new PetCommand(petRegistry, petEntityController));
    }

    @Override
    public void onDisable() {
        petRegistry.unregisterAllPetImplementations();
        petEntityController.shutdown();
    }

}
