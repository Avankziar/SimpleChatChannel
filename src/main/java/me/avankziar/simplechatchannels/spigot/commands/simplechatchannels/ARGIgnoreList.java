package main.java.me.avankziar.simplechatchannels.spigot.commands.simplechatchannels;

import java.util.ArrayList;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.simplechatchannels.objects.ChatApi;
import main.java.me.avankziar.simplechatchannels.objects.ConvertHandler;
import main.java.me.avankziar.simplechatchannels.objects.IgnoreObject;
import main.java.me.avankziar.simplechatchannels.spigot.SimpleChatChannels;
import main.java.me.avankziar.simplechatchannels.spigot.assistance.Utility;
import main.java.me.avankziar.simplechatchannels.spigot.commands.tree.ArgumentConstructor;
import main.java.me.avankziar.simplechatchannels.spigot.commands.tree.ArgumentModule;
import main.java.me.avankziar.simplechatchannels.spigot.database.MysqlHandler;

public class ARGIgnoreList extends ArgumentModule
{
	private SimpleChatChannels plugin;
	
	public ARGIgnoreList(SimpleChatChannels plugin, ArgumentConstructor argumentConstructor)
	{
		super(plugin, argumentConstructor);
		this.plugin = plugin;
	}

	@Override
	public void run(CommandSender sender, String[] args)
	{
		Player player = (Player) sender;
		Utility utility = plugin.getUtility();
		String language = "CmdScc.";
		if(utility.rightArgs(player,args,1))
		{
			return;
		}
		int end = plugin.getMysqlHandler().lastID(MysqlHandler.Type.IGNOREOBJECT);
		ArrayList<IgnoreObject> iolist = ConvertHandler.convertListII(
				plugin.getMysqlHandler().getList(MysqlHandler.Type.IGNOREOBJECT,
						"`id`", true, 0, end, "`player_uuid` = ?", player.getUniqueId().toString()));
		String list = "";
		if(iolist == null)
		{
			list = "None";
		}
		if(iolist.isEmpty())
		{
			list = "None";
		}
		for(IgnoreObject io : iolist)
		{
			list += io.getIgnoreName()+" &9| &r";
		}
		player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString(language+"Ignore.List")
				.replace("%il%", list)));
		return;
	}
}