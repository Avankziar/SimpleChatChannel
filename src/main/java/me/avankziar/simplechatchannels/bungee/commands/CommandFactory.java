package main.java.me.avankziar.simplechatchannels.bungee.commands;

import java.util.ArrayList;
import java.util.List;

import main.java.me.avankziar.simplechatchannels.bungee.SimpleChatChannels;
import main.java.me.avankziar.simplechatchannels.bungee.interfaces.CustomChannel;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class CommandFactory
{
	private SimpleChatChannels plugin;
	private String scc = ".CMD_SCC.";
	
	public CommandFactory(SimpleChatChannels plugin)
	{
		this.plugin = plugin;
	}
	
	public void scc(ProxiedPlayer player, String language)
	{
		if(player.hasPermission("scc.option.admin"))
		{
			player.sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+scc+"info.msg10")));
			player.sendMessage(plugin.getUtility().suggestCmdText(language+scc+"info.msg01", "/scc <channel>"));
			player.sendMessage(plugin.getUtility().suggestCmdText(language+scc+"info.msg02", "/scc mute <Player>"));
			player.sendMessage(plugin.getUtility().suggestCmdText(language+scc+"info.msg03", "/scc unmute <Player>"));
			player.sendMessage(plugin.getUtility().suggestCmdText(language+scc+"info.msg04", "/scc ignore <Player>"));
			player.sendMessage(plugin.getUtility().suggestCmdText(language+scc+"info.msg05", "/scc ignorelist"));
			player.sendMessage(plugin.getUtility().suggestCmdText(language+scc+"info.msg06", "/scc wordfilter <add/remove> <Word>"));
			player.sendMessage(plugin.getUtility().suggestCmdText(language+scc+"info.msg07", "/scc broadcast <Message>"));
			player.sendMessage(plugin.getUtility().suggestCmdText(language+scc+"info.msg08", "/scc playerlist [String]"));
			player.sendMessage(plugin.getUtility().suggestCmdText(language+scc+"info.msg09", "/scc bungee"));
			player.sendMessage(plugin.getUtility().suggestCmdText(language+scc+"info.msg11", "/scc grouplist [String]"));
			player.sendMessage(plugin.getUtility().suggestCmdText(language+scc+"info.msg12", "/scc cccreate <Name> [Password]"));
			player.sendMessage(plugin.getUtility().suggestCmdText(language+scc+"info.msg13", "/scc ccjoin <Name>"));
			player.sendMessage(plugin.getUtility().suggestCmdText(language+scc+"info.msg14", "/scc ccleave"));
			player.sendMessage(plugin.getUtility().suggestCmdText(language+scc+"info.msg15", "/scc cckick <Player>"));
			player.sendMessage(plugin.getUtility().suggestCmdText(language+scc+"info.msg16", "/scc ccban <Player>"));
			player.sendMessage(plugin.getUtility().suggestCmdText(language+scc+"info.msg17", "/scc ccunban <Player>"));
			player.sendMessage(plugin.getUtility().suggestCmdText(language+scc+"info.msg18", "/scc changepassword <Password>"));
			player.sendMessage(plugin.getUtility().suggestCmdText(language+scc+"info.msg19", "/scc channelinfo"));
		} else
		{
			player.sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+scc+"info.msg10")));
			player.sendMessage(plugin.getUtility().suggestCmdText(language+scc+"info.msg01", "/scc <channel>"));
			player.sendMessage(plugin.getUtility().suggestCmdText(language+scc+"info.msg04", "/scc ignore <Player>"));
			player.sendMessage(plugin.getUtility().suggestCmdText(language+scc+"info.msg05", "/scc ignorelist"));
			player.sendMessage(plugin.getUtility().suggestCmdText(language+scc+"info.msg08", "/scc playerlist [String]"));
			player.sendMessage(plugin.getUtility().suggestCmdText(language+scc+"info.msg11", "/scc grouplist [String]"));
			player.sendMessage(plugin.getUtility().suggestCmdText(language+scc+"info.msg12", "/scc cccreate <Name> [Password]"));
			player.sendMessage(plugin.getUtility().suggestCmdText(language+scc+"info.msg13", "/scc ccjoin <Name>"));
			player.sendMessage(plugin.getUtility().suggestCmdText(language+scc+"info.msg14", "/scc ccleave"));
			player.sendMessage(plugin.getUtility().suggestCmdText(language+scc+"info.msg15", "/scc cckick <Player>"));
			player.sendMessage(plugin.getUtility().suggestCmdText(language+scc+"info.msg16", "/scc ccban <Player>"));
			player.sendMessage(plugin.getUtility().suggestCmdText(language+scc+"info.msg17", "/scc ccunban <Player>"));
			player.sendMessage(plugin.getUtility().suggestCmdText(language+scc+"info.msg18", "/scc changepassword <Password>"));
			player.sendMessage(plugin.getUtility().suggestCmdText(language+scc+"info.msg19", "/scc channelinfo"));
		}
	}
	
	//Obsolete
	public void playergrouplist(ProxiedPlayer player, String language, List<BaseComponent> list, String path)
	{
		TextComponent MSG = plugin.getUtility().tc("");
		if(list.isEmpty())
		{
			player.sendMessage(plugin.getUtility().tcl(
					plugin.getYamlHandler().getL().getString(language+scc+path+".msg02")));
			return;
		}
		MSG.setExtra(list);
		player.sendMessage(plugin.getUtility().tcl(
				plugin.getYamlHandler().getL().getString(language+scc+path+".msg01")));
		player.sendMessage(MSG);
	}
	
	public void channeltoggle(ProxiedPlayer player, String[] args, String language, String channel, String replacer)
	{
		if(plugin.getUtility().rightArgs(player,args,1))
		{
			return;
		}
		if((boolean) plugin.getMysqlInterface().getDataI(player, "channel_"+channel, "player_uuid"))
		{
			plugin.getMysqlInterface().updateDataI(player, false, "channel_"+channel);
			player.sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+scc+"channel.msg01")
					.replace("%channel%", replacer)));
		} else
		{
			plugin.getMysqlInterface().updateDataI(player, true, "channel_"+channel);
			player.sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+scc+"channel.msg02")
					.replace("%channel%", replacer)));
		}
		return;
	}
	
	public void optiontoggle(ProxiedPlayer player, String[] args, String language, String optionperm,
			String option, String replacer)
	{
		if(!player.hasPermission("scc.option."+optionperm))
		{
			player.sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+scc+"msg02")));
			return;
		}
		if(plugin.getUtility().rightArgs(player,args,1))
		{
			return;
		}
		if(((boolean) plugin.getMysqlInterface().getDataI(player, option, "player_uuid")))
		{
			plugin.getMysqlInterface().updateDataI(player, false, option);
			player.sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+scc+"channel.msg01")
					.replace("%channel%", replacer)));
		} else
		{
			plugin.getMysqlInterface().updateDataI(player, true, option);
			player.sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+scc+"channel.msg02")
					.replace("%channel%", replacer)));
		}
	}
	
	public void cccreate(ProxiedPlayer player, String language, CustomChannel cc, String name, String password)
	{
		ArrayList<ProxiedPlayer> members = new ArrayList<ProxiedPlayer>();
		members.add(player);
		cc = new CustomChannel(name, player, members, password, new ArrayList<ProxiedPlayer>());
		CustomChannel.addCustomChannel(cc);
		if(password==null)
		{
			player.sendMessage(plugin.getUtility().tc(plugin.getUtility().tl(
					plugin.getYamlHandler().getL().getString(language+scc+"channelcreate.msg02")
					.replace("%channel%", cc.getName()))));
		} else
		{
			player.sendMessage(plugin.getUtility().tc(plugin.getUtility().tl(
					plugin.getYamlHandler().getL().getString(language+scc+"channelcreate.msg03")
					.replace("%channel%", cc.getName())
					.replace("%password%", password))));
		}
	}
	
	public void ccjoin(ProxiedPlayer player, String language, String name, String password)
	{
		CustomChannel cc = CustomChannel.getCustomChannel(name);
		CustomChannel oldcc = CustomChannel.getCustomChannel(player);
		if(oldcc!=null)
		{
			player.sendMessage(plugin.getUtility().tcl(
					plugin.getYamlHandler().getL().getString(language+scc+"joinchannel.msg06")));
			return;
		}
		if(cc==null)
		{
			player.sendMessage(plugin.getUtility().tcl(
					plugin.getYamlHandler().getL().getString(language+scc+"joinchannel.msg01")
					.replace("%name%", name)));
			return;
		}
		if(cc.getBanned().contains(player))
		{
			player.sendMessage(plugin.getUtility().tcl(
					plugin.getYamlHandler().getL().getString(language+scc+"joinchannel.msg07")));
			return;
		}
		if(password==null)
		{
			if(cc.getPassword()!=null)
			{
				player.sendMessage(plugin.getUtility().tcl(
						plugin.getYamlHandler().getL().getString(language+scc+"joinchannel.msg02")));
				return;
			}
		} else
		{
			if(cc.getPassword()==null)
			{
				player.sendMessage(plugin.getUtility().tcl(
						plugin.getYamlHandler().getL().getString(language+scc+"joinchannel.msg04")));
				return;
			}
			if(!cc.getPassword().equals(password))
			{
				player.sendMessage(plugin.getUtility().tcl(
						plugin.getYamlHandler().getL().getString(language+scc+"joinchannel.msg03")));
				return;
			}
		}
		cc.addMembers(player);
		player.sendMessage(plugin.getUtility().tcl(
				plugin.getYamlHandler().getL().getString(language+scc+"joinchannel.msg05")
				.replace("%channel%", cc.getName())));
		return;
	}
}
