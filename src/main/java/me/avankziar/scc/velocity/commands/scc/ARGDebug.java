package main.java.me.avankziar.scc.velocity.commands.scc;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;

import main.java.me.avankziar.scc.general.assistance.ChatApi;
import main.java.me.avankziar.scc.general.commands.tree.ArgumentConstructor;
import main.java.me.avankziar.scc.velocity.SCC;
import main.java.me.avankziar.scc.velocity.commands.tree.ArgumentModule;

public class ARGDebug extends ArgumentModule
{
	private SCC plugin;
	
	public ARGDebug(SCC plugin, ArgumentConstructor argumentConstructor)
	{
		super(argumentConstructor);
		this.plugin = plugin;
	}

	@Override
	public void run(CommandSource sender, String[] args)
	{
		Player player = (Player) sender;
		if(args.length <= 1)
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdMsg.PleaseEnterAMessage")));
			return;
		}
		String message = "";
		int i = 1;
		while(i < args.length)
		{
			message += args[i];
			if(i < (args.length-1))
			{
				message += " ";
			}
			i++;
		}
		//Component c = Component.text().content("Web").color(NamedTextColor.GRAY).clickEvent(ClickEvent.copyToClipboard("google.com")).build().asComponent();
		player.sendMessage(ChatApi.tl(message));
		player.sendMessage(ChatApi.tl(ChatApi.click("<white>Web</white><gray>seite</gray>", "COPY_TO_CLIPBOARD", "google.com")));
	}
}