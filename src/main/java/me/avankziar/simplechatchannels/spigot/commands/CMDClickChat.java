package main.java.de.avankziar.simplechatchannels.spigot.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.de.avankziar.simplechatchannels.spigot.SimpleChatChannels;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class CMDClickChat implements CommandExecutor
{
	private SimpleChatChannels plugin;
	
	public CMDClickChat(SimpleChatChannels plugin)
	{
		this.plugin = plugin;
	}
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lable, String[] args) 
	{
		if(!(sender instanceof Player))
    	{
    		return false;
    	}
		Player p = (Player)sender;
		String l = plugin.getYamlHandler().get().getString("language");
		if(!p.hasPermission("scc.cmd.clickchat"))
		{
			return false;
		}
    	if(args.length<3)
    	{
    		return false;
    	}
    	if(Bukkit.getPlayer(args[0])==null)
    	{
    		return false;
    	}
    	Player t = Bukkit.getPlayer(args[0]);
    	String number = args[1];
    	String msg = "";
    	for(int i = 2;i < args.length;i++)
    	{
    		msg += args[i]+" "; 
    	}
    	TextComponent msg1 = plugin.getUtility().tcl(msg);
    	msg1.setHoverEvent( new HoverEvent(HoverEvent.Action.SHOW_TEXT, 
				new ComponentBuilder(plugin.getUtility().tl(
						plugin.getYamlHandler().getL().getString(l+".CMD_ClickChat.msg01")
						.replaceAll("%number%", args[1]))).create()));
		msg1.setClickEvent( new ClickEvent(ClickEvent.Action.RUN_COMMAND, number));
    	t.spigot().sendMessage(msg1);
		return false;
	}
}
