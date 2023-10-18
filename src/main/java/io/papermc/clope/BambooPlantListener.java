package io.papermc.clope;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class BambooPlantListener implements Listener{

        @EventHandler
        public void onPlayerInteract(PlayerInteractEvent event) {
        ItemStack itemInHand = event.getItem();


        if (ClopeCommand.isCigarette(itemInHand)) {
            event.setCancelled(true);
        }
    }
}
