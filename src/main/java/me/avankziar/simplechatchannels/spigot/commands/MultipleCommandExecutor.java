package main.java.me.avankziar.simplechatchannels.spigot.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.simplechatchannels.spigot.SimpleChatChannels;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;

public class MultipleCommandExecutor implements CommandExecutor 
{
	private SimpleChatChannels plugin;
	
	public MultipleCommandExecutor(SimpleChatChannels plugin)
	{
		this.plugin = plugin;
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String lable, String[] args) 
	{
		// Checks if the label is one of yours.
		String language = plugin.getUtility().getLanguage();
		if (cmd.getName().equalsIgnoreCase("scc") || cmd.getName().equalsIgnoreCase("simplechatchannels")) 
		{		
			if (!(sender instanceof Player)) 
			{
				SimpleChatChannels.log.info("/scc is only for Player!");
				return false;
			}
			Player player = (Player) sender;
			if (args.length == 0) 
			{
				plugin.getCommandHelper().scc(player); //Info Command
				return false;
			}
			if (SimpleChatChannels.sccarguments.containsKey(args[0])) 
			{
				CommandModule mod = SimpleChatChannels.sccarguments.get(args[0]);
				//Abfrage ob der Spieler die Permission hat
				if (player.hasPermission(mod.permission)) 
				{
					//Abfrage, ob der Spieler in den min und max Argumenten Bereich ist.
					if(args.length >= mod.minArgs && args.length <= mod.maxArgs)
					{
						mod.run(sender, args);
					} else
					{
						///Deine Eingabe ist fehlerhaft, klicke hier auf den Text um &cweitere Infos zu bekommen!
						player.spigot().sendMessage(plugin.getUtility().clickEvent(language+".CmdScc.InputIsWrong",
								ClickEvent.Action.RUN_COMMAND, "/scc", true));
						return false;
					}
				} else 
				{
					///Du hast dafür keine Rechte!
					player.spigot().sendMessage(plugin.getUtility().tctlYaml(language+".CmdScc.NoPermission"));
					return false;
				}
			} else 
			{
				///Deine Eingabe ist fehlerhaft, klicke hier auf den Text um &cweitere Infos zu bekommen!
				player.spigot().sendMessage(plugin.getUtility().clickEvent(language+".CmdScc.InputIsWrong",
						ClickEvent.Action.RUN_COMMAND, "/scc", true));
				return false;
			}
		} else if(cmd.getName().equalsIgnoreCase("clch") || cmd.getName().equalsIgnoreCase("clickchat"))
		{
			if(!(sender instanceof Player))
	    	{
				SimpleChatChannels.log.info("/clch is only for Player!");
				return false;
	    	}
	    	if(args.length<3)
	    	{
	    		return false;
	    	}
	    	if(Bukkit.getPlayer(args[0])==null)
	    	{
	    		return false;
	    	}
	    	Player t = Bukkit.getPlayer(args[0]);
	    	String msg = "";
	    	for(int i = 2;i < args.length;i++)
	    	{
	    		msg += args[i]+" "; 
	    	}
	    	///Klicke hier um die %number%.te Antwortmöglichkeit zu nehmen!
			t.spigot().sendMessage(plugin.getUtility().apichat(msg, ClickEvent.Action.RUN_COMMAND, args[1],
					HoverEvent.Action.SHOW_TEXT, plugin.getYamlHandler().getL().getString(language+".CmdClickChat.ClickAnswer"), false));
	    	return true;
		} else if(cmd.getName().equalsIgnoreCase("scceditor"))
		{
			if(!(sender instanceof Player))
	    	{
				SimpleChatChannels.log.info("/scceditor is only for Player!");
				return false;
	    	}
			Player player = (Player) sender;
	    	String scc = ".CmdSccEditor.";
	    	if(!player.hasPermission("scc.cmd.editor"))
			{
				player.spigot().sendMessage(plugin.getUtility().tctl(
						plugin.getYamlHandler().getL().getString(language+".CmdScc.NoPermission")));
				return false;
			}
	    	if(args.length == 0)
	    	{
	    		if(SimpleChatChannels.editorplayers.contains(player.getName()))
	    		{
	    			SimpleChatChannels.editorplayers.remove(player.getName());
	    			player.spigot().sendMessage(plugin.getUtility().tctlYaml(language+scc+"Out"));
	    		} else
	    		{
	    			SimpleChatChannels.editorplayers.add(player.getName());
	    			player.spigot().sendMessage(plugin.getUtility().tctlYaml(language+scc+"In"));
	    		}
	    	} else
	    	{
	    		return false;
	    	}
	    	return false;
		}
		return false;
	}
}
