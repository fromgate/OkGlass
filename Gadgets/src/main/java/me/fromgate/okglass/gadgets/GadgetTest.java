package me.fromgate.okglass.gadgets;

import me.fromgate.okglass.Gadget;
import me.fromgate.okglass.OkGlass;

public class GadgetTest extends Gadget {
    OkGlass plg;
    String c = "0123456789abcdef";

    @Override
    public String getName() {
        return "Test";
    }

    @Override
    public void onDisable() {
    }

    @Override
    public void onEnable() {
        plg = (OkGlass) getOkGlassPlugin();
    }

    @Override
    public void process() {
        addResult("&" + c.charAt(plg.u.getRandomInt(c.length())) + "TEST", -1);
    }

}
