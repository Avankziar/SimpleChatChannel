package main.java.me.avankziar.scc.bungee.commands;

import main.java.me.avankziar.scc.bungee.SimpleChatChannels;
import main.java.me.avankziar.scc.bungee.commands.tree.CommandConstructor;
import main.java.me.avankziar.scc.objects.ChatApi;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class SccEditorCommandExecutor extends Command
{
	private SimpleChatChannels plugin;
	private static CommandConstructor cc;
	
	public SccEditorCommandExecutor(SimpleChatChannels plugin, CommandConstructor cc)
	{
		super(cc.getName(), null);
		this.plugin = plugin;
		SccEditorCommandExecutor.cc = cc;
	}
	
    public void execute(CommandSender sender, String[] args)
    {
    	if(!(sender instanceof ProxiedPlayer))
    	{
    		return;
    	}
    	ProxiedPlayer player = (ProxiedPlayer) sender;
    	if(!player.hasPermission(cc.getPermission()))
		{
			player.sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("NoPermission")));
			return;
		}
    	if(args.length == 0)
    	{
    		if(plugin.editorplayers.contains(player.getName()))
    		{
    			plugin.editorplayers.remove(player.getName());
    			player.sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("CmdEditor.Deactive")));
    		} else
    		{
    			plugin.editorplayers.add(player.getName());
    			player.sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("CmdEditor.Active")));
    		}
    		return;
    	} else if(args.length == 1)
    	{
    		if(args[0].equalsIgnoreCase("true"))
    		{
    			if(plugin.editorplayers.contains(player.getName()))
        		{
        			plugin.editorplayers.remove(player.getName());
        			player.sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("CmdEditor.Deactive")));
        		} else
        		{
        			plugin.editorplayers.add(player.getName());
        			player.sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("CmdEditor.Active")));
        		}
        		return;
    		} else if(args[0].equalsIgnoreCase("false"))
    		{
    			if(plugin.editorplayers.contains(player.getName()))
        		{
        			plugin.editorplayers.remove(player.getName());
        		} else
        		{
        			plugin.editorplayers.add(player.getName());
        		}
        		return;
    		}
    	}
    }
}
