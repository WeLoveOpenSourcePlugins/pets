package de.cerus.wlosp.pets.listener;

import de.cerus.wlosp.pets.pet.PetEntityController;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {

    private final PetEntityController petEntityController;

    public PlayerListener(PetEntityController petEntityController) {
        this.petEntityController = petEntityController;
    }

    @EventHandler
    public void onPlayerDisconnect(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        petEntityController.despawnPet(player);
    }

    @EventHandler
    public void onPlayerChangedWorlds(PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();
        petEntityController.despawnPet(player);
    }

}
