package main.java.me.avankziar.simplechatchannels.bungee.commands;

import java.util.ArrayList;
import java.util.List;

import main.java.me.avankziar.simplechatchannels.bungee.SimpleChatChannels;
import main.java.me.avankziar.simplechatchannels.bungee.interfaces.CustomChannel;
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
    		plugin.getCommandHandler().info(player, language);
    		return;
    	} else if("playerlist".equalsIgnoreCase(args[0]) || "pl".equalsIgnoreCase(args[0]) 
    			|| "spielerlist".equalsIgnoreCase(args[0]) || "spielerlist".equalsIgnoreCase(args[0])) 
    	{
    		if(!player.hasPermission("scc.cmd.playerlist"))
			{
				player.sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+scc+"msg02")));
				return;
			}
			
			List<BaseComponent> list = new ArrayList<>();
    		if(args.length==1)
    		{
    			for(ProxiedPlayer pp : plugin.getProxy().getPlayers())
        		{
    				TextComponent prefix = plugin.getUtility().tcl("&e"+pp.getName()+" ");
    				prefix.setClickEvent( new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND,
    						plugin.getYamlHandler().getSymbol("pm")+pp.getName()+" "));
    				list.add(prefix);
        		}
    		} else if(args.length==2)
    		{
    			String s = args[1];
    			String caseCapitalize = "";
    			if(s.length()>=2)
    			{
    				caseCapitalize = s.substring(0,1).toUpperCase() + s.substring(1).toLowerCase();
    			} else
    			{
    				caseCapitalize = s;
    			}
    			for(ProxiedPlayer pp : plugin.getProxy().getPlayers())
        		{
    				if(pp.getName().startsWith(s) || pp.getName().startsWith(s.toLowerCase()) 
    						|| pp.getName().startsWith(s.toUpperCase()) || pp.getName().startsWith(caseCapitalize))
    				{
    					TextComponent prefix = plugin.getUtility().tcl("&e"+pp.getName()+" ");
        				prefix.setClickEvent( new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND,
        						plugin.getYamlHandler().getSymbol("pm")+pp.getName()+" "));
        				list.add(prefix);
    				}
        		}
    		} else
    		{
    			plugin.getUtility().rightArgs(player,args,2);
    			return;
    		}
    		plugin.getCommandHandler().playergrouplist(player, language, list, "playerlist");
			return;
    	} else if("grouplist".equalsIgnoreCase(args[0]) || "gl".equalsIgnoreCase(args[0])) 
    	{
    		if(!player.hasPermission("scc.cmd.grouplist"))
			{
				player.sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+scc+"msg02")));
				return;
			}
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
    		} else if(args.length==2)
    		{
    			String s = args[1];
    			for(String g : groups)
        		{
    				if(s.startsWith(g))
    				{
    					TextComponent prefix = plugin.getUtility().tcl("&6"+g+" ");
        				prefix.setClickEvent( new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND,
        						plugin.getYamlHandler().getSymbol("group")+g+" "));
        				list.add(prefix);	
    				}
        		}
    		} else
    		{
    			plugin.getUtility().rightArgs(player,args,2);
    			return;
    		}
    		plugin.getCommandHandler().playergrouplist(player, language, list, "grouplist");
    		return;
    	} else if("global".equalsIgnoreCase(args[0]))
		{
    		plugin.getCommandHandler().channeltoggle(player, args, language, "global", "Global");
			return;
		} else if("trade".equalsIgnoreCase(args[0]) || "handel".equalsIgnoreCase(args[0]))
		{
			plugin.getCommandHandler().channeltoggle(player, args, language, "trade", "Trade");
			return;
		} else if("auction".equalsIgnoreCase(args[0]) || "auktion".equalsIgnoreCase(args[0]))
		{
			plugin.getCommandHandler().channeltoggle(player, args, language, "auction", "Auction");
			return;
		} else if(args[0].equalsIgnoreCase("local") || args[0].equalsIgnoreCase("lokal"))//--------------------------------------------------local
		{
			plugin.getCommandHandler().channeltoggle(player, args, language, "local", "Local");
			return;
		} else if("support".equalsIgnoreCase(args[0]) || "hilfe".equalsIgnoreCase(args[0]))
		{
			plugin.getCommandHandler().channeltoggle(player, args, language, "support", "Support");
			return;
		} else if("team".equalsIgnoreCase(args[0]))
		{
			plugin.getCommandHandler().channeltoggle(player, args, language, "team", "Team");
			return;
		} else if("admin".equalsIgnoreCase(args[0]))
		{
			plugin.getCommandHandler().channeltoggle(player, args, language, "admin", "Admin");
			return;
		} else if(args[0].equalsIgnoreCase("world") || args[0].equalsIgnoreCase("welt"))//--------------------------------------------------world
		{
			plugin.getCommandHandler().channeltoggle(player, args, language, "world", "World");
			return;
		} else if("pm".equalsIgnoreCase(args[0]) || "pn".equalsIgnoreCase(args[0]))
		{
			plugin.getCommandHandler().channeltoggle(player, args, language, "pm", "Private Message");
			return;
		} else if("group".equalsIgnoreCase(args[0]) || "gruppe".equalsIgnoreCase(args[0]))
		{
			plugin.getCommandHandler().channeltoggle(player, args, language, "group", "Group");
			return;
		} else if("custom".equalsIgnoreCase(args[0]))
		{
			plugin.getCommandHandler().channeltoggle(player, args, language, "custom", "Custom");
			return;
		} else if("spy".equalsIgnoreCase(args[0]) || "spitzeln".equalsIgnoreCase(args[0]))
		{
			plugin.getCommandHandler().optiontoggle(player, args, language, "spy", "spy", "Spy");
			return;
		} else if("join".equalsIgnoreCase(args[0]) 
				|| "joinmessage".equalsIgnoreCase(args[0])
				|| "Eintrittsnachricht".equalsIgnoreCase(args[0]))
		{
			plugin.getCommandHandler().optiontoggle(player, args, language, "join", "joinmessage", "Join Message");
			return;
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
			plugin.getUtility().sendMessage(player.getServer(),"simplechatchannels:sccbungee", "bungeeswitch");
			return;
		} else if("mute".equalsIgnoreCase(args[0]) || "verstummen".equalsIgnoreCase(args[0]))
		{
			if(!player.hasPermission("scc.cmd.mute"))
			{
				player.sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+scc+"msg02")));
				return;
			}
			String target = args[1];
			if(ProxyServer.getInstance().getPlayer(target)== null)
			{
				player.sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+scc+"msg03")));
				return;
			}
			ProxiedPlayer t = ProxyServer.getInstance().getPlayer(target);
			if(args.length == 2)
			{
    			plugin.getMysqlInterface().updateDataI(player, false, "can_chat");
    			plugin.getMysqlInterface().updateDataI(player, 0L, "mutetime");
    			t.sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+scc+"mute.msg01")));
			} else if(args.length == 3)
			{
    			int num = 0;
    			try
    			{
    				  num = Integer.parseInt(args[2]);
    			} catch (NumberFormatException e) 
    			{
    				  player.sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+scc+"msg04")
    						  .replaceAll("%arg%", args[2])));
    				  return;
    			}
    			Long time = 60L*1000;
    			Long mutetime = System.currentTimeMillis()+num*time;
    			plugin.getMysqlInterface().updateDataI(player, false, "can_chat");
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
			return;
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
			return;
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
			}
			return;
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
    		TextComponent MSG = plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+scc+"broadcast.msg20")+" ");
    		MSG.setExtra(plugin.getUtility().msgLater(player, 0, "global", msg));
    		for(ProxiedPlayer all : ProxyServer.getInstance().getPlayers())
    		{
    			all.sendMessage(MSG);
    		}
    		return;
    	} else if("cccreate".equalsIgnoreCase(args[0])) 
    	{
    		if(!player.hasPermission("scc.cmd.cc.create"))
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
    		String name = null;
			String password = null;
    		if(args.length==2)
    		{
    			name = args[1];
    		} else if(args.length==3)
    		{
    			name = args[1];
    			password = args[2];
    		} else
    		{
    			plugin.getUtility().rightArgs(player,args,3);
    			return;
    		}
    		plugin.getCommandHandler().cccreate(player, language, cc, name, password);
			return;
    	} else if("ccjoin".equalsIgnoreCase(args[0])) 
    	{
    		if(!player.hasPermission("scc.cmd.cc.join"))
    		{
    			player.sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+scc+"msg02")));
    			return;
    		}
    		String name = null;
			String password = null;
    		if(args.length==2)
    		{
    			name = args[1];
    		} else if(args.length==3)
    		{
    			name = args[1];
    			password = args[2];
    		} else
    		{
    			plugin.getUtility().rightArgs(player,args,3);
    			return;
    		}
    		plugin.getCommandHandler().ccjoin(player, language, name, password);
    		return;
    	} else if("ccinfo".equalsIgnoreCase(args[0])) 
    	{
    		if(!player.hasPermission("scc.cmd.cc.info"))
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
    	} else if("ccleave".equalsIgnoreCase(args[0]) || "leave".equalsIgnoreCase(args[0])) 
    	{
    		if(!player.hasPermission("scc.cmd.cc.leave"))
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
    	} else if("cckick".equalsIgnoreCase(args[0])) 
    	{
    		if(!player.hasPermission("scc.cmd.cc.kick"))
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
    	} else if("ccban".equalsIgnoreCase(args[0])) 
    	{
    		if(!player.hasPermission("scc.cmd.cc.ban"))
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
    	} else if("ccunban".equalsIgnoreCase(args[0])) 
    	{
    		if(!player.hasPermission("scc.cmd.cc.unban"))
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
    	} else if("ccchangepassword".equalsIgnoreCase(args[0])) 
    	{
    		if(!player.hasPermission("scc.cmd.cc.changepassword"))
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
    	} else
		{
			TextComponent msg = plugin.getUtility().tc(plugin.getUtility().tl(plugin.getYamlHandler().getL().getString(language+scc+"msg01")));
			msg.setClickEvent( new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/scc"));
			player.sendMessage(msg);
			return;
		}
    }
}
