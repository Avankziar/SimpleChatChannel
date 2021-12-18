package main.java.me.avankziar.scc.spigot.commands;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.scc.objects.ChatApi;
import main.java.me.avankziar.scc.objects.StaticValues;
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
    	if(sender instanceof Player)
    	{
    		Player player = (Player) sender;
        	if(!player.hasPermission(cc.getPermission()))
    		{
    			player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("CmdScc.NoPermission")));
    			return false;
    		}
        	if(args.length == 0)
        	{
        		send(player);
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
        	} else if(args.length == 1)
        	{
        		if(args[0].equalsIgnoreCase("true"))
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
        			send(player);
            		return true;
        		} else if(args[0].equalsIgnoreCase("false"))
        		{
        			if(plugin.editorplayers.contains(player.getName()))
            		{
            			plugin.editorplayers.remove(player.getName());
            		} else
            		{
            			plugin.editorplayers.add(player.getName());
            		}
        			send(player);
            		return true;
        		}
        	}
    	} else
    	{
    		if(args.length == 2)
        	{
    			String playername = args[1];
        		if(args[0].equalsIgnoreCase("true"))
        		{
        			if(plugin.editorplayers.contains(playername))
            		{
            			plugin.editorplayers.remove(playername);
            			sender.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdEditor.Deactive")));
            		} else
            		{
            			plugin.editorplayers.add(playername);
            			sender.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdEditor.Active")));
            		}
        			send(Bukkit.getPlayer(playername));
            		return true;
        		} else if(args[0].equalsIgnoreCase("false"))
        		{
        			if(plugin.editorplayers.contains(playername))
            		{
            			plugin.editorplayers.remove(playername);
            		} else
            		{
            			plugin.editorplayers.add(playername);
            		}
        			send(Bukkit.getPlayer(playername));
            		return true;
        		}
        	}
    	}
    	
    	return false;
    }
    
    private void send(Player player)
    {
    	ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(stream);
        try {
			out.writeUTF(StaticValues.SCC_EDITOR);
			out.writeUTF(player.getName());
		} catch (IOException e) {
			e.printStackTrace();
		}
        player.sendPluginMessage(plugin, StaticValues.SCC_TOBUNGEE, stream.toByteArray());
    }
}
