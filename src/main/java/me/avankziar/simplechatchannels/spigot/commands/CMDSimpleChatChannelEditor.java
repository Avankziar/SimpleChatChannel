package main.java.de.avankziar.simplechatchannels.spigot.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.de.avankziar.simplechatchannels.spigot.SimpleChatChannels;

public class CMDSimpleChatChannelEditor implements CommandExecutor 
{
	private SimpleChatChannels plugin;
	private String scc = ".CMD_SCCEditor.";
	
	public CMDSimpleChatChannelEditor(SimpleChatChannels plugin)
	{
		this.plugin = plugin;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lable, String[] args)
	{
		if(!(sender instanceof Player))
    	{
    		return false;
    	}
		Player player = (Player) sender;
    	String language = plugin.getYamlHandler().get().getString("language");
    	if(!player.hasPermission("scc.cmd.editor"))
		{
			player.spigot().sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+scc+"msg02")));
			return false;
		}
    	if(args.length == 0)
    	{
    		if(SimpleChatChannels.editorplayers.contains(player.getName()))
    		{
    			SimpleChatChannels.editorplayers.remove(player.getName());
    			player.spigot().sendMessage(plugin.getUtility().tcl(
    					plugin.getYamlHandler().getL().getString(language+scc+"msg02")));
    		} else
    		{
    			SimpleChatChannels.editorplayers.add(player.getName());
    			player.spigot().sendMessage(plugin.getUtility().tcl(
    					plugin.getYamlHandler().getL().getString(language+scc+"msg01")));
    		}
    	} else
    	{
    		return false;
    	}
    	return false;
	}
}
