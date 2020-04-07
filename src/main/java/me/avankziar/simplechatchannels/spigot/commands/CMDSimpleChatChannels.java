package main.java.me.avankziar.simplechatchannels.spigot.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.simplechatchannels.spigot.SimpleChatChannels;
import main.java.me.avankziar.simplechatchannels.spigot.interfaces.CustomChannel;
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
    		plugin.getCommandHandler().info(player, language);
    		return true;
    	} else if("playerlist".equalsIgnoreCase(args[0]) || "pl".equalsIgnoreCase(args[0]) 
    			|| "spielerlist".equalsIgnoreCase(args[0]) || "spielerlist".equalsIgnoreCase(args[0])) 
    	{
    		if(!player.hasPermission("scc.cmd.playerlist"))
			{
				player.spigot().sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+scc+"msg02")));
				return false;
			}
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
    			return true;
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
    			for(Player pp : plugin.getServer().getOnlinePlayers())
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
    			return false;
    		}
    		plugin.getCommandHandler().playergrouplist(player, language, list, "playerlist");
    		return true;
    	} else if("grouplist".equalsIgnoreCase(args[0]) || "gl".equalsIgnoreCase(args[0])
    			|| "gruppenlist".equalsIgnoreCase(args[0]) || "gruppenliste".equalsIgnoreCase(args[0])) 
    	{
    		if(!player.hasPermission("scc.cmd.grouplist"))
			{
				player.spigot().sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+scc+"msg02")));
				return false;
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
    			if(list.isEmpty())
    			{
    				player.spigot().sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+scc+"grouplist.msg02")));
    				return false;
    			}
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
    		} else
    		{
    			plugin.getUtility().rightArgs(player,args,2);
    			return false;
    		}
    		plugin.getCommandHandler().playergrouplist(player, language, list, "grouplist");
    		return true;
    	} else if("global".equalsIgnoreCase(args[0]))
		{
    		plugin.getCommandHandler().channeltoggle(player, args, language, "global", "Global");
			return true;
		} else if("trade".equalsIgnoreCase(args[0]) || "handel".equalsIgnoreCase(args[0]))
		{
			plugin.getCommandHandler().channeltoggle(player, args, language, "trade", "Trade");
			return true;
		} else if("auction".equalsIgnoreCase(args[0]) || "auktion".equalsIgnoreCase(args[0]))
		{
			plugin.getCommandHandler().channeltoggle(player, args, language, "auction", "Auction");
			return true;
		} else if(args[0].equalsIgnoreCase("local") || args[0].equalsIgnoreCase("lokal"))//--------------------------------------------------local
		{
			plugin.getCommandHandler().channeltoggle(player, args, language, "local", "Local");
			return true;
		} else if("support".equalsIgnoreCase(args[0]) || "hilfe".equalsIgnoreCase(args[0]))
		{
			plugin.getCommandHandler().channeltoggle(player, args, language, "support", "Support");
			return true;
		} else if("team".equalsIgnoreCase(args[0]))
		{
			plugin.getCommandHandler().channeltoggle(player, args, language, "team", "Team");
			return true;
		} else if("admin".equalsIgnoreCase(args[0]))
		{
			plugin.getCommandHandler().channeltoggle(player, args, language, "admin", "Admin");
			return true;
		} else if("pm".equalsIgnoreCase(args[0]) || "pn".equalsIgnoreCase(args[0]))
		{
			plugin.getCommandHandler().channeltoggle(player, args, language, "pm", "Private Message");
			return true;
		} else if(args[0].equalsIgnoreCase("world") || args[0].equalsIgnoreCase("welt"))//--------------------------------------------------world
		{
			plugin.getCommandHandler().channeltoggle(player, args, language, "world", "World");
			return true;
		} else if("group".equalsIgnoreCase(args[0]) || "gruppe".equalsIgnoreCase(args[0]))
		{
			plugin.getCommandHandler().channeltoggle(player, args, language, "group", "Group");
			return true;
		} else if("custom".equalsIgnoreCase(args[0]))
		{
			plugin.getCommandHandler().channeltoggle(player, args, language, "custom", "Custom");
			return true;
		} else if("spy".equalsIgnoreCase(args[0]) || "spitzeln".equalsIgnoreCase(args[0]))
		{
			plugin.getCommandHandler().optiontoggle(player, args, language, "spy", "spy", "Spy");
			return true;
		} else if("join".equalsIgnoreCase(args[0]) 
				|| "joinmessage".equalsIgnoreCase(args[0])
				|| "Eintrittsnachricht".equalsIgnoreCase(args[0]))
			//--------------------------------------------------joinmessage
		{
			plugin.getCommandHandler().optiontoggle(player, args, language, "join", "joinmessage", "Join Message");
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
					.replace("%il%", list)));
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
    					.replace("%time%", args[2])));
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
						.replace("%player%", target)));
				return true;
			} else
			{
				plugin.getMysqlInterface().createIgnore(player, t);
				player.spigot().sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+scc+"ignore.msg01")
						.replace("%player%", target)));
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
							.replace("%word%", word)));
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
							.replace("%word%", word)));
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
    		TextComponent MSG = plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+scc+"broadcast.msg20")+" ");
    		MSG.setExtra(plugin.getUtility().msgLater(player, 0, "global", msg));
    		for(Player all : plugin.getServer().getOnlinePlayers())
    		{
    			all.spigot().sendMessage(MSG);
    		}
    		return true;
    	} else if("reload".equals(args[0]))
    	{
    		if(!player.hasPermission("scc.cmd.reload"))
    		{
    			player.spigot().sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+scc+"msg02")));
    			return false;
    		}
    		if(plugin.getUtility().rightArgs(player,args,1))
			{
				return false;
			}
    		plugin.getYamlHandler().mkdir();
    		if(plugin.getYamlHandler().loadYamls())
    		{
    			plugin.getYamlHandler().reload();
    			player.spigot().sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+scc+"reload.msg01")));
    			return true;
    		} else
    		{
    			player.spigot().sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+scc+"reload.msg02")));
    			return false;
    		}
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
						.replace("%channel%", cc.getName()))));
				return false;
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
    			return false;
    		}
    		plugin.getCommandHandler().cccreate(player, language, cc, name, password);
			return true;
    	} else if("ccjoin".equalsIgnoreCase(args[0]) || "join".equalsIgnoreCase(args[0])) 
    	{
    		if(!player.hasPermission("scc.cmd.cc.join"))
    		{
    			player.spigot().sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+scc+"msg02")));
    			return false;
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
    			return false;
    		}
    		plugin.getCommandHandler().ccjoin(player, language, name, password);
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
        					.replace("%channel%", cc.getName())));
    			} else 
    			{
    				CustomChannel.removeCustomChannel(cc);
    				cc = null;
    			}
    		}
    		player.spigot().sendMessage(plugin.getUtility().tcl(
					plugin.getYamlHandler().getL().getString(language+scc+"leavechannel.msg02")
					.replace("%channel%", name)));
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
					.replace("%player%", args[1])));
    		for(Player members : cc.getMembers())
    		{
    			members.spigot().sendMessage(plugin.getUtility().tcl(
    					plugin.getYamlHandler().getL().getString(language+scc+"kickchannel.msg05")
    					.replace("%player%", args[1])));
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
					.replace("%player%", args[1])));
    		target.spigot().sendMessage(plugin.getUtility().tcl(
					plugin.getYamlHandler().getL().getString(language+scc+"banchannel.msg03")
					.replace("%channel%", cc.getName())));
    		for(Player members : cc.getMembers())
    		{
    			members.spigot().sendMessage(plugin.getUtility().tcl(
    					plugin.getYamlHandler().getL().getString(language+scc+"banchannel.msg04")
    					.replace("%player%", args[1])));
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
					.replace("%player%", target.getName())));
    		for(Player members : cc.getMembers())
    		{
    			members.spigot().sendMessage(plugin.getUtility().tcl(
    					plugin.getYamlHandler().getL().getString(language+scc+"unbanchannel.msg03")
    					.replace("%player%", target.getName())));
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
					.replace("%password%", args[1])));
			return true;
    	} else if("debug".equalsIgnoreCase(args[0])) 
    	{
    		if(!player.hasPermission("scc.cmd.debug"))
    		{
    			player.spigot().sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+scc+"msg02")));
    			return false;
    		}
    		return false;
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
