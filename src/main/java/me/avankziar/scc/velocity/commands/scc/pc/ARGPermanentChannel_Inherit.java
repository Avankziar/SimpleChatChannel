package main.java.me.avankziar.scc.velocity.commands.scc.pc;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;

import main.java.me.avankziar.scc.general.assistance.ChatApi;
import main.java.me.avankziar.scc.general.commands.tree.ArgumentConstructor;
import main.java.me.avankziar.scc.general.database.MysqlType;
import main.java.me.avankziar.scc.general.objects.ChatUser;
import main.java.me.avankziar.scc.general.objects.PermanentChannel;
import main.java.me.avankziar.scc.velocity.SCC;
import main.java.me.avankziar.scc.velocity.assistance.Utility;
import main.java.me.avankziar.scc.velocity.commands.tree.ArgumentModule;

public class ARGPermanentChannel_Inherit extends ArgumentModule
{
	private SCC plugin;
	
	public ARGPermanentChannel_Inherit(SCC plugin, ArgumentConstructor argumentConstructor)
	{
		super(argumentConstructor);
		this.plugin = plugin;
	}

	@Override
	public void run(CommandSource sender, String[] args)
	{
		Player player = (Player) sender;
		String channel = args[2];
		String newcreator = args[3];
		PermanentChannel cc = PermanentChannel.getChannelFromName(channel);
		ChatUser cuoffline = (ChatUser) plugin.getMysqlHandler().getData(MysqlType.CHATUSER, 
				"`player_name` = ?", newcreator);
		if(cuoffline == null)
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("PlayerNotExist")));
			return;
		}
		String uuid =  cuoffline.getUUID();
		final String oldcreatoruuid = cc.getCreator();
		final String oldcreator = Utility.convertUUIDToName(oldcreatoruuid);
		cc.removeMembers(oldcreatoruuid);
		cc.setCreator(uuid);
		cc.addMembers(uuid);
		plugin.getUtility().updatePermanentChannels(cc);
		String msg = plugin.getYamlHandler().getLang().getString("CmdScc.PermanentChannel.Inherit.NewCreator")
				.replace("%channel%", cc.getNameColor()+cc.getName())
				.replace("%creator%", newcreator)
				.replace("%oldcreator%", oldcreator);
		player.sendMessage(ChatApi.tl(msg));
		if(plugin.getServer().getPlayer(oldcreator).isEmpty())
		{
			Player target = plugin.getServer().getPlayer(oldcreator).get();
			target.sendMessage(ChatApi.tl(msg));
		}
		if(plugin.getServer().getPlayer(newcreator).isEmpty())
		{
			Player target = plugin.getServer().getPlayer(newcreator).get();
			target.sendMessage(ChatApi.tl(msg));
		}
		for(Player member : plugin.getServer().getAllPlayers())
		{
			if(cc.getMembers().contains(member.getUniqueId().toString()))
			{
				member.sendMessage(ChatApi.tl(msg));
			}
		}
		return;
	}
}
