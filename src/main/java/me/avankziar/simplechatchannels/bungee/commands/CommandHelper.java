package main.java.me.avankziar.simplechatchannels.bungee.commands;

import java.util.ArrayList;
import java.util.List;

import main.java.me.avankziar.simplechatchannels.bungee.SimpleChatChannels;
import main.java.me.avankziar.simplechatchannels.bungee.interfaces.TemporaryChannel;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class CommandHelper
{
	private SimpleChatChannels plugin;
	private String scc = ".CmdScc.";
	
	public CommandHelper(SimpleChatChannels plugin)
	{
		this.plugin = plugin;
	}
	
	public void scc(ProxiedPlayer player) //CCinvite hinzufügen
	{
		String language = plugin.getUtility().getLanguage();
		if(player.hasPermission("scc.option.admin"))
		{
			player.sendMessage(plugin.getUtility().tctlYaml(language+scc+"Info.msg10"));
			player.sendMessage(plugin.getUtility().clickEvent(language+scc+"Info.msg01",
					ClickEvent.Action.SUGGEST_COMMAND, "/scc <channel>", true));
			player.sendMessage(plugin.getUtility().clickEvent(language+scc+"Info.msg02",
					ClickEvent.Action.SUGGEST_COMMAND,  "/scc mute <Player>", true));
			player.sendMessage(plugin.getUtility().clickEvent(language+scc+"Info.msg03",
					ClickEvent.Action.SUGGEST_COMMAND,  "/scc unmute <Player>", true));
			player.sendMessage(plugin.getUtility().clickEvent(language+scc+"Info.msg04",
					ClickEvent.Action.SUGGEST_COMMAND,  "/scc ignore <Player>", true));
			player.sendMessage(plugin.getUtility().clickEvent(language+scc+"Info.msg05",
					ClickEvent.Action.SUGGEST_COMMAND,  "/scc ignorelist", true));
			player.sendMessage(plugin.getUtility().clickEvent(language+scc+"Info.msg06",
					ClickEvent.Action.SUGGEST_COMMAND,  "/scc wordfilter <add/remove> <Word>", true));
			player.sendMessage(plugin.getUtility().clickEvent(language+scc+"Info.msg07",
					ClickEvent.Action.SUGGEST_COMMAND,  "/scc broadcast <Message>", true));
			player.sendMessage(plugin.getUtility().clickEvent(language+scc+"Info.msg08",
					ClickEvent.Action.SUGGEST_COMMAND,  "/scc playerlist [String]", true));
			player.sendMessage(plugin.getUtility().clickEvent(language+scc+"Info.msg09",
					ClickEvent.Action.SUGGEST_COMMAND,  "/scc bungee", true));
			player.sendMessage(plugin.getUtility().clickEvent(language+scc+"Info.msg11",
					ClickEvent.Action.SUGGEST_COMMAND,  "/scc grouplist [String]", true));
			player.sendMessage(plugin.getUtility().clickEvent(language+scc+"Info.msg12",
					ClickEvent.Action.SUGGEST_COMMAND,  "/scc tccreate <Name> [Password]", true));
			player.sendMessage(plugin.getUtility().clickEvent(language+scc+"Info.msg13",
					ClickEvent.Action.SUGGEST_COMMAND,  "/scc tcjoin <Name>", true));
			player.sendMessage(plugin.getUtility().clickEvent(language+scc+"Info.msg14",
					ClickEvent.Action.SUGGEST_COMMAND,  "/scc tcleave", true));
			player.sendMessage(plugin.getUtility().clickEvent(language+scc+"Info.msg15",
					ClickEvent.Action.SUGGEST_COMMAND,  "/scc tckick <Player>", true));
			player.sendMessage(plugin.getUtility().clickEvent(language+scc+"Info.msg16",
					ClickEvent.Action.SUGGEST_COMMAND,  "/scc tcban <Player>", true));
			player.sendMessage(plugin.getUtility().clickEvent(language+scc+"Info.msg17",
					ClickEvent.Action.SUGGEST_COMMAND,  "/scc tcunban <Player>", true));
			player.sendMessage(plugin.getUtility().clickEvent(language+scc+"Info.msg18",
					ClickEvent.Action.SUGGEST_COMMAND,  "/scc tcchangepassword <Password>", true));
			player.sendMessage(plugin.getUtility().clickEvent(language+scc+"Info.msg19",
					ClickEvent.Action.SUGGEST_COMMAND,  "/scc tcinfo", true));
			
			player.sendMessage(plugin.getUtility().clickEvent(language+scc+"Info.msg20",
					ClickEvent.Action.SUGGEST_COMMAND,  "/scc tcinvite <Player>", true));
			player.sendMessage(plugin.getUtility().clickEvent(language+scc+"Info.msg21",
					ClickEvent.Action.SUGGEST_COMMAND,  "/scc pccreate <Name> [Password]", true));
			player.sendMessage(plugin.getUtility().clickEvent(language+scc+"Info.msg22",
					ClickEvent.Action.SUGGEST_COMMAND,  "/scc pcjoin <Name>", true));
			player.sendMessage(plugin.getUtility().clickEvent(language+scc+"Info.msg23",
					ClickEvent.Action.SUGGEST_COMMAND,  "/scc pcleave confirm", true));
			player.sendMessage(plugin.getUtility().clickEvent(language+scc+"Info.msg24",
					ClickEvent.Action.SUGGEST_COMMAND,  "/scc pckick <Player>", true));
			player.sendMessage(plugin.getUtility().clickEvent(language+scc+"Info.msg25",
					ClickEvent.Action.SUGGEST_COMMAND,  "/scc pcban <Player>", true));
			player.sendMessage(plugin.getUtility().clickEvent(language+scc+"Info.msg26",
					ClickEvent.Action.SUGGEST_COMMAND,  "/scc pcunban <Player>", true));
			player.sendMessage(plugin.getUtility().clickEvent(language+scc+"Info.msg27",
					ClickEvent.Action.SUGGEST_COMMAND,  "/scc pcchangepassword <Password>", true));
			player.sendMessage(plugin.getUtility().clickEvent(language+scc+"Info.msg28",
					ClickEvent.Action.SUGGEST_COMMAND,  "/scc pcinfo", true));
			player.sendMessage(plugin.getUtility().clickEvent(language+scc+"Info.msg29",
					ClickEvent.Action.SUGGEST_COMMAND,  "/scc pcinvite <Player>", true));
			player.sendMessage(plugin.getUtility().clickEvent(language+scc+"Info.msg30",
					ClickEvent.Action.SUGGEST_COMMAND,  "/scc pcvice <Player>", true));
		} else
		{
			player.sendMessage(plugin.getUtility().tctlYaml(language+scc+"Info.msg10"));
			player.sendMessage(plugin.getUtility().clickEvent(language+scc+"Info.msg01",
					ClickEvent.Action.SUGGEST_COMMAND,  "/scc <channel>", true));
			player.sendMessage(plugin.getUtility().clickEvent(language+scc+"Info.msg04",
					ClickEvent.Action.SUGGEST_COMMAND,  "/scc ignore <Player>", true));
			player.sendMessage(plugin.getUtility().clickEvent(language+scc+"Info.msg05",
					ClickEvent.Action.SUGGEST_COMMAND,  "/scc ignorelist", true));
			player.sendMessage(plugin.getUtility().clickEvent(language+scc+"Info.msg08",
					ClickEvent.Action.SUGGEST_COMMAND,  "/scc playerlist [String]", true));
			player.sendMessage(plugin.getUtility().clickEvent(language+scc+"Info.msg11",
					ClickEvent.Action.SUGGEST_COMMAND,  "/scc grouplist [String]", true));
			player.sendMessage(plugin.getUtility().clickEvent(language+scc+"Info.msg12",
					ClickEvent.Action.SUGGEST_COMMAND,  "/scc tccreate <Name> [Password]", true));
			player.sendMessage(plugin.getUtility().clickEvent(language+scc+"Info.msg13",
					ClickEvent.Action.SUGGEST_COMMAND,  "/scc tcjoin <Name>", true));
			player.sendMessage(plugin.getUtility().clickEvent(language+scc+"Info.msg14",
					ClickEvent.Action.SUGGEST_COMMAND,  "/scc tcleave", true));
			player.sendMessage(plugin.getUtility().clickEvent(language+scc+"Info.msg15",
					ClickEvent.Action.SUGGEST_COMMAND,  "/scc tckick <Player>", true));
			player.sendMessage(plugin.getUtility().clickEvent(language+scc+"Info.msg16",
					ClickEvent.Action.SUGGEST_COMMAND,  "/scc tcban <Player>", true));
			player.sendMessage(plugin.getUtility().clickEvent(language+scc+"Info.msg17",
					ClickEvent.Action.SUGGEST_COMMAND,  "/scc tcunban <Player>", true));
			player.sendMessage(plugin.getUtility().clickEvent(language+scc+"Info.msg18",
					ClickEvent.Action.SUGGEST_COMMAND,  "/scc thangepassword <Password>", true));
			player.sendMessage(plugin.getUtility().clickEvent(language+scc+"Info.msg19",
					ClickEvent.Action.SUGGEST_COMMAND,  "/scc tcinfo", true));
		}
	}
	
	public void playergrouplist(ProxiedPlayer player, List<BaseComponent> list, String path)
	{
		TextComponent MSG = plugin.getUtility().tc("");
		if(list.isEmpty())
		{
			player.sendMessage(plugin.getUtility().tctlYaml(plugin.getUtility().getLanguage()+scc+path+".WrongInput"));
			return;
		}
		MSG.setExtra(list);
		player.sendMessage(plugin.getUtility().tctlYaml(plugin.getUtility().getLanguage()+scc+path+".Headline"));
		player.sendMessage(MSG);
	}
	
	public void channeltoggle(ProxiedPlayer player, String channel, String replacer)
	{
		if((boolean) plugin.getMysqlHandler().getDataI(player.getUniqueId().toString(),"channel_"+channel, "player_uuid"))
		{
			///Du hast den Channel %channel% &causgeblendet!
			plugin.getMysqlHandler().updateDataI(player, false, "channel_"+channel);
			player.sendMessage(plugin.getUtility().tctl(
					plugin.getYamlHandler().getL().getString(plugin.getUtility().getLanguage()+scc+"ChannelToggle.ChannelOff")
					.replace("%channel%", replacer)));
		} else
		{
			///Du hast den Channel %channel% &aeingeblendet!
			plugin.getMysqlHandler().updateDataI(player, true, "channel_"+channel);
			player.sendMessage(plugin.getUtility().tctl(
					plugin.getYamlHandler().getL().getString(plugin.getUtility().getLanguage()+scc+"ChannelToggle.ChannelOn")
					.replace("%channel%", replacer)));
		}
		return;
	}
	
	public void optiontoggle(ProxiedPlayer player, String optionperm,
			String option, String replacer)
	{
		if(((boolean) plugin.getMysqlHandler().getDataI(player.getUniqueId().toString(), option, "player_uuid")))
		{
			plugin.getMysqlHandler().updateDataI(player, false, option);
			///Du hast den Channel %channel% &causgeblendet!
			player.sendMessage(plugin.getUtility().tctl(
					plugin.getYamlHandler().getL().getString(plugin.getUtility().getLanguage()+scc+"ChannelToggle.ChannelOff")
					.replace("%channel%", replacer)));
		} else
		{
			plugin.getMysqlHandler().updateDataI(player, true, option);
			///Du hast den Channel %channel% &aeingeblendet!
			player.sendMessage(plugin.getUtility().tctl(
					plugin.getYamlHandler().getL().getString(plugin.getUtility().getLanguage()+scc+"ChannelToggle.ChannelOn")
					.replace("%channel%", replacer)));
		}
	}
	
	//Obsolete
	public void cccreate(ProxiedPlayer player, String language, TemporaryChannel cc, String name, String password)
	{
		ArrayList<ProxiedPlayer> members = new ArrayList<ProxiedPlayer>();
		members.add(player);
		cc = new TemporaryChannel(name, player, members, password, new ArrayList<ProxiedPlayer>());
		TemporaryChannel.addCustomChannel(cc);
		if(password==null)
		{
			player.sendMessage(plugin.getUtility().tctl(
					plugin.getYamlHandler().getL().getString(language+scc+"ChannelCreate.msg02")
					.replace("%channel%", cc.getName())));
		} else
		{
			player.sendMessage(plugin.getUtility().tctl(
					plugin.getYamlHandler().getL().getString(language+scc+"ChannelCreate.msg03")
					.replace("%channel%", cc.getName())
					.replace("%password%", password)));
		}
	}
	
	//Obsolete
	public void ccjoin(ProxiedPlayer player, String language, String name, String password)
	{
		TemporaryChannel cc = TemporaryChannel.getCustomChannel(name);
		TemporaryChannel oldcc = TemporaryChannel.getCustomChannel(player);
		if(oldcc!=null)
		{
			player.sendMessage(plugin.getUtility().tctl(
					plugin.getYamlHandler().getL().getString(language+scc+"ChannelJoin.msg06")));
			return;
		}
		if(cc==null)
		{
			player.sendMessage(plugin.getUtility().tctl(
					plugin.getYamlHandler().getL().getString(language+scc+"ChannelJoin.msg01")
					.replace("%name%", name)));
			return;
		}
		if(cc.getBanned().contains(player))
		{
			player.sendMessage(plugin.getUtility().tctl(
					plugin.getYamlHandler().getL().getString(language+scc+"ChannelJoin.msg07")));
			return;
		}
		if(password==null)
		{
			if(cc.getPassword()!=null)
			{
				player.sendMessage(plugin.getUtility().tctl(
						plugin.getYamlHandler().getL().getString(language+scc+"ChannelJoin.msg02")));
				return;
			}
		} else
		{
			if(cc.getPassword()==null)
			{
				player.sendMessage(plugin.getUtility().tctl(
						plugin.getYamlHandler().getL().getString(language+scc+"ChannelJoin.msg04")));
				return;
			}
			if(!cc.getPassword().equals(password))
			{
				player.sendMessage(plugin.getUtility().tctl(
						plugin.getYamlHandler().getL().getString(language+scc+"ChannelJoin.msg03")));
				return;
			}
		}
		cc.addMembers(player);
		player.sendMessage(plugin.getUtility().tctl(
				plugin.getYamlHandler().getL().getString(language+scc+"ChannelJoin.msg05")
				.replace("%channel%", cc.getName())));
		return;
	}
}
