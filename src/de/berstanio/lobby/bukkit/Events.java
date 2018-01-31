package de.berstanio.lobby.bukkit;

import de.berstanio.lobby.bukkit.gadgets.Gadget;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.*;
import java.util.Base64;
import java.util.NoSuchElementException;

public class Events implements Listener {


    @EventHandler
    public void onClick(PlayerInteractEvent e){
        if (e.getAction() == Action.PHYSICAL) return;
        if (Gadget.getGadget(e.getPlayer().getItemInHand()) != null) {
            BukkitMain.getInstance().executeGadgets(LobbyPlayer.getLobbyPlayer(e.getPlayer()), LobbyAction.valueOf(e.getAction().name().replace("_AIR", "").replace("_BLOCK", "")));
            return;
        }
        if (e.getAction().name().contains("LEFT")) return;
        if (e.getPlayer().getItemInHand().getType() == Material.COMPASS) {
            Inventory inventory = Bukkit.createInventory(null, InventoryType.CHEST, ChatColor.GREEN + "Shop");
            BukkitMain.getInstance().getGadgets().forEach(gadget -> inventory.addItem(gadget.getItemStack()));
            e.getPlayer().openInventory(inventory);
        }else if (e.getPlayer().getItemInHand().getType() == Material.INK_SACK) {
            if (e.getPlayer().getItemInHand().getItemMeta().getDisplayName().contains("An")){
                Bukkit.getOnlinePlayers().forEach(player -> e.getPlayer().hidePlayer(player));
                ItemMeta dyeMeta = e.getPlayer().getItemInHand().getItemMeta();
                dyeMeta.setDisplayName(ChatColor.GRAY + "✦ " + ChatColor.BLUE + "Spieler Sichtbarkeit: " + ChatColor.RED  + "Aus " + ChatColor.GRAY + "✦");
                e.getPlayer().getItemInHand().setItemMeta(dyeMeta);
                e.getPlayer().getItemInHand().setDurability((short) 8);
            }else {
                Bukkit.getOnlinePlayers().forEach(player -> e.getPlayer().showPlayer(player));
                ItemMeta dyeMeta = e.getPlayer().getItemInHand().getItemMeta();
                dyeMeta.setDisplayName(ChatColor.GRAY + "✦ " + ChatColor.BLUE + "Spieler Sichtbarkeit: " + ChatColor.GREEN  + "An " + ChatColor.GRAY + "✦");
                e.getPlayer().getItemInHand().setItemMeta(dyeMeta);
                e.getPlayer().getItemInHand().setDurability((short) 10);
            }
        }else if (e.getPlayer().getItemInHand().getType() == Material.WATCH) {
            e.getPlayer().openInventory(BukkitMain.getInstance().getInventory());
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (e.getCurrentItem() == null) return;
        if (BukkitMain.getInstance().getLocationHashMap().containsKey(e.getCurrentItem())) {
            e.getWhoClicked().teleport(BukkitMain.getInstance().getLocationHashMap().get(e.getCurrentItem()));
            e.getWhoClicked().closeInventory();
            return;
        }
        if (e.getInventory().getTitle().equals(ChatColor.GREEN + "Shop")) {
            try {
                Gadget foundGadget = Gadget.getGadget(e.getCurrentItem());
                assert foundGadget != null;
                LobbyPlayer lobbyPlayer = LobbyPlayer.getLobbyPlayer(Bukkit.getPlayer(e.getWhoClicked().getName()));
                assert lobbyPlayer != null;
                if (lobbyPlayer.getBoughtGadgets().contains(foundGadget)){
                    if (!lobbyPlayer.getEquipedGadgets().contains(foundGadget)){
                        foundGadget.equip(lobbyPlayer);
                        e.setCancelled(true);
                        return;
                    }else {
                        foundGadget.unEquip(lobbyPlayer);
                        e.setCancelled(true);
                        return;
                    }
                }
                Inventory inv = Bukkit.createInventory(e.getWhoClicked(), InventoryType.HOPPER, ChatColor.GREEN + "Kaufen");
                ItemStack itemStackBuy = new ItemStack(Material.EMERALD_BLOCK, 1);
                ItemMeta itemBuyMeta = itemStackBuy.getItemMeta();
                itemBuyMeta.setDisplayName(ChatColor.GREEN + "Annehmen");
                itemStackBuy.setItemMeta(itemBuyMeta);
                ItemStack itemStackNotBuy = new ItemStack(Material.REDSTONE_BLOCK, 1);
                ItemMeta itemNotMeta = itemStackNotBuy.getItemMeta();
                itemNotMeta.setDisplayName(ChatColor.RED + "Ablehnen");
                itemStackNotBuy.setItemMeta(itemNotMeta);
                inv.setItem(0, itemStackBuy);
                inv.setItem(2, foundGadget.getItemStack());
                inv.setItem(4, itemStackNotBuy);
                e.getWhoClicked().openInventory(inv);
            } catch (NoSuchElementException ex) {
                ex.printStackTrace();
            }
        }else if (e.getInventory().getTitle().equals(ChatColor.GREEN + "Kaufen")){
            if (e.getCurrentItem().getType() == Material.REDSTONE_BLOCK) {
                e.getWhoClicked().closeInventory();
            } else if (e.getCurrentItem().getType() == Material.EMERALD_BLOCK) {
                e.getWhoClicked().closeInventory();
                Gadget foundGadget = Gadget.getGadget(e.getInventory().getItem(2));
                LobbyPlayer lobbyPlayer =  LobbyPlayer.getLobbyPlayer(Bukkit.getPlayer(e.getWhoClicked().getName()));
                assert lobbyPlayer != null;
                assert foundGadget != null;
                if (lobbyPlayer.getCoins() - foundGadget.getPrice() > 0) {
                    lobbyPlayer.getBoughtGadgets().add(foundGadget);
                    e.getWhoClicked().sendMessage(ChatColor.GREEN + "Du hast es gekauft!");
                    lobbyPlayer.setCoins(lobbyPlayer.getCoins() - foundGadget.getPrice());
                    e.getWhoClicked().sendMessage(ChatColor.GREEN + "Du hast noch " + LobbyPlayer.getLobbyPlayer(Bukkit.getPlayer(e.getWhoClicked().getName())).getCoins() + " Coins!");
                }else {
                    e.getWhoClicked().sendMessage(ChatColor.RED + "Du hast nicht genug Coins!");
                }
            } else {
                e.getCurrentItem().getType();
            }
        }
        e.setCancelled(true);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        e.setJoinMessage(null);
        e.getPlayer().getInventory().clear();
        ItemStack compassStack = new ItemStack(Material.COMPASS);
        ItemMeta compassMeta = compassStack.getItemMeta();
        compassMeta.setDisplayName(ChatColor.GRAY + "✦ " + ChatColor.DARK_GREEN + "Gadgets " + ChatColor.GRAY + "✦");
        compassStack.setItemMeta(compassMeta);
        ItemStack watchStack = new ItemStack(Material.WATCH);
        ItemMeta watchMeta = watchStack.getItemMeta();
        watchMeta.setDisplayName(ChatColor.GRAY + "✦ " + ChatColor.LIGHT_PURPLE + "Teleporter Uhr " + ChatColor.GRAY + "✦");
        watchStack.setItemMeta(watchMeta);
        ItemStack dyeStack = new ItemStack(Material.INK_SACK, 1, (short) 10);
        ItemMeta dyeMeta = dyeStack.getItemMeta();
        dyeMeta.setDisplayName(ChatColor.GRAY + "✦ " + ChatColor.BLUE + "Spieler Sichtbarkeit: " + ChatColor.GREEN  + "An " + ChatColor.GRAY + "✦");
        dyeStack.setItemMeta(dyeMeta);
        e.getPlayer().getInventory().setItem(1, compassStack);
        e.getPlayer().getInventory().setItem(3, watchStack);
        e.getPlayer().getInventory().setItem(5, dyeStack);
        if (BukkitMain.getInstance().getMySqlConnection().getLobbyPlayer(e.getPlayer().getName()) == null) {
            BukkitMain.getInstance().getLobbyPlayers().add(new LobbyPlayer(e.getPlayer(), 100));
        }else {
            LobbyPlayer lobbyPlayer = (LobbyPlayer) deSerialize(BukkitMain.getInstance().getMySqlConnection().getLobbyPlayer(e.getPlayer().getName()));
            BukkitMain.getInstance().getLobbyPlayers().add(lobbyPlayer);
        }
        if (e.getPlayer().hasPermission("system.premium")){
            LobbyPlayer.getLobbyPlayer(e.getPlayer()).getBoughtGadgets().addAll(BukkitMain.getInstance().getGadgets());
        }
        System.out.println(e.getPlayer().hasPermission("system.premium"));
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        e.setQuitMessage(null);
        LobbyPlayer.getLobbyPlayer(e.getPlayer()).getEquipedGadgets().forEach(gadget -> gadget.unEquip(LobbyPlayer.getLobbyPlayer(e.getPlayer())));
        BukkitMain.getInstance().getMySqlConnection().pushOrUpdateLobbyPlayer(e.getPlayer().getName(), serialize(LobbyPlayer.getLobbyPlayer(e.getPlayer())));
    }

    @EventHandler
    public void onDamage(EntityDamageEvent e){
        e.setCancelled(true);
    }

    @EventHandler
    public void onWeather(WeatherChangeEvent e){
        e.setCancelled(e.toWeatherState());
    }

    @EventHandler
    public void onWeather(FoodLevelChangeEvent e){
        e.setCancelled(true);
    }

    @EventHandler
    public void onMobSpawn(CreatureSpawnEvent e){
        if (e.getEntityType() != EntityType.PLAYER){
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onMobSpawn(EntitySpawnEvent e){
        if (e.getEntityType() != EntityType.PLAYER){
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockSpawn(BlockBreakEvent e){
        e.setCancelled(true);
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent e){
        e.setCancelled(true);
    }

    public static Object deSerialize(String s) {
        try {
            byte [] data = Base64.getDecoder().decode(s);
            ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data));
            Object o = ois.readObject();
            ois.close();
            return o;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static String serialize(Serializable o) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(o);
            oos.close();
            return Base64.getEncoder().encodeToString(baos.toByteArray());
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

}
