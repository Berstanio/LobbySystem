package de.berstanio.lobby.bukkit.gadgets;

import de.berstanio.lobby.bukkit.LobbyAction;
import de.berstanio.lobby.bukkit.LobbyPlayer;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class FlyBoots extends UseableGadget {


    public FlyBoots(ItemStack itemStack, List<LobbyAction> actions, String name, int price, Rarity rarity) {
        super(itemStack, actions, name, price, rarity);
    }

    @Override
    public void execute(LobbyPlayer lobbyPlayer, LobbyAction action) {
        if (lobbyPlayer.getPlayer().isSneaking()) {
            lobbyPlayer.getPlayer().setVelocity(lobbyPlayer.getPlayer().getLocation().getDirection().normalize());
        }
    }
}
