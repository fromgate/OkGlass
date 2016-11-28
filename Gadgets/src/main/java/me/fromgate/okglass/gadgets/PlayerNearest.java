package me.fromgate.okglass.gadgets;

import me.fromgate.okglass.Gadget;
import org.bukkit.entity.Player;

public class PlayerNearest extends Gadget {
    @Override
    public String getName() {
        return "NearestPlayer";
    }

    @Override
    public void onDisable() {
    }

    @Override
    public void onEnable() {
    }

    @Override
    public void process() {
        Player p = getPlayer();
        if (p.getWorld().getPlayers().size() <= 1) return;
        double distance = -1;
        String pname = "";
        for (Player tp : p.getWorld().getPlayers()) {
            if (tp.equals(p)) continue;
            if (distance < p.getLocation().distance(tp.getLocation())) {
                distance = p.getLocation().distance(tp.getLocation());
                pname = tp.getName();
            }
        }
        addResult(pname, (int) distance);
    }

}
