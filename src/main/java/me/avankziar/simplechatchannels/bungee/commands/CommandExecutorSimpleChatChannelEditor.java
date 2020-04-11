package main.java.me.avankziar.simplechatchannels.bungee.commands;

import main.java.me.avankziar.simplechatchannels.bungee.SimpleChatChannels;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class CommandExecutorSimpleChatChannelEditor extends Command
{
	private SimpleChatChannels plugin;
	private String scc = ".CmdSccEditor.";
	
	public CommandExecutorSimpleChatChannelEditor(SimpleChatChannels plugin)
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
    	String language = plugin.getUtility().getLanguage();
    	if(!player.hasPermission("scc.cmd.editor"))
		{
			player.sendMessage(plugin.getUtility().tctlYaml(language+".CmdScc.NoPermission"));
			return;
		}
    	if(args.length == 0)
    	{
    		if(plugin.editorplayers.contains(player.getName()))
    		{
    			plugin.editorplayers.remove(player.getName());
    			player.sendMessage(plugin.getUtility().tctlYaml(language+scc+"Out"));
    			String message = "editor"+µ+player.getName()+µ+"remove";
    			plugin.getUtility().sendSpigotMessage("simplechatchannels:sccbungee", message);
    		} else
    		{
    			plugin.editorplayers.add(player.getName());
    			player.sendMessage(plugin.getUtility().tctlYaml(language+scc+"In"));
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
