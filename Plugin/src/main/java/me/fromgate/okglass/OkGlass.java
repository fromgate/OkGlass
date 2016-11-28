/*  
 *  OkGlass, Minecraft bukkit plugin
 *  (c)2013, fromgate, fromgate@gmail.com
 *  http://dev.bukkit.org/server-mods/okglass/
 *    
 *  This file is part of OkGlass.
 *  
 *  OkGlass is free software: you can redistribute it and/or modify
 *	it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  OkGlass is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with OkGlass.  If not, see <http://www.gnorg/licenses/>.
 * 
 */

package me.fromgate.okglass;

import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

public class OkGlass extends JavaPlugin {
    public OGUtil u;
    private OKListener l;

    boolean vcheck = false;
    boolean language_save = false;
    String language = "english";
    int refreshdelay = 10;
    boolean updateDefaultGadgets = true;

    String brdname = "&6OK'GLASS";
    boolean debug = false;
    String defaultcolor = "a";
    boolean autoshow = false;

    Gadgets gadgets;

	
	/* 
     * TODO
	 * - поддержка каналов для гаджетов
	 */

    @Override
    public void onEnable() {
        reloadCfg();
        u = new OGUtil(this, vcheck, language_save, language, "okglass", "OkGlass", "okglass", "&b[&3OkGlass&b] ");
        l = new OKListener(this);
        gadgets = new Gadgets(this);
        getCommand("okglass").setExecutor(u);
        getServer().getPluginManager().registerEvents(l, this);

		try {
			MetricsLite metrics = new MetricsLite(this);
			metrics.start();
		} catch (IOException e) {
		}
    }


    public void reloadCfg() {
        vcheck = getConfig().getBoolean("general.check-updates", true);
        getConfig().set("general.check-updates", vcheck);

        language = getConfig().getString("general.language", "english");
        getConfig().set("general.language", language);

        language_save = getConfig().getBoolean("general.language-save", false);
        getConfig().set("general.language-save", language_save);

        updateDefaultGadgets = getConfig().getBoolean("OkGlass.save-default-gadgets", true);
        getConfig().set("OkGlass.save-default-gadgets", updateDefaultGadgets);

        refreshdelay = getConfig().getInt("OkGlass.display-refresh-delay", 10);
        getConfig().set("OkGlass.display-refresh-delay", refreshdelay);

        autoshow = getConfig().getBoolean("OkGlass.show-display-after-login", false);
        getConfig().set("OkGlass.show-display-after-login", autoshow);

        brdname = getConfig().getString("OkGlass.title", "&6OK'GLASS");
        getConfig().set("OkGlass.title", brdname);

        defaultcolor = getConfig().getString("OkGlass.default-color", "a");
        getConfig().set("OkGlass.default-color", defaultcolor);

        debug = getConfig().getBoolean("OkGlass.debug", false);
        getConfig().set("OkGlass.debug", debug);

        saveConfig();
    }

    @Override
    public void onDisable() {
        gadgets.disableAllGadgets();
    }


    public void saveDefaultGadgets() {
        if (updateDefaultGadgets){
            saveResource("Gadgets/Gadgets.jar", true);
        }
    }
}
