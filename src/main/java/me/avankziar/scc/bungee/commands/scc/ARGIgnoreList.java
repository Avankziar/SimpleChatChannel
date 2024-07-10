package main.java.me.avankziar.scc.bungee.commands.scc;

import java.util.ArrayList;
import java.util.UUID;

import main.java.me.avankziar.scc.bungee.SCC;
import main.java.me.avankziar.scc.bungee.assistance.Utility;
import main.java.me.avankziar.scc.bungee.commands.tree.ArgumentModule;
import main.java.me.avankziar.scc.bungee.objects.PluginSettings;
import main.java.me.avankziar.scc.general.assistance.ChatApiOld;
import main.java.me.avankziar.scc.general.commands.tree.ArgumentConstructor;
import main.java.me.avankziar.scc.general.database.MysqlType;
import main.java.me.avankziar.scc.general.handlers.ConvertHandler;
import main.java.me.avankziar.scc.general.objects.IgnoreObject;
import main.java.me.avankziar.scc.general.objects.KeyHandler;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class ARGIgnoreList extends ArgumentModule
{
	private SCC plugin;
	
	public ARGIgnoreList(SCC plugin, ArgumentConstructor argumentConstructor)
	{
		super(argumentConstructor);
		this.plugin = plugin;
	}

	@Override
	public void run(CommandSender sender, String[] args)
	{
		ProxiedPlayer player = (ProxiedPlayer) sender;
		ArrayList<IgnoreObject> iolist = new ArrayList<>();
		String name = player.getName();
		if(args.length == 2)
		{
			String otherplayer = args[1];
			UUID uuid = Utility.convertNameToUUID(otherplayer);
			if(uuid == null)
			{
				player.sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("PlayerNotExist")));
				return;
			}
			name = otherplayer;
			iolist = ConvertHandler.convertListII(
					plugin.getMysqlHandler().getFullList(MysqlType.IGNOREOBJECT,
							"`id` ASC", "`player_uuid` = ?", player.getUniqueId().toString()));
		} else
		{
			iolist = ConvertHandler.convertListII(
					plugin.getMysqlHandler().getFullList(MysqlType.IGNOREOBJECT,
							"`id` ASC", "`player_uuid` = ?", player.getUniqueId().toString()));
		}
		if(iolist.isEmpty())
		{
			player.sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdScc.Ignore.NoOne")));
			return;
		}
		ArrayList<BaseComponent> bclist = new ArrayList<>();
		for(IgnoreObject io : iolist)
		{
			bclist.add(ChatApiOld.clickHover(
					io.getIgnoreName()+" ",
					"RUN_COMMAND",
					PluginSettings.settings.getCommands(KeyHandler.SCC_IGNORE)+io.getIgnoreName(),
					"SHOW_TEXT",
					plugin.getYamlHandler().getLang().getString("CmdScc.Ignore.Hover")));
		}
		player.sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdScc.Ignore.Headline")
				.replace("%player%", name)));
		player.sendMessage(ChatApiOld.tctl(bclist));
		return;
	}
}
