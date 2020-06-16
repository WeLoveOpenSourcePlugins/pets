package de.cerus.wlosp.pets.command;

import de.cerus.wlosp.pets.pet.Pet;
import de.cerus.wlosp.pets.pet.PetEntityController;
import de.cerus.wlosp.pets.pet.PetRegistry;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

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

        if (args.length == 0) {
            if (petEntityController.hasPetSpawned(player)) {
                petEntityController.despawnPet(player);
                player.sendMessage("§aYour current pet was removed!");
                return true;
            }

            player.sendMessage("§cNo arguments were specified!");
            player.sendMessage("§7To spawn a pet type §e/pet <name>§7.");
            player.sendMessage("§7To list all the pets that are available to you type §e/pet list§7.");
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "list":
                handleListCommand(player);
                break;
            default:
                handleSpawnCommand(player, Arrays.copyOfRange(args, 1, args.length));
                break;
        }
        return true;
    }

    private void handleSpawnCommand(Player player, String[] args) {
        if (args.length == 0) {
            player.sendMessage("§cPlease specify a pet name!");
            return;
        }

        String name = String.join(" ", args);

        Optional<Pet<?>> petOptional = petRegistry.getPetImplementation(name);
        if (!petOptional.isPresent()) {
            player.sendMessage("§cThe specified pet was not found!");
            return;
        }

        Pet<?> pet = petOptional.get();
        if (!player.hasPermission("pet." + pet.getName())) {
            player.sendMessage("§cYou are not allowed to use this pet!");
            return;
        }

        boolean success = petEntityController.spawnPet(player, pet);
        player.sendMessage(success ? "§aYour pet was successfully spawned!" : "§cFailed to spawn your pet!");
    }

    private void handleListCommand(Player player) {
        Set<Pet<?>> pets = petRegistry.getApplicablePetImplementations(player);
        player.sendMessage("§dYou have §f" + pets.size() + " §davailable pets.");

        if (!pets.isEmpty()) {
            player.sendMessage("§7" + pets.stream().map(Pet::getName).collect(Collectors.joining(", ")));
        }
    }
}
