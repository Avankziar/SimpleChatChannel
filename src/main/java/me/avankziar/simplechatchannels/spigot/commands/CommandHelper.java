package main.java.me.avankziar.simplechatchannels.spigot.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

import main.java.me.avankziar.simplechatchannels.spigot.SimpleChatChannels;
import main.java.me.avankziar.simplechatchannels.spigot.interfaces.CustomChannel;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;

public class CommandHelper
{
	private SimpleChatChannels plugin;
	private String scc = ".CMD_SCC.";
	
	public CommandHelper(SimpleChatChannels plugin)
	{
		this.plugin = plugin;
	}
	
	public void info(Player player, String language)
	{
		if(player.hasPermission("scc.option.admin"))
		{
			player.spigot().sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+scc+"info.msg10")));
			player.spigot().sendMessage(plugin.getUtility().suggestCmdText(language+scc+"info.msg01", "/scc <channel>"));
			player.spigot().sendMessage(plugin.getUtility().suggestCmdText(language+scc+"info.msg02", "/scc mute <Player>"));
			player.spigot().sendMessage(plugin.getUtility().suggestCmdText(language+scc+"info.msg03", "/scc unmute <Player>"));
			player.spigot().sendMessage(plugin.getUtility().suggestCmdText(language+scc+"info.msg04", "/scc ignore <Player>"));
			player.spigot().sendMessage(plugin.getUtility().suggestCmdText(language+scc+"info.msg05", "/scc ignorelist"));
			player.spigot().sendMessage(plugin.getUtility().suggestCmdText(language+scc+"info.msg06", "/scc wordfilter <add/remove> <Word>"));
			player.spigot().sendMessage(plugin.getUtility().suggestCmdText(language+scc+"info.msg07", "/scc broadcast <Message>"));
			player.spigot().sendMessage(plugin.getUtility().suggestCmdText(language+scc+"info.msg08", "/scc playerlist [String]"));
			player.spigot().sendMessage(plugin.getUtility().suggestCmdText(language+scc+"info.msg09", "/scc bungee"));
			player.spigot().sendMessage(plugin.getUtility().suggestCmdText(language+scc+"info.msg11", "/scc grouplist [String]"));
			player.spigot().sendMessage(plugin.getUtility().suggestCmdText(language+scc+"info.msg12", "/scc cccreate <Name> [Password]"));
			player.spigot().sendMessage(plugin.getUtility().suggestCmdText(language+scc+"info.msg13", "/scc ccjoin <Name>"));
			player.spigot().sendMessage(plugin.getUtility().suggestCmdText(language+scc+"info.msg14", "/scc ccleave"));
			player.spigot().sendMessage(plugin.getUtility().suggestCmdText(language+scc+"info.msg15", "/scc cckick <Player>"));
			player.spigot().sendMessage(plugin.getUtility().suggestCmdText(language+scc+"info.msg16", "/scc ccban <Player>"));
			player.spigot().sendMessage(plugin.getUtility().suggestCmdText(language+scc+"info.msg17", "/scc ccunban <Player>"));
			player.spigot().sendMessage(plugin.getUtility().suggestCmdText(language+scc+"info.msg18", "/scc changepassword <Password>"));
			player.spigot().sendMessage(plugin.getUtility().suggestCmdText(language+scc+"info.msg19", "/scc channelinfo"));
		} else
		{
			player.spigot().sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+scc+"info.msg10")));
			player.spigot().sendMessage(plugin.getUtility().suggestCmdText(language+scc+"info.msg01", "/scc <channel>"));
			player.spigot().sendMessage(plugin.getUtility().suggestCmdText(language+scc+"info.msg04", "/scc ignore <Player>"));
			player.spigot().sendMessage(plugin.getUtility().suggestCmdText(language+scc+"info.msg05", "/scc ignorelist"));
			player.spigot().sendMessage(plugin.getUtility().suggestCmdText(language+scc+"info.msg08", "/scc playerlist [String]"));
			player.spigot().sendMessage(plugin.getUtility().suggestCmdText(language+scc+"info.msg11", "/scc grouplist [String]"));
			player.spigot().sendMessage(plugin.getUtility().suggestCmdText(language+scc+"info.msg12", "/scc cccreate <Name> [Password]"));
			player.spigot().sendMessage(plugin.getUtility().suggestCmdText(language+scc+"info.msg13", "/scc ccjoin <Name>"));
			player.spigot().sendMessage(plugin.getUtility().suggestCmdText(language+scc+"info.msg14", "/scc ccleave"));
			player.spigot().sendMessage(plugin.getUtility().suggestCmdText(language+scc+"info.msg15", "/scc cckick <Player>"));
			player.spigot().sendMessage(plugin.getUtility().suggestCmdText(language+scc+"info.msg16", "/scc ccban <Player>"));
			player.spigot().sendMessage(plugin.getUtility().suggestCmdText(language+scc+"info.msg17", "/scc ccunban <Player>"));
			player.spigot().sendMessage(plugin.getUtility().suggestCmdText(language+scc+"info.msg18", "/scc changepassword <Password>"));
			player.spigot().sendMessage(plugin.getUtility().suggestCmdText(language+scc+"info.msg19", "/scc channelinfo"));
		}
	}
	
	public void playergrouplist(Player player, String language, List<BaseComponent> list, String path)
	{
		TextComponent MSG = plugin.getUtility().tc("");
		if(list.isEmpty())
		{
			player.spigot().sendMessage(plugin.getUtility().tcl(
					plugin.getYamlHandler().getL().getString(language+scc+path+".msg02")));
			return;
		}
		MSG.setExtra(list);
		player.spigot().sendMessage(plugin.getUtility().tcl(
				plugin.getYamlHandler().getL().getString(language+scc+path+".msg01")));
		player.spigot().sendMessage(MSG);
	}
	
	public void channeltoggle(Player player, String[] args, String language, String channel, String replacer)
	{
		if(!player.hasPermission("scc.channels."+channel))
		{
			player.spigot().sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+scc+"msg02")));
			return;
		}
		if(plugin.getUtility().rightArgs(player,args,1))
		{
			return;
		}
		if((boolean) plugin.getMysqlInterface().getDataI(player, "channel_"+channel, "player_uuid"))
		{
			plugin.getMysqlInterface().updateDataI(player, false, "channel_"+channel);
			player.spigot().sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+scc+"channel.msg01")
					.replace("%channel%", replacer)));
		} else
		{
			plugin.getMysqlInterface().updateDataI(player, true, "channel_"+channel);
			player.spigot().sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+scc+"channel.msg02")
					.replace("%channel%", replacer)));
		}
		return;
	}
	
	public void optiontoggle(Player player, String[] args, String language, String optionperm,
			String option, String replacer)
	{
		if(!player.hasPermission("scc.option."+optionperm))
		{
			player.spigot().sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+scc+"msg02")));
			return;
		}
		if(plugin.getUtility().rightArgs(player,args,1))
		{
			return;
		}
		if(((boolean) plugin.getMysqlInterface().getDataI(player, option, "player_uuid")))
		{
			plugin.getMysqlInterface().updateDataI(player, false, option);
			player.spigot().sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+scc+"channel.msg01")
					.replace("%channel%", replacer)));
		} else
		{
			plugin.getMysqlInterface().updateDataI(player, true, option);
			player.spigot().sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+scc+"channel.msg02")
					.replace("%channel%", replacer)));
		}
	}
	
	public void cccreate(Player player, String language, CustomChannel cc, String name, String password)
	{
		ArrayList<Player> members = new ArrayList<Player>();
		members.add(player);
		cc = new CustomChannel(name, player, members, password, new ArrayList<Player>());
		CustomChannel.addCustomChannel(cc);
		if(password==null)
		{
			player.spigot().sendMessage(plugin.getUtility().tc(plugin.getUtility().tl(
					plugin.getYamlHandler().getL().getString(language+scc+"channelcreate.msg02")
					.replace("%channel%", cc.getName()))));
		} else
		{
			player.spigot().sendMessage(plugin.getUtility().tc(plugin.getUtility().tl(
					plugin.getYamlHandler().getL().getString(language+scc+"channelcreate.msg03")
					.replace("%channel%", cc.getName())
					.replace("%password%", password))));
		}
	}
	
	public void ccjoin(Player player, String language, String name, String password)
	{
		CustomChannel cc = CustomChannel.getCustomChannel(name);
		CustomChannel oldcc = CustomChannel.getCustomChannel(player);
		if(oldcc!=null)
		{
			player.spigot().sendMessage(plugin.getUtility().tcl(
					plugin.getYamlHandler().getL().getString(language+scc+"joinchannel.msg06")));
			return;
		}
		if(cc==null)
		{
			player.spigot().sendMessage(plugin.getUtility().tcl(
					plugin.getYamlHandler().getL().getString(language+scc+"joinchannel.msg01")
					.replace("%name%", name)));
			return;
		}
		if(cc.getBanned().contains(player))
		{
			player.spigot().sendMessage(plugin.getUtility().tcl(
					plugin.getYamlHandler().getL().getString(language+scc+"joinchannel.msg07")));
			return;
		}
		if(password==null)
		{
			if(cc.getPassword()!=null)
			{
				player.spigot().sendMessage(plugin.getUtility().tcl(
						plugin.getYamlHandler().getL().getString(language+scc+"joinchannel.msg02")));
				return;
			}
		} else
		{
			if(cc.getPassword()==null)
			{
				player.spigot().sendMessage(plugin.getUtility().tcl(
						plugin.getYamlHandler().getL().getString(language+scc+"joinchannel.msg04")));
				return;
			}
			if(!cc.getPassword().equals(password))
			{
				player.spigot().sendMessage(plugin.getUtility().tcl(
						plugin.getYamlHandler().getL().getString(language+scc+"joinchannel.msg03")));
				return;
			}
		}
		cc.addMembers(player);
		player.spigot().sendMessage(plugin.getUtility().tcl(
				plugin.getYamlHandler().getL().getString(language+scc+"joinchannel.msg05")
				.replace("%channel%", cc.getName())));
		return;
	}
}
