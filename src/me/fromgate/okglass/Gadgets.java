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

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

public class Gadgets {
	OkGlass plg;
	OGUtil u;
	List<Gadget> gadgets;
	Map<String,Scoreboard> brds;

	public Gadgets (OkGlass plg){
		this.plg = plg;
		this.u = plg.u;
		this.gadgets = new ArrayList<Gadget>();
		this.brds = new HashMap<String,Scoreboard>();
		init();
		refreshTicks();
	}

	public void init(){
		gadgets.clear();
		loadGadgetsFromJar();
	}

	public void loadGadgetsFromJar(){
		File cf = new File (plg.getDataFolder()+File.separator+"gadgets.yml");
		YamlConfiguration cfg = new YamlConfiguration();
		try {
			cfg.load(cf);
		} catch (Exception cfge) {
			u.log("Something wrong while loading the gadgets.yml file. Default settings will be used...");
		}

		File dir = new File ( plg.getDataFolder()+File.separator+"Gadgets");
		if (!dir.exists()) return;
		if (!dir.isDirectory()) return;

		File[] fl = dir.listFiles();
		if (fl.length>0)
			for (File f : fl){
				if (!f.getName().toLowerCase().endsWith(".jar")) continue;
				JarFile jarFile;
				try {
					jarFile = new JarFile(f.getPath());
				} catch (Exception e1) {
					return;
				}
				Enumeration<JarEntry> e = jarFile.entries();
				ClassLoader cl;					
				try {
					URL[] urls = { new URL("jar:file:" +f.getPath()+"!/") };
					cl = URLClassLoader.newInstance(urls, plg.getClass().getClassLoader());
				} catch (Exception e2) {
					u.log("Failed to load jar file");
					return;
				}					

				while (e.hasMoreElements()) {
					JarEntry je = (JarEntry) e.nextElement();
					if(je.isDirectory() || !je.getName().endsWith(".class")){
						continue;
					}
					String className = je.getName().substring(0,je.getName().length()-6);
					className = className.replace('/', '.');
					if (className.contains("$")) continue; //пропускаем лишнее
					try {
						Class<?> clazz = Class.forName(className, true, cl);
						Class<? extends Gadget> c = clazz.asSubclass(Gadget.class);
						Gadget g = (Gadget) c.newInstance();
						g.initGadget(plg,cfg);
						if (g.isEnabled()) {
							gadgets.add(g);
							u.log("Gadget loaded: "+g.getGadgetName());
						} else u.log("Gadjet "+g.getGadgetName()+" was not loaded (disabled at the gadgets.yml)");
					} catch (Exception e3){
						u.log("Failed to load gadget(s) from class: "+className);
						continue;
					}
				}
			}
		try {
			cfg.save(cf);
		}catch (Exception cfge){
			u.log("Failed to save settings at gadgets.yml file");
		}


	}
	
	public void resetScoreBoard (Player p){
		Scoreboard brd =plg.getServer().getScoreboardManager().getNewScoreboard();
		Objective obj = brd.getObjective(getTitle());
		if (obj == null) obj = brd.registerNewObjective(getTitle(), "dummy");
		obj.setDisplaySlot(DisplaySlot.SIDEBAR);
		p.setScoreboard(brd);
		brds.put(p.getName(), brd);
	}
	
	public Scoreboard getScoreBoard(Player p){
		if (brds.containsKey(p.getName())) return brds.get(p.getName());
		resetScoreBoard(p);
		return brds.get(p.getName());
	}

	public void clearGadgets(Player p){
		Scoreboard brd = getScoreBoard(p);
		Objective obj = brd.getObjective(getTitle());
		if (obj == null) obj = brd.registerNewObjective(getTitle(), "dummy");
		for (OfflinePlayer op : brd.getPlayers())
			brd.resetScores(op);
	}

	public void clearGadgets(){
		for (Player p : Bukkit.getOnlinePlayers())
			clearGadgets(p);
	}
	
	public Objective getObjective(Scoreboard brd){
		Objective obj = brd.getObjective(getTitle());
		if (obj != null) return obj; 
		obj = brd.registerNewObjective(getTitle(), "dummy");
		obj.setDisplaySlot(DisplaySlot.SIDEBAR);
		return obj;
	}
	
	public void refreshGadgets(Player p){
		if (gadgets.isEmpty()) return;
		Set<String> showngadgets  = new HashSet<String>();
		Scoreboard brd = getScoreBoard(p);
		Objective obj = getObjective(brd);
		for (int i = gadgets.size()-1; i>=0; i--){
			Gadget g = gadgets.get(i);
			if (!g.hasPermission(p)) continue;
			try {
				Map<String,Integer> rst = g.getGadgetResult(p);
				if ((rst == null)||(rst.isEmpty())) continue; //пустых пропускаем
				for (String key : rst.keySet()){
					Score score = obj.getScore(Bukkit.getOfflinePlayer(key));
					showngadgets.add(key);
					score.setScore(rst.get(key));
				}
			} catch (Exception e){
				u.log("Failed to interact with gadget "+g.getGadgetName()+". Gadget was disabled.");
				gadgets.remove(i);
				if (plg.debug) e.printStackTrace();
			} catch (Error e){
				u.log("Failed to interact with gadget "+g.getGadgetName()+". Gadget was disabled.");
				gadgets.remove(i);
				if (plg.debug) e.printStackTrace();
			}
		}
		for (OfflinePlayer op : brd.getPlayers())
			if (!showngadgets.contains(op.getName()))
				brd.resetScores(op);
	}
	
	public void sendGadgetsToAllPlayers(){
		for (Player p : Bukkit.getOnlinePlayers()){
			if (!p.hasPermission("okglass.show")) continue;
			if (isPlayerCanSeeGadget(p)) refreshGadgets(p);
			else clearGadgets(p);
		}
	}

	public boolean isPlayerCanSeeGadget(Player p){
		if (p.hasMetadata("okglass")) return true;
		return false;
	}

	public void setPlayerCanSeeGadget (Player p, boolean cansee){
		if (cansee) p.setMetadata("okglass", new FixedMetadataValue (plg, true));
		else if (p.hasMetadata("okglass")) p.removeMetadata("okglass", plg);
	}

	public void refreshTicks(){
		Bukkit.getScheduler().runTaskTimerAsynchronously(plg, new Runnable(){
			@Override
			public void run() {
				sendGadgetsToAllPlayers();
			}
		}, 200,20*plg.refreshdelay);
	}

	public void printGadgetList(Player p){
		if (gadgets.isEmpty()) u.printMSG(p, "gl_gadgjetlistempty");
		else {
			String gs = "";
			for (int i = 0; i<gadgets.size();i++)
				gs = gs+", "+gadgets.get(i).getGadgetName();
			gs = gs.replaceFirst(", ","");
			u.printMSG(p, "gl_gadgjetlist",gadgets.size(),"&e"+gs);
		}
	}

	public String getTitle(){
		return ChatColor.translateAlternateColorCodes('&', plg.brdname);
	}
	
	public void disableAllGadgets(){
		for (Gadget g : gadgets){
			g.disableGadget();
			g.log("Disabled...");
			g = null;
		}
		gadgets.clear();
	}


}
