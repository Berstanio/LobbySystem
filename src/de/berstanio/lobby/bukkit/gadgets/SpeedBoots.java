package de.berstanio.lobby.bukkit.gadgets;

import de.berstanio.lobby.bukkit.LobbyAction;
import de.berstanio.lobby.bukkit.LobbyPlayer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

public class SpeedBoots extends UseableGadget {


    public SpeedBoots(ItemStack itemStack, List<LobbyAction> actions, String name, int price, Rarity rarity) {
        super(itemStack, actions, name, price, rarity);
    }

    @Override
    public void execute(LobbyPlayer lobbyPlayer, LobbyAction action) {
        lobbyPlayer.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SPEED,1, 5));
    }
}
