package io.papermc.clope;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.Material;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.ItemStack;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.enchantments.*;
import java.util.Random;



import java.util.ArrayList;
import java.util.List;

public class ClopeCommand implements CommandExecutor {

    //NamespaceKey purpose, don't touch
    private final JavaPlugin plugin;

    public ClopeCommand(JavaPlugin plugin){
        this.plugin = plugin;
    }



    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Command can only be used by Player");
            return true;
        }


        if(player.getInventory().firstEmpty() == -1){
            player.sendMessage("\u00A7cTes poches sont deja pleines cowboy!");
        }else{


            ItemStack cigarette = createUniqueCigarette();

            Damageable damageable = (Damageable) cigarette.getItemMeta();
            damageable.setDamage(0); // 0 means brand new

            ItemMeta meta = cigarette.getItemMeta();
            Component displayName = Component.text("\u00A76\u00A7lCigarette \u00A7eGolden"); // Name
            meta.displayName(displayName);
            meta.setCustomModelData(1);
            cigarette.setItemMeta(meta);



            player.getInventory().addItem(cigarette);

        }

        return true;
    }

    public static boolean isCigarette(ItemStack item) {
        if(item != null && item.getType() == Material.BAMBOO){
            ItemMeta meta = item.getItemMeta();
            if(meta != null && meta.hasCustomModelData() && meta.getCustomModelData() == 1){
                return true;
            }
        }
        return false;
    }

    private ItemStack createUniqueCigarette(){
        ItemStack cigarette = new ItemStack(Material.BAMBOO,1 );

        ItemMeta meta = cigarette.getItemMeta();

        Component displayName = Component.text("Cigarette");
        meta.displayName(displayName);
        meta.setCustomModelData(1);

        List<Component> lore = new ArrayList<>();
        lore.add(Component.text("\u00A7f7 \u00A7dbarres restantes"));
        meta.lore(lore);


        NamespacedKey key = new NamespacedKey(plugin, "CigaretteUses");
        meta.getPersistentDataContainer().set(key, PersistentDataType.INTEGER, 0);

        NamespacedKey key2 = new NamespacedKey(plugin, "RandomID");
        Random random = new Random();
        meta.getPersistentDataContainer().set(key2,PersistentDataType.INTEGER, random.nextInt(1001));

        meta.addEnchant(Enchantment.DURABILITY,1,true);

        cigarette.setItemMeta(meta);
        return cigarette;
    }
}
