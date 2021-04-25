package main.java.me.avankziar.simplechatchannels.spigot.commands.simplechatchannels;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.simplechatchannels.objects.ChatApi;
import main.java.me.avankziar.simplechatchannels.objects.PermanentChannel;
import main.java.me.avankziar.simplechatchannels.spigot.SimpleChatChannels;
import main.java.me.avankziar.simplechatchannels.spigot.assistance.Utility;
import main.java.me.avankziar.simplechatchannels.spigot.commands.tree.ArgumentConstructor;
import main.java.me.avankziar.simplechatchannels.spigot.commands.tree.ArgumentModule;

public class ARGPermanentChannelUnban extends ArgumentModule
{
	private SimpleChatChannels plugin;
	
	public ARGPermanentChannelUnban(SimpleChatChannels plugin, ArgumentConstructor argumentConstructor)
	{
		super(plugin, argumentConstructor);
		this.plugin = plugin;
	}

	@Override
	public void run(CommandSender sender, String[] args)
	{
		Player player = (Player) sender;
		String language = "CmdScc.";
		PermanentChannel cc = PermanentChannel.getChannelFromName(args[1]);
		if(cc==null)
		{
			///Der angegebene Channel &5perma&fnenten %channel% existiert nicht!
			player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString("CmdScc.PermanentChannel.YouAreNotInAChannel")
					.replace("%channel%", args[1])));
			return;
		}
		if(!cc.getCreator().equals(player.getUniqueId().toString())
				&& !cc.getVice().contains(player.getUniqueId().toString()))
		{
			///Du bist weder der Ersteller noch ein Stellvertreter im &5perma&fnenten &cChannel &f%channel%&c!
			player.spigot().sendMessage(ChatApi.tctl("CmdScc.PermanentChannel.YouAreNotTheOwnerOrVice"));
			return;
		}
		String targetuuid = Utility.convertNameToUUID(args[2]).toString();
		if(targetuuid == null)
		{
			///Der Spieler ist nicht online oder existiert nicht!
			player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString("PlayerNotExist")));
			return;
		}
		
		if(!cc.getBanned().contains(targetuuid))
		{
			///Der Spieler ist nicht auf der Bannliste!
			player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString(language+"PCUnban.PlayerNotBanned")));
			return;
		}
		cc.removeBanned(targetuuid);
		plugin.getUtility().updatePermanentChannels(cc);
		///Du hast &f%player% &efür den CustomChannel entbannt!
		player.spigot().sendMessage(ChatApi.tctl(
				plugin.getYamlHandler().getL().getString(language+"PCUnban.YouUnbanPlayer")
				.replace("%player%", args[2])));
		if(plugin.getServer().getPlayer(args[2]) != null)
		{
			Player target = plugin.getServer().getPlayer(args[2]);
			///Du wurdest vom CustomChannel %channel% gebannt!
			target.spigot().sendMessage(ChatApi.tctl(
					plugin.getYamlHandler().getL().getString(language+"PCUnban.CreatorUnbanPlayer")
					.replace("%player%", args[2])
					.replace("%channel%", cc.getNameColor()+cc.getName())));
		}
		for(Player members : plugin.getServer().getOnlinePlayers())
		{
			if(cc.getMembers().contains(members.getUniqueId().toString()))
			{
				///Der Spieler &f%player% &ewurde für den CustomChannel entbannt.
				members.spigot().sendMessage(ChatApi.tctl(
						plugin.getYamlHandler().getL().getString(language+"PCUnban.CreatorUnbanPlayer")
						.replace("%player%", args[2])
						.replace("%channel%", cc.getNameColor()+cc.getName())));
			}
		}
		return;
	}
}