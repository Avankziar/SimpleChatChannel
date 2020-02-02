package main.java.de.avankziar.simplechatchannels.spigot.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.de.avankziar.simplechatchannels.spigot.interfaces.CustomChannel;
import main.java.de.avankziar.simplechatchannels.spigot.SimpleChatChannels;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class CMDSimpleChatChannels implements CommandExecutor
{
	private SimpleChatChannels plugin;
	private String scc = ".CMD_SCC.";
	
	public CMDSimpleChatChannels(SimpleChatChannels plugin)
	{
		this.plugin = plugin;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lable, String[] args)
	{
		if(!(sender instanceof Player))
    	{
    		return false;
    	}
		Player player = (Player) sender;
    	String language = plugin.getYamlHandler().get().getString("language");
    	if(args.length == 0)
    	{
    		if(player.hasPermission("scc.option.admin"))
    		{
    			TextComponent msg1 = plugin.getUtility().tc(plugin.getUtility().tl(
    					plugin.getYamlHandler().getL().getString(language+scc+"info.msg01")));
    			msg1.setClickEvent( new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/scc <channel>"));
    			TextComponent msg2 = plugin.getUtility().tc(plugin.getUtility().tl(
    					plugin.getYamlHandler().getL().getString(language+scc+"info.msg02")));
    			msg2.setClickEvent( new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/scc mute <Player>"));
    			TextComponent msg3 = plugin.getUtility().tc(plugin.getUtility().tl(
    					plugin.getYamlHandler().getL().getString(language+scc+"info.msg03")));
    			msg3.setClickEvent( new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/scc unmute <Player>"));
    			TextComponent msg4 = plugin.getUtility().tc(plugin.getUtility().tl(
    					plugin.getYamlHandler().getL().getString(language+scc+"info.msg04")));
    			msg4.setClickEvent( new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/scc ignore <Player>"));
    			TextComponent msg5 = plugin.getUtility().tc(plugin.getUtility().tl(
    					plugin.getYamlHandler().getL().getString(language+scc+"info.msg05")));
    			msg5.setClickEvent( new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/scc ignorelist"));
    			TextComponent msg6 = plugin.getUtility().tc(plugin.getUtility().tl(
    					plugin.getYamlHandler().getL().getString(language+scc+"info.msg06")));
    			msg6.setClickEvent( new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/scc wordfilter <add/remove> <Word>"));
    			TextComponent msg7 = plugin.getUtility().tc(plugin.getUtility().tl(
    					plugin.getYamlHandler().getL().getString(language+scc+"info.msg07")));
    			msg7.setClickEvent( new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/scc broadcast <Message>"));
    			TextComponent msg8 = plugin.getUtility().tc(plugin.getUtility().tl(
    					plugin.getYamlHandler().getL().getString(language+scc+"info.msg08")));
    			msg8.setClickEvent( new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/scc playerlist [String]"));
    			TextComponent msg10 = plugin.getUtility().tc(plugin.getUtility().tl(
    					plugin.getYamlHandler().getL().getString(language+scc+"info.msg09")));
    			msg10.setClickEvent( new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/scc bungee"));
    			TextComponent msg11 = plugin.getUtility().tc(plugin.getUtility().tl(
    					plugin.getYamlHandler().getL().getString(language+scc+"info.msg11")));
    			msg11.setClickEvent( new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/scc grouplist [String]"));
    			TextComponent msg12 = plugin.getUtility().tc(plugin.getUtility().tl(
    					plugin.getYamlHandler().getL().getString(language+scc+"info.msg12")));
    			msg12.setClickEvent( new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/scc channelcreate <Name> [Password]"));
    			TextComponent msg13 = plugin.getUtility().tc(plugin.getUtility().tl(
    					plugin.getYamlHandler().getL().getString(language+scc+"info.msg13")));
    			msg13.setClickEvent( new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/scc channeljoin <Name>"));
    			TextComponent msg14 = plugin.getUtility().tc(plugin.getUtility().tl(
    					plugin.getYamlHandler().getL().getString(language+scc+"info.msg14")));
    			msg14.setClickEvent( new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/scc channelleave"));
    			TextComponent msg15 = plugin.getUtility().tc(plugin.getUtility().tl(
    					plugin.getYamlHandler().getL().getString(language+scc+"info.msg15")));
    			msg15.setClickEvent( new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/scc channelkick <Player>"));
    			TextComponent msg16 = plugin.getUtility().tc(plugin.getUtility().tl(
    					plugin.getYamlHandler().getL().getString(language+scc+"info.msg16")));
    			msg16.setClickEvent( new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/scc channelban <Player>"));
    			TextComponent msg17 = plugin.getUtility().tc(plugin.getUtility().tl(
    					plugin.getYamlHandler().getL().getString(language+scc+"info.msg17")));
    			msg17.setClickEvent( new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/scc channelunban <Player>"));
    			TextComponent msg18 = plugin.getUtility().tc(plugin.getUtility().tl(
    					plugin.getYamlHandler().getL().getString(language+scc+"info.msg18")));
    			msg18.setClickEvent( new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/scc changepassword <Password>"));
    			TextComponent msg19 = plugin.getUtility().tc(plugin.getUtility().tl(
    					plugin.getYamlHandler().getL().getString(language+scc+"info.msg19")));
    			msg19.setClickEvent( new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/scc channelinfo"));
    			player.spigot().sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+scc+"info.msg10")));
    			player.spigot().sendMessage(msg1);
    			player.spigot().sendMessage(msg2);
    			player.spigot().sendMessage(msg3);
    			player.spigot().sendMessage(msg4);
    			player.spigot().sendMessage(msg5);
    			player.spigot().sendMessage(msg6);
    			player.spigot().sendMessage(msg7);
    			player.spigot().sendMessage(msg8);
    			player.spigot().sendMessage(msg10);
    			player.spigot().sendMessage(msg11);
    			player.spigot().sendMessage(msg12);
    			player.spigot().sendMessage(msg13);
    			player.spigot().sendMessage(msg14);
    			player.spigot().sendMessage(msg15);
    			player.spigot().sendMessage(msg16);
    			player.spigot().sendMessage(msg17);
    			player.spigot().sendMessage(msg18);
    			player.spigot().sendMessage(msg19);
    		} else
    		{
    			TextComponent msg1 = plugin.getUtility().tc(plugin.getUtility().tl(
    					plugin.getYamlHandler().getL().getString(language+scc+"info.msg01")));
    			msg1.setClickEvent( new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/scc <channel>"));
    			TextComponent msg4 = plugin.getUtility().tc(plugin.getUtility().tl(
    					plugin.getYamlHandler().getL().getString(language+scc+"info.msg04")));
    			msg4.setClickEvent( new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/scc ignore <Player>"));
    			TextComponent msg5 = plugin.getUtility().tc(plugin.getUtility().tl(
    					plugin.getYamlHandler().getL().getString(language+scc+"info.msg05")));
    			msg5.setClickEvent( new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/scc ignorelist"));
    			TextComponent msg8 = plugin.getUtility().tc(plugin.getUtility().tl(
    					plugin.getYamlHandler().getL().getString(language+scc+"info.msg08")));
    			msg8.setClickEvent( new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/scc playerlist [String]"));
    			TextComponent msg11 = plugin.getUtility().tc(plugin.getUtility().tl(
    					plugin.getYamlHandler().getL().getString(language+scc+"info.msg11")));
    			msg11.setClickEvent( new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/scc grouplist [String]"));
    			TextComponent msg12 = plugin.getUtility().tc(plugin.getUtility().tl(
    					plugin.getYamlHandler().getL().getString(language+scc+"info.msg12")));
    			msg12.setClickEvent( new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/scc channelcreate <Name> [Password]"));
    			TextComponent msg13 = plugin.getUtility().tc(plugin.getUtility().tl(
    					plugin.getYamlHandler().getL().getString(language+scc+"info.msg13")));
    			msg13.setClickEvent( new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/scc channeljoin <Name>"));
    			TextComponent msg14 = plugin.getUtility().tc(plugin.getUtility().tl(
    					plugin.getYamlHandler().getL().getString(language+scc+"info.msg14")));
    			msg14.setClickEvent( new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/scc channelleave"));
    			TextComponent msg15 = plugin.getUtility().tc(plugin.getUtility().tl(
    					plugin.getYamlHandler().getL().getString(language+scc+"info.msg15")));
    			msg15.setClickEvent( new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/scc channelkick <Player>"));
    			TextComponent msg16 = plugin.getUtility().tc(plugin.getUtility().tl(
    					plugin.getYamlHandler().getL().getString(language+scc+"info.msg16")));
    			msg16.setClickEvent( new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/scc channelban <Player>"));
    			TextComponent msg17 = plugin.getUtility().tc(plugin.getUtility().tl(
    					plugin.getYamlHandler().getL().getString(language+scc+"info.msg17")));
    			msg17.setClickEvent( new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/scc channelunban <Player>"));
    			TextComponent msg18 = plugin.getUtility().tc(plugin.getUtility().tl(
    					plugin.getYamlHandler().getL().getString(language+scc+"info.msg18")));
    			msg18.setClickEvent( new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/scc changepassword <Password>"));
    			TextComponent msg19 = plugin.getUtility().tc(plugin.getUtility().tl(
    					plugin.getYamlHandler().getL().getString(language+scc+"info.msg19")));
    			msg19.setClickEvent( new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/scc channelinfo"));
    			player.spigot().sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+scc+"info.msg10")));
    			player.spigot().sendMessage(msg1);
    			player.spigot().sendMessage(msg4);
    			player.spigot().sendMessage(msg5);
    			player.spigot().sendMessage(msg11);
    			player.spigot().sendMessage(msg11);
    			player.spigot().sendMessage(msg12);
    			player.spigot().sendMessage(msg13);
    			player.spigot().sendMessage(msg14);
    			player.spigot().sendMessage(msg15);
    			player.spigot().sendMessage(msg16);
    			player.spigot().sendMessage(msg17);
    			player.spigot().sendMessage(msg18);
    			player.spigot().sendMessage(msg19);
    		}
    		return true;
    	} else if("playerlist".equalsIgnoreCase(args[0]) || "pl".equalsIgnoreCase(args[0]) 
    			|| "spielerlist".equalsIgnoreCase(args[0]) || "spielerlist".equalsIgnoreCase(args[0])) 
    	{
    		if(!player.hasPermission("scc.cmd.playerlist"))
			{
				player.spigot().sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+scc+"msg02")));
				return false;
			}
			TextComponent MSG = plugin.getUtility().tc("");
			List<BaseComponent> list = new ArrayList<>();
    		if(args.length==1)
    		{
    			for(Player pp : plugin.getServer().getOnlinePlayers())
        		{
    				TextComponent prefix = plugin.getUtility().tcl("&e"+pp.getName()+" ");
    				prefix.setClickEvent( new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND,
    						plugin.getYamlHandler().getSymbol("pm")+pp.getName()+" "));
    				list.add(prefix);
        		}
    			if(list.isEmpty())
    			{
    				player.spigot().sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+scc+"playerlist.msg02")));
    				return false;
    			}
    			MSG.setExtra(list);
    			player.spigot().sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+scc+"playerlist.msg01")));
    			player.spigot().sendMessage(MSG);
    			return true;
    		} else if(args.length==2)
    		{
    			String s = args[1];
    			for(Player pp : plugin.getServer().getOnlinePlayers())
        		{
    				if(pp.getName().contains(s))
    				{
    					TextComponent prefix = plugin.getUtility().tcl("&e"+pp.getName()+" ");
        				prefix.setClickEvent( new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND,
        						plugin.getYamlHandler().getSymbol("pm")+pp.getName()+" "));
        				list.add(prefix);
    				}
        		}
    			if(list.isEmpty())
    			{
    				player.spigot().sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+scc+"playerlist.msg02")));
    				return false;
    			}
    			MSG.setExtra(list);
    			player.spigot().sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+scc+"playerlist.msg01")));
    			player.spigot().sendMessage(MSG);
    		} else
    		{
    			plugin.getUtility().rightArgs(player,args,2);
    			return false;
    		}
    	} else if("grouplist".equalsIgnoreCase(args[0]) || "gl".equalsIgnoreCase(args[0])
    			|| "gruppenlist".equalsIgnoreCase(args[0]) || "gruppenliste".equalsIgnoreCase(args[0])) 
    	{
    		if(!player.hasPermission("scc.cmd.grouplist"))
			{
				player.spigot().sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+scc+"msg02")));
				return false;
			}
			TextComponent MSG = plugin.getUtility().tc("");
			List<BaseComponent> list = new ArrayList<>();
			int i = 1;
			int groupamount = Integer.parseInt(plugin.getYamlHandler().getL().getString(language+".prefixsuffixamount"));
			ArrayList<String> groups = new ArrayList<>();
			while(i<=groupamount)
			{
				if(plugin.getYamlHandler().getL().getString(language+".prefix."+i).contains("&"))
				{
					groups.add(plugin.getYamlHandler().getL().getString(language+".prefix."+i).substring(2));
				} else
				{
					groups.add(plugin.getYamlHandler().getL().getString(language+".prefix."+i));
				}
				if(plugin.getYamlHandler().getL().getString(language+".suffix."+i).contains("&"))
				{
					groups.add(plugin.getYamlHandler().getL().getString(language+".suffix."+i).substring(2));
				} else
				{
					groups.add(plugin.getYamlHandler().getL().getString(language+".suffix."+i));
				}
				i++;
			}
    		if(args.length==1)
    		{
    			for(String g : groups)
        		{
    				TextComponent prefix = plugin.getUtility().tcl("&6"+g+" ");
    				prefix.setClickEvent( new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND,
    						plugin.getYamlHandler().getSymbol("group")+g+" "));
    				list.add(prefix);
        		}
    			if(list.isEmpty())
    			{
    				player.spigot().sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+scc+"grouplist.msg02")));
    				return false;
    			}
    			MSG.setExtra(list);
    			player.spigot().sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+scc+"grouplist.msg01")));
    			player.spigot().sendMessage(MSG);
    			return true;
    		} else if(args.length==2)
    		{
    			String s = args[1];
    			for(String g : groups)
        		{
    				if(s.contains(g))
    				{
    					TextComponent prefix = plugin.getUtility().tcl("&6"+g+" ");
        				prefix.setClickEvent( new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND,
        						plugin.getYamlHandler().getSymbol("group")+g+" "));
        				list.add(prefix);	
    				}
        		}
    			if(list.isEmpty())
    			{
    				player.spigot().sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+scc+"playerlist.msg02")));
    				return false;
    			}
    			MSG.setExtra(list);
    			player.spigot().sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+scc+"grouplist.msg01")));
    			player.spigot().sendMessage(MSG);
    			return true;
    		} else
    		{
    			plugin.getUtility().rightArgs(player,args,2);
    			return false;
    		}
    	} else if("global".equalsIgnoreCase(args[0]))
		{
			if(!player.hasPermission("scc.channels.global"))
			{
				player.spigot().sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+scc+"msg02")));
				return false;
			}
			if(plugin.getUtility().rightArgs(player,args,1))
			{
				return false;
			}
			if((boolean) plugin.getMysqlInterface().getDataI(player, "channel_global", "player_uuid"))
			{
				plugin.getMysqlInterface().updateDataI(player, false, "channel_global");
				player.spigot().sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+scc+"channel.msg01")
						.replaceAll("%channel%", "Global")));
			} else
			{
				plugin.getMysqlInterface().updateDataI(player, true, "channel_global");
				player.spigot().sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+scc+"channel.msg02")
						.replaceAll("%channel%", "Global")));
			}
			return true;
		} else if("trade".equalsIgnoreCase(args[0]) || "handel".equalsIgnoreCase(args[0]))
		{
			if(!player.hasPermission("scc.channels.trade"))
			{
				player.spigot().sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+scc+"msg02")));
				return false;
			}
			if(plugin.getUtility().rightArgs(player,args,1))
			{
				return false;
			}
			if((boolean) plugin.getMysqlInterface().getDataI(player, "channel_trade", "player_uuid"))
			{
				plugin.getMysqlInterface().updateDataI(player, false, "channel_trade");
				player.spigot().sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+scc+"channel.msg01")
						.replaceAll("%channel%", "Trade")));
			} else
			{
				plugin.getMysqlInterface().updateDataI(player, true, "channel_trade");
				player.spigot().sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+scc+"channel.msg02")
						.replaceAll("%channel%", "Trade")));
			}
			return true;
		} else if("auction".equalsIgnoreCase(args[0]) || "auktion".equalsIgnoreCase(args[0]))
		{
			if(!player.hasPermission("scc.channels.auction"))
			{
				player.spigot().sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+scc+"msg02")));
				return false;
			}
			if(plugin.getUtility().rightArgs(player,args,1))
			{
				return false;
			}
			if((boolean) plugin.getMysqlInterface().getDataI(player, "channel_auction", "player_uuid"))
			{
				plugin.getMysqlInterface().updateDataI(player, false, "channel_auction");
				player.spigot().sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+scc+"channel.msg01")
						.replaceAll("%channel%", "Auction")));
			} else
			{
				plugin.getMysqlInterface().updateDataI(player, true, "channel_auction");
				player.spigot().sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+scc+"channel.msg02")
						.replaceAll("%channel%", "Auction")));
			}
			return true;
		} else if(args[0].equalsIgnoreCase("local") || args[0].equalsIgnoreCase("lokal"))//--------------------------------------------------local
		{
			if(!player.hasPermission("scc.channels.local"))
			{
				player.spigot().sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+scc+"msg02")));
				return false;
			}
			if(plugin.getUtility().rightArgs(player,args,1))
			{
				return false;
			}
			if((boolean) plugin.getMysqlInterface().getDataI(player, "channel_local", "player_uuid"))
			{
				plugin.getMysqlInterface().updateDataI(player, false, "channel_local");
				player.spigot().sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+scc+"channel.msg01")
						.replaceAll("%channel%", "Local")));
			} else
			{
				plugin.getMysqlInterface().updateDataI(player, true, "channel_local");
				player.spigot().sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+scc+"channel.msg02")
						.replaceAll("%channel%", "Local")));
			}
			return true;
		} else if("support".equalsIgnoreCase(args[0]) || "hilfe".equalsIgnoreCase(args[0]))
		{
			if(!player.hasPermission("scc.channels.support"))
			{
				player.spigot().sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+scc+"msg02")));
				return false;
			}
			if(plugin.getUtility().rightArgs(player,args,1))
			{
				return false;
			}
			if(((boolean) plugin.getMysqlInterface().getDataI(player, "channel_support", "player_uuid")))
			{
				plugin.getMysqlInterface().updateDataI(player, false, "channel_support");
				player.spigot().sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+scc+"channel.msg01")
						.replaceAll("%channel%", "Support")));
			} else
			{
				plugin.getMysqlInterface().updateDataI(player, true, "channel_support");
				player.spigot().sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+scc+"channel.msg02")
						.replaceAll("%channel%", "Support")));
			}
			return true;
		} else if("team".equalsIgnoreCase(args[0]))
		{
			if(!player.hasPermission("scc.channels.team"))
			{
				player.spigot().sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+scc+"msg02")));
				return false;
			}
			if(plugin.getUtility().rightArgs(player,args,1))
			{
				return false;
			}
			if(((boolean) plugin.getMysqlInterface().getDataI(player, "channel_team", "player_uuid")))
			{
				plugin.getMysqlInterface().updateDataI(player, false, "channel_team");
				player.spigot().sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+scc+"channel.msg01")
						.replaceAll("%channel%", "Team")));
			} else
			{
				plugin.getMysqlInterface().updateDataI(player, true, "channel_team");
				player.spigot().sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+scc+"channel.msg02")
						.replaceAll("%channel%", "Team")));
			}
			return true;
		} else if("admin".equalsIgnoreCase(args[0]))
		{
			if(!player.hasPermission("scc.channels.admin"))
			{
				player.spigot().sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+scc+"msg02")));
				return false;
			}
			if(plugin.getUtility().rightArgs(player,args,1))
			{
				return false;
			}
			if(((boolean) plugin.getMysqlInterface().getDataI(player, "channel_admin", "player_uuid")))
			{
				plugin.getMysqlInterface().updateDataI(player, false, "channel_admin");
				player.spigot().sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+scc+"channel.msg01")
						.replaceAll("%channel%", "Admin")));
			} else
			{
				plugin.getMysqlInterface().updateDataI(player, true, "channel_admin");
				player.spigot().sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+scc+"channel.msg02")
						.replaceAll("%channel%", "Admin")));
			}
			return true;
		} else if("pm".equalsIgnoreCase(args[0]) || "pn".equalsIgnoreCase(args[0]))
		{
			if(!player.hasPermission("scc.channels.pm"))
			{
				player.spigot().sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+scc+"msg02")));
				return false;
			}
			if(plugin.getUtility().rightArgs(player,args,1))
			{
				return false;
			}
			if(((boolean) plugin.getMysqlInterface().getDataI(player, "channel_pm", "player_uuid")))
			{
				plugin.getMysqlInterface().updateDataI(player, false, "channel_pm");
				player.spigot().sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+scc+"channel.msg01")
						.replaceAll("%channel%", "Privat Message")));
			} else
			{
				plugin.getMysqlInterface().updateDataI(player, true, "channel_pm");
				player.spigot().sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+scc+"channel.msg02")
						.replaceAll("%channel%", "Private Message")));
			}
			return true;
		} else if(args[0].equalsIgnoreCase("world") || args[0].equalsIgnoreCase("welt"))//--------------------------------------------------world
		{
			if(!player.hasPermission("scc.channels.world"))
			{
				player.spigot().sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+scc+"msg02")));
				return false;
			}
			if(plugin.getUtility().rightArgs(player,args,1))
			{
				return false;
			}
			if((boolean) plugin.getMysqlInterface().getDataI(player, "channel_world", "player_uuid"))
			{
				plugin.getMysqlInterface().updateDataI(player, false, "channel_world");
				player.spigot().sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+scc+"channel.msg01")
						.replaceAll("%channel%", "World")));
			} else
			{
				plugin.getMysqlInterface().updateDataI(player, true, "channel_world");
				player.spigot().sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+scc+"channel.msg02")
						.replaceAll("%channel%", "World")));
			}
			return true;
		} else if("group".equalsIgnoreCase(args[0]) || "gruppe".equalsIgnoreCase(args[0]))
		{
			if(!player.hasPermission("scc.channels.group"))
			{
				player.spigot().sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+scc+"msg02")));
				return false;
			}
			if(plugin.getUtility().rightArgs(player,args,1))
			{
				return false;
			}
			if(((boolean) plugin.getMysqlInterface().getDataI(player, "channel_group", "player_uuid")))
			{
				plugin.getMysqlInterface().updateDataI(player, false, "channel_group");
				player.spigot().sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+scc+"channel.msg01")
						.replaceAll("%channel%", "Group")));
			} else
			{
				plugin.getMysqlInterface().updateDataI(player, true, "channel_group");
				player.spigot().sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+scc+"channel.msg02")
						.replaceAll("%channel%", "Group")));
			}
			return true;
		} else if("custom".equalsIgnoreCase(args[0]))
		{
			if(!player.hasPermission("scc.channels.custom"))
			{
				player.spigot().sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+scc+"msg02")));
				return false;
			}
			if(plugin.getUtility().rightArgs(player,args,1))
			{
				return false;
			}
			if((boolean) plugin.getMysqlInterface().getDataI(player, "channel_custom", "player_uuid"))
			{
				plugin.getMysqlInterface().updateDataI(player, false, "channel_custom");
				player.spigot().sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+scc+"channel.msg01")
						.replaceAll("%channel%", "Custom")));
			} else
			{
				plugin.getMysqlInterface().updateDataI(player, true, "channel_custom");
				player.spigot().sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+scc+"channel.msg02")
						.replaceAll("%channel%", "Custom")));
			}
			return true;
		} else if("spy".equalsIgnoreCase(args[0]) || "spitzeln".equalsIgnoreCase(args[0]))
		{
			if(!player.hasPermission("scc.option.spy"))
			{
				player.spigot().sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+scc+"msg02")));
				return false;
			}
			if(plugin.getUtility().rightArgs(player,args,1))
			{
				return false;
			}
			if(((boolean) plugin.getMysqlInterface().getDataI(player, "spy", "player_uuid")))
			{
				plugin.getMysqlInterface().updateDataI(player, false, "spy");
				player.spigot().sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+scc+"channel.msg01")
						.replaceAll("%channel%", "Spy")));
			} else
			{
				plugin.getMysqlInterface().updateDataI(player, true, "spy");
				player.spigot().sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+scc+"channel.msg02")
						.replaceAll("%channel%", "Spy")));
			}
			return true;
		} else if("join".equalsIgnoreCase(args[0]) 
				|| "joinmessage".equalsIgnoreCase(args[0])
				|| "Eintrittsnachricht".equalsIgnoreCase(args[0]))
			//--------------------------------------------------joinmessage
		{
			if(!player.hasPermission("scc.option.join"))
			{
				player.spigot().sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+scc+"msg02")));
				return false;
			}
			if(plugin.getUtility().rightArgs(player,args,1))
			{
				return false;
			}
			if(((boolean) plugin.getMysqlInterface().getDataI(player, "joinmessage", "player_uuid")))
			{
				plugin.getMysqlInterface().updateDataI(player, false, "joinmessage");
				player.spigot().sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+scc+"channel.msg01")
						.replaceAll("%channel%", "Join Message")));
			} else
			{
				plugin.getMysqlInterface().updateDataI(player, true, "joinmessage");
				player.spigot().sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+scc+"channel.msg02")
						.replaceAll("%channel%", "Join Message")));
			}
			return true;
		} else if("ignorelist".equalsIgnoreCase(args[0]) || "ignorierliste".equalsIgnoreCase(args[0]))
		{
			if(!player.hasPermission("scc.cmd.ignorelist"))
			{
				player.spigot().sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+scc+"msg02")));
				return false;
			}
			if(plugin.getUtility().rightArgs(player,args,0))
			{
				return false;
			}
			String list = plugin.getMysqlInterface().getIgnoreList(player, "ignore_name", "player_uuid");
			if(list == null)
			{
				list = "None";
			}
			player.spigot().sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+scc+"ignore.msg03")
					.replaceAll("%il%", list)));
			return true;
			//--------------------------------------------------bungee Auf Bungee und Spigot Server
		} else if("bungee".equalsIgnoreCase(args[0]))
		{
			if(plugin.getYamlHandler().get().getString("bungee").equals("true"))
    		{
    			return false;
    		}
			if(!player.hasPermission("scc.cmd.bungee"))
			{
				player.spigot().sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+scc+"msg02")));
				return false;
			}
			if(plugin.getUtility().rightArgs(player,args,1))
			{
				return false;
			}
			if(plugin.getYamlHandler().get().getString("bungee").equals("true"))
			{
				plugin.getYamlHandler().get().set("bungee", "false");
				plugin.getYamlHandler().saveConfig();
				player.spigot().sendMessage(plugin.getUtility().tcl(
						plugin.getYamlHandler().getL().getString(language+".CMD_SCCB.serverlistener.msg01")));
				return true;
			} else if(plugin.getYamlHandler().get().getString("bungee").equals("false"))
			{
				plugin.getYamlHandler().get().set("bungee", "true");
				plugin.getYamlHandler().saveConfig();
				player.spigot().sendMessage(plugin.getUtility().tcl(
						plugin.getYamlHandler().getL().getString(language+".CMD_SCCB.serverlistener.msg02")));
				return true;
			} else
			{
				plugin.getYamlHandler().get().set("bungee", "false");
				plugin.getYamlHandler().saveConfig();
				player.spigot().sendMessage(plugin.getUtility().tcl(
						plugin.getYamlHandler().getL().getString(language+".CMD_SCCB.serverlistener.msg01")));
				return true;
			}
		} else if("mute".equalsIgnoreCase(args[0]) || "verstummen".equalsIgnoreCase(args[0]))
		{
			if(!player.hasPermission("scc.cmd.mute"))
			{
				player.spigot().sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+scc+"msg02")));
				return false;
			}
			if(args.length == 2)
			{
				String target = args[1];
    			if(Bukkit.getPlayer(target)== null)
    			{
    				player.spigot().sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+scc+"msg03")));
    				return false;
    			}
    			Player t = Bukkit.getPlayer(target);
    			plugin.getMysqlInterface().updateDataI(player, false, "can_chat");
    			plugin.getMysqlInterface().updateDataI(player, 0L, "mutetime");
    			t.spigot().sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+scc+"mute.msg01")));
    			return true;
			} else if(args.length == 3)
			{
				String target = args[1];
    			if(Bukkit.getPlayer(target)== null)
    			{
    				player.spigot().sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+scc+"msg03")));
    				return false;
    			}
    			Player t = Bukkit.getPlayer(target);
    			int num = 0;
    			try{
    				  num = Integer.parseInt(args[2]);
    			} catch (NumberFormatException e) {
    				  e.printStackTrace();
    			}
    			Long time = 60L*1000;
    			plugin.getMysqlInterface().updateDataI(player, false, "can_chat");
    			Long mutetime = System.currentTimeMillis()+num*time;
    			plugin.getMysqlInterface().updateDataI(player, mutetime, "mutetime");
    			t.spigot().sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+scc+"mute.msg02")
    					.replaceAll("%time%", args[2])));
    			return true;
			} else if(plugin.getUtility().rightArgs(player,args,3))
			{
				return false;
			}
		} else if("unmute".equalsIgnoreCase(args[0]))
		{
			if(!player.hasPermission("scc.cmd.unmute"))
			{
				player.spigot().sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+scc+"msg02")));
				return false;
			}
			if(plugin.getUtility().rightArgs(player,args,2))
			{
				return false;
			}
			String target = args[1];
			if(Bukkit.getPlayer(target)== null)
			{
				player.spigot().sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+scc+"msg03")));
				return false;
			}
			Player t = Bukkit.getPlayer(target);
			plugin.getMysqlInterface().updateDataI(player, true, "can_chat");
			plugin.getMysqlInterface().updateDataI(player, 0L, "mutetime");
			t.spigot().sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+scc+"mute.msg03")));
		} else if("ignore".equalsIgnoreCase(args[0]) || "ignorieren".equalsIgnoreCase(args[0]))
		{
			if(!player.hasPermission("scc.cmd.ignore"))
			{
				player.spigot().sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+scc+"msg02")));
				return false;
			}
			if(plugin.getUtility().rightArgs(player,args,2))
			{
				return false;
			}
			String target = args[1];
			if(Bukkit.getPlayer(target)== null)
			{
				player.spigot().sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+scc+"msg03")));
				return false;
			}
			Player t = Bukkit.getPlayer(target);
			if(plugin.getMysqlInterface().existIgnore(player, t.getUniqueId().toString()))
			{
				plugin.getMysqlInterface().deleteDataII(player.getUniqueId().toString(), t.getUniqueId().toString(), "player_uuid", "ignore_uuid");
				player.spigot().sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+scc+"ignore.msg02")
						.replaceAll("%player%", target)));
				return true;
			} else
			{
				plugin.getMysqlInterface().createIgnore(player, t);
				player.spigot().sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+scc+"ignore.msg01")
						.replaceAll("%player%", target)));
				return true;
			}
		} else if("wordfilter".equalsIgnoreCase(args[0]) || "wortfilter".equalsIgnoreCase(args[0]))
		{
			if(!player.hasPermission("scc.cmd.wordfilter"))
			{
				player.spigot().sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+scc+"msg02")));
				return false;
			}
			if(plugin.getUtility().rightArgs(player,args,3))
			{
				return false;
			}
			if(args[1].equals("add"))
			{
				List<String> wordfilter= plugin.getYamlHandler().get().getStringList("wordfilter");
				String word = args[2];
				if(wordfilter.contains(word))
				{
					player.spigot().sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+scc+"wordfilter.msg01")));
					return false;
				} else
				{
					wordfilter.add(word);
					plugin.getYamlHandler().get().set("wordfilter", wordfilter);
					plugin.getYamlHandler().saveConfig();
					player.spigot().sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+scc+"wordfilter.msg02")
							.replaceAll("%word%", word)));
					return true;
				}
			} else if(args[1].equals("remove"))
			{
				List<String> wordfilter= plugin.getYamlHandler().get().getStringList("wordfilter");
				String word = args[2];
				if(wordfilter.contains(word))
				{
					wordfilter.remove(word);
					plugin.getYamlHandler().get().set("wordfilter", wordfilter);
					plugin.getYamlHandler().saveConfig();
					player.spigot().sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+scc+"wordfilter.msg04")
							.replaceAll("%word%", word)));
					return true;
				} else
				{
					player.spigot().sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+scc+"wordfilter.msg03")));
					return false;
				}
			}
		} else if("broadcast".equals(args[0]))
    	{
    		if(!player.hasPermission("scc.cmd.broadcast"))
    		{
    			player.spigot().sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+scc+"msg02")));
    			return false;
    		}
    		String msg = "";
    		for (int i = 1; i < args.length; i++) 
	        {
    			msg += args[i] + " ";
	        }
    		if(plugin.getUtility().getWordfilter(msg))
    		{
    			player.spigot().sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+".EVENT_Chat.msg06")));
    			return false;
    		}
    		for(Player all : Bukkit.getOnlinePlayers())
    		{
    			all.spigot().sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+scc+"broadcast.msg20")
    					.replaceAll("%msg%", plugin.getUtility().MsgLater(player, 0, "global", msg))));
    		}
    		return true;
    	} else if("cccreate".equalsIgnoreCase(args[0])) 
    	{
    		if(!player.hasPermission("scc.cmd.cc.channel"))
    		{
    			player.spigot().sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+scc+"msg02")));
    			return false;
    		}
    		CustomChannel cc = CustomChannel.getCustomChannel(player);
    		if(cc!=null)
			{
				player.spigot().sendMessage(plugin.getUtility().tc(plugin.getUtility().tl(
						plugin.getYamlHandler().getL().getString(language+scc+"createchannel.msg01")
						.replaceAll("%channel%", cc.getName()))));
				return false;
			}
    		if(args.length==2)
    		{
    			String name = args[1];
        		ArrayList<Player> members = new ArrayList<Player>();
        		members.add(player);
        		cc = new CustomChannel(name, player, members, null, new ArrayList<Player>());
        		CustomChannel.addCustomChannel(cc);
        		player.spigot().sendMessage(plugin.getUtility().tc(plugin.getUtility().tl(
    					plugin.getYamlHandler().getL().getString(language+scc+"createchannel.msg02").replaceAll("%channel%", cc.getName()))));
    		} else if(args.length==3)
    		{
    			String name = args[1];
    			String password = args[2];
        		ArrayList<Player> members = new ArrayList<Player>();
        		members.add(player);
        		cc = new CustomChannel(name, player, members, null, new ArrayList<Player>());
        		CustomChannel.addCustomChannel(cc);
        		player.spigot().sendMessage(plugin.getUtility().tc(plugin.getUtility().tl(
    					plugin.getYamlHandler().getL().getString(language+scc+"createchannel.msg03")
    					.replaceAll("%channel%", cc.getName())
    					.replaceAll("%password%", password))));
    		} else
    		{
    			plugin.getUtility().rightArgs(player,args,3);
    		}
			return true;
    	} else if("ccjoin".equalsIgnoreCase(args[0]) || "join".equalsIgnoreCase(args[0])) 
    	{
    		if(!player.hasPermission("scc.cmd.cc.join"))
    		{
    			player.spigot().sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+scc+"msg02")));
    			return false;
    		}
    		if(args.length==2)
    		{
    			String name = args[1];
    			CustomChannel cc = CustomChannel.getCustomChannel(name);
    			CustomChannel oldcc = CustomChannel.getCustomChannel(player);
    			if(oldcc!=null)
    			{
    				player.spigot().sendMessage(plugin.getUtility().tcl(
    						plugin.getYamlHandler().getL().getString(language+scc+"joinchannel.msg06")));
        			return false;
    			}
    			if(cc==null)
    			{
    				player.spigot().sendMessage(plugin.getUtility().tcl(
    						plugin.getYamlHandler().getL().getString(language+scc+"joinchannel.msg01")
    						.replaceAll("%name%", name)));
        			return false;
    			}
    			if(cc.getBanned().contains(player))
    			{
    				player.spigot().sendMessage(plugin.getUtility().tcl(
    						plugin.getYamlHandler().getL().getString(language+scc+"joinchannel.msg07")));
        			return false;
    			}
    			if(cc.getPassword()!=null)
    			{
    				player.spigot().sendMessage(plugin.getUtility().tcl(
    						plugin.getYamlHandler().getL().getString(language+scc+"joinchannel.msg02")));
        			return false;
    			}
    			cc.addMembers(player);
    			player.spigot().sendMessage(plugin.getUtility().tcl(
						plugin.getYamlHandler().getL().getString(language+scc+"joinchannel.msg05")
						.replaceAll("%channel%", cc.getName())));
    			return true;
    		} else if(args.length==3)
    		{
    			String name = args[1];
    			CustomChannel cc = CustomChannel.getCustomChannel(name);
    			CustomChannel oldcc = CustomChannel.getCustomChannel(player);
    			String password = args[2];
    			if(oldcc!=null)
    			{
    				player.spigot().sendMessage(plugin.getUtility().tcl(
    						plugin.getYamlHandler().getL().getString(language+scc+"joinchannel.msg06")));
        			return false;
    			}
    			if(cc==null)
    			{
    				player.spigot().sendMessage(plugin.getUtility().tcl(
    						plugin.getYamlHandler().getL().getString(language+scc+"joinchannel.msg01")
    						.replaceAll("%name%", name)));
        			return false;
    			}
    			if(cc.getBanned().contains(player))
    			{
    				player.spigot().sendMessage(plugin.getUtility().tcl(
    						plugin.getYamlHandler().getL().getString(language+scc+"joinchannel.msg07")));
        			return false;
    			}
    			if(cc.getPassword()==null)
    			{
    				player.spigot().sendMessage(plugin.getUtility().tcl(
    						plugin.getYamlHandler().getL().getString(language+scc+"joinchannel.msg04")));
        			return false;
    			}
    			if(!cc.getPassword().equals(password))
    			{
    				player.spigot().sendMessage(plugin.getUtility().tcl(
    						plugin.getYamlHandler().getL().getString(language+scc+"joinchannel.msg03")));
        			return false;
    			}
    			cc.addMembers(player);
    			player.spigot().sendMessage(plugin.getUtility().tcl(
						plugin.getYamlHandler().getL().getString(language+scc+"joinchannel.msg05")
						.replaceAll("%channel%", cc.getName())));
    			return false;
    		} else
    		{
    			plugin.getUtility().rightArgs(player,args,3);
    		}
    		return true;
    	} else if("ccleave".equalsIgnoreCase(args[0]) || "leave".equalsIgnoreCase(args[0])) 
    	{
    		if(!player.hasPermission("scc.cmd.cc.leave"))
    		{
    			player.spigot().sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+scc+"msg02")));
    			return false;
    		}
    		if(plugin.getUtility().rightArgs(player,args,1))
			{
				return false;
			}
    		CustomChannel cc = CustomChannel.getCustomChannel(player);
    		if(cc==null)
			{
				player.spigot().sendMessage(plugin.getUtility().tcl(
						plugin.getYamlHandler().getL().getString(language+scc+"leavechannel.msg01")));
    			return false;
			}
    		final String name = cc.getName();
    		cc.removeMembers(player);
    		if(cc.getCreator().getName().equals(player.getName()))
    		{
    			Player newcreator = null;
    			for(Player pp : cc.getMembers())
    			{
    				if(pp!=null)
    				{
    					newcreator = pp;
    				}
    			}
    			if(newcreator!=null)
    			{
    				cc.setCreator(newcreator);
        			newcreator.spigot().sendMessage(plugin.getUtility().tcl(
        					plugin.getYamlHandler().getL().getString(language+scc+"leavechannel.msg03")
        					.replaceAll("%channel%", cc.getName())));
    			} else 
    			{
    				CustomChannel.removeCustomChannel(cc);
    				cc = null;
    			}
    		}
    		player.spigot().sendMessage(plugin.getUtility().tcl(
					plugin.getYamlHandler().getL().getString(language+scc+"leavechannel.msg02")
					.replaceAll("%channel%", name)));
			return true;
    	} else if("cckick".equalsIgnoreCase(args[0])) 
    	{
    		if(!player.hasPermission("scc.cmd.cc.kick"))
    		{
    			player.spigot().sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+scc+"msg02")));
    			return false;
    		}
    		if(plugin.getUtility().rightArgs(player,args,2))
			{
				return false;
			}
    		CustomChannel cc = CustomChannel.getCustomChannel(player);
    		if(cc==null)
			{
				player.spigot().sendMessage(plugin.getUtility().tcl(
						plugin.getYamlHandler().getL().getString(language+scc+"leavechannel.msg01")));
    			return false;
			}
    		Player creator = cc.getCreator();
    		if(!creator.getName().equals(player.getName()))
    		{
    			player.spigot().sendMessage(plugin.getUtility().tcl(
						plugin.getYamlHandler().getL().getString(language+scc+"kickchannel.msg01")));
    			return false;
    		}
    		if(plugin.getServer().getPlayer(args[1])!=null)
    		{
    			player.spigot().sendMessage(plugin.getUtility().tcl(
						plugin.getYamlHandler().getL().getString(language+scc+"kickchannel.msg02")));
    			return false;
    		}
    		Player target = plugin.getServer().getPlayer(args[1]); 
    		if(target.getName().equals(player.getName()))
    		{
    			player.spigot().sendMessage(plugin.getUtility().tcl(
						plugin.getYamlHandler().getL().getString(language+scc+"kickchannel.msg06")));
    			return false;
    		}
    		cc.removeMembers(target);
    		target.spigot().sendMessage(plugin.getUtility().tcl(
					plugin.getYamlHandler().getL().getString(language+scc+"kickchannel.msg03")));
    		player.spigot().sendMessage(plugin.getUtility().tcl(
					plugin.getYamlHandler().getL().getString(language+scc+"kickchannel.msg04")
					.replaceAll("%player%", args[1])));
    		for(Player members : cc.getMembers())
    		{
    			members.spigot().sendMessage(plugin.getUtility().tcl(
    					plugin.getYamlHandler().getL().getString(language+scc+"kickchannel.msg05")
    					.replaceAll("%player%", args[1])));
    		}
			return true;
    	} else if("ccban".equalsIgnoreCase(args[0])) 
    	{
    		if(!player.hasPermission("scc.cmd.cc.ban"))
    		{
    			player.spigot().sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+scc+"msg02")));
    			return false;
    		}
    		if(plugin.getUtility().rightArgs(player,args,2))
			{
				return false;
			}
    		CustomChannel cc = CustomChannel.getCustomChannel(player);
    		if(cc==null)
			{
				player.spigot().sendMessage(plugin.getUtility().tcl(
						plugin.getYamlHandler().getL().getString(language+scc+"leavechannel.msg01")));
    			return false;
			}
    		Player creator = cc.getCreator();
    		if(!creator.getName().equals(player.getName()))
    		{
    			player.spigot().sendMessage(plugin.getUtility().tcl(
						plugin.getYamlHandler().getL().getString(language+scc+"kickchannel.msg01")));
    			return false;
    		}
    		if(plugin.getServer().getPlayer(args[1])!=null)
    		{
    			player.spigot().sendMessage(plugin.getUtility().tcl(
						plugin.getYamlHandler().getL().getString(language+scc+"kickchannel.msg02")));
    			return false;
    		}
    		Player target = plugin.getServer().getPlayer(args[1]);
    		if(target.getName().equals(player.getName()))
    		{
    			player.spigot().sendMessage(plugin.getUtility().tcl(
						plugin.getYamlHandler().getL().getString(language+scc+"banchannel.msg04")));
    			return false;
    		}
    		if(cc.getBanned().contains(target))
    		{
    			player.spigot().sendMessage(plugin.getUtility().tcl(
						plugin.getYamlHandler().getL().getString(language+scc+"banchannel.msg01")));
    			return false;
    		}
    		cc.addBanned(target);
    		player.spigot().sendMessage(plugin.getUtility().tcl(
					plugin.getYamlHandler().getL().getString(language+scc+"banchannel.msg02")
					.replaceAll("%player%", args[1])));
    		target.spigot().sendMessage(plugin.getUtility().tcl(
					plugin.getYamlHandler().getL().getString(language+scc+"banchannel.msg03")
					.replaceAll("%channel%", cc.getName())));
    		for(Player members : cc.getMembers())
    		{
    			members.spigot().sendMessage(plugin.getUtility().tcl(
    					plugin.getYamlHandler().getL().getString(language+scc+"banchannel.msg04")
    					.replaceAll("%player%", args[1])));
    		}
			return true;
    	} else if("ccunban".equalsIgnoreCase(args[0])) 
    	{
    		if(!player.hasPermission("scc.cmd.cc.unban"))
    		{
    			player.spigot().sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+scc+"msg02")));
    			return false;
    		}
    		if(plugin.getUtility().rightArgs(player,args,2))
			{
				return false;
			}
    		CustomChannel cc = CustomChannel.getCustomChannel(player);
    		if(cc==null)
			{
				player.spigot().sendMessage(plugin.getUtility().tcl(
						plugin.getYamlHandler().getL().getString(language+scc+"leavechannel.msg01")));
    			return false;
			}
    		Player creator = cc.getCreator();
    		if(!creator.getName().equals(player.getName()))
    		{
    			player.spigot().sendMessage(plugin.getUtility().tcl(
						plugin.getYamlHandler().getL().getString(language+scc+"kickchannel.msg01")));
    			return false;
    		}
    		if(plugin.getServer().getPlayer(args[1])!=null)
    		{
    			player.spigot().sendMessage(plugin.getUtility().tcl(
						plugin.getYamlHandler().getL().getString(language+scc+"kickchannel.msg02")));
    			return false;
    		}
    		Player target = plugin.getServer().getPlayer(args[1]); 
    		if(!cc.getBanned().contains(target))
    		{
    			player.spigot().sendMessage(plugin.getUtility().tcl(
						plugin.getYamlHandler().getL().getString(language+scc+"unbanchannel.msg01")));
    			return false;
    		}
    		cc.removeBanned(target);
    		player.spigot().sendMessage(plugin.getUtility().tcl(
					plugin.getYamlHandler().getL().getString(language+scc+"unbanchannel.msg02")
					.replaceAll("%player%", target.getName())));
    		for(Player members : cc.getMembers())
    		{
    			members.spigot().sendMessage(plugin.getUtility().tcl(
    					plugin.getYamlHandler().getL().getString(language+scc+"unbanchannel.msg03")
    					.replaceAll("%player%", target.getName())));
    		}
			return true;
    	} else if("ccchangepassword".equalsIgnoreCase(args[0])) 
    	{
    		if(!player.hasPermission("scc.cmd.cc.changepassword"))
    		{
    			player.spigot().sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+scc+"msg02")));
    			return false;
    		}
    		if(plugin.getUtility().rightArgs(player,args,2))
			{
				return false;
			}
    		CustomChannel cc = CustomChannel.getCustomChannel(player);
    		if(cc==null)
			{
				player.spigot().sendMessage(plugin.getUtility().tcl(
						plugin.getYamlHandler().getL().getString(language+scc+"leavechannel.msg01")));
    			return false;
			}
    		Player creator = cc.getCreator();
    		if(!creator.getName().equals(player.getName()))
    		{
    			player.spigot().sendMessage(plugin.getUtility().tcl(
						plugin.getYamlHandler().getL().getString(language+scc+"kickchannel.msg01")));
    			return false;
    		}
    		cc.setPassword(args[1]);
    		player.spigot().sendMessage(plugin.getUtility().tcl(
					plugin.getYamlHandler().getL().getString(language+scc+"changepassword.msg01")
					.replaceAll("%password%", args[1])));
			return true;
    	} else
		{
			TextComponent msg = plugin.getUtility().tc(plugin.getUtility().tl(plugin.getYamlHandler().getL().getString(language+scc+"msg01")));
			msg.setClickEvent( new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/scc"));
			player.spigot().sendMessage(msg);
			return false;
		}
		return false;
	}
}
