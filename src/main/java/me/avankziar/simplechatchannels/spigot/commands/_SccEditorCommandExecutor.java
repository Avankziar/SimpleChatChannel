package main.java.me.avankziar.simplechatchannels.spigot.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.scc.objects.ChatApi;
import main.java.me.avankziar.simplechatchannels.spigot.SimpleChatChannels;
import main.java.me.avankziar.simplechatchannels.spigot.commands.tree.CommandConstructor;

public class _SccEditorCommandExecutor
{
	/*private SimpleChatChannels plugin;
	private String scc = "CmdSccEditor.";
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
			player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString("CmdScc.NoPermission")));
			return false;
		}
    	if(args.length == 0)
    	{
    		if(SimpleChatChannels.editorplayers.contains(player.getName()))
    		{
    			SimpleChatChannels.editorplayers.remove(player.getName());
    			player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString(scc+"Out")));
    		} else
    		{
    			SimpleChatChannels.editorplayers.add(player.getName());
    			player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString(scc+"In")));
    		}
    		return true;
    	} else 
    	{
    		return false;
    	}
    }*/
}
