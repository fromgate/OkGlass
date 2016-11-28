package me.fromgate.okglass.gadgets;

import me.fromgate.okglass.Gadget;
import org.bukkit.Bukkit;
import org.bukkit.World;

public class GadgetEntities extends Gadget {

    @Override
    public String getName() {
        return "Entities";
    }

    public int countEntities() {
        int e = 0;
        for (World w : Bukkit.getWorlds()) {
            if (w == null) continue;
            if ((w.getEntities() == null) || (w.getEntities().isEmpty())) continue;
            e += w.getEntities().size();
        }
        return e;
    }

    @Override
    public void onDisable() {
        // TODO Auto-generated method stub

    }

    @Override
    public void onEnable() {
        // TODO Auto-generated method stub

    }

    @Override
    public void process() {
        addResult("Entities", countEntities());
    }
}
