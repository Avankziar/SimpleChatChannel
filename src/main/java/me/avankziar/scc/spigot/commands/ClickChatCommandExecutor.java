package main.java.me.avankziar.scc.spigot.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.scc.general.assistance.ChatApi;
import main.java.me.avankziar.scc.general.commands.tree.CommandConstructor;
import main.java.me.avankziar.scc.spigot.SCC;

public class ClickChatCommandExecutor implements CommandExecutor
{
	private SCC plugin;
	private static CommandConstructor cc;
	
	public ClickChatCommandExecutor(SCC plugin, CommandConstructor cc)
	{
		this.plugin = plugin;
		ClickChatCommandExecutor.cc = cc;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lable, String[] args)
	{
		if(!(sender instanceof Player))
    	{
    		return false;
    	}	
		Player player = (Player) sender;
		if(cc.getPermission() != null)
		{
			if(!player.hasPermission(cc.getPermission()))
			{
				player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("NoPermission")));
				return false;
			}
		}
    	if(args.length<3)
    	{
    		return false;
    	}
    	if(plugin.getServer().getPlayer(args[0])==null)
    	{
    		return false;
    	}
    	Player t = plugin.getServer().getPlayer(args[0]);	
    	String msg = "";
    	for(int i = 2;i < args.length;i++)
    	{
    		msg += args[i]+" "; 
    	}
    	t.spigot().sendMessage(ChatApi.tctl(ChatApi.clickHover(msg, 
    			"RUN_COMMAND", args[1],
				"SHOW_TEXT", plugin.getYamlHandler().getLang().getString("CmdClickChat.ClickAnswer")
				.replace("%number%", args[1]))));
    	return false;
	}
}
