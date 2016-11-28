package me.fromgate.okglass.gadgets;

import me.fromgate.okglass.Gadget;
import org.bukkit.Bukkit;
import org.bukkit.World;

public class GadgetChunks extends Gadget {

    @Override
    public String getName() {
        return "Chunks";
    }

    public int countChunks() {
        int chunks = 0;
        for (World w : Bukkit.getWorlds()) {
            if (w == null) continue;
            if (w.getLoadedChunks() == null) continue;
            chunks += w.getLoadedChunks().length;
        }
        return chunks;
    }

    @Override
    public void process() {
        addResult("Chunks", countChunks());
    }

    @Override
    public void onDisable() {
        // TODO Auto-generated method stub

    }

    @Override
    public void onEnable() {
        // TODO Auto-generated method stub

    }

}
