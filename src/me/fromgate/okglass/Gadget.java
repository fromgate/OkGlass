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


import java.util.HashMap;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;


public abstract class Gadget {
	private OkGlass plg;
	YamlConfiguration cfg;
	boolean enabled = true;
	private Player currentplayer = null;
	private Map<String,Integer> result;
	private String perm = "okglass.show";
	
	public boolean isEnabled(){
		return enabled;
	}
	
	public void setEnabled(boolean enabled){
		this.enabled = enabled;
	}
	
	protected void initGadget(OkGlass plugin, YamlConfiguration cfg){
		this.cfg = cfg;
		plg = plugin;
		setEnabled(loadBoolean ("enabled",true));
		
		try{
		if (this.enabled) onEnable();
		} catch (Throwable e){
			log("Gadget initialization failed!");
			if (plg.debug) e.printStackTrace();
			this.enabled = false;
		}
		perm = (loadStr("permission",perm));
	}
	
	protected void disableGadget(){
		try{
		if (this.enabled) onDisable();
		} catch (Throwable e){
			log("Failed to disable Gadget");
			if (plg.debug) e.printStackTrace();
			this.enabled = false;
		}
	}

	public int loadInt(String key, int defvalue){
		int v = cfg.getInt(getName()+"."+key,defvalue);
		cfg.set(getName()+"."+key, v);
		return v;
	}
	public String loadStr(String key, String defvalue){
		String v = cfg.getString(getName()+"."+key,defvalue);
		cfg.set(getName()+"."+key, v);
		return v;
	}
	public boolean loadBoolean(String key, boolean defvalue){
		boolean v = cfg.getBoolean(getName()+"."+key,defvalue);
		cfg.set(getName()+"."+key, v);
		return v;
	}
	
	
	public void addResult(String name, int value){
		if (result != null) result.put(name, value);
	}
	
	protected Map<String,Integer> getGadgetResult(Player p){
		currentplayer = p;
		result = new HashMap<String,Integer>();
		process();
		if ((result==null)||(result.isEmpty())) return null;
		Map<String,Integer> newrst = new HashMap<String,Integer>();
		for (String mapkey : result.keySet()){
			String newkey = ChatColor.translateAlternateColorCodes('&', mapkey);
			if (newkey.equalsIgnoreCase(ChatColor.stripColor(newkey))) 
				newkey = ChatColor.translateAlternateColorCodes('&', plg.defaultcolor.isEmpty() ? newkey : "&"+plg.defaultcolor+newkey);
			if (newkey.length()>16) newkey = newkey.substring(0,15);
			newrst.put(newkey, result.get(mapkey));
		}
		return newrst;
	}
	
	protected String getGadgetName(){
		String str = getName();
		if ((str == null)||(str.isEmpty())) str = this.getClass().getName();
		return str;
	}
	
	public Player getPlayer(){
		return this.currentplayer;
	}
	
	public void log(String msg){
		plg.u.log("["+getName()+"] "+msg);
	}
	
	public Server getServer(){
		return plg.getServer();
	}
	
	public JavaPlugin getOkGlassPlugin(){
		return plg;
	}
	
	public boolean hasPermission (Player p){
		return p.hasPermission(perm);
	}

	public void setPermission (String permission){
		this.perm = permission;
	}
	
	public abstract void onEnable();	
	public abstract String getName();
	public abstract void onDisable();
	public abstract void process();

	
}
