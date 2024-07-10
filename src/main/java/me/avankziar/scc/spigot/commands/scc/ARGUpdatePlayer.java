package main.java.me.avankziar.scc.spigot.commands.scc;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.scc.general.assistance.ChatApi;
import main.java.me.avankziar.scc.general.commands.tree.ArgumentConstructor;
import main.java.me.avankziar.scc.general.database.MysqlType;
import main.java.me.avankziar.scc.general.handlers.ConvertHandler;
import main.java.me.avankziar.scc.general.objects.ChatUser;
import main.java.me.avankziar.scc.general.objects.UsedChannel;
import main.java.me.avankziar.scc.spigot.SCC;
import main.java.me.avankziar.scc.spigot.assistance.Utility;
import main.java.me.avankziar.scc.spigot.commands.tree.ArgumentModule;
import main.java.me.avankziar.scc.spigot.objects.ChatUserHandler;

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
		Player player = (Player) sender;
		String playername = args[1];
		Player target = plugin.getServer().getPlayer(playername);
		ChatUser cuo = ChatUserHandler.getChatUser(args[1]);
		if(cuo == null || target == null)
		{
			player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("NoPlayerExist")));
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
		
		player.spigot().sendMessage(ChatApi.tctl(
				plugin.getYamlHandler().getLang().getString("CmdScc.UpdatePlayer.IsUpdated")
				.replace("%player%", playername)));
		target.spigot().sendMessage(ChatApi.tctl(
				plugin.getYamlHandler().getLang().getString("CmdScc.UpdatePlayer.YouWasUpdated")
				.replace("%player%", target.getName())));
	}
}