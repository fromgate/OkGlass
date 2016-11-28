package me.fromgate.okglass.gadgets;

import me.fromgate.okglass.Gadget;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

public class GadgetTPS extends Gadget {

    boolean tmp = true;

    // Конфигурация
    int tps_update = 10;

    // Текущие переменные
    Long lasttime = 0L;
    Long prevtime = 0L;
    int tps = 20;

    BukkitTask bt;

    @Override
    public void onEnable() {
        prevtime = System.currentTimeMillis();
        tps_update = this.loadInt("tps-update-delay", 10);
        startTick();
    }

    public void startTick() {
        bt = Bukkit.getScheduler().runTaskTimer(getOkGlassPlugin(), new Runnable() {
            @Override
            public void run() {
                prevtime = lasttime;
                lasttime = System.currentTimeMillis();
                float rt = lasttime - prevtime;
                float tpsf = 20 * ((rt) / (1000 * tps_update));
                tps = Math.min(Math.round(tpsf), 20);
            }
        }, 20 * tps_update, 20 * tps_update);
    }


    @Override
    public String getName() {
        return "TPS";
    }

    @Override
    public void onDisable() {
        bt.cancel();
    }

    @Override
    public void process() {
        String px = "&a";
        if (tps < 20) px = "&e";
        if (tps < 18) px = "&6";
        if (tps < 15) px = "&c";
        if (tps < 10) px = "&4";
        addResult(px + "TPS", tps);
    }

}
