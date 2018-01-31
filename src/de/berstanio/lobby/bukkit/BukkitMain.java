package de.berstanio.lobby.bukkit;

import de.berstanio.lobby.bukkit.gadgets.*;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class BukkitMain extends JavaPlugin {

    private static BukkitMain instance;
    private ArrayList<LobbyPlayer> lobbyPlayers = new ArrayList<>();
    private ArrayList<Gadget> gadgets = new ArrayList<>();
    private Inventory inventory;
    private HashMap<ItemStack, Location> locationHashMap = new HashMap<>();
    private MySqlConnection mySqlConnection;
    private HashMap<Gadget, ItemStack> gadgetItemStackHashMap = new HashMap<>();

    @Override
    public void onEnable() {
        setMySqlConnection(new MySqlConnection());
        setInstance(this);
        getServer().getPluginManager().registerEvents(new Events(), this);
        Bukkit.getOnlinePlayers().forEach(player -> getLobbyPlayers().add(new LobbyPlayer(player, 100)));
        startAlways();
        loadGadgets();
        generateInventory();
    }

    @Override
    public void onDisable() {
        getLobbyPlayers().forEach(lobbyPlayer -> lobbyPlayer.getEquipedGadgets().forEach(gadget -> gadget.unEquip(lobbyPlayer)));
    }

    public void loadGadgets() {
        getGadgets().add(new DecorationGadget(new ItemStack(Material.DIAMOND_BOOTS),ChatColor.GREEN + "BOOTS",10000, Rarity.COMMON));
        getLobbyPlayers().stream().filter(lobbyPlayer -> lobbyPlayer.getPlayer().hasPermission("system.premium")).forEach(lobbyPlayer -> lobbyPlayer.setBoughtGadgets(getGadgets()));
        loadUseableGadgets();
    }

    public void loadUseableGadgets(){
        {
            ItemStack itemStack = new ItemStack(Material.LEATHER_BOOTS);
            LeatherArmorMeta leatherArmorMeta = (LeatherArmorMeta) itemStack.getItemMeta();
            leatherArmorMeta.setColor(Color.AQUA);
            itemStack.setItemMeta(leatherArmorMeta);
            List<LobbyAction> lobbyActions = Arrays.asList(new LobbyAction[]{LobbyAction.ALWAYS});
            String name = ChatColor.GREEN + "JUMP_BOOTS";
            int price = 20000;
            getGadgets().add(new JumpBoots(itemStack, lobbyActions, name, price, Rarity.RARE));
        }
        {
            ItemStack itemStack = new ItemStack(Material.LEATHER_BOOTS);
            LeatherArmorMeta leatherArmorMeta = (LeatherArmorMeta) itemStack.getItemMeta();
            leatherArmorMeta.setColor(Color.BLACK);
            itemStack.setItemMeta(leatherArmorMeta);
            List<LobbyAction> lobbyActions = Arrays.asList(new LobbyAction[]{LobbyAction.ALWAYS});
            String name = ChatColor.GREEN + "SPEED_BOOTS";
            int price = 30000;
            getGadgets().add(new SpeedBoots(itemStack, lobbyActions, name, price, Rarity.RARE));
        }
        {
            ItemStack itemStack = new ItemStack(Material.LEATHER_BOOTS);
            LeatherArmorMeta leatherArmorMeta = (LeatherArmorMeta) itemStack.getItemMeta();
            leatherArmorMeta.setColor(Color.GREEN);
            itemStack.setItemMeta(leatherArmorMeta);
            List<LobbyAction> lobbyActions = Arrays.asList(new LobbyAction[]{LobbyAction.ALWAYS});
            String name = ChatColor.GREEN + "FLY_BOOTS";
            int price = 40000;
            getGadgets().add(new FlyBoots(itemStack, lobbyActions, name, price, Rarity.EPIC));
        }
        {
            ItemStack itemStack = new ItemStack(Material.BLAZE_ROD);
            List<LobbyAction> lobbyActions = Arrays.asList(new LobbyAction[]{LobbyAction.ALWAYS});
            String name = ChatColor.GREEN + "LOKOMOTIVE_ROD";
            int price = 50000;
            getGadgets().add(new LokGadget(itemStack, lobbyActions, name, price, Rarity.LEGENDARY));
        }
    }

    public void executeGadgets(LobbyPlayer lobbyPlayer, LobbyAction action) {
        lobbyPlayer.getEquipedGadgets().stream().filter(gadget -> gadget instanceof UseableGadget).filter(gadget -> ((UseableGadget) gadget).getActions().contains(action)).forEach(gadget -> ((UseableGadget) gadget).execute(lobbyPlayer, action));
    }

    public void startAlways(){
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> getLobbyPlayers().forEach(lobbyPlayer -> executeGadgets(lobbyPlayer, LobbyAction.ALWAYS)),0,1);
    }

    public void generateInventory(){
        Inventory inventory = Bukkit.createInventory(null, 45, ChatColor.GREEN + "Spielmodies");
        ItemStack itemStack = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 7);
        renameItem(itemStack, " ", "");
        for (int i = 0; i < inventory.getSize(); i++) {
            inventory.setItem(i, itemStack);
        }
        ItemStack barrier = new ItemStack(Material.BARRIER);
        renameItem(barrier, ChatColor.RED + "Coming soon...", "Wird bald kommen.");
        ItemStack anvil = new ItemStack(Material.ANVIL);
        renameItem(anvil, ChatColor.YELLOW + "Training", "Das perfekte Training für einen CW.");
        ItemStack emerald = new ItemStack(Material.EMERALD);
        renameItem(emerald, ChatColor.GREEN + "Unser Team", "Teleportiere dich um das Team zu sehen.");
        ItemStack netherStar = new ItemStack(Material.NETHER_STAR);
        renameItem(netherStar, ChatColor.AQUA + "Teleport zum Spawn", "Zurück zum Anfang");
        ItemStack sign = new ItemStack(Material.SIGN);
        renameItem(sign, ChatColor.DARK_PURPLE + "Top 10", "Sehe die 10 besten Spieler.");
        ItemStack diamond = new ItemStack(Material.DIAMOND);
        renameItem(diamond, ChatColor.YELLOW  + "" +  ChatColor.ITALIC + "[NEW] " + ChatColor.RESET + "" + ChatColor.RED + "DMSimulator", "Bekämpfe mit deinem Team das andere Team.");
        ItemStack sandStone = new ItemStack(Material.SANDSTONE);
        renameItem(sandStone, ChatColor.DARK_AQUA + "BuildFFA", "Kämpfe dich zur Spitze.");
        inventory.setItem(4, sandStone);
        inventory.setItem(19, barrier);
        inventory.setItem(21, emerald);
        inventory.setItem(22, netherStar);
        inventory.setItem(23, sign);
        inventory.setItem(25, diamond);
        inventory.setItem(38, barrier);
        inventory.setItem(42, anvil);
        getLocationHashMap().put(anvil, new Location(Bukkit.getWorld("world"),7, 66, -54));
        getLocationHashMap().put(emerald, new Location(Bukkit.getWorld("world"),-86, 64, 29));
        getLocationHashMap().put(netherStar, new Location(Bukkit.getWorld("world"),-51, 66, 28));
        getLocationHashMap().put(diamond, new Location(Bukkit.getWorld("world"),21, 71, 59));
        getLocationHashMap().put(sandStone, new Location(Bukkit.getWorld("world"),55, 66, 27));
        //getLocationHashMap().put(sign, new Location(Bukkit.getWorld("world"),-51, 4, 28));
        // TODO: 06.12.17 Vllt. aus Spaß aktivieren
        setInventory(inventory);
    }

    public ItemStack renameItem(ItemStack itemStack, String newName, String lore){
        List<String> lores = new ArrayList<>();
        lores.add(ChatColor.GRAY + lore);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(newName);
        itemMeta.setLore(lores);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            if (command.getName().equalsIgnoreCase("test")) {
                sender.addAttachment(this, "system.premium", true);
            } else if (command.getName().equalsIgnoreCase("coins")) {
                sender.sendMessage(ChatColor.GREEN + "Du hast " + LobbyPlayer.getLobbyPlayer((Player) sender).getCoins() + " Coins!");
            }
        }
        return true;
    }

    public HashMap<ItemStack, Location> getLocationHashMap() {
        return locationHashMap;
    }

    public void setLocationHashMap(HashMap<ItemStack, Location> locationHashMap) {
        this.locationHashMap = locationHashMap;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    public ArrayList<Gadget> getGadgets() {
        return gadgets;
    }

    public void setGadgets(ArrayList<Gadget> gadgets) {
        this.gadgets = gadgets;
    }

    public static BukkitMain getInstance() {
        return instance;
    }

    private static void setInstance(BukkitMain instance) {
        BukkitMain.instance = instance;
    }

    public ArrayList<LobbyPlayer> getLobbyPlayers() {
        return lobbyPlayers;
    }

    public void setLobbyPlayers(ArrayList<LobbyPlayer> lobbyPlayers) {
        this.lobbyPlayers = lobbyPlayers;
    }

    public MySqlConnection getMySqlConnection() {
        return mySqlConnection;
    }

    public void setMySqlConnection(MySqlConnection mySqlConnection) {
        this.mySqlConnection = mySqlConnection;
    }

    public HashMap<Gadget, ItemStack> getGadgetItemStackHashMap() {
        return gadgetItemStackHashMap;
    }

    public void setGadgetItemStackHashMap(HashMap<Gadget, ItemStack> gadgetItemStackHashMap) {
        this.gadgetItemStackHashMap = gadgetItemStackHashMap;
    }
}
