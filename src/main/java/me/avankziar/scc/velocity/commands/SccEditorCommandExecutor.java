package main.java.me.avankziar.scc.velocity.commands;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;

import main.java.me.avankziar.scc.general.assistance.ChatApi;
import main.java.me.avankziar.scc.general.commands.tree.CommandConstructor;
import main.java.me.avankziar.scc.velocity.SCC;

public class SccEditorCommandExecutor implements SimpleCommand
{
	private SCC plugin;
	private static CommandConstructor cc;
	
	public SccEditorCommandExecutor(SCC plugin, CommandConstructor cc)
	{
		this.plugin = plugin;
		SccEditorCommandExecutor.cc = cc;
	}
	
    public CompletableFuture<List<String>> suggestAsync(final Invocation invocation) 
    {
        return CompletableFuture.completedFuture(new TabCompletion().getTabs(invocation.source(), cc.getName(), invocation.arguments()));
    }
	
    public void execute(final Invocation invocation) 
	{
        CommandSource sender = invocation.source();
        String[] args = invocation.arguments();
        
    	if(sender instanceof Player)
    	{
    		Player player = (Player) sender;
        	if(!player.hasPermission(cc.getPermission()))
    		{
    			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("NoPermission")));
    			return;
    		}
        	if(args.length == 0)
        	{
        		if(plugin.editorplayers.contains(player.getUsername()))
        		{
        			plugin.editorplayers.remove(player.getUsername());
        			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdEditor.Deactive")));
        		} else
        		{
        			plugin.editorplayers.add(player.getUsername());
        			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdEditor.Active")));
        		}
        		return;
        	} else if(args.length == 1)
        	{
        		if(args[0].equalsIgnoreCase("true"))
        		{
        			if(plugin.editorplayers.contains(player.getUsername()))
            		{
            			plugin.editorplayers.remove(player.getUsername());
            			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdEditor.Deactive")));
            		} else
            		{
            			plugin.editorplayers.add(player.getUsername());
            			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdEditor.Active")));
            		}
            		return;
        		} else if(args[0].equalsIgnoreCase("false"))
        		{
        			if(plugin.editorplayers.contains(player.getUsername()))
            		{
            			plugin.editorplayers.remove(player.getUsername());
            		} else
            		{
            			plugin.editorplayers.add(player.getUsername());
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
            			sender.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdEditor.Deactive")));
            		} else
            		{
            			plugin.editorplayers.add(playername);
            			sender.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdEditor.Active")));
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
            			sender.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdEditor.Deactive")));
            		} else
            		{
            			plugin.editorplayers.add(playername);
            			sender.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdEditor.Active")));
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
