package main.java.me.avankziar.scc.bungee.commands.scc;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import main.java.me.avankziar.scc.bungee.SCC;
import main.java.me.avankziar.scc.bungee.assistance.Utility;
import main.java.me.avankziar.scc.bungee.commands.tree.ArgumentModule;
import main.java.me.avankziar.scc.bungee.objects.ChatUserHandler;
import main.java.me.avankziar.scc.general.assistance.ChatApiOld;
import main.java.me.avankziar.scc.general.commands.tree.ArgumentConstructor;
import main.java.me.avankziar.scc.general.database.MysqlType;
import main.java.me.avankziar.scc.general.handlers.ConvertHandler;
import main.java.me.avankziar.scc.general.objects.ChatUser;
import main.java.me.avankziar.scc.general.objects.UsedChannel;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class ARGUpdatePlayer extends ArgumentModule
{
	private SCC plugin;
	
	public ARGUpdatePlayer(SCC plugin, ArgumentConstructor argumentConstructor)
	{
		super(argumentConstructor);
		this.plugin = plugin;
	}

	@Override
	public void run(CommandSender sender, String[] args)
	{
		ProxiedPlayer players = (ProxiedPlayer) sender;
		String playername = args[1];
		ProxiedPlayer target = plugin.getProxy().getPlayer(playername);
		ChatUser cuo = ChatUserHandler.getChatUser(args[1]);
		if(cuo == null || target == null)
		{
			players.sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("NoPlayerExist")));
			return;
		}
		plugin.getUtility().controlUsedChannels(target);
		ArrayList<UsedChannel> usedChannelslist = ConvertHandler.convertListV(plugin.getMysqlHandler().getFullList(MysqlType.USEDCHANNEL,
				"`id` ASC", "`player_uuid` = ?", target.getUniqueId().toString()));
		
		Utility.playerUsedChannels.remove(target.getUniqueId().toString());
		LinkedHashMap<String, UsedChannel> usedChannels = new LinkedHashMap<>();
		for(UsedChannel uc : usedChannelslist)
		{
			usedChannels.put(uc.getUniqueIdentifierName(), uc);
		}
		
		Utility.playerUsedChannels.put(target.getUniqueId().toString(),	usedChannels);
		
		players.sendMessage(ChatApiOld.tctl(
				plugin.getYamlHandler().getLang().getString("CmdScc.UpdatePlayer.IsUpdated")
				.replace("%player%", playername)));
		target.sendMessage(ChatApiOld.tctl(
				plugin.getYamlHandler().getLang().getString("CmdScc.UpdatePlayer.YouWasUpdated")
				.replace("%player%", target.getName())));
	}
}