package de.cerus.wlosp.pets.command;

import de.cerus.wlosp.pets.pet.PetEntityController;
import de.cerus.wlosp.pets.pet.PetRegistry;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Cat;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;

public class PetCommand implements CommandExecutor {

    private final PetRegistry petRegistry;
    private final PetEntityController petEntityController;

    public PetCommand(PetRegistry petRegistry, PetEntityController petEntityController) {
        this.petRegistry = petRegistry;
        this.petEntityController = petEntityController;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (!(commandSender instanceof Player)) {
            return true;
        }
        Player player = (Player) commandSender;

        if (petEntityController.hasPetSpawned(player)) {
            petEntityController.despawnPet(player);
        } else {
            petEntityController.spawnPet(player, petRegistry.getPetImplementation(Cat.class).get());
        }
        return true;
    }
}
