package main.java.me.avankziar.simplechatchannels.spigot.commands.scc.pc;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.scc.objects.ChatApi;
import main.java.me.avankziar.scc.objects.ChatUser;
import main.java.me.avankziar.scc.objects.PermanentChannel;
import main.java.me.avankziar.simplechatchannels.spigot.SimpleChatChannels;
import main.java.me.avankziar.simplechatchannels.spigot.assistance.Utility;
import main.java.me.avankziar.simplechatchannels.spigot.commands.tree.ArgumentConstructor;
import main.java.me.avankziar.simplechatchannels.spigot.commands.tree.ArgumentModule;
import main.java.me.avankziar.simplechatchannels.spigot.database.MysqlHandler;

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
		Player player = (Player) sender;
		String channel = args[2];
		String newcreator = args[3];
		PermanentChannel cc = PermanentChannel.getChannelFromName(channel);
		ChatUser cuoffline = (ChatUser) plugin.getMysqlHandler().getData(MysqlHandler.Type.CHATUSER, 
				"`player_name` = ?", newcreator);
		if(cuoffline == null)
		{
			player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("PlayerNotExist")));
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
		player.spigot().sendMessage(ChatApi.tctl(msg));
		if(plugin.getServer().getPlayer(oldcreator) != null)
		{
			Player target = plugin.getServer().getPlayer(oldcreator);
			target.spigot().sendMessage(ChatApi.tctl(msg));
		}
		if(plugin.getServer().getPlayer(newcreator) != null)
		{
			Player target = plugin.getServer().getPlayer(newcreator);
			target.spigot().sendMessage(ChatApi.tctl(msg));
		}
		for(Player member : plugin.getServer().getOnlinePlayers())
		{
			if(cc.getMembers().contains(member.getUniqueId().toString()))
			{
				member.spigot().sendMessage(ChatApi.tctl(msg));
			}
		}
		return;
	}
}
