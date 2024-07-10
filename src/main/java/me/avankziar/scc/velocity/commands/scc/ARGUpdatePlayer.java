package main.java.me.avankziar.scc.velocity.commands.scc;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Optional;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;

import main.java.me.avankziar.scc.general.assistance.ChatApi;
import main.java.me.avankziar.scc.general.commands.tree.ArgumentConstructor;
import main.java.me.avankziar.scc.general.database.MysqlType;
import main.java.me.avankziar.scc.general.handlers.ConvertHandler;
import main.java.me.avankziar.scc.general.objects.ChatUser;
import main.java.me.avankziar.scc.general.objects.UsedChannel;
import main.java.me.avankziar.scc.velocity.SCC;
import main.java.me.avankziar.scc.velocity.assistance.Utility;
import main.java.me.avankziar.scc.velocity.commands.tree.ArgumentModule;
import main.java.me.avankziar.scc.velocity.objects.ChatUserHandler;

public class ARGUpdatePlayer extends ArgumentModule
{
	private SCC plugin;
	
	public ARGUpdatePlayer(SCC plugin, ArgumentConstructor argumentConstructor)
	{
		super(argumentConstructor);
		this.plugin = plugin;
	}

	@Override
	public void run(CommandSource sender, String[] args)
	{
		Player players = (Player) sender;
		String playername = args[1];
		Optional<Player> target = plugin.getServer().getPlayer(playername);
		ChatUser cuo = ChatUserHandler.getChatUser(args[1]);
		if(cuo == null || target.isEmpty())
		{
			players.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("NoPlayerExist")));
			return;
		}
		plugin.getUtility().controlUsedChannels(target.get());
		ArrayList<UsedChannel> usedChannelslist = ConvertHandler.convertListV(plugin.getMysqlHandler().getFullList(MysqlType.USEDCHANNEL,
				"`id` ASC", "`player_uuid` = ?", target.get().getUniqueId().toString()));
		
		Utility.playerUsedChannels.remove(target.get().getUniqueId().toString());
		LinkedHashMap<String, UsedChannel> usedChannels = new LinkedHashMap<>();
		for(UsedChannel uc : usedChannelslist)
		{
			usedChannels.put(uc.getUniqueIdentifierName(), uc);
		}
		
		Utility.playerUsedChannels.put(target.get().getUniqueId().toString(),	usedChannels);
		
		players.sendMessage(ChatApi.tl(
				plugin.getYamlHandler().getLang().getString("CmdScc.UpdatePlayer.IsUpdated")
				.replace("%player%", playername)));
		target.get().sendMessage(ChatApi.tl(
				plugin.getYamlHandler().getLang().getString("CmdScc.UpdatePlayer.YouWasUpdated")
				.replace("%player%", target.get().getUsername())));
	}
}