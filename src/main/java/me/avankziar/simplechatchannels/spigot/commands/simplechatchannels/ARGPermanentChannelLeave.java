package main.java.me.avankziar.simplechatchannels.spigot.commands.simplechatchannels;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.simplechatchannels.objects.ChatApi;
import main.java.me.avankziar.simplechatchannels.objects.PermanentChannel;
import main.java.me.avankziar.simplechatchannels.spigot.SimpleChatChannels;
import main.java.me.avankziar.simplechatchannels.spigot.commands.tree.ArgumentConstructor;
import main.java.me.avankziar.simplechatchannels.spigot.commands.tree.ArgumentModule;
import main.java.me.avankziar.simplechatchannels.spigot.database.MysqlHandler;
import net.md_5.bungee.api.chat.ClickEvent;

public class ARGPermanentChannelLeave extends ArgumentModule
{
	private SimpleChatChannels plugin;
	
	public ARGPermanentChannelLeave(SimpleChatChannels plugin, ArgumentConstructor argumentConstructor)
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
		if(args.length==2)
		{
			if(cc.getCreator().equals(player.getUniqueId().toString()))
			{
				player.spigot().sendMessage(ChatApi.clickEvent(
						plugin.getYamlHandler().getL().getString(language+"PCLeave.Confirm"),
						ClickEvent.Action.SUGGEST_COMMAND, "/scc pcleave "+args[1]+" confirm"));
				return;
			} else
			{
				cc.removeMembers(player.getUniqueId().toString());
				cc.removeVice(player.getUniqueId().toString());
				plugin.getUtility().updatePermanentChannels(cc);
				///Du hast den CustomChannel &f%channel% &everlassen!
				player.spigot().sendMessage(ChatApi.tctl(
						plugin.getYamlHandler().getL().getString(language+"PCLeave.YouLeft")
						.replace("%channel%", cc.getNameColor()+cc.getName())));
				for(Player members : plugin.getServer().getOnlinePlayers())
				{
					if(cc.getMembers().contains(members.getUniqueId().toString()))
					{
						///Der Spieler &f%player% &chat den &5perma&fnenten &cChannel &f%channel% &cverlassen!
						members.spigot().sendMessage(ChatApi.tctl(
								plugin.getYamlHandler().getL().getString(language+"PCLeave.PlayerLeft")
								.replace("%player%", player.getName()).replace("%channel%", cc.getNameColor()+cc.getName())));
					}
				}
			}
			return;
		} else if(args.length == 3)
		{
			if(args[2].equalsIgnoreCase("confirm") || args[2].equalsIgnoreCase("bestätigen"))
			{
				if(cc.getCreator().equals(player.getUniqueId().toString()))
				{
					for(Player members : plugin.getServer().getOnlinePlayers())
					{
						if(cc.getMembers().contains(members.getUniqueId().toString()))
						{
							members.spigot().sendMessage(ChatApi.tctl(
									plugin.getYamlHandler().getL().getString(language+"PCLeave.CreatorLeft")
									.replace("%channel%", cc.getNameColor()+cc.getName())));
						}
					}
					plugin.getMysqlHandler().deleteData(MysqlHandler.Type.PERMANENTCHANNEL, "`id` = ?", cc.getId());
					PermanentChannel.removeCustomChannel(cc);
					return;
				} else
				{
					cc.removeMembers(player.getUniqueId().toString());
					cc.removeVice(player.getUniqueId().toString());
					plugin.getUtility().updatePermanentChannels(cc);
					///Du hast den CustomChannel &f%channel% &everlassen!
					player.spigot().sendMessage(ChatApi.tctl(
							plugin.getYamlHandler().getL().getString(language+"PCLeave.YouLeft")
							.replace("%channel%", cc.getNameColor()+cc.getName())));
					for(Player members : plugin.getServer().getOnlinePlayers())
					{
						if(cc.getMembers().contains(members.getUniqueId().toString()))
						{
							///Der Spieler &f%player% &chat den &5perma&fnenten &cChannel &f%channel% &cverlassen!
							members.spigot().sendMessage(ChatApi.tctl(
									plugin.getYamlHandler().getL().getString(language+"PCLeave.PlayerLeft")
									.replace("%player%", player.getName()).replace("%channel%", cc.getNameColor()+cc.getName())));
						}
					}
					return;
				}
			} else
			{
				///Deine Eingabe ist fehlerhaft, klicke hier auf den Text um &cweitere Infos zu bekommen!
				player.spigot().sendMessage(ChatApi.clickEvent(
						plugin.getYamlHandler().getL().getString(language+".CmdScc.InputIsWrong"),
						ClickEvent.Action.RUN_COMMAND, "/scc"));
				return;
			}
		}
	}
}
