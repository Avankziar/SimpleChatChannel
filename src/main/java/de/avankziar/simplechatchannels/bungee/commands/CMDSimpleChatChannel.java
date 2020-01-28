package main.java.de.avankziar.simplechatchannels.bungee.commands;

import java.util.ArrayList;
import java.util.List;

import main.java.de.avankziar.simplechatchannels.bungee.SimpleChatChannels;
import main.java.de.avankziar.simplechatchannels.bungee.interfaces.CustomChannel;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class CMDSimpleChatChannel extends Command
{
	private SimpleChatChannels plugin;
	private String scc = ".CMD_SCC.";
	
	public CMDSimpleChatChannel(SimpleChatChannels plugin)
	{
        super("scc",null,"simplechatchannel");
        this.plugin = plugin;
    }
	
    public void execute(CommandSender sender, String[] args)
    {
    	if(!(sender instanceof ProxiedPlayer))
    	{
    		return;
    	}
    	ProxiedPlayer player = (ProxiedPlayer) sender;
    	String language = plugin.getYamlHandler().get().getString("language");
    	if(args.length == 0)
    	{
    		if(player.hasPermission("scc.admin"))
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
    			player.sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+scc+"info.msg10")));
    			player.sendMessage(msg1);
    			player.sendMessage(msg2);
    			player.sendMessage(msg3);
    			player.sendMessage(msg4);
    			player.sendMessage(msg5);
    			player.sendMessage(msg6);
    			player.sendMessage(msg7);
    			player.sendMessage(msg8);
    			player.sendMessage(msg10);
    			player.sendMessage(msg11);
    			player.sendMessage(msg12);
    			player.sendMessage(msg13);
    			player.sendMessage(msg14);
    			player.sendMessage(msg15);
    			player.sendMessage(msg16);
    			player.sendMessage(msg17);
    			player.sendMessage(msg18);
    			player.sendMessage(msg19);
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
    			player.sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+scc+"info.msg10")));
    			player.sendMessage(msg1);
    			player.sendMessage(msg4);
    			player.sendMessage(msg5);
    			player.sendMessage(msg11);
    			player.sendMessage(msg11);
    			player.sendMessage(msg12);
    			player.sendMessage(msg13);
    			player.sendMessage(msg14);
    			player.sendMessage(msg15);
    			player.sendMessage(msg16);
    			player.sendMessage(msg17);
    			player.sendMessage(msg18);
    			player.sendMessage(msg19);
    		}
    	} else if("playerlist".equalsIgnoreCase(args[0]) || "pl".equalsIgnoreCase(args[0]) 
    			|| "spielerlist".equalsIgnoreCase(args[0]) || "spielerlist".equalsIgnoreCase(args[0])) 
    	{
    		if(!player.hasPermission("scc.cmd.playerlist"))
			{
				player.sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+scc+"msg02")));
				return;
			}
			TextComponent MSG = plugin.getUtility().tc("");
			List<BaseComponent> list = new ArrayList<>();
    		if(args.length==1)
    		{
    			for(ProxiedPlayer pp : plugin.getProxy().getPlayers())
        		{
    				TextComponent prefix = plugin.getUtility().tcl("&e"+pp.getName()+" ");
    				prefix.setClickEvent( new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND,"@"+pp.getName()+" "));
    				list.add(prefix);
        		}
    			if(list.isEmpty())
    			{
    				player.sendMessage(plugin.getUtility().tcl(
    						plugin.getYamlHandler().getL().getString(language+scc+"playerlist.msg02")));
    				return;
    			}
    			MSG.setExtra(list);
    			player.sendMessage(plugin.getUtility().tcl(
    					plugin.getYamlHandler().getL().getString(language+scc+"playerlist.msg01")));
    			player.sendMessage(MSG);
    			return;
    		} else if(args.length==2)
    		{
    			String s = args[1];
    			for(ProxiedPlayer pp : plugin.getProxy().getPlayers())
        		{
    				if(pp.getName().contains(s))
    				{
    					TextComponent prefix = plugin.getUtility().tcl("&e"+pp.getName()+" ");
        				prefix.setClickEvent( new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND,"@"+pp.getName()+" "));
        				list.add(prefix);
    				}
        		}
    			if(list.isEmpty())
    			{
    				player.sendMessage(plugin.getUtility().tcl(
    						plugin.getYamlHandler().getL().getString(language+scc+"playerlist.msg02")));
    				return;
    			}
    			MSG.setExtra(list);
    			player.sendMessage(plugin.getUtility().tcl(
    					plugin.getYamlHandler().getL().getString(language+scc+"playerlist.msg01")));
    			player.sendMessage(MSG);
    		} else
    		{
    			plugin.getUtility().rightArgs(player,args,2);
    			return;
    		}
    	} else if("grouplist".equalsIgnoreCase(args[0]) || "gl".equalsIgnoreCase(args[0])) 
    	{
    		if(!player.hasPermission("scc.cmd.grouplist"))
			{
				player.sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+scc+"msg02")));
				return;
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
    				prefix.setClickEvent( new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND,"@*"+g+" "));
    				list.add(prefix);
        		}
    			if(list.isEmpty())
    			{
    				player.sendMessage(plugin.getUtility().tcl(
    						plugin.getYamlHandler().getL().getString(language+scc+"grouplist.msg02")));
    				return;
    			}
    			MSG.setExtra(list);
    			player.sendMessage(plugin.getUtility().tcl(
    					plugin.getYamlHandler().getL().getString(language+scc+"grouplist.msg01")));
    			player.sendMessage(MSG);
    			return;
    		} else if(args.length==2)
    		{
    			String s = args[1];
    			for(String g : groups)
        		{
    				if(s.contains(g))
    				{
    					TextComponent prefix = plugin.getUtility().tcl("&6"+g+" ");
        				prefix.setClickEvent( new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND,"@*"+g+" "));
        				list.add(prefix);	
    				}
        		}
    			if(list.isEmpty())
    			{
    				player.sendMessage(plugin.getUtility().tcl(
    						plugin.getYamlHandler().getL().getString(language+scc+"playerlist.msg02")));
    				return;
    			}
    			MSG.setExtra(list);
    			player.sendMessage(plugin.getUtility().tcl(
    					plugin.getYamlHandler().getL().getString(language+scc+"grouplist.msg01")));
    			player.sendMessage(MSG);
    		} else
    		{
    			plugin.getUtility().rightArgs(player,args,2);
    			return;
    		}
    	} else if("global".equalsIgnoreCase(args[0]))
		{
			if(!player.hasPermission("scc.channels.global"))
			{
				player.sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+scc+"msg02")));
				return;
			}
			if(plugin.getUtility().rightArgs(player,args,1))
			{
				return;
			}
			if((boolean) plugin.getMysqlInterface().getDataI(player, "channel_global", "player_uuid"))
			{
				plugin.getMysqlInterface().updateDataI(player, false, "channel_global");
				player.sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+scc+"channel.msg01")
						.replaceAll("%channel%", "Global")));
			} else
			{
				plugin.getMysqlInterface().updateDataI(player, true, "channel_global");
				player.sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+scc+"channel.msg02")
						.replaceAll("%channel%", "Global")));
			}
		} else if("trade".equalsIgnoreCase(args[0]) || "handel".equalsIgnoreCase(args[0]))
		{
			if(!player.hasPermission("scc.channels.trade"))
			{
				player.sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+scc+"msg02")));
				return;
			}
			if(plugin.getUtility().rightArgs(player,args,1))
			{
				return;
			}
			if((boolean) plugin.getMysqlInterface().getDataI(player, "channel_trade", "player_uuid"))
			{
				plugin.getMysqlInterface().updateDataI(player, false, "channel_trade");
				player.sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+scc+"channel.msg01")
						.replaceAll("%channel%", "Trade")));
			} else
			{
				plugin.getMysqlInterface().updateDataI(player, true, "channel_trade");
				player.sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+scc+"channel.msg02")
						.replaceAll("%channel%", "Trade")));
			}
		} else if("auction".equalsIgnoreCase(args[0]) || "auktion".equalsIgnoreCase(args[0]))
		{
			if(!player.hasPermission("scc.channels.auction"))
			{
				player.sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+scc+"msg02")));
				return;
			}
			if(plugin.getUtility().rightArgs(player,args,1))
			{
				return;
			}
			if((boolean) plugin.getMysqlInterface().getDataI(player, "channel_auction", "player_uuid"))
			{
				plugin.getMysqlInterface().updateDataI(player, false, "channel_auction");
				player.sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+scc+"channel.msg01")
						.replaceAll("%channel%", "Auction")));
			} else
			{
				plugin.getMysqlInterface().updateDataI(player, true, "channel_auction");
				player.sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+scc+"channel.msg02")
						.replaceAll("%channel%", "Auction")));
			}
		} else if("support".equalsIgnoreCase(args[0]) || "hilfe".equalsIgnoreCase(args[0]))
		{
			if(!player.hasPermission("scc.channels.support"))
			{
				player.sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+scc+"msg02")));
				return;
			}
			if(plugin.getUtility().rightArgs(player,args,1))
			{
				return;
			}
			if(((boolean) plugin.getMysqlInterface().getDataI(player, "channel_support", "player_uuid")))
			{
				plugin.getMysqlInterface().updateDataI(player, false, "channel_support");
				player.sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+scc+"channel.msg01")
						.replaceAll("%channel%", "Support")));
			} else
			{
				plugin.getMysqlInterface().updateDataI(player, true, "channel_support");
				player.sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+scc+"channel.msg02")
						.replaceAll("%channel%", "Support")));
			}
		} else if("team".equalsIgnoreCase(args[0]))
		{
			if(!player.hasPermission("scc.channels.team"))
			{
				player.sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+scc+"msg02")));
				return;
			}
			if(plugin.getUtility().rightArgs(player,args,1))
			{
				return;
			}
			if(((boolean) plugin.getMysqlInterface().getDataI(player, "channel_team", "player_uuid")))
			{
				plugin.getMysqlInterface().updateDataI(player, false, "channel_team");
				player.sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+scc+"channel.msg01")
						.replaceAll("%channel%", "Team")));
			} else
			{
				plugin.getMysqlInterface().updateDataI(player, true, "channel_team");
				player.sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+scc+"channel.msg02")
						.replaceAll("%channel%", "Team")));
			}
		} else if("admin".equalsIgnoreCase(args[0]))
		{
			if(!player.hasPermission("scc.channels.admin"))
			{
				player.sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+scc+"msg02")));
				return;
			}
			if(plugin.getUtility().rightArgs(player,args,1))
			{
				return;
			}
			if(((boolean) plugin.getMysqlInterface().getDataI(player, "channel_admin", "player_uuid")))
			{
				plugin.getMysqlInterface().updateDataI(player, false, "channel_admin");
				player.sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+scc+"channel.msg01")
						.replaceAll("%channel%", "Admin")));
			} else
			{
				plugin.getMysqlInterface().updateDataI(player, true, "channel_admin");
				player.sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+scc+"channel.msg02")
						.replaceAll("%channel%", "Admin")));
			}
		} else if("pm".equalsIgnoreCase(args[0]) || "pn".equalsIgnoreCase(args[0]))
		{
			if(!player.hasPermission("scc.channels.pm"))
			{
				player.sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+scc+"msg02")));
				return;
			}
			if(plugin.getUtility().rightArgs(player,args,1))
			{
				return;
			}
			if(((boolean) plugin.getMysqlInterface().getDataI(player, "channel_pm", "player_uuid")))
			{
				plugin.getMysqlInterface().updateDataI(player, false, "channel_pm");
				player.sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+scc+"channel.msg01")
						.replaceAll("%channel%", "Privat Message")));
			} else
			{
				plugin.getMysqlInterface().updateDataI(player, true, "channel_pm");
				player.sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+scc+"channel.msg02")
						.replaceAll("%channel%", "Private Message")));
			}
		} else if("group".equalsIgnoreCase(args[0]) || "gruppe".equalsIgnoreCase(args[0]))
		{
			if(!player.hasPermission("scc.channels.group"))
			{
				player.sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+scc+"msg02")));
				return;
			}
			if(plugin.getUtility().rightArgs(player,args,1))
			{
				return;
			}
			if(((boolean) plugin.getMysqlInterface().getDataI(player, "channel_group", "player_uuid")))
			{
				plugin.getMysqlInterface().updateDataI(player, false, "channel_group");
				player.sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+scc+"channel.msg01")
						.replaceAll("%channel%", "Group")));
			} else
			{
				plugin.getMysqlInterface().updateDataI(player, true, "channel_group");
				player.sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+scc+"channel.msg02")
						.replaceAll("%channel%", "Group")));
			}
		} else if("custom".equalsIgnoreCase(args[0]))
		{
			if(!player.hasPermission("scc.channels.custom"))
			{
				player.sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+scc+"msg02")));
				return;
			}
			if(plugin.getUtility().rightArgs(player,args,1))
			{
				return;
			}
			if((boolean) plugin.getMysqlInterface().getDataI(player, "channel_custom", "player_uuid"))
			{
				plugin.getMysqlInterface().updateDataI(player, false, "channel_custom");
				player.sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+scc+"channel.msg01")
						.replaceAll("%channel%", "Custom")));
			} else
			{
				plugin.getMysqlInterface().updateDataI(player, true, "channel_custom");
				player.sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+scc+"channel.msg02")
						.replaceAll("%channel%", "Custom")));
			}
		} else if("spy".equalsIgnoreCase(args[0]) || "spitzeln".equalsIgnoreCase(args[0]))
		{
			if(!player.hasPermission("scc.spy"))
			{
				player.sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+scc+"msg02")));
				return;
			}
			if(plugin.getUtility().rightArgs(player,args,1))
			{
				return;
			}
			if(((boolean) plugin.getMysqlInterface().getDataI(player, "spy", "player_uuid")))
			{
				plugin.getMysqlInterface().updateDataI(player, false, "spy");
				player.sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+scc+"channel.msg01")
						.replaceAll("%channel%", "Spy")));
			} else
			{
				plugin.getMysqlInterface().updateDataI(player, true, "spy");
				player.sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+scc+"channel.msg02")
						.replaceAll("%channel%", "Spy")));
			}
		} else if("join".equalsIgnoreCase(args[0]) 
				|| "joinmessage".equalsIgnoreCase(args[0])
				|| "Eintrittsnachricht".equalsIgnoreCase(args[0]))
			//--------------------------------------------------joinmessage
		{
			if(!player.hasPermission("scc.join"))
			{
				player.sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+scc+"msg02")));
				return;
			}
			if(plugin.getUtility().rightArgs(player,args,1))
			{
				return;
			}
			if(((boolean) plugin.getMysqlInterface().getDataI(player, "joinmessage", "player_uuid")))
			{
				plugin.getMysqlInterface().updateDataI(player, false, "joinmessage");
				player.sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+scc+"channel.msg01")
						.replaceAll("%channel%", "Join Message")));
			} else
			{
				plugin.getMysqlInterface().updateDataI(player, true, "joinmessage");
				player.sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+scc+"channel.msg02")
						.replaceAll("%channel%", "Join Message")));
			}
		} else if("ignorelist".equalsIgnoreCase(args[0]) || "ignorierliste".equalsIgnoreCase(args[0]))
		{
			if(!player.hasPermission("scc.cmd.ignorelist"))
			{
				player.sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+scc+"msg02")));
				return;
			}
			if(plugin.getUtility().rightArgs(player,args,1))
			{
				return;
			}
			String list = plugin.getMysqlInterface().getIgnoreList(player, "ignore_name", "player_uuid");
			if(list == null)
			{
				list = "None";
			}
			player.sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+scc+"ignore.msg03")
					.replaceAll("%il%", list)));
			return;
			//--------------------------------------------------bungee Auf Bungee und Spigot Server
		} else if("bungee".equalsIgnoreCase(args[0]))
		{
			if(!player.hasPermission("scc.cmd.bungee"))
			{
				player.sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+scc+"msg02")));
				return;
			}
			if(plugin.getUtility().rightArgs(player,args,1))
			{
				return;
			}
			plugin.getUtility().sendMessage(player.getServer(),"simplechatchannels:scc", "bungeeswitch");
		} else if("mute".equalsIgnoreCase(args[0]) || "verstummen".equalsIgnoreCase(args[0]))
		{
			if(!player.hasPermission("scc.cmd.mute"))
			{
				player.sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+scc+"msg02")));
				return;
			}
			if(args.length == 2)
			{
				String target = args[1];
    			if(ProxyServer.getInstance().getPlayer(target)== null)
    			{
    				player.sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+scc+"msg03")));
    				return;
    			}
    			ProxiedPlayer t = ProxyServer.getInstance().getPlayer(target);
    			plugin.getMysqlInterface().updateDataI(player, false, "can_chat");
    			plugin.getMysqlInterface().updateDataI(player, 0L, "mutetime");
    			t.sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+scc+"mute.msg01")));
			} else if(args.length == 3)
			{
				String target = args[1];
    			if(ProxyServer.getInstance().getPlayer(target)== null)
    			{
    				player.sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+scc+"msg03")));
    				return;
    			}
    			ProxiedPlayer t = ProxyServer.getInstance().getPlayer(target);
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
    			t.sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+scc+"mute.msg02")
    					.replaceAll("%time%", args[2])));
			} else if(plugin.getUtility().rightArgs(player,args,3))
			{
				return;
			}
		} else if("unmute".equalsIgnoreCase(args[0]))
		{
			if(!player.hasPermission("scc.cmd.unmute"))
			{
				player.sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+scc+"msg02")));
				return;
			}
			if(plugin.getUtility().rightArgs(player,args,2))
			{
				return;
			}
			String target = args[1];
			if(ProxyServer.getInstance().getPlayer(target)== null)
			{
				player.sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+scc+"msg03")));
				return;
			}
			ProxiedPlayer t = ProxyServer.getInstance().getPlayer(target);
			plugin.getMysqlInterface().updateDataI(player, true, "can_chat");
			plugin.getMysqlInterface().updateDataI(player, 0L, "mutetime");
			t.sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+scc+"mute.msg03")));
		} else if("ignore".equalsIgnoreCase(args[0]) || "ignorieren".equalsIgnoreCase(args[0]))
		{
			if(!player.hasPermission("scc.cmd.ignore"))
			{
				player.sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+scc+"msg02")));
				return;
			}
			if(plugin.getUtility().rightArgs(player,args,2))
			{
				return;
			}
			String target = args[1];
			if(ProxyServer.getInstance().getPlayer(target)== null)
			{
				player.sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+scc+"msg03")));
				return;
			}
			ProxiedPlayer t = ProxyServer.getInstance().getPlayer(target);
			if(plugin.getMysqlInterface().existIgnore(player, t.getUniqueId().toString()))
			{
				plugin.getMysqlInterface().deleteDataII(
						player.getUniqueId().toString(), t.getUniqueId().toString(), "player_uuid", "ignore_uuid");
				player.sendMessage(plugin.getUtility().tcl(
						plugin.getYamlHandler().getL().getString(language+scc+"ignore.msg02")
						.replaceAll("%player%", target)));
			} else
			{
				plugin.getMysqlInterface().createIgnore(player, t);
				player.sendMessage(plugin.getUtility().tcl(
						plugin.getYamlHandler().getL().getString(language+scc+"ignore.msg01")
						.replaceAll("%player%", target)));
			}
		} else if("wordfilter".equalsIgnoreCase(args[0]) || "wortfilter".equalsIgnoreCase(args[0]))
		{
			if(!player.hasPermission("scc.cmd.wordfilter"))
			{
				player.sendMessage(plugin.getUtility().tcl(
						plugin.getYamlHandler().getL().getString(language+scc+"msg02")));
				return;
			}
			if(plugin.getUtility().rightArgs(player,args,3))
			{
				return;
			}
			if(args[1].equals("add"))
			{
				List<String> wordfilter= plugin.getYamlHandler().get().getStringList("wordfilter");
				String word = args[2];
				if(wordfilter.contains(word))
				{
					player.sendMessage(plugin.getUtility().tcl(
							plugin.getYamlHandler().getL().getString(language+scc+"wordfilter.msg01")));
				} else
				{
					wordfilter.add(word);
					plugin.getYamlHandler().get().set("wordfilter", wordfilter);
					plugin.getYamlHandler().saveConfig();
					player.sendMessage(plugin.getUtility().tcl(
							plugin.getYamlHandler().getL().getString(language+scc+"wordfilter.msg02")
							.replaceAll("%word%", word)));
				}
				return;
			} else if(args[1].equals("remove"))
			{
				List<String> wordfilter= plugin.getYamlHandler().get().getStringList("wordfilter");
				String word = args[2];
				if(wordfilter.contains(word))
				{
					wordfilter.remove(word);
					plugin.getYamlHandler().get().set("wordfilter", wordfilter);
					plugin.getYamlHandler().saveConfig();
					player.sendMessage(plugin.getUtility().tcl(
							plugin.getYamlHandler().getL().getString(language+scc+"wordfilter.msg04")
							.replaceAll("%word%", word)));
				} else
				{
					player.sendMessage(plugin.getUtility().tcl(
							plugin.getYamlHandler().getL().getString(language+scc+"wordfilter.msg03")));
				}
				return;
			}
		} else if("broadcast".equals(args[0]))
    	{
    		if(!player.hasPermission("scc.cmd.broadcast"))
    		{
    			player.sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+scc+"msg02")));
    			return;
    		}
    		String msg = "";
    		for (int i = 1; i < args.length; i++) 
	        {
    			msg += args[i] + " ";
	        }
    		if(plugin.getUtility().getWordfilter(msg))
    		{
    			player.sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+".EVENT_Chat.msg06")));
    			return;
    		}
    		for(ProxiedPlayer all : ProxyServer.getInstance().getPlayers())
    		{
    			all.sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+scc+"msg20")
    					.replaceAll("%msg%", msg)));
    		}
    	} else if("channelcreate".equalsIgnoreCase(args[0])) 
    	{
    		if(!player.hasPermission("scc.cmd.channelcreate"))
    		{
    			player.sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+scc+"msg02")));
    			return;
    		}
    		CustomChannel cc = CustomChannel.getCustomChannel(player);
    		if(cc!=null)
			{
				player.sendMessage(plugin.getUtility().tc(plugin.getUtility().tl(
						plugin.getYamlHandler().getL().getString(language+scc+"channelcreate.msg01")
						.replaceAll("%channel%", cc.getName()))));
				return;
			}
    		if(args.length==2)
    		{
    			String name = args[1];
        		ArrayList<ProxiedPlayer> members = new ArrayList<ProxiedPlayer>();
        		members.add(player);
        		cc = new CustomChannel(name, player, members, null, new ArrayList<ProxiedPlayer>());
        		CustomChannel.addCustomChannel(cc);
        		player.sendMessage(plugin.getUtility().tc(plugin.getUtility().tl(
    					plugin.getYamlHandler().getL().getString(language+scc+"channelcreate.msg02")
    					.replaceAll("%channel%", cc.getName()))));
    		} else if(args.length==3)
    		{
    			String name = args[1];
    			String password = args[2];
        		ArrayList<ProxiedPlayer> members = new ArrayList<ProxiedPlayer>();
        		members.add(player);
        		cc = new CustomChannel(name, player, members, password, new ArrayList<ProxiedPlayer>());
        		CustomChannel.addCustomChannel(cc);
        		player.sendMessage(plugin.getUtility().tc(plugin.getUtility().tl(
    					plugin.getYamlHandler().getL().getString(language+scc+"channelcreate.msg03")
    					.replaceAll("%channel%", cc.getName())
    					.replaceAll("%password%", password))));
    		} else
    		{
    			plugin.getUtility().rightArgs(player,args,3);
    		}
			return;
    	} else if("channeljoin".equalsIgnoreCase(args[0]) || "join".equalsIgnoreCase(args[0])) 
    	{
    		if(!player.hasPermission("scc.cmd.channeljoin"))
    		{
    			player.sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+scc+"msg02")));
    			return;
    		}
    		if(args.length==2)
    		{
    			String name = args[1];
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
    						.replaceAll("%name%", name)));
        			return;
    			}
    			if(cc.getBanned().contains(player))
    			{
    				player.sendMessage(plugin.getUtility().tcl(
    						plugin.getYamlHandler().getL().getString(language+scc+"joinchannel.msg07")));
        			return;
    			}
    			if(cc.getPassword()!=null)
    			{
    				player.sendMessage(plugin.getUtility().tcl(
    						plugin.getYamlHandler().getL().getString(language+scc+"joinchannel.msg02")));
        			return;
    			}
    			cc.addMembers(player);
    			player.sendMessage(plugin.getUtility().tcl(
						plugin.getYamlHandler().getL().getString(language+scc+"joinchannel.msg05")
						.replaceAll("%channel%", cc.getName())));
    			return;
    		} else if(args.length==3)
    		{
    			String name = args[1];
    			CustomChannel cc = CustomChannel.getCustomChannel(name);
    			CustomChannel oldcc = CustomChannel.getCustomChannel(player);
    			String password = args[2];
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
    						.replaceAll("%name%", name)));
        			return;
    			}
    			if(cc.getBanned().contains(player))
    			{
    				player.sendMessage(plugin.getUtility().tcl(
    						plugin.getYamlHandler().getL().getString(language+scc+"joinchannel.msg07")));
        			return;
    			}
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
    			cc.addMembers(player);
    			player.sendMessage(plugin.getUtility().tcl(
						plugin.getYamlHandler().getL().getString(language+scc+"joinchannel.msg05")
						.replaceAll("%channel%", cc.getName())));
    			return;
    		} else
    		{
    			plugin.getUtility().rightArgs(player,args,3);
    		}
    		return;
    	} else if("channelinfo".equalsIgnoreCase(args[0])) 
    	{
    		if(!player.hasPermission("scc.cmd.channelinfo"))
    		{
    			player.sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+scc+"msg02")));
    			return;
    		}
    		if(plugin.getUtility().rightArgs(player,args,1))
			{
				return;
			}
    		CustomChannel cc = CustomChannel.getCustomChannel(player);
    		if(cc==null)
			{
				player.sendMessage(plugin.getUtility().tcl(
						plugin.getYamlHandler().getL().getString(language+scc+"leavechannel.msg01")));
    			return;
			}
    		player.sendMessage(plugin.getUtility().tcl(
					plugin.getYamlHandler().getL().getString(language+scc+"channelinfo.msg01")
					.replaceAll("%channel%", cc.getName())));
    		player.sendMessage(plugin.getUtility().tcl(
					plugin.getYamlHandler().getL().getString(language+scc+"channelinfo.msg02")
					.replaceAll("%creator%", cc.getCreator().getName())));
    		player.sendMessage(plugin.getUtility().tcl(
					plugin.getYamlHandler().getL().getString(language+scc+"channelinfo.msg03")
					.replaceAll("%members%", cc.getMembers().toString())));
    		if(cc.getPassword()!=null)
    		{
    			player.sendMessage(plugin.getUtility().tcl(
    					plugin.getYamlHandler().getL().getString(language+scc+"channelinfo.msg04")
    					.replaceAll("%password%", cc.getPassword())));
    		}
    		
    		player.sendMessage(plugin.getUtility().tcl(
					plugin.getYamlHandler().getL().getString(language+scc+"channelinfo.msg05")
					.replaceAll("%banned%", cc.getBanned().toString())));
			return;
    	} else if("channelleave".equalsIgnoreCase(args[0]) || "leave".equalsIgnoreCase(args[0])) 
    	{
    		if(!player.hasPermission("scc.cmd.channelleave"))
    		{
    			player.sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+scc+"msg02")));
    			return;
    		}
    		if(plugin.getUtility().rightArgs(player,args,1))
			{
				return;
			}
    		CustomChannel cc = CustomChannel.getCustomChannel(player);
    		if(cc==null)
			{
				player.sendMessage(plugin.getUtility().tcl(
						plugin.getYamlHandler().getL().getString(language+scc+"leavechannel.msg01")));
    			return;
			}
    		final String name = cc.getName();
    		cc.removeMembers(player);
    		if(cc.getCreator().getName().equals(player.getName()))
    		{
    			ProxiedPlayer newcreator = null;
    			for(ProxiedPlayer pp : cc.getMembers())
    			{
    				if(pp!=null)
    				{
    					newcreator = pp;
    				}
    			}
    			if(newcreator!=null)
    			{
    				cc.setCreator(newcreator);
        			newcreator.sendMessage(plugin.getUtility().tcl(
        					plugin.getYamlHandler().getL().getString(language+scc+"leavechannel.msg03")
        					.replaceAll("%channel%", cc.getName())));
    			} else 
    			{
    				CustomChannel.removeCustomChannel(cc);
    				cc = null;
    			}
    			
    		}
    		player.sendMessage(plugin.getUtility().tcl(
					plugin.getYamlHandler().getL().getString(language+scc+"leavechannel.msg02")
					.replaceAll("%channel%", name)));
			return;
    	} else if("channelkick".equalsIgnoreCase(args[0])) 
    	{
    		if(!player.hasPermission("scc.cmd.channelkick"))
    		{
    			player.sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+scc+"msg02")));
    			return;
    		}
    		if(plugin.getUtility().rightArgs(player,args,2))
			{
				return;
			}
    		CustomChannel cc = CustomChannel.getCustomChannel(player);
    		if(cc==null)
			{
				player.sendMessage(plugin.getUtility().tcl(
						plugin.getYamlHandler().getL().getString(language+scc+"leavechannel.msg01")));
    			return;
			}
    		ProxiedPlayer creator = cc.getCreator();
    		if(!creator.getName().equals(player.getName()))
    		{
    			player.sendMessage(plugin.getUtility().tcl(
						plugin.getYamlHandler().getL().getString(language+scc+"kickchannel.msg01")));
    			return;
    		}
    		if(plugin.getProxy().getPlayer(args[1])!=null)
    		{
    			player.sendMessage(plugin.getUtility().tcl(
						plugin.getYamlHandler().getL().getString(language+scc+"kickchannel.msg02")));
    			return;
    		}
    		ProxiedPlayer target = plugin.getProxy().getPlayer(args[1]); 
    		if(target.getName().equals(player.getName()))
    		{
    			player.sendMessage(plugin.getUtility().tcl(
						plugin.getYamlHandler().getL().getString(language+scc+"kickchannel.msg06")));
    			return;
    		}
    		cc.removeMembers(target);
    		target.sendMessage(plugin.getUtility().tcl(
					plugin.getYamlHandler().getL().getString(language+scc+"kickchannel.msg03")));
    		player.sendMessage(plugin.getUtility().tcl(
					plugin.getYamlHandler().getL().getString(language+scc+"kickchannel.msg04")
					.replaceAll("%player%", args[1])));
    		for(ProxiedPlayer members : cc.getMembers())
    		{
    			members.sendMessage(plugin.getUtility().tcl(
    					plugin.getYamlHandler().getL().getString(language+scc+"kickchannel.msg05")
    					.replaceAll("%player%", args[1])));
    		}
			return;
    	} else if("channelban".equalsIgnoreCase(args[0])) 
    	{
    		if(!player.hasPermission("scc.cmd.channelban"))
    		{
    			player.sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+scc+"msg02")));
    			return;
    		}
    		if(plugin.getUtility().rightArgs(player,args,2))
			{
				return;
			}
    		CustomChannel cc = CustomChannel.getCustomChannel(player);
    		if(cc==null)
			{
				player.sendMessage(plugin.getUtility().tcl(
						plugin.getYamlHandler().getL().getString(language+scc+"leavechannel.msg01")));
    			return;
			}
    		ProxiedPlayer creator = cc.getCreator();
    		if(!creator.getName().equals(player.getName()))
    		{
    			player.sendMessage(plugin.getUtility().tcl(
						plugin.getYamlHandler().getL().getString(language+scc+"kickchannel.msg01")));
    			return;
    		}
    		if(plugin.getProxy().getPlayer(args[1])!=null)
    		{
    			player.sendMessage(plugin.getUtility().tcl(
						plugin.getYamlHandler().getL().getString(language+scc+"kickchannel.msg02")));
    			return;
    		}
    		ProxiedPlayer target = plugin.getProxy().getPlayer(args[1]);
    		if(target.getName().equals(player.getName()))
    		{
    			player.sendMessage(plugin.getUtility().tcl(
						plugin.getYamlHandler().getL().getString(language+scc+"banchannel.msg04")));
    			return;
    		}
    		if(cc.getBanned().contains(target))
    		{
    			player.sendMessage(plugin.getUtility().tcl(
						plugin.getYamlHandler().getL().getString(language+scc+"banchannel.msg01")));
    			return;
    		}
    		cc.addBanned(target);
    		player.sendMessage(plugin.getUtility().tcl(
					plugin.getYamlHandler().getL().getString(language+scc+"banchannel.msg02")
					.replaceAll("%player%", args[1])));
    		target.sendMessage(plugin.getUtility().tcl(
					plugin.getYamlHandler().getL().getString(language+scc+"banchannel.msg03")
					.replaceAll("%channel%", cc.getName())));
    		for(ProxiedPlayer members : cc.getMembers())
    		{
    			members.sendMessage(plugin.getUtility().tcl(
    					plugin.getYamlHandler().getL().getString(language+scc+"banchannel.msg04")
    					.replaceAll("%player%", args[1])));
    		}
			return;
    	} else if("channelunban".equalsIgnoreCase(args[0])) 
    	{
    		if(!player.hasPermission("scc.cmd.channelunban"))
    		{
    			player.sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+scc+"msg02")));
    			return;
    		}
    		if(plugin.getUtility().rightArgs(player,args,2))
			{
				return;
			}
    		CustomChannel cc = CustomChannel.getCustomChannel(player);
    		if(cc==null)
			{
				player.sendMessage(plugin.getUtility().tcl(
						plugin.getYamlHandler().getL().getString(language+scc+"leavechannel.msg01")));
    			return;
			}
    		ProxiedPlayer creator = cc.getCreator();
    		if(!creator.getName().equals(player.getName()))
    		{
    			player.sendMessage(plugin.getUtility().tcl(
						plugin.getYamlHandler().getL().getString(language+scc+"kickchannel.msg01")));
    			return;
    		}
    		if(plugin.getProxy().getPlayer(args[1])!=null)
    		{
    			player.sendMessage(plugin.getUtility().tcl(
						plugin.getYamlHandler().getL().getString(language+scc+"kickchannel.msg02")));
    			return;
    		}
    		ProxiedPlayer target = plugin.getProxy().getPlayer(args[1]); 
    		if(!cc.getBanned().contains(target))
    		{
    			player.sendMessage(plugin.getUtility().tcl(
						plugin.getYamlHandler().getL().getString(language+scc+"unbanchannel.msg01")));
    			return;
    		}
    		cc.removeBanned(target);
    		player.sendMessage(plugin.getUtility().tcl(
					plugin.getYamlHandler().getL().getString(language+scc+"unbanchannel.msg02")
					.replaceAll("%player%", target.getName())));
    		for(ProxiedPlayer members : cc.getMembers())
    		{
    			members.sendMessage(plugin.getUtility().tcl(
    					plugin.getYamlHandler().getL().getString(language+scc+"unbanchannel.msg03")
    					.replaceAll("%player%", target.getName())));
    		}
			return;
    	} else if("changepassword".equalsIgnoreCase(args[0])) 
    	{
    		if(!player.hasPermission("scc.cmd.changepassword"))
    		{
    			player.sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+scc+"msg02")));
    			return;
    		}
    		if(plugin.getUtility().rightArgs(player,args,2))
			{
				return;
			}
    		CustomChannel cc = CustomChannel.getCustomChannel(player);
    		if(cc==null)
			{
				player.sendMessage(plugin.getUtility().tcl(
						plugin.getYamlHandler().getL().getString(language+scc+"leavechannel.msg01")));
    			return;
			}
    		ProxiedPlayer creator = cc.getCreator();
    		if(!creator.getName().equals(player.getName()))
    		{
    			player.sendMessage(plugin.getUtility().tcl(
						plugin.getYamlHandler().getL().getString(language+scc+"kickchannel.msg01")));
    			return;
    		}
    		cc.setPassword(args[1]);
    		player.sendMessage(plugin.getUtility().tcl(
					plugin.getYamlHandler().getL().getString(language+scc+"changepassword.msg01")
					.replaceAll("%password%", args[1])));
			return;
    	} else if(args[0].equalsIgnoreCase("lokal")
    			||args[0].equalsIgnoreCase("local")
    			||args[0].equalsIgnoreCase("world")
    			||args[0].equalsIgnoreCase("welt"))
    	{
    		return;
    	} else
		{
			TextComponent msg = plugin.getUtility().tc(plugin.getUtility().tl(plugin.getYamlHandler().getL().getString(language+scc+"msg01")));
			msg.setClickEvent( new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/scc"));
			player.sendMessage(msg);
			return;
		}
    }
    
	/*@Override
	public Iterable<String> onTabComplete(CommandSender sender, String[] args) 
	{
		sender.sendMessage("TabComplete : "+args.toString());
		if(args.length==0)
		{
			if(args[0].startsWith("scc"))
			{
				return 
			}
		} else if(args.length==1)
		{
			return Collections.emptyList();
		}
		return Collections.emptyList();
	}
	
	public void initSubCommands()
	{
		subCommands.put("global", "scc");
		subCommands.put("trade", "scc");
	}*/
}
