package main.java.me.avankziar.simplechatchannels.spigot.commands.simplechatchannels;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.simplechatchannels.objects.ChatApi;
import main.java.me.avankziar.simplechatchannels.objects.ChatUser;
import main.java.me.avankziar.simplechatchannels.objects.PermanentChannel;
import main.java.me.avankziar.simplechatchannels.spigot.SimpleChatChannels;
import main.java.me.avankziar.simplechatchannels.spigot.assistance.Utility;
import main.java.me.avankziar.simplechatchannels.spigot.commands.tree.ArgumentConstructor;
import main.java.me.avankziar.simplechatchannels.spigot.commands.tree.ArgumentModule;
import main.java.me.avankziar.simplechatchannels.spigot.database.MysqlHandler;

public class ARGPermanentChannelInherit extends ArgumentModule
{
	private SimpleChatChannels plugin;
	
	public ARGPermanentChannelInherit(SimpleChatChannels plugin, ArgumentConstructor argumentConstructor)
	{
		super(plugin, argumentConstructor);
		this.plugin = plugin;
	}

	@Override
	public void run(CommandSender sender, String[] args)
	{
		Player player = (Player) sender;
		String scc = "CmdScc.";
		PermanentChannel cc = PermanentChannel.getChannelFromName(args[1]);
		ChatUser cuoffline = (ChatUser) plugin.getMysqlHandler().getData(MysqlHandler.Type.CHATUSER, 
				"`player_name` = ?", args[2]);
		if(cuoffline == null)
		{
			///Der Spieler ist nicht online oder existiert nicht!
			player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString("PlayerNotExist")));
			return;
		}
		String uuid = cuoffline.getUUID();
		final String oldcreatoruuid = cc.getCreator();
		final String oldcreator = Utility.convertUUIDToName(oldcreatoruuid);
		cc.removeMembers(oldcreatoruuid);
		cc.setCreator(uuid);
		cc.addMembers(uuid);
		plugin.getUtility().updatePermanentChannels(cc);
		///Im &5perma&fnenten &eChannel %channel% &r&ebeerbt der Spieler &a%creator% &eden Spieler &c%oldcreator% &eals neuer Ersteller des Channels.
		player.spigot().sendMessage(ChatApi.tctl(
				plugin.getYamlHandler().getL().getString(scc+"PCInherit.NewCreator")
				.replace("%channel%", cc.getNameColor()+cc.getName())
				.replace("%creator%", args[2])
				.replace("%oldcreator%", oldcreator)));
		if(plugin.getServer().getPlayer(oldcreator) != null)
		{
			Player target = plugin.getServer().getPlayer(oldcreator);
			///Du wurdest vom CustomChannel %channel% gebannt!
			target.spigot().sendMessage(ChatApi.tctl(
					plugin.getYamlHandler().getL().getString(scc+"PCInherit.NewCreator")
					.replace("%channel%", cc.getNameColor()+cc.getName())
					.replace("%creator%", args[2])
					.replace("%oldcreator%", oldcreator)));
		}
		if(plugin.getServer().getPlayer(args[2]) != null)
		{
			Player target = plugin.getServer().getPlayer(args[2]);
			///Du wurdest vom CustomChannel %channel% gebannt!
			target.spigot().sendMessage(ChatApi.tctl(
					plugin.getYamlHandler().getL().getString(scc+"PCInherit.NewCreator")
					.replace("%channel%", cc.getNameColor()+cc.getName())
					.replace("%creator%", args[2])
					.replace("%oldcreator%", oldcreator)));
		}
		for(Player member : plugin.getServer().getOnlinePlayers())
		{
			if(cc.getMembers().contains(member.getUniqueId().toString()))
			{
				member.spigot().sendMessage(ChatApi.tctl(
						plugin.getYamlHandler().getL().getString(scc+"PCInherit.NewCreator")
						.replace("%channel%", cc.getNameColor()+cc.getName())
						.replace("%creator%", args[2])
						.replace("%oldcreator%", oldcreator)));
			}
		}
		return;
	}
}
