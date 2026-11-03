package com.enderdevelopment.plugin;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public final class Main extends JavaPlugin implements Listener {

    private final NamespacedKey flameGauntletKey = new NamespacedKey(this, "flame_gauntlet");

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("flamegauntlet")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                giveFlameGauntlet(player);
                return true;
            } else {
                sender.sendMessage("Only players can use this command.");
                return false;
            }
        }
        return false;
    }

    private void giveFlameGauntlet(Player player) {
        ItemStack item = new ItemStack(Material.NETHERITE_SWORD);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName("Flame Gauntlet");
            meta.getPersistentDataContainer().set(flameGauntletKey, PersistentDataType.BYTE, (byte) 1);
            item.setItemMeta(meta);
            player.getInventory().addItem(item);
            player.sendMessage(Component.text("You have been given the Flame Gauntlet!").color(NamedTextColor.GOLD));
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();
        if (item != null && item.hasItemMeta()) {
            ItemMeta meta = item.getItemMeta();
            if (meta != null && meta.getPersistentDataContainer().has(flameGauntletKey, PersistentDataType.BYTE)) {
                if (event.getAction().toString().contains("RIGHT")) {
                    if (player.isSneaking()) {
                        // 360-degree fire wave
                        fireWave(player);
                    } else {
                        // Fire projectile
                        fireProjectile(player);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerToggleSneak(PlayerToggleSneakEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();
        if (item != null && item.hasItemMeta()) {
            ItemMeta meta = item.getItemMeta();
            if (meta != null && meta.getPersistentDataContainer().has(flameGauntletKey, PersistentDataType.BYTE)) {
                if (event.isSneaking()) {
                    // Apply fire resistance
                    player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 20 * 60, 0));
                }
            }
        }
    }

    @EventHandler
    public void onPlayerItemHeld(PlayerItemHeldEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItem(event.getNewSlot());
        if (item != null && item.hasItemMeta()) {
            ItemMeta meta = item.getItemMeta();
            if (meta != null && meta.getPersistentDataContainer().has(flameGauntletKey, PersistentDataType.BYTE)) {
                // Apply fire resistance when holding the item
                player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 20 * 60, 0));
            }
        }
    }

    private void fireProjectile(Player player) {
        // Logic for firing a flame projectile
        player.sendMessage(Component.text("Firing a flame projectile!").color(NamedTextColor.RED));
        // Add particle effects and sound effects here
    }

    private void fireWave(Player player) {
        // Logic for 360-degree fire wave
        player.sendMessage(Component.text("Triggering a 360-degree fire wave!").color(NamedTextColor.RED));
        // Add particle effects and sound effects here
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        player.sendMessage(Component.text("Welcome to the Flame Gauntlet plugin!").color(NamedTextColor.GOLD));
    }
}