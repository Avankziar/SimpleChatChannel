package main.java.me.avankziar.simplechatchannels.bungee.commands;

import main.java.me.avankziar.simplechatchannels.bungee.SimpleChatChannels;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class CMDSimpleChatChannelEditor extends Command
{
	private SimpleChatChannels plugin;
	private String scc = ".CMD_SCCEditor.";
	
	public CMDSimpleChatChannelEditor(SimpleChatChannels plugin)
	{
        super("scceditor",null,"simplechatchanneleditor");
        this.plugin = plugin;
    }
	
    public void execute(CommandSender sender, String[] args)
    {
    	if(!(sender instanceof ProxiedPlayer))
    	{
    		return;
    	}
    	ProxiedPlayer player = (ProxiedPlayer) sender;
    	String µ = "µ";
    	String language = plugin.getYamlHandler().get().getString("language");
    	if(!player.hasPermission("scc.cmd.editor"))
		{
			player.sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+scc+"msg02")));
			return;
		}
    	if(args.length == 0)
    	{
    		if(plugin.editorplayers.contains(player.getName()))
    		{
    			plugin.editorplayers.remove(player.getName());
    			player.sendMessage(plugin.getUtility().tcl(
    					plugin.getYamlHandler().getL().getString(language+scc+"msg02")));
    			String message = "editor"+µ+player.getName()+µ+"remove";
    			plugin.getUtility().sendSpigotMessage("simplechatchannels:sccbungee", message);
    		} else
    		{
    			plugin.editorplayers.add(player.getName());
    			player.sendMessage(plugin.getUtility().tcl(
    					plugin.getYamlHandler().getL().getString(language+scc+"msg01")));
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
