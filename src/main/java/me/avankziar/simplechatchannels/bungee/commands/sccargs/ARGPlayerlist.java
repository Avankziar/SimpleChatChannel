package main.java.me.avankziar.simplechatchannels.bungee.commands.sccargs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import main.java.me.avankziar.simplechatchannels.bungee.SimpleChatChannels;
import main.java.me.avankziar.simplechatchannels.bungee.commands.CommandModule;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class ARGPlayerlist extends CommandModule
{
	private SimpleChatChannels plugin;
	
	public ARGPlayerlist(SimpleChatChannels plugin)
	{
		super("playerlist",
				"scc.cmd.playerlist", SimpleChatChannels.sccarguments, 1,1,
				"pl","spielerlist","spielerliste");
		this.plugin = plugin;
	}

	@Override
	public void run(CommandSender sender, String[] args)
	{
		//All needed vars
		ProxiedPlayer player = (ProxiedPlayer) sender;
		String scc = ".CMD_SCC.";
		String language = plugin.getUtility().getLanguage();
		List<BaseComponent> list = new ArrayList<>();
		
		//if only /scc playerlist
		if(args.length==1)
		{
			for(ProxiedPlayer pp : plugin.getProxy().getPlayers())
    		{
				//Add all online player to the list, with clickevent
				list.add(plugin.getUtility().suggestCmd("&e"+pp.getName()+" ", 
						plugin.getYamlHandler().getSymbol("pm")+pp.getName()+" "));
    		}
			
		//if /scc playerlist [String]
		} else if(args.length==2)
		{
			String s = args[1];
			String caseCapitalize = "";
			if(s.length()>=2)
			{
				//for word bigger than 1 character, this word becomes standardized with the fist Letter in Uppercase
				//Example: "Avankziar" not "avankziar"
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
					//Add all online player to the list, with clickevent who also apply
					list.add(plugin.getUtility().suggestCmd("&e"+pp.getName()+" ", 
							plugin.getYamlHandler().getSymbol("pm")+pp.getName()+" "));
				}
    		}
		} else
		{
			plugin.getUtility().rightArgs(player,args,2);
			return;
		}
		if(list.isEmpty())
		{
			//Es passt kein Spieler auf die Beschreibung.
			player.sendMessage(plugin.getUtility().tcl(
					plugin.getYamlHandler().getL().getString(language+scc+"playerlist.msg02")));
			return;
		}
		//=====Msg SpielerListe=====
		player.sendMessage(plugin.getUtility().tcl(
				plugin.getYamlHandler().getL().getString(language+scc+"playerlist.msg01")));
		player.sendMessage(plugin.getUtility().TextWithExtra("", list));
		return;
	}

}
