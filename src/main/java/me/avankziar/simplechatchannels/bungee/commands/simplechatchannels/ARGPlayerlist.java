package main.java.me.avankziar.simplechatchannels.bungee.commands.simplechatchannels;

import java.util.ArrayList;
import java.util.List;

import main.java.me.avankziar.simplechatchannels.bungee.SimpleChatChannels;
import main.java.me.avankziar.simplechatchannels.bungee.commands.CommandModule;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class ARGPlayerlist extends CommandModule
{
	private SimpleChatChannels plugin;
	
	public ARGPlayerlist(SimpleChatChannels plugin)
	{
		super("playerlist",
				"scc.cmd.playerlist", SimpleChatChannels.sccarguments,1,2,"spielerliste");
		this.plugin = plugin;
	}

	@Override
	public void run(CommandSender sender, String[] args)
	{
		//All needed vars
		ProxiedPlayer player = (ProxiedPlayer) sender;
		String language = plugin.getUtility().getLanguage();
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
		plugin.getCommandHelper().playergrouplist(player, language, list, "playerlist");
		return;
	}

}
