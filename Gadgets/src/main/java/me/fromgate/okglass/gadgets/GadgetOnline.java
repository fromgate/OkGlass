package me.fromgate.okglass.gadgets;

import me.fromgate.okglass.Gadget;

public class GadgetOnline extends Gadget {

    @Override
    public String getName() {
        return "Online";
    }

    @Override
    public void process() {
        addResult("Online", getServer().getOnlinePlayers().size());
    }

    @Override
    public void onEnable() {
    }

    @Override
    public void onDisable() {
    }

}
