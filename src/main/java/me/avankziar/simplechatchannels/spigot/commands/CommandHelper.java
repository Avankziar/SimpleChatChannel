package main.java.me.avankziar.simplechatchannels.spigot.commands;

import java.util.List;

import org.bukkit.entity.Player;

import main.java.me.avankziar.simplechatchannels.spigot.SimpleChatChannels;
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
	
	public void scc(Player player) //CCinvite hinzufügen
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
					ClickEvent.Action.SUGGEST_COMMAND,  "/scc tccreate <Name> [Password]", true));
			player.spigot().sendMessage(plugin.getUtility().clickEvent(language+scc+"Info.msg13",
					ClickEvent.Action.SUGGEST_COMMAND,  "/scc tcjoin <Name>", true));
			player.spigot().sendMessage(plugin.getUtility().clickEvent(language+scc+"Info.msg14",
					ClickEvent.Action.SUGGEST_COMMAND,  "/scc tcleave", true));
			player.spigot().sendMessage(plugin.getUtility().clickEvent(language+scc+"Info.msg15",
					ClickEvent.Action.SUGGEST_COMMAND,  "/scc tckick <Player>", true));
			player.spigot().sendMessage(plugin.getUtility().clickEvent(language+scc+"Info.msg16",
					ClickEvent.Action.SUGGEST_COMMAND,  "/scc tcban <Player>", true));
			player.spigot().sendMessage(plugin.getUtility().clickEvent(language+scc+"Info.msg17",
					ClickEvent.Action.SUGGEST_COMMAND,  "/scc tcunban <Player>", true));
			player.spigot().sendMessage(plugin.getUtility().clickEvent(language+scc+"Info.msg18",
					ClickEvent.Action.SUGGEST_COMMAND,  "/scc tcchangepassword <Password>", true));
			player.spigot().sendMessage(plugin.getUtility().clickEvent(language+scc+"Info.msg19",
					ClickEvent.Action.SUGGEST_COMMAND,  "/scc tcinfo", true));
			
			player.spigot().sendMessage(plugin.getUtility().clickEvent(language+scc+"Info.msg20",
					ClickEvent.Action.SUGGEST_COMMAND,  "/scc tcinvite <Player>", true));
			player.spigot().sendMessage(plugin.getUtility().clickEvent(language+scc+"Info.msg21",
					ClickEvent.Action.SUGGEST_COMMAND,  "/scc pccreate <Name> [Password]", true));
			player.spigot().sendMessage(plugin.getUtility().clickEvent(language+scc+"Info.msg22",
					ClickEvent.Action.SUGGEST_COMMAND,  "/scc pcjoin <Channelname> <Name>", true));
			player.spigot().sendMessage(plugin.getUtility().clickEvent(language+scc+"Info.msg23",
					ClickEvent.Action.SUGGEST_COMMAND,  "/scc pcleave <Channelname> confirm", true));
			player.spigot().sendMessage(plugin.getUtility().clickEvent(language+scc+"Info.msg24",
					ClickEvent.Action.SUGGEST_COMMAND,  "/scc pckick <Channelname> <Player>", true));
			player.spigot().sendMessage(plugin.getUtility().clickEvent(language+scc+"Info.msg25",
					ClickEvent.Action.SUGGEST_COMMAND,  "/scc pcban <Channelname> <Player>", true));
			player.spigot().sendMessage(plugin.getUtility().clickEvent(language+scc+"Info.msg26",
					ClickEvent.Action.SUGGEST_COMMAND,  "/scc pcunban <Channelname> <Player>", true));
			player.spigot().sendMessage(plugin.getUtility().clickEvent(language+scc+"Info.msg27",
					ClickEvent.Action.SUGGEST_COMMAND,  "/scc pcchangepassword <Channelname> <Password>", true));
			player.spigot().sendMessage(plugin.getUtility().clickEvent(language+scc+"Info.msg28",
					ClickEvent.Action.SUGGEST_COMMAND,  "/scc pcinfo [Channelname]", true));
			player.spigot().sendMessage(plugin.getUtility().clickEvent(language+scc+"Info.msg29",
					ClickEvent.Action.SUGGEST_COMMAND,  "/scc pcinvite <Channelname> <Player>", true));
			player.spigot().sendMessage(plugin.getUtility().clickEvent(language+scc+"Info.msg30",
					ClickEvent.Action.SUGGEST_COMMAND,  "/scc pcvice <Channelname> <Player>", true));
			
			player.spigot().sendMessage(plugin.getUtility().clickEvent(language+scc+"Info.msg31",
					ClickEvent.Action.SUGGEST_COMMAND,  "/scc pcdelete <Channelname>", true));
			player.spigot().sendMessage(plugin.getUtility().clickEvent(language+scc+"Info.msg32",
					ClickEvent.Action.SUGGEST_COMMAND,  "/scc pcinherit <Channelname> <Player>", true));
			player.spigot().sendMessage(plugin.getUtility().clickEvent(language+scc+"Info.msg33",
					ClickEvent.Action.SUGGEST_COMMAND,  "/scc pcchatcolor <Channelname> <Player>", true));
			player.spigot().sendMessage(plugin.getUtility().clickEvent(language+scc+"Info.msg34",
					ClickEvent.Action.SUGGEST_COMMAND,  "/scc pcnamecolor <Channelname>", true));
			player.spigot().sendMessage(plugin.getUtility().clickEvent(language+scc+"Info.msg35",
					ClickEvent.Action.SUGGEST_COMMAND,  "/scc pcplayer [Player]", true));
			player.spigot().sendMessage(plugin.getUtility().clickEvent(language+scc+"Info.msg36",
					ClickEvent.Action.SUGGEST_COMMAND,  "/scc pcchannels", true));
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
					ClickEvent.Action.SUGGEST_COMMAND,  "/scc tccreate <Name> [Password]", true));
			player.spigot().sendMessage(plugin.getUtility().clickEvent(language+scc+"Info.msg13",
					ClickEvent.Action.SUGGEST_COMMAND,  "/scc tcjoin <Name>", true));
			player.spigot().sendMessage(plugin.getUtility().clickEvent(language+scc+"Info.msg14",
					ClickEvent.Action.SUGGEST_COMMAND,  "/scc tcleave", true));
			player.spigot().sendMessage(plugin.getUtility().clickEvent(language+scc+"Info.msg15",
					ClickEvent.Action.SUGGEST_COMMAND,  "/scc tckick <Player>", true));
			player.spigot().sendMessage(plugin.getUtility().clickEvent(language+scc+"Info.msg16",
					ClickEvent.Action.SUGGEST_COMMAND,  "/scc tcban <Player>", true));
			player.spigot().sendMessage(plugin.getUtility().clickEvent(language+scc+"Info.msg17",
					ClickEvent.Action.SUGGEST_COMMAND,  "/scc tcunban <Player>", true));
			player.spigot().sendMessage(plugin.getUtility().clickEvent(language+scc+"Info.msg18",
					ClickEvent.Action.SUGGEST_COMMAND,  "/scc thangepassword <Password>", true));
			player.spigot().sendMessage(plugin.getUtility().clickEvent(language+scc+"Info.msg19",
					ClickEvent.Action.SUGGEST_COMMAND,  "/scc tcinfo", true));
			
			player.spigot().sendMessage(plugin.getUtility().clickEvent(language+scc+"Info.msg20",
					ClickEvent.Action.SUGGEST_COMMAND,  "/scc tcinvite <Player>", true));
			player.spigot().sendMessage(plugin.getUtility().clickEvent(language+scc+"Info.msg21",
					ClickEvent.Action.SUGGEST_COMMAND,  "/scc pccreate <Name> [Password]", true));
			player.spigot().sendMessage(plugin.getUtility().clickEvent(language+scc+"Info.msg22",
					ClickEvent.Action.SUGGEST_COMMAND,  "/scc pcjoin <Channelname> <Name>", true));
			player.spigot().sendMessage(plugin.getUtility().clickEvent(language+scc+"Info.msg23",
					ClickEvent.Action.SUGGEST_COMMAND,  "/scc pcleave <Channelname> confirm", true));
			player.spigot().sendMessage(plugin.getUtility().clickEvent(language+scc+"Info.msg24",
					ClickEvent.Action.SUGGEST_COMMAND,  "/scc pckick <Channelname> <Player>", true));
			player.spigot().sendMessage(plugin.getUtility().clickEvent(language+scc+"Info.msg25",
					ClickEvent.Action.SUGGEST_COMMAND,  "/scc pcban <Channelname> <Player>", true));
			player.spigot().sendMessage(plugin.getUtility().clickEvent(language+scc+"Info.msg26",
					ClickEvent.Action.SUGGEST_COMMAND,  "/scc pcunban <Channelname> <Player>", true));
			player.spigot().sendMessage(plugin.getUtility().clickEvent(language+scc+"Info.msg27",
					ClickEvent.Action.SUGGEST_COMMAND,  "/scc pcchangepassword <Channelname> <Password>", true));
			player.spigot().sendMessage(plugin.getUtility().clickEvent(language+scc+"Info.msg28",
					ClickEvent.Action.SUGGEST_COMMAND,  "/scc pcinfo [Channelname]", true));
			player.spigot().sendMessage(plugin.getUtility().clickEvent(language+scc+"Info.msg29",
					ClickEvent.Action.SUGGEST_COMMAND,  "/scc pcinvite <Channelname> <Player>", true));
			player.spigot().sendMessage(plugin.getUtility().clickEvent(language+scc+"Info.msg30",
					ClickEvent.Action.SUGGEST_COMMAND,  "/scc pcvice <Channelname> <Player>", true));
	
			player.spigot().sendMessage(plugin.getUtility().clickEvent(language+scc+"Info.msg33",
					ClickEvent.Action.SUGGEST_COMMAND,  "/scc pcchatcolor <Channelname> <Player>", true));
			player.spigot().sendMessage(plugin.getUtility().clickEvent(language+scc+"Info.msg34",
					ClickEvent.Action.SUGGEST_COMMAND,  "/scc pcnamecolor <Channelname>", true));
			player.spigot().sendMessage(plugin.getUtility().clickEvent(language+scc+"Info.msg35",
					ClickEvent.Action.SUGGEST_COMMAND,  "/scc pcplayer [Player]", true));
			player.spigot().sendMessage(plugin.getUtility().clickEvent(language+scc+"Info.msg36",
					ClickEvent.Action.SUGGEST_COMMAND,  "/scc pcchannels", true));
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
		if((boolean) plugin.getMysqlHandler().getDataI(player.getUniqueId().toString(),"channel_"+channel, "player_uuid"))
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
}