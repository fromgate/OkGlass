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

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class OGUtil extends FGUtilCore implements CommandExecutor {
	OkGlass plg;
	
	public OGUtil(OkGlass plugin, boolean vcheck, boolean savelng, String language, String devbukkitname, String version_name, String plgcmd, String px){
		super (plugin, vcheck, savelng, language, devbukkitname, version_name, plgcmd, px);
		this.plg = plugin;
		fillMSG();
		if (savelng) this.SaveMSG();
		initCommands();
	}
	
	public void initCommands(){
		addCmd("help", "config", "hlp_thishelp", "/gadget help");
		addCmd("show", "showhide", "hlp_show", "/gadget [show]");
		addCmd("hide", "showhide", "hlp_hide", "/gadget hide");
		addCmd("cfg", "config", "hlp_cfg", "/gadget cfg");
		addCmd("reload", "config", "hlp_reload", "/gadget reload");
	}
	
	public boolean executeCommand(Player p, String cmd){
		if (cmd.equalsIgnoreCase("help")){
			PrintHlpList(p, 1, 100);
		} else if (cmd.equalsIgnoreCase("show")){
			if (plg.gadgets.isPlayerCanSeeGadget(p)) printMSG (p,"msg_alreadyshown");
			else {
				plg.gadgets.setPlayerCanSeeGadget(p, true);
				printEnDis(p, "msg_cmdshow",true);
				plg.gadgets.sendGadgetsToAll();
			}
		} else if (cmd.equalsIgnoreCase("hide")){
			if (!plg.gadgets.isPlayerCanSeeGadget(p)) printMSG (p,"msg_alreadyhidden");
			else {
				plg.gadgets.setPlayerCanSeeGadget(p, false);
				printEnDis(p, "msg_cmdshow",false);
				plg.gadgets.sendGadgetsToAll();
			}
		} else if (cmd.equalsIgnoreCase("reload")){
			plg.gadgets.clearGadgets();
			plg.gadgets.disableAllGadgets();
			plg.gadgets.init();
			plg.reloadConfig();
			plg.reloadCfg();
			plg.gadgets.sendGadgetsToAll();
			printMSG(p,"msg_reload");
		} else if (cmd.equalsIgnoreCase("cfg")){
			printConfig(p, 1, 1000, false, true);
			plg.gadgets.printGadgetList(p);
		} else return false;
		return true;
	}
	
	
	public void fillMSG(){
		addMSG ("gl_gadgjetlistempty", "Gadjet list is empty. You need to download and install Ok'Glass gadjet!");
		addMSG ("gl_gadgjetlist", "Installed %1% gadgets: %2%");
		addMSG ("msg_cmdshow", "Ok'Glass display is");
		addMSG ("msg_alreadyhidden", "Ok'Glass window is already hidden!");
		addMSG ("msg_alreadyshown", "Ok'Glass window is already visible!");
		addMSG ("msg_reload", "Reloaded...");
		addMSG ("hlp_show", "%1% - show (than hide) Ok'Glass window");
		addMSG ("hlp_hide", "%1% - hide Ok'Glass window");
		addMSG ("hlp_cfg", "%1% - display current configuration");
		addMSG ("hlp_reload", "%1% - reload plugin configuration and reload gadgets");
		addMSG ("cfgmsg_OkGlass.display-refresh-delay", "Display refresh rate (seconds): %1%");
		addMSG ("cfgmsg_OkGlass.title", "OkGlass window title (default: OK'GLASS): %1%");
		addMSG ("cfgmsg_OkGlass.debug", "Debug mode: %1%");
		addMSG ("cfgmsg_OkGlass.default-color", "Default color of the gadget's title: %1%");
		
		
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String cmdLabel, String[] args) {
		if (sender instanceof Player){
			Player p = (Player) sender;
			if ((args.length==0)&&(checkCmdPerm(p, "show"))) {
				if (plg.gadgets.isPlayerCanSeeGadget(p)) return executeCommand(p, "hide");
				else return executeCommand(p, "show");
			} else if ((args.length>0)&&(checkCmdPerm(p, args[0]))) return executeCommand(p, args[0]);
			else printMSG(p, "cmd_cmdpermerr",'c');
		}
		return false;
	}
	
	
}
