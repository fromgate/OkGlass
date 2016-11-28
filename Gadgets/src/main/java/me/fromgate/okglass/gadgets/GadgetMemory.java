package me.fromgate.okglass.gadgets;

import me.fromgate.okglass.Gadget;


public class GadgetMemory extends Gadget {
    boolean gb = false;
    boolean showused = true;
    boolean showmax = true;
    boolean showtotal = true;
    boolean showfree = true;

    @Override
    public void onEnable() {
        gb = loadBoolean("gigabytes", false);
        showused = loadBoolean("show-used-memory", true);
        showmax = loadBoolean("show-max-memory", false);
        showtotal = loadBoolean("show-total-memory", false);
        showfree = loadBoolean("show-free-memory", false);
    }

    @Override
    public String getName() {
        return "Memory";
    }

    @Override
    public void onDisable() {
    }

    @Override
    public void process() {
        int max = (int) (Runtime.getRuntime().maxMemory() / 1048576);
        int total = (int) (Runtime.getRuntime().totalMemory() / 1048576);
        int free = (int) (Runtime.getRuntime().freeMemory() / 1048576);
        int used = total - free;

        if (showused) {
            double prc = (((double) used / (double) max) * 100);
            String memStr = "Mem " + (gb ? (used / 1024) : used) + (gb ? "Gb" : "Mb") + " / %";
            addResult(memStr, (int) prc);
        }
        if (showmax) addResult("Max mem, " + (gb ? "Gb" : "Mb"), max);
        if (showtotal) addResult("Total mem, " + (gb ? "Gb" : "Mb"), total);
        if (showfree) addResult("Free mem, " + (gb ? "Gb" : "Mb"), free);

    }
}
