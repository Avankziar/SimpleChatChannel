package main.java.me.avankziar.scc.bungee.commands.scc.pc;

import main.java.me.avankziar.scc.bungee.SimpleChatChannels;
import main.java.me.avankziar.scc.bungee.assistance.Utility;
import main.java.me.avankziar.scc.bungee.commands.tree.ArgumentConstructor;
import main.java.me.avankziar.scc.bungee.commands.tree.ArgumentModule;
import main.java.me.avankziar.scc.bungee.database.MysqlHandler;
import main.java.me.avankziar.scc.objects.ChatApi;
import main.java.me.avankziar.scc.objects.ChatUser;
import main.java.me.avankziar.scc.objects.PermanentChannel;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class ARGPermanentChannel_Inherit extends ArgumentModule
{
	private SimpleChatChannels plugin;
	
	public ARGPermanentChannel_Inherit(SimpleChatChannels plugin, ArgumentConstructor argumentConstructor)
	{
		super(argumentConstructor);
		this.plugin = plugin;
	}

	@Override
	public void run(CommandSender sender, String[] args)
	{
		ProxiedPlayer player = (ProxiedPlayer) sender;
		String channel = args[2];
		String newcreator = args[3];
		PermanentChannel cc = PermanentChannel.getChannelFromName(channel);
		ChatUser cuoffline = (ChatUser) plugin.getMysqlHandler().getData(MysqlHandler.Type.CHATUSER, 
				"`player_name` = ?", newcreator);
		if(cuoffline == null)
		{
			player.sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("PlayerNotExist")));
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
		player.sendMessage(ChatApi.tctl(msg));
		if(ProxyServer.getInstance().getPlayer(oldcreator) != null)
		{
			ProxiedPlayer target = ProxyServer.getInstance().getPlayer(oldcreator);
			target.sendMessage(ChatApi.tctl(msg));
		}
		if(ProxyServer.getInstance().getPlayer(newcreator) != null)
		{
			ProxiedPlayer target = ProxyServer.getInstance().getPlayer(newcreator);
			target.sendMessage(ChatApi.tctl(msg));
		}
		for(ProxiedPlayer member : ProxyServer.getInstance().getPlayers())
		{
			if(cc.getMembers().contains(member.getUniqueId().toString()))
			{
				member.sendMessage(ChatApi.tctl(msg));
			}
		}
		return;
	}
}
