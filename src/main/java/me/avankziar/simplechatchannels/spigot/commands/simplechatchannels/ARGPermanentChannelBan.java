package main.java.me.avankziar.simplechatchannels.spigot.commands.simplechatchannels;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.simplechatchannels.objects.ChatApi;
import main.java.me.avankziar.simplechatchannels.objects.ChatUser;
import main.java.me.avankziar.simplechatchannels.objects.PermanentChannel;
import main.java.me.avankziar.simplechatchannels.spigot.SimpleChatChannels;
import main.java.me.avankziar.simplechatchannels.spigot.commands.tree.ArgumentConstructor;
import main.java.me.avankziar.simplechatchannels.spigot.commands.tree.ArgumentModule;
import main.java.me.avankziar.simplechatchannels.spigot.objects.ChatUserHandler;

public class ARGPermanentChannelBan extends ArgumentModule
{
	private SimpleChatChannels plugin;
	
	public ARGPermanentChannelBan(SimpleChatChannels plugin, ArgumentConstructor argumentConstructor)
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
			///Du bist in keinem permanenten Channel!
			player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString(language+"ChannelGeneral.ChannelNotExistII")
					.replace("%channel%", args[1])));
			return;
		}
		if(!cc.getCreator().equals(player.getUniqueId().toString()) && !cc.getVice().contains(player.getUniqueId().toString()))
		{
			///Du bist weder der Ersteller noch der Stellvertreter im permanenten Channel %channel%!
			player.spigot().sendMessage(ChatApi.tctl(
					plugin.getYamlHandler().getL().getString(language+"ChannelGeneral.NotChannelViceII")
					.replace("%channel%", cc.getName())));
			return;
		}
		ChatUser cut = ChatUserHandler.getChatUser(args[2]);
		if(cut == null)
		{
			///Der Spieler ist nicht online oder existiert nicht!
			player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString(language+"NoPlayerExist")));
			return;
		}
		String targetuuid = cut.getUUID();
		if(cc.getCreator().equals(targetuuid) && cc.getVice().contains(player.getUniqueId().toString()))
		{
			///Du als Stellvertreter kannst den Ersteller nicht ban!
			player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString(language+"PCBan.ViceCannotBanCreator")));
			return;
		}
		if(targetuuid.equals(player.getUniqueId().toString()))
		{
			///Du als Ersteller kannst dich nicht selber bannen!
			player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString(language+"PCBan.YourselfCannotBan")));
			return;
		}
		if(cc.getBanned().contains(targetuuid))
		{
			//Der Spieler ist schon auf der Bannliste!
			player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString(language+"PCBan.AlreadyBanned")));
			return;
		}
		cc.addBanned(targetuuid);
		cc.removeVice(targetuuid);
		cc.removeMembers(targetuuid);
		plugin.getUtility().updatePermanentChannels(cc);
		///Du hast den Spieler &f%player% &eaus dem CustomChannel gebannt.
		player.spigot().sendMessage(ChatApi.tctl(
				plugin.getYamlHandler().getL().getString(language+"PCBan.YouHasBanned")
				.replace("%player%", args[2])));
		if(plugin.getServer().getPlayer(args[2]) != null)
		{
			Player target = plugin.getServer().getPlayer(args[2]);
			///Du wurdest vom CustomChannel %channel% gebannt!
			target.spigot().sendMessage(ChatApi.tctl(
					plugin.getYamlHandler().getL().getString(language+"PCBan.YourWereBanned")
					.replace("%channel%", cc.getNameColor()+cc.getName())));
		}
		for(Player members : plugin.getServer().getOnlinePlayers())
		{
			if(cc.getMembers().contains(members.getUniqueId().toString()))
			{
				///Der Spieler &f%player% &ewurde aus dem &5perma&fnenten &eChannel verbannt.
				members.spigot().sendMessage(ChatApi.tctl(
						plugin.getYamlHandler().getL().getString(language+"PCBan.CreatorHasBanned")
						.replace("%player%", args[2])));
			}
		}
		return;
	}
}