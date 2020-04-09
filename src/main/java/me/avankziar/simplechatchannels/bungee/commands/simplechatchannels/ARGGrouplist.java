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

public class ARGGrouplist extends CommandModule
{
	private SimpleChatChannels plugin;

	public ARGGrouplist(SimpleChatChannels plugin)
	{
		super("grouplist",
				"scc.cmd.grouplist",SimpleChatChannels.sccarguments,1,2,"gruppenliste");
		this.plugin = plugin;
	}

	@Override
	public void run(CommandSender sender, String[] args)
	{
		//All needed vars
		ProxiedPlayer player = (ProxiedPlayer) sender;
		String language = plugin.getUtility().getLanguage();
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
		plugin.getCommandHelper().playergrouplist(player, language, list, "grouplist");
		return;
	}

}
