package de.berstanio.lobby.bukkit;

import de.berstanio.lobby.bukkit.gadgets.Gadget;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.NoSuchElementException;

public class LobbyPlayer implements Serializable {

    private String player;
    private int coins;
    private ArrayList<Gadget> boughtGadgets = new ArrayList<>();
    private ArrayList<Gadget> equipedGadgets = new ArrayList<>();
    private int chests;

    public LobbyPlayer(Player player, int coins) {
        setPlayer(player);
        setCoins(coins);
    }

    public static LobbyPlayer getLobbyPlayer(Player player){
        try {
            return BukkitMain.getInstance().getLobbyPlayers().stream().filter(lobbyPlayer -> lobbyPlayer.getPlayer().equals(player)).findFirst().get();
        }catch (NoSuchElementException e){
            System.out.println("Player gibts nicht!");
            return null;
        }
    }

    public int getChests() {
        return chests;
    }

    public void setChests(int chests) {
        this.chests = chests;
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(player);
    }

    public void setPlayer(Player player) {
        this.player = player.getName();
    }

    public int getCoins() {
        return coins;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }

    public ArrayList<Gadget> getBoughtGadgets() {
        return boughtGadgets;
    }

    public void setBoughtGadgets(ArrayList<Gadget> boughtGadgets) {
        this.boughtGadgets = boughtGadgets;
    }

    public ArrayList<Gadget> getEquipedGadgets() {
        return equipedGadgets;
    }

    public void setEquipedGadgets(ArrayList<Gadget> equipedGadgets) {
        this.equipedGadgets = equipedGadgets;
    }
}
