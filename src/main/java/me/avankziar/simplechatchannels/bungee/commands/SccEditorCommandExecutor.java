package main.java.me.avankziar.simplechatchannels.bungee.commands;

import main.java.me.avankziar.scc.bungee.SimpleChatChannels;
import main.java.me.avankziar.simplechatchannels.bungee.commands.tree.CommandConstructor;
import main.java.me.avankziar.simplechatchannels.objects.ChatApi;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class SccEditorCommandExecutor extends Command
{
	private SimpleChatChannels plugin;
	private String scc = "CmdSccEditor.";
	private static CommandConstructor cc;
	
	public SccEditorCommandExecutor(SimpleChatChannels plugin, CommandConstructor cc)
	{
		super(cc.getName(),null);
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
    	String µ = "µ";
    	if(!player.hasPermission(cc.getPermission()))
		{
			player.sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("CmdScc.NoPermission")));
			return;
		}
    	if(args.length == 0)
    	{
    		if(plugin.editorplayers.contains(player.getName()))
    		{
    			plugin.editorplayers.remove(player.getName());
    			player.sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString(scc+"Out")));
    			String message = "editor"+µ+player.getName()+µ+"remove";
    			plugin.getUtility().sendSpigotMessage("simplechatchannels:sccbungee", message);
    		} else
    		{
    			plugin.editorplayers.add(player.getName());
    			player.sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString(scc+"In")));
    			String message = "editor"+µ+player.getName()+µ+"add";
    			plugin.getUtility().sendSpigotMessage("simplechatchannels:sccbungee", message);
    		}
    		return;
    	} else 
    	{
    		return;
    	}
    }
}
