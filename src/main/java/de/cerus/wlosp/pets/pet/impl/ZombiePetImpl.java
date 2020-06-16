package de.cerus.wlosp.pets.pet.impl;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class ZombiePetImpl extends DefaultPetImpl<Zombie> {

    public ZombiePetImpl(JavaPlugin plugin) {
        super(plugin, Zombie.class);
    }

    @Override
    public void petSpawned(Zombie entity, Player owner) {
        entity.getEquipment().setHelmet(new ItemStack(Material.IRON_HELMET));
        super.petSpawned(entity, owner);
    }

}
