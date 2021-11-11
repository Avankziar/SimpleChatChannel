package main.java.me.avankziar.scc.bungee.commands.scc;

import java.util.ArrayList;

import main.java.me.avankziar.scc.bungee.SimpleChatChannels;
import main.java.me.avankziar.scc.bungee.commands.tree.ArgumentConstructor;
import main.java.me.avankziar.scc.bungee.commands.tree.ArgumentModule;
import main.java.me.avankziar.scc.objects.ChatApi;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class ARGDebug extends ArgumentModule
{
	private SimpleChatChannels plugin;
	
	public ARGDebug(SimpleChatChannels plugin, ArgumentConstructor argumentConstructor)
	{
		super(argumentConstructor);
		this.plugin = plugin;
	}

	@Override
	public void run(CommandSender sender, String[] args)
	{
		TextComponent tc1 = ChatApi.tctl("&7[&2S&6CC&7] ");
		TextComponent tc2 = ChatApi.apiChat("&aJa", 
				ClickEvent.Action.SUGGEST_COMMAND, "/umfrage ja",
				HoverEvent.Action.SHOW_TEXT, "&eKlicke hier");
		TextComponent tc3 = ChatApi.tctl(" &d| ");
		TextComponent tc4 = ChatApi.apiChat("&cNein", 
				ClickEvent.Action.SUGGEST_COMMAND, "/umfrage nein",
				HoverEvent.Action.SHOW_TEXT, "&eKlicke hier");
		ArrayList<BaseComponent> list = new ArrayList<>();
		list.add(tc1);
		list.add(tc2);
		list.add(tc3);
		list.add(tc4);
		
		String s = ChatApi.serialized(list);
		TextComponent tc = ChatApi.deserialized(s);
		sender.sendMessage(tc);
	}
}