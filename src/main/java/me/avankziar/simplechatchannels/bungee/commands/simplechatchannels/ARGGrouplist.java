package main.java.me.avankziar.simplechatchannels.bungee.commands.simplechatchannels;

import java.util.ArrayList;
import java.util.List;

import main.java.me.avankziar.simplechatchannels.bungee.SimpleChatChannels;
import main.java.me.avankziar.simplechatchannels.bungee.assistance.Utility;
import main.java.me.avankziar.simplechatchannels.bungee.commands.tree.ArgumentConstructor;
import main.java.me.avankziar.simplechatchannels.bungee.commands.tree.ArgumentModule;
import main.java.me.avankziar.simplechatchannels.objects.ChatApi;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class ARGGrouplist extends ArgumentModule
{
	private SimpleChatChannels plugin;

	public ARGGrouplist(SimpleChatChannels plugin, ArgumentConstructor argumentConstructor)
	{
		super(plugin, argumentConstructor);
		this.plugin = plugin;
	}

	@Override
	public void run(CommandSender sender, String[] args)
	{
		//All needed vars
		ProxiedPlayer player = (ProxiedPlayer) sender;
		List<BaseComponent> list = new ArrayList<>();
		int i = 1;
		int groupamount = Integer.parseInt(plugin.getYamlHandler().getL().getString("PrefixSuffixAmount"));
		ArrayList<String> groups = new ArrayList<>();
		while(i<=groupamount)
		{
			if(plugin.getYamlHandler().getL().getString("Prefix."+i).contains("&"))
			{
				groups.add(plugin.getYamlHandler().getL().getString("Prefix."+i).substring(2));
			} else
			{
				groups.add(plugin.getYamlHandler().getL().getString("Prefix."+i));
			}
			if(plugin.getYamlHandler().getL().getString("Suffix"+i).contains("&"))
			{
				groups.add(plugin.getYamlHandler().getL().getString("Suffix"+i).substring(2));
			} else
			{
				groups.add(plugin.getYamlHandler().getL().getString("Suffix"+i));
			}
			i++;
		}
		if(args.length==1)
		{
			for(String g : groups)
    		{
				TextComponent prefix = ChatApi.clickEvent("&6"+g+" ", 
						ClickEvent.Action.SUGGEST_COMMAND, plugin.getYamlHandler().getSymbol("Group")+g+" ");
				list.add(prefix);
    		}
		} else if(args.length==2)
		{
			String s = args[1];
			for(String g : groups)
    		{
				if(Utility.containsIgnoreCase(s, g))
				{
					TextComponent prefix = ChatApi.clickEvent("&6"+g+" ", 
							ClickEvent.Action.SUGGEST_COMMAND, plugin.getYamlHandler().getSymbol("Group")+g+" ");
    				list.add(prefix);	
				}
    		}
		}
		plugin.getCommandHelper().playergrouplist(player, list, "GroupList");
		return;
	}

}
