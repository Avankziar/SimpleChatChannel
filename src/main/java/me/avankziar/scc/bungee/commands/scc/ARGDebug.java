package main.java.me.avankziar.scc.bungee.commands.scc;

import main.java.me.avankziar.scc.bungee.SCC;
import main.java.me.avankziar.scc.bungee.commands.tree.ArgumentModule;
import main.java.me.avankziar.scc.general.commands.tree.ArgumentConstructor;
import net.md_5.bungee.api.CommandSender;

public class ARGDebug extends ArgumentModule
{
	//private SCC plugin;
	
	public ARGDebug(SCC plugin, ArgumentConstructor argumentConstructor)
	{
		super(argumentConstructor);
		//this.plugin = plugin;
	}

	@Override
	public void run(CommandSender sender, String[] args)
	{
		/*TextComponent tc1 = ChatApiOld.tctl("&7[&2S&6CC&7] ");
		TextComponent tc2 = ChatApiOld.apiChat("&aJa", 
				ClickEvent.Action.SUGGEST_COMMAND, "/umfrage ja",
				HoverEvent.Action.SHOW_TEXT, "&eKlicke hier");
		TextComponent tc3 = ChatApiOld.tctl(" &d| ");
		TextComponent tc4 = ChatApiOld.apiChat("&cNein", 
				ClickEvent.Action.SUGGEST_COMMAND, "/umfrage nein",
				HoverEvent.Action.SHOW_TEXT, "&eKlicke hier");
		ArrayList<BaseComponent> list = new ArrayList<>();
		list.add(tc1);
		list.add(tc2);
		list.add(tc3);
		list.add(tc4);
		
		String s = ChatApiOld.serialized(list);
		TextComponent tc = ChatApiOld.deserialized(s);
		sender.sendMessage(tc);
		Component c = Component.text().build().asComponent();
		sender.sendMessage(ChatApi.tctl(s));*/
	}
}