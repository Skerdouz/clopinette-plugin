package io.papermc.clope;

import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.entity.Item;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import java.util.Random;
import org.bukkit.scheduler.BukkitRunnable;


import java.util.*;


public class ClopeListener implements Listener{


    //NamespaceKey purpose, don't touch
    private final ClopePlugin plugin;
    public ClopeListener(ClopePlugin plugin){
        this.plugin = plugin;
    }

    private Map<UUID, Long> lastCigaretteUse = new HashMap<>();


    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event){
        Player player = event.getPlayer();
        ItemStack item = event.getItem();



        if(event.hasItem()){
            if(ClopeCommand.isCigarette(item)){
                if(event.getAction().toString().contains("RIGHT")){

                    if(isOnCooldown(player)){
                        return;
                    }

                    updateCooldown(player);

                    for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {


                        //Green
                        //onlinePlayer.spawnParticle(Particle.REDSTONE, player.getEyeLocation().add(player.getLocation().getDirection().multiply(1.5)), 100, 0.2, 0.2, 0.2, 0, new Particle.DustOptions(Color.fromRGB(41, 248, 13), 1));
                        //onlinePlayer.spawnParticle(Particle.REDSTONE, player.getEyeLocation().add(player.getLocation().getDirection().multiply(1.5)), 30, 0.2, 0.2, 0.2, 0, new Particle.DustOptions(Color.fromRGB(15, 108, 55), 1));
                        //onlinePlayer.spawnParticle(Particle.REDSTONE, player.getEyeLocation().add(player.getLocation().getDirection().multiply(0.5)), 3, 0.2, 0.1, 0.2, 0, new Particle.DustOptions(Color.fromRGB(0, 0, 0), 1));
                        //onlinePlayer.spawnParticle(Particle.REDSTONE, player.getEyeLocation().add(player.getLocation().getDirection().multiply(1)), 10, 0.2, 0.1, 0.2, 0, new Particle.DustOptions(Color.fromRGB(41, 248, 13), 1));

                        //Golden
                        onlinePlayer.spawnParticle(Particle.REDSTONE, player.getEyeLocation().add(player.getLocation().getDirection().multiply(1.5)), 100, 0.2, 0.2, 0.2, 0, new Particle.DustOptions(Color.fromRGB(255, 255, 0), 1));
                        onlinePlayer.spawnParticle(Particle.REDSTONE, player.getEyeLocation().add(player.getLocation().getDirection().multiply(1.5)), 10, 0.2, 0.2, 0.2, 0, new Particle.DustOptions(Color.fromRGB(204, 204, 0), 1));
                        onlinePlayer.spawnParticle(Particle.REDSTONE, player.getEyeLocation().add(player.getLocation().getDirection().multiply(0.5)), 3, 0.2, 0.1, 0.2, 0, new Particle.DustOptions(Color.fromRGB(0, 0, 0), 1));
                        onlinePlayer.spawnParticle(Particle.REDSTONE, player.getEyeLocation().add(player.getLocation().getDirection().multiply(1)), 10, 0.2, 0.1, 0.2, 0, new Particle.DustOptions(Color.fromRGB(255, 255, 0), 1));

                        onlinePlayer.playSound(player.getLocation(), Sound.ENTITY_BLAZE_BURN, 0.6f, 0.8f);
                    }

                    Random random = new Random();
                    if(random.nextInt(4) == 3){
                        player.damage(2.0);
                        PotionEffect slowEffect = new PotionEffect(PotionEffectType.SLOW,15,4);
                        player.addPotionEffect(slowEffect);
                    }


                    NamespacedKey key = new NamespacedKey(ClopePlugin.getPlugin(ClopePlugin.class), "CigaretteUses");
                    ItemMeta meta = item.getItemMeta();
                    Integer currentUses = meta.getPersistentDataContainer().get(key, PersistentDataType.INTEGER);

                    if(currentUses != null) {
                        if (currentUses < 6) {

                            meta.getPersistentDataContainer().set(key, PersistentDataType.INTEGER, currentUses + 1);
                            item.setItemMeta(meta);

                            List<Component> lore = meta.lore();
                            if(lore != null){
                                lore.set(0,Component.text("\u00A7f"+ (6 - currentUses) + " \u00A7dbarres restantes"));
                                meta.lore(lore);
                                item.setItemMeta(meta);
                            }

                        } else {
                            ItemStack usedCigarette = player.getInventory().getItemInMainHand();

                            if(ClopeCommand.isCigarette(player.getInventory().getItemInOffHand())){
                                ItemStack offHandItem = new ItemStack(Material.AIR);
                                event.getPlayer().getInventory().setItemInOffHand(offHandItem);
                                usedCigarette = player.getInventory().getItemInOffHand();
                            }else{
                                event.getPlayer().getInventory().remove(item);
                            }

                            player.playSound(player.getLocation(), Sound.ENTITY_ITEM_BREAK, 0.3f, 0.3f);
                            event.getPlayer().sendMessage("\u00A77\u00A7oVous jetez votre megot par terre...");
                            Item itemEntity = player.getWorld().dropItem(player.getLocation(), usedCigarette);
                            itemEntity.setPickupDelay(Integer.MAX_VALUE);
                            Component itemName = Component.text("\u00A77megot");
                            itemEntity.customName(itemName);
                            itemEntity.setCustomNameVisible(true);


                            // delete the item 40sec after
                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    itemEntity.remove();
                                }
                            }.runTaskLater(plugin, 20 * 40); // 20 ticks * 2 = 40sec
                        }
                    }
                }

            }
        }
    }

    private boolean isOnCooldown(Player player) {
        long lastUseTime = lastCigaretteUse.getOrDefault(player.getUniqueId(), 0L);
        long currentTime = System.currentTimeMillis();
        return (currentTime - lastUseTime) < 3000; // 3s cooldown
    }

    private void updateCooldown(Player player) {
        lastCigaretteUse.put(player.getUniqueId(), System.currentTimeMillis());
    }
}
