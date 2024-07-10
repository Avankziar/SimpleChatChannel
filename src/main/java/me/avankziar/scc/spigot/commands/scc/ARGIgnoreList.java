package main.java.me.avankziar.scc.spigot.commands.scc;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.scc.general.assistance.ChatApi;
import main.java.me.avankziar.scc.general.commands.tree.ArgumentConstructor;
import main.java.me.avankziar.scc.general.database.MysqlType;
import main.java.me.avankziar.scc.general.handlers.ConvertHandler;
import main.java.me.avankziar.scc.general.objects.IgnoreObject;
import main.java.me.avankziar.scc.general.objects.KeyHandler;
import main.java.me.avankziar.scc.spigot.SCC;
import main.java.me.avankziar.scc.spigot.assistance.Utility;
import main.java.me.avankziar.scc.spigot.commands.tree.ArgumentModule;
import main.java.me.avankziar.scc.spigot.objects.PluginSettings;

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
		Player player = (Player) sender;
		ArrayList<IgnoreObject> iolist = new ArrayList<>();
		String name = player.getName();
		if(args.length == 2)
		{
			String otherplayer = args[1];
			UUID uuid = Utility.convertNameToUUID(otherplayer);
			if(uuid == null)
			{
				player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("PlayerNotExist")));
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
			player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("CmdScc.Ignore.NoOne")));
			return;
		}
		ArrayList<String> bclist = new ArrayList<>();
		for(IgnoreObject io : iolist)
		{
			bclist.add(ChatApi.clickHover(
					io.getIgnoreName()+" ",
					"RUN_COMMAND",
					PluginSettings.settings.getCommands(KeyHandler.SCC_IGNORE)+io.getIgnoreName(),
					"SHOW_TEXT",
					plugin.getYamlHandler().getLang().getString("CmdScc.Ignore.Hover")));
		}
		player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("CmdScc.Ignore.Headline")
				.replace("%player%", name)));
		player.spigot().sendMessage(ChatApi.tctl(String.join("", bclist)));
	}
}
