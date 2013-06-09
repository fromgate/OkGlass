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

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class OKListener implements Listener{
	OkGlass plg;
	
	public OKListener (OkGlass plg){
		this.plg = plg;
	}
	
	
	@EventHandler(priority=EventPriority.NORMAL)
	public void onJoin (PlayerJoinEvent event){
		plg.u.UpdateMsg(event.getPlayer());
		plg.gadgets.setPlayerCanSeeGadget(event.getPlayer(), true);
		plg.gadgets.refreshGadgets(event.getPlayer());
	}

}
