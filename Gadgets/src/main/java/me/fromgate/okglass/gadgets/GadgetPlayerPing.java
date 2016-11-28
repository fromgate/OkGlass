package me.fromgate.okglass.gadgets;

import me.fromgate.okglass.Gadget;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class GadgetPlayerPing extends Gadget {

    boolean fpo_installed = false;
    boolean show_average = true;
    boolean show_yourping = true;


    private static String version = "";
    private static String[] tested_versions = {"v1_11_R1"};
    private static String cboPrefix = "org.bukkit.craftbukkit.";
    private static String nmsPrefix = "net.minecraft.server.";
    private static boolean block_executing = false;
    private static Class<?> CraftEntity;
    private static Field CraftEntity_entity;
    private static Class<?> EntityPlayer;
    private static Field entityPlayer_ping;

    @Override
    public String getName() {
        return "PlayerPing";
    }

    public int getAveragePing() {
        if (!fpo_installed) return -1;
        int pingsum = 0;
        for (Player p : Bukkit.getOnlinePlayers())
            pingsum += getPlayerPing(p);
        return Math.max(pingsum / Bukkit.getOnlinePlayers().size(), 1);
    }

    public int getPlayerPing() {
        return getPlayerPing(getPlayer());
    }


    @Override
    public void onEnable() {
        Plugin fpo = Bukkit.getPluginManager().getPlugin("FakePlayersOnline");
        fpo_installed = (fpo != null);
        show_average = loadBoolean("show-average-ping", true);
        show_yourping = loadBoolean("show-player-ping", true);
        initNms();
    }


    public void initNms() {
        try {
            Object s = Bukkit.getServer();
            Method m = s.getClass().getMethod("getHandle");
            Object cs = m.invoke(s);
            String className = cs.getClass().getName();
            String[] v = className.split("\\.");
            if (v.length == 5) {
                version = v[3];
                cboPrefix = "org.bukkit.craftbukkit." + version + ".";
                nmsPrefix = "net.minecraft.server." + version + ".";
            }
            EntityPlayer = nmsClass("EntityPlayer");
            CraftEntity = cboClass("entity.CraftEntity");
            CraftEntity_entity = CraftEntity.getDeclaredField("entity");
            CraftEntity_entity.setAccessible(true);
            entityPlayer_ping = EntityPlayer.getField("ping");

        } catch (Exception e) {
            block_executing = true;
            e.printStackTrace();
        }
    }


    @Override
    public void onDisable() {
    }

    @Override
    public void process() {
        if (show_average) addResult("Average Ping", getAveragePing());
        if (show_yourping) addResult(getPlayer().getName() + " ☇", getPlayerPing());
    }

    private static Class<?> nmsClass(String classname) throws Exception {
        return Class.forName(nmsPrefix + classname);
    }

    private static Class<?> cboClass(String classname) throws Exception {
        return Class.forName(cboPrefix + classname);
    }

    public int getPlayerPing(Player p) {
        if (block_executing) return 0;
        try {
            Object craftEntity = p;
            Object nmsPlayer = CraftEntity_entity.get(craftEntity);
            return entityPlayer_ping.getInt(nmsPlayer);
        } catch (Exception e) {
        }
        return 1;
    }


}
