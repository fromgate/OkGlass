package me.fromgate.okglass.gadgets;

import me.fromgate.okglass.Gadget;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.metadata.FixedMetadataValue;

public class KingOfTheHill extends Gadget implements Listener {

	/*
	 * + Счётчик убитых мобов
	 * + Счётчик убитых игроков
	 * + Время онлайн 
	 */

    boolean kingkiller = true;
    int maxkillcount = -1;
    String maxkillplayer = "";

    boolean kinghunter = true;
    int maxhuntcount = -1;
    String maxhuntplayer = "";

    boolean longplay = true;
    Long maxtimecount = 0L;
    String maxtimeplayer = "";


    @Override
    public String getName() {
        return "KingOfTheHill";
    }

    @Override
    public void onDisable() {


    }

    @Override
    public void onEnable() {
        setPermission("okglass.king");
        kingkiller = loadBoolean("killer", true);
        kinghunter = loadBoolean("hunter", true);
        longplay = loadBoolean("time-online", true);
        getServer().getPluginManager().registerEvents(this, getOkGlassPlugin());
        initLoginTimeTick();
    }

    @Override
    public void process() {
        addOnlineTime();

    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // OnlineTime
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void setLoginTime(Player p) {
        p.setMetadata("login-time", new FixedMetadataValue(getOkGlassPlugin(), System.currentTimeMillis()));
    }

    public long getLoginTime(Player p) {
        if (!p.hasMetadata("login-time")) setLoginTime(p);
        return p.getMetadata("login-time").get(0).asLong();
    }


    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerJoin(PlayerJoinEvent event) {
        setLoginTime(event.getPlayer());
    }

    public void initLoginTimeTick() {
        Bukkit.getScheduler().runTaskTimerAsynchronously(getOkGlassPlugin(), new Runnable() {
            @SuppressWarnings("deprecation")
            @Override
            public void run() {
                if (Bukkit.getOnlinePlayers().isEmpty()) return;
                if ((Bukkit.getPlayer(maxtimeplayer) == null) || (!Bukkit.getPlayer(maxtimeplayer).isOnline())) {
                    maxtimeplayer = "";
                    maxtimecount = 0L;
                }

                if (maxtimeplayer.isEmpty()) {
                    Player[] players = Bukkit.getOnlinePlayers().toArray(new Player[Bukkit.getOnlinePlayers().size()]);
                    maxtimeplayer = players[0].getName();
                    maxtimecount = getLoginTime(players[0]);
                }

                for (Player p : Bukkit.getOnlinePlayers()) {
                    if (getLoginTime(p) < maxtimecount) {
                        maxtimecount = getLoginTime(p);
                        maxtimeplayer = p.getName();
                    }
                }
            }
        }, 100L, 600L);
    }

    public void addOnlineTime() {
        if (!longplay) return;
        if (maxtimeplayer.isEmpty()) return;
        addResult("⏰ " + maxtimeplayer, (int) Math.max(((System.currentTimeMillis() - maxtimecount) / 1000 / 60), 1));
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

}
