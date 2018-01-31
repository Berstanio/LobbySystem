package de.berstanio.lobby.bukkit.gadgets;

import de.berstanio.lobby.bukkit.LobbyAction;
import de.berstanio.lobby.bukkit.LobbyPlayer;
import org.bukkit.inventory.ItemStack;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public abstract class UseableGadget extends Gadget {

    private List<LobbyAction> actions;

    public UseableGadget(ItemStack itemStack, List<LobbyAction> actions, String name, int price,Rarity rarity) {
        super(itemStack, name, price, rarity);
        setActions(actions);
    }


    public abstract void execute(LobbyPlayer player, LobbyAction action);

    public List<LobbyAction> getActions() {
        return actions;
    }

    public void setActions(List<LobbyAction> actions) {
        this.actions = actions;
    }
}
