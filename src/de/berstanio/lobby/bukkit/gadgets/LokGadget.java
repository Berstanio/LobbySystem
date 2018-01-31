package de.berstanio.lobby.bukkit.gadgets;

import de.berstanio.lobby.bukkit.LobbyAction;
import de.berstanio.lobby.bukkit.LobbyPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class LokGadget extends UseableGadget {


    public LokGadget(ItemStack itemStack, List<LobbyAction> actions, String name, int price, Rarity rarity) {
        super(itemStack, actions, name, price, rarity);
    }

    @Override
    public void execute(LobbyPlayer lobbyPlayer, LobbyAction action) {
        Bukkit.getOnlinePlayers().stream().filter(player -> lobbyPlayer.getPlayer().getItemInHand().equals(this.getItemStack())).forEach(player -> player.playEffect(lobbyPlayer.getPlayer().getLocation().add(0, 2, 0), Effect.VILLAGER_THUNDERCLOUD, 0));
    }
}
