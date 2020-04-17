package main.java.me.avankziar.simplechatchannels.spigot.commands.simplechatchannels;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.simplechatchannels.spigot.SimpleChatChannels;
import main.java.me.avankziar.simplechatchannels.spigot.Utility;
import main.java.me.avankziar.simplechatchannels.spigot.commands.CommandModule;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;

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
		Player player = (Player) sender;
		Utility utility = plugin.getUtility();
		String language = utility.getLanguage();
		List<BaseComponent> list = new ArrayList<>();
		int i = 1;
		int groupamount = Integer.parseInt(plugin.getYamlHandler().getL().getString(language+".PrefixSuffixAmount"));
		ArrayList<String> groups = new ArrayList<>();
		while(i<=groupamount)
		{
			if(plugin.getYamlHandler().getL().getString(language+".Prefix."+i).contains("&"))
			{
				groups.add(plugin.getYamlHandler().getL().getString(language+".Prefix."+i).substring(2));
			} else
			{
				groups.add(plugin.getYamlHandler().getL().getString(language+".Prefix."+i));
			}
			if(plugin.getYamlHandler().getL().getString(language+".Suffix"+i).contains("&"))
			{
				groups.add(plugin.getYamlHandler().getL().getString(language+".Suffix"+i).substring(2));
			} else
			{
				groups.add(plugin.getYamlHandler().getL().getString(language+".Suffix"+i));
			}
			i++;
		}
		if(args.length==1)
		{
			for(String g : groups)
    		{
				TextComponent prefix = utility.clickEvent("&6"+g+" ", 
						ClickEvent.Action.SUGGEST_COMMAND, plugin.getYamlHandler().getSymbol("group")+g+" ", false);
				list.add(prefix);
    		}
		} else if(args.length==2)
		{
			String s = args[1];
			for(String g : groups)
    		{
				if(s.startsWith(g))
				{
					TextComponent prefix = utility.clickEvent("&6"+g+" ", 
							ClickEvent.Action.SUGGEST_COMMAND, plugin.getYamlHandler().getSymbol("group")+g+" ", false);
    				list.add(prefix);	
				}
    		}
		}
		plugin.getCommandHelper().playergrouplist(player, list, "GroupList");
		return;
	}

}