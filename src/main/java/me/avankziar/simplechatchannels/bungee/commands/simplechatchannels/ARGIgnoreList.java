package main.java.me.avankziar.simplechatchannels.bungee.commands.simplechatchannels;

import java.util.ArrayList;

import main.java.me.avankziar.simplechatchannels.bungee.SimpleChatChannels;
import main.java.me.avankziar.simplechatchannels.bungee.assistance.Utility;
import main.java.me.avankziar.simplechatchannels.bungee.commands.tree.ArgumentConstructor;
import main.java.me.avankziar.simplechatchannels.bungee.commands.tree.ArgumentModule;
import main.java.me.avankziar.simplechatchannels.objects.ChatApi;
import main.java.me.avankziar.simplechatchannels.objects.ConvertHandler;
import main.java.me.avankziar.simplechatchannels.objects.IgnoreObject;
import main.java.me.avankziar.simplechatchannels.bungee.database.MysqlHandler;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

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
		ProxiedPlayer player = (ProxiedPlayer) sender;
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
		player.sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString(language+"Ignore.List")
				.replace("%il%", list)));
		return;
	}
}
