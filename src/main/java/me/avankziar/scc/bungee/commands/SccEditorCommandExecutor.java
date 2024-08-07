package main.java.me.avankziar.scc.bungee.commands;

import main.java.me.avankziar.scc.bungee.SCC;
import main.java.me.avankziar.scc.general.assistance.ChatApiOld;
import main.java.me.avankziar.scc.general.commands.tree.CommandConstructor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class SccEditorCommandExecutor extends Command
{
	private SCC plugin;
	private static CommandConstructor cc;
	
	public SccEditorCommandExecutor(SCC plugin, CommandConstructor cc)
	{
		super(cc.getName(), null);
		this.plugin = plugin;
		SccEditorCommandExecutor.cc = cc;
	}
	
    public void execute(CommandSender sender, String[] args)
    {
    	if(sender instanceof ProxiedPlayer)
    	{
    		ProxiedPlayer player = (ProxiedPlayer) sender;
        	if(!player.hasPermission(cc.getPermission()))
    		{
    			player.sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("NoPermission")));
    			return;
    		}
        	if(args.length == 0)
        	{
        		if(plugin.editorplayers.contains(player.getName()))
        		{
        			plugin.editorplayers.remove(player.getName());
        			player.sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdEditor.Deactive")));
        		} else
        		{
        			plugin.editorplayers.add(player.getName());
        			player.sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdEditor.Active")));
        		}
        		return;
        	} else if(args.length == 1)
        	{
        		if(args[0].equalsIgnoreCase("true"))
        		{
        			if(plugin.editorplayers.contains(player.getName()))
            		{
            			plugin.editorplayers.remove(player.getName());
            			player.sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdEditor.Deactive")));
            		} else
            		{
            			plugin.editorplayers.add(player.getName());
            			player.sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdEditor.Active")));
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
        	} else if(args.length == 2)
        	{
    			String playername = args[1];
        		if(args[0].equalsIgnoreCase("true"))
        		{
        			if(plugin.editorplayers.contains(playername))
            		{
            			plugin.editorplayers.remove(playername);
            			sender.sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdEditor.Deactive")));
            		} else
            		{
            			plugin.editorplayers.add(playername);
            			sender.sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdEditor.Active")));
            		}
            		return;
        		} else if(args[0].equalsIgnoreCase("false"))
        		{
        			if(plugin.editorplayers.contains(playername))
            		{
            			plugin.editorplayers.remove(playername);
            		} else
            		{
            			plugin.editorplayers.add(playername);
            		}
            		return;
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
            			sender.sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdEditor.Deactive")));
            		} else
            		{
            			plugin.editorplayers.add(playername);
            			sender.sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdEditor.Active")));
            		}
            		return;
        		} else if(args[0].equalsIgnoreCase("false"))
        		{
        			if(plugin.editorplayers.contains(playername))
            		{
            			plugin.editorplayers.remove(playername);
            		} else
            		{
            			plugin.editorplayers.add(playername);
            		}
            		return;
        		}
        	}
    	}    	
    }
}
