package me.fromgate.okglass.gadgets;

import me.fromgate.okglass.Gadget;

public class GadgetWorldOnline extends Gadget {

    @Override
    public String getName() {
        return "WorldOnline";
    }

    @Override
    public void onDisable() {
    }

    @Override
    public void onEnable() {
    }

    @Override
    public void process() {
        addResult(getPlayer().getWorld().getName(), getPlayer().getWorld().getPlayers().size());
    }


}
