/*  
 *  OkGlass, Minecraft bukkit plugin
 *  (c)2013, fromgate, fromgate@gmail.com
 *  http://dev.bukkit.org/server-mods/okglass/
 *    
 *  This file is part of CPFix.
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
 *  along with CPFix.  If not, see <http://www.gnorg/licenses/>.
 * 
 */

package me.fromgate.okglass;

import java.io.IOException;

import org.bukkit.plugin.java.JavaPlugin;

public class OkGlass extends JavaPlugin {
	public OGUtil u;
	
	boolean vcheck=false;
	boolean language_save=false;
	String language="english";
	int refreshdelay = 10;
	
	Gadgets gadgets;
	
	
	/* Память+, пинг (может пинг до какого-то узла?)+
	 * пинг до игрока+
	 * Количество чанков в памяти+
	 * Количество энтитей+
	 * Количество игроков
	 * TPS+
	 */
	
	@Override
	public void onEnable() {
		reloadCfg();
		u = new OGUtil(this, vcheck, language_save, language, "okglass", "OkGlass", "okglass", "&b[&3OkGlass&b] ");
		gadgets = new Gadgets(this);
		getCommand("okglass").setExecutor(u);
		
		try {
			MetricsLite metrics = new MetricsLite(this);
			metrics.start();
		} catch (IOException e) {
		}		
	}
	
	public void reloadCfg(){
		vcheck = getConfig().getBoolean("general.check-updates",true);
		getConfig().set("general.check-updates", vcheck);
		
		language = getConfig().getString("general.language","english");
		getConfig().set("general.language", language);
		
		language_save = getConfig().getBoolean("general.language-save",false);
		getConfig().set("general.language-save", language_save);
		
		refreshdelay = getConfig().getInt("OkGlass.display-refresh-delay",10);
		getConfig().set("OkGlass.display-refresh-delay", refreshdelay);
		
		saveConfig();
	}



}
