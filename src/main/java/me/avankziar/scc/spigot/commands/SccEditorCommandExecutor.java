package main.java.me.avankziar.scc.spigot.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.scc.objects.ChatApi;
import main.java.me.avankziar.scc.spigot.SimpleChatChannels;
import main.java.me.avankziar.scc.spigot.commands.tree.CommandConstructor;

public class SccEditorCommandExecutor implements CommandExecutor
{
	private SimpleChatChannels plugin;
	private static CommandConstructor cc;
	
	public SccEditorCommandExecutor(SimpleChatChannels plugin, CommandConstructor cc)
	{
		this.plugin = plugin;
		SccEditorCommandExecutor.cc = cc;
	}
	
    public boolean onCommand(CommandSender sender, Command cmd, String lable, String[] args)
    {
    	if(!(sender instanceof Player))
    	{
    		return false;
    	}
    	Player player = (Player) sender;
    	if(!player.hasPermission(cc.getPermission()))
		{
			player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("CmdScc.NoPermission")));
			return false;
		}
    	if(args.length == 0)
    	{
    		if(plugin.editorplayers.contains(player.getName()))
    		{
    			plugin.editorplayers.remove(player.getName());
    			player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("CmdEditor.Deactive")));
    		} else
    		{
    			plugin.editorplayers.add(player.getName());
    			player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("CmdEditor.Active")));
    		}
    		return true;
    	}
    	return false;
    }
}
