package de.berstanio.lobby.bukkit.gadgets;

import de.berstanio.lobby.bukkit.BukkitMain;
import de.berstanio.lobby.bukkit.LobbyPlayer;
import net.minecraft.server.v1_8_R3.ItemArmor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.io.Serializable;
import java.util.NoSuchElementException;

public abstract class Gadget implements Cloneable, Serializable{

    private String name;
    private int price;
    private Rarity rarity;

    public Gadget(ItemStack itemStack, String name, int price, Rarity rarity) {
        setItemStack(itemStack);
        setName(name);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(getName());
        itemStack.setItemMeta(itemMeta);
        setPrice(price);
        setRarity(rarity);
    }

    public static Gadget getGadget(ItemStack itemStack){
        try {
            return BukkitMain.getInstance().getGadgets().stream().filter(gadget -> gadget.getItemStack().equals(itemStack)).findFirst().get();
        } catch (NoSuchElementException e) {
            System.out.println("Gadget gibts nicht!");
            return null;
        }
    }

    public void equip(LobbyPlayer lobbyPlayer) {
        net.minecraft.server.v1_8_R3.ItemStack r = CraftItemStack.asNMSCopy(getItemStack());
        if (r.getItem() instanceof ItemArmor) {
            if (getItemStack().getType().name().contains("HELMET")){
                if (lobbyPlayer.getPlayer().getInventory().getHelmet() == null){
                    lobbyPlayer.getPlayer().getInventory().setHelmet(getItemStack());
                    lobbyPlayer.getEquipedGadgets().add(this);
                }else {
                    lobbyPlayer.getEquipedGadgets().remove(getGadget(new ItemStack(lobbyPlayer.getPlayer().getInventory().getHelmet())));
                    lobbyPlayer.getEquipedGadgets().add(this);
                    lobbyPlayer.getPlayer().getInventory().setHelmet(getItemStack());
                }
            }else if (getItemStack().getType().name().contains("BOOTS")){

                if (lobbyPlayer.getPlayer().getInventory().getBoots() == null){
                    lobbyPlayer.getPlayer().getInventory().setBoots(getItemStack());
                    lobbyPlayer.getEquipedGadgets().add(this);
                }else {
                    lobbyPlayer.getEquipedGadgets().remove(getGadget(new ItemStack(lobbyPlayer.getPlayer().getInventory().getBoots())));
                    lobbyPlayer.getEquipedGadgets().add(this);
                    lobbyPlayer.getPlayer().getInventory().setBoots(getItemStack());
                }
            }else if (getItemStack().getType().name().contains("LEGGINGS")){
                if (lobbyPlayer.getPlayer().getInventory().getLeggings() == null){
                    lobbyPlayer.getPlayer().getInventory().setLeggings(getItemStack());
                    lobbyPlayer.getEquipedGadgets().add(this);
                }else {
                    lobbyPlayer.getEquipedGadgets().remove(getGadget(new ItemStack(lobbyPlayer.getPlayer().getInventory().getLeggings())));
                    lobbyPlayer.getEquipedGadgets().add(this);
                    lobbyPlayer.getPlayer().getInventory().setLeggings(getItemStack());
                }
            }else if (getItemStack().getType().name().contains("CHESTPLATE")){
                if (lobbyPlayer.getPlayer().getInventory().getChestplate() == null){
                    lobbyPlayer.getPlayer().getInventory().setChestplate(getItemStack());
                    lobbyPlayer.getEquipedGadgets().add(this);
                }else {
                    lobbyPlayer.getEquipedGadgets().remove(getGadget(new ItemStack(lobbyPlayer.getPlayer().getInventory().getChestplate())));
                    lobbyPlayer.getEquipedGadgets().add(this);
                    lobbyPlayer.getPlayer().getInventory().setChestplate(getItemStack());
                }
            }
        }else {
            if (lobbyPlayer.getPlayer().getInventory().getItem(0) == null){
                lobbyPlayer.getPlayer().getInventory().setItem(0, getItemStack());
                lobbyPlayer.getEquipedGadgets().add(this);
            }else {
                lobbyPlayer.getEquipedGadgets().remove(getGadget(new ItemStack(lobbyPlayer.getPlayer().getInventory().getItem(0))));
                lobbyPlayer.getEquipedGadgets().add(this);
            }
        }
    }

    public void unEquip(LobbyPlayer lobbyPlayer) {
        net.minecraft.server.v1_8_R3.ItemStack r = CraftItemStack.asNMSCopy(getItemStack());
        if (r.getItem() instanceof ItemArmor) {
            if (getItemStack().getType().name().contains("HELMET")){
                if (lobbyPlayer.getPlayer().getInventory().getHelmet() != null){
                    lobbyPlayer.getPlayer().getInventory().setHelmet(null);
                    lobbyPlayer.getEquipedGadgets().remove(this);
                }
            }else if (getItemStack().getType().name().contains("BOOTS")){

                if (lobbyPlayer.getPlayer().getInventory().getBoots() != null){
                    lobbyPlayer.getPlayer().getInventory().setBoots(null);
                    lobbyPlayer.getEquipedGadgets().remove(this);
                }
            }else if (getItemStack().getType().name().contains("LEGGINGS")){
                if (lobbyPlayer.getPlayer().getInventory().getLeggings() != null){
                    lobbyPlayer.getPlayer().getInventory().setLeggings(null);
                    lobbyPlayer.getEquipedGadgets().remove(this);
                }
            }else if (getItemStack().getType().name().contains("CHESTPLATE")){
                if (lobbyPlayer.getPlayer().getInventory().getChestplate() != null){
                    lobbyPlayer.getPlayer().getInventory().setChestplate(null);
                    lobbyPlayer.getEquipedGadgets().remove(this);
                }
            }
        }else {
            if (lobbyPlayer.getPlayer().getInventory().getItem(0) != null){
                lobbyPlayer.getPlayer().getInventory().setItem(0, null);
                lobbyPlayer.getEquipedGadgets().remove(this);
            }
        }
    }

    public Rarity getRarity() {
        return rarity;
    }

    public void setRarity(Rarity rarity) {
        this.rarity = rarity;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public ItemStack getItemStack() {
        return BukkitMain.getInstance().getGadgetItemStackHashMap().get(this);
    }

    public void setItemStack(ItemStack itemStack) {
        BukkitMain.getInstance().getGadgetItemStackHashMap().put(this, itemStack);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
