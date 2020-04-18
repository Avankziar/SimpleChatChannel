package main.java.me.avankziar.simplechatchannels.spigot.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

import main.java.me.avankziar.simplechatchannels.spigot.SimpleChatChannels;
import main.java.me.avankziar.simplechatchannels.spigot.interfaces.TemporaryChannel;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class CommandHelper
{
	private SimpleChatChannels plugin;
	private String scc = ".CmdScc.";
	
	public CommandHelper(SimpleChatChannels plugin)
	{
		this.plugin = plugin;
	}
	
	public void scc(Player player)
	{
		String language = plugin.getUtility().getLanguage();
		if(player.hasPermission("scc.option.admin"))
		{
			player.spigot().sendMessage(plugin.getUtility().tctlYaml(language+scc+"Info.msg10"));
			player.spigot().sendMessage(plugin.getUtility().clickEvent(language+scc+"Info.msg01",
					ClickEvent.Action.SUGGEST_COMMAND, "/scc <channel>", true));
			player.spigot().sendMessage(plugin.getUtility().clickEvent(language+scc+"Info.msg02",
					ClickEvent.Action.SUGGEST_COMMAND,  "/scc mute <Player>", true));
			player.spigot().sendMessage(plugin.getUtility().clickEvent(language+scc+"Info.msg03",
					ClickEvent.Action.SUGGEST_COMMAND,  "/scc unmute <Player>", true));
			player.spigot().sendMessage(plugin.getUtility().clickEvent(language+scc+"Info.msg04",
					ClickEvent.Action.SUGGEST_COMMAND,  "/scc ignore <Player>", true));
			player.spigot().sendMessage(plugin.getUtility().clickEvent(language+scc+"Info.msg05",
					ClickEvent.Action.SUGGEST_COMMAND,  "/scc ignorelist", true));
			player.spigot().sendMessage(plugin.getUtility().clickEvent(language+scc+"Info.msg06",
					ClickEvent.Action.SUGGEST_COMMAND,  "/scc wordfilter <add/remove> <Word>", true));
			player.spigot().sendMessage(plugin.getUtility().clickEvent(language+scc+"Info.msg07",
					ClickEvent.Action.SUGGEST_COMMAND,  "/scc broadcast <Message>", true));
			player.spigot().sendMessage(plugin.getUtility().clickEvent(language+scc+"Info.msg08",
					ClickEvent.Action.SUGGEST_COMMAND,  "/scc playerlist [String]", true));
			player.spigot().sendMessage(plugin.getUtility().clickEvent(language+scc+"Info.msg09",
					ClickEvent.Action.SUGGEST_COMMAND,  "/scc bungee", true));
			player.spigot().sendMessage(plugin.getUtility().clickEvent(language+scc+"Info.msg11",
					ClickEvent.Action.SUGGEST_COMMAND,  "/scc grouplist [String]", true));
			player.spigot().sendMessage(plugin.getUtility().clickEvent(language+scc+"Info.msg12",
					ClickEvent.Action.SUGGEST_COMMAND,  "/scc cccreate <Name> [Password]", true));
			player.spigot().sendMessage(plugin.getUtility().clickEvent(language+scc+"Info.msg13",
					ClickEvent.Action.SUGGEST_COMMAND,  "/scc ccjoin <Name>", true));
			player.spigot().sendMessage(plugin.getUtility().clickEvent(language+scc+"Info.msg14",
					ClickEvent.Action.SUGGEST_COMMAND,  "/scc ccleave", true));
			player.spigot().sendMessage(plugin.getUtility().clickEvent(language+scc+"Info.msg15",
					ClickEvent.Action.SUGGEST_COMMAND,  "/scc cckick <Player>", true));
			player.spigot().sendMessage(plugin.getUtility().clickEvent(language+scc+"Info.msg16",
					ClickEvent.Action.SUGGEST_COMMAND,  "/scc ccban <Player>", true));
			player.spigot().sendMessage(plugin.getUtility().clickEvent(language+scc+"Info.msg17",
					ClickEvent.Action.SUGGEST_COMMAND,  "/scc ccunban <Player>", true));
			player.spigot().sendMessage(plugin.getUtility().clickEvent(language+scc+"Info.msg18",
					ClickEvent.Action.SUGGEST_COMMAND,  "/scc changepassword <Password>", true));
			player.spigot().sendMessage(plugin.getUtility().clickEvent(language+scc+"Info.msg19",
					ClickEvent.Action.SUGGEST_COMMAND,  "/scc channelinfo", true));
		} else
		{
			player.spigot().sendMessage(plugin.getUtility().tctlYaml(language+scc+"Info.msg10"));
			player.spigot().sendMessage(plugin.getUtility().clickEvent(language+scc+"Info.msg01",
					ClickEvent.Action.SUGGEST_COMMAND,  "/scc <channel>", true));
			player.spigot().sendMessage(plugin.getUtility().clickEvent(language+scc+"Info.msg04",
					ClickEvent.Action.SUGGEST_COMMAND,  "/scc ignore <Player>", true));
			player.spigot().sendMessage(plugin.getUtility().clickEvent(language+scc+"Info.msg05",
					ClickEvent.Action.SUGGEST_COMMAND,  "/scc ignorelist", true));
			player.spigot().sendMessage(plugin.getUtility().clickEvent(language+scc+"Info.msg08",
					ClickEvent.Action.SUGGEST_COMMAND,  "/scc playerlist [String]", true));
			player.spigot().sendMessage(plugin.getUtility().clickEvent(language+scc+"Info.msg11",
					ClickEvent.Action.SUGGEST_COMMAND,  "/scc grouplist [String]", true));
			player.spigot().sendMessage(plugin.getUtility().clickEvent(language+scc+"Info.msg12",
					ClickEvent.Action.SUGGEST_COMMAND,  "/scc cccreate <Name> [Password]", true));
			player.spigot().sendMessage(plugin.getUtility().clickEvent(language+scc+"Info.msg13",
					ClickEvent.Action.SUGGEST_COMMAND,  "/scc ccjoin <Name>", true));
			player.spigot().sendMessage(plugin.getUtility().clickEvent(language+scc+"Info.msg14",
					ClickEvent.Action.SUGGEST_COMMAND,  "/scc ccleave", true));
			player.spigot().sendMessage(plugin.getUtility().clickEvent(language+scc+"Info.msg15",
					ClickEvent.Action.SUGGEST_COMMAND,  "/scc cckick <Player>", true));
			player.spigot().sendMessage(plugin.getUtility().clickEvent(language+scc+"Info.msg16",
					ClickEvent.Action.SUGGEST_COMMAND,  "/scc ccban <Player>", true));
			player.spigot().sendMessage(plugin.getUtility().clickEvent(language+scc+"Info.msg17",
					ClickEvent.Action.SUGGEST_COMMAND,  "/scc ccunban <Player>", true));
			player.spigot().sendMessage(plugin.getUtility().clickEvent(language+scc+"Info.msg18",
					ClickEvent.Action.SUGGEST_COMMAND,  "/scc changepassword <Password>", true));
			player.spigot().sendMessage(plugin.getUtility().clickEvent(language+scc+"Info.msg19",
					ClickEvent.Action.SUGGEST_COMMAND,  "/scc channelinfo", true));
		}
	}
	
	public void playergrouplist(Player player, List<BaseComponent> list, String path)
	{
		TextComponent MSG = plugin.getUtility().tc("");
		if(list.isEmpty())
		{
			player.spigot().sendMessage(plugin.getUtility().tctlYaml(plugin.getUtility().getLanguage()+scc+path+".WrongInput"));
			return;
		}
		MSG.setExtra(list);
		player.spigot().sendMessage(plugin.getUtility().tctlYaml(plugin.getUtility().getLanguage()+scc+path+".Headline"));
		player.spigot().sendMessage(MSG);
	}
	
	public void channeltoggle(Player player, String channel, String replacer)
	{
		if((boolean) plugin.getMysqlHandler().getDataI(player.getUniqueId().toString(), "channel_"+channel, "player_uuid"))
		{
			///Du hast den Channel %channel% &causgeblendet!
			plugin.getMysqlHandler().updateDataI(player, false, "channel_"+channel);
			player.spigot().sendMessage(plugin.getUtility().tctl(
					plugin.getYamlHandler().getL().getString(plugin.getUtility().getLanguage()+scc+"ChannelToggle.ChannelOff")
					.replace("%channel%", replacer)));
		} else
		{
			///Du hast den Channel %channel% &aeingeblendet!
			plugin.getMysqlHandler().updateDataI(player, true, "channel_"+channel);
			player.spigot().sendMessage(plugin.getUtility().tctl(
					plugin.getYamlHandler().getL().getString(plugin.getUtility().getLanguage()+scc+"ChannelToggle.ChannelOn")
					.replace("%channel%", replacer)));
		}
		return;
	}
	
	public void optiontoggle(Player player, String optionperm,
			String option, String replacer)
	{
		if(((boolean) plugin.getMysqlHandler().getDataI(player.getUniqueId().toString(), option, "player_uuid")))
		{
			plugin.getMysqlHandler().updateDataI(player, false, option);
			///Du hast den Channel %channel% &causgeblendet!
			player.spigot().sendMessage(plugin.getUtility().tctl(
					plugin.getYamlHandler().getL().getString(plugin.getUtility().getLanguage()+scc+"ChannelToggle.ChannelOff")
					.replace("%channel%", replacer)));
		} else
		{
			plugin.getMysqlHandler().updateDataI(player, true, option);
			///Du hast den Channel %channel% &aeingeblendet!
			player.spigot().sendMessage(plugin.getUtility().tctl(
					plugin.getYamlHandler().getL().getString(plugin.getUtility().getLanguage()+scc+"ChannelToggle.ChannelOn")
					.replace("%channel%", replacer)));
		}
	}
	
	//Obsolete
	public void cccreate(Player player, String language, TemporaryChannel cc, String name, String password)
	{
		ArrayList<Player> members = new ArrayList<Player>();
		members.add(player);
		cc = new TemporaryChannel(name, player, members, password, new ArrayList<Player>());
		TemporaryChannel.addCustomChannel(cc);
		if(password==null)
		{
			player.spigot().sendMessage(plugin.getUtility().tc(plugin.getUtility().tl(
					plugin.getYamlHandler().getL().getString(language+scc+"ChannelCreate.msg02")
					.replace("%channel%", cc.getName()))));
		} else
		{
			player.spigot().sendMessage(plugin.getUtility().tc(plugin.getUtility().tl(
					plugin.getYamlHandler().getL().getString(language+scc+"ChannelCreate.msg03")
					.replace("%channel%", cc.getName())
					.replace("%password%", password))));
		}
	}
	
	//Obsolete
	public void ccjoin(Player player, String language, String name, String password)
	{
		TemporaryChannel cc = TemporaryChannel.getCustomChannel(name);
		TemporaryChannel oldcc = TemporaryChannel.getCustomChannel(player);
		if(oldcc!=null)
		{
			player.spigot().sendMessage(plugin.getUtility().tctl(
					plugin.getYamlHandler().getL().getString(language+scc+"ChannelJoin.msg06")));
			return;
		}
		if(cc==null)
		{
			player.spigot().sendMessage(plugin.getUtility().tctl(
					plugin.getYamlHandler().getL().getString(language+scc+"ChannelJoin.msg01")
					.replace("%name%", name)));
			return;
		}
		if(cc.getBanned().contains(player))
		{
			player.spigot().sendMessage(plugin.getUtility().tctl(
					plugin.getYamlHandler().getL().getString(language+scc+"ChannelJoin.msg07")));
			return;
		}
		if(password==null)
		{
			if(cc.getPassword()!=null)
			{
				player.spigot().sendMessage(plugin.getUtility().tctl(
						plugin.getYamlHandler().getL().getString(language+scc+"ChannelJoin.msg02")));
				return;
			}
		} else
		{
			if(cc.getPassword()==null)
			{
				player.spigot().sendMessage(plugin.getUtility().tctl(
						plugin.getYamlHandler().getL().getString(language+scc+"ChannelJoin.msg04")));
				return;
			}
			if(!cc.getPassword().equals(password))
			{
				player.spigot().sendMessage(plugin.getUtility().tctl(
						plugin.getYamlHandler().getL().getString(language+scc+"ChannelJoin.msg03")));
				return;
			}
		}
		cc.addMembers(player);
		player.spigot().sendMessage(plugin.getUtility().tctl(
				plugin.getYamlHandler().getL().getString(language+scc+"ChannelJoin.msg05")
				.replace("%channel%", cc.getName())));
		return;
	}
}