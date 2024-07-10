package main.java.me.avankziar.scc.velocity.commands.scc;

import java.util.ArrayList;
import java.util.UUID;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;

import main.java.me.avankziar.scc.general.assistance.ChatApi;
import main.java.me.avankziar.scc.general.commands.tree.ArgumentConstructor;
import main.java.me.avankziar.scc.general.database.MysqlType;
import main.java.me.avankziar.scc.general.handlers.ConvertHandler;
import main.java.me.avankziar.scc.general.objects.IgnoreObject;
import main.java.me.avankziar.scc.general.objects.KeyHandler;
import main.java.me.avankziar.scc.velocity.SCC;
import main.java.me.avankziar.scc.velocity.assistance.Utility;
import main.java.me.avankziar.scc.velocity.commands.tree.ArgumentModule;
import main.java.me.avankziar.scc.velocity.objects.PluginSettings;

public class ARGIgnoreList extends ArgumentModule
{
	private SCC plugin;
	
	public ARGIgnoreList(SCC plugin, ArgumentConstructor argumentConstructor)
	{
		super(argumentConstructor);
		this.plugin = plugin;
	}

	@Override
	public void run(CommandSource sender, String[] args)
	{
		Player player = (Player) sender;
		ArrayList<IgnoreObject> iolist = new ArrayList<>();
		String name = player.getUsername();
		if(args.length == 2)
		{
			String otherplayer = args[1];
			UUID uuid = Utility.convertNameToUUID(otherplayer);
			if(uuid == null)
			{
				player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("PlayerNotExist")));
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
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdScc.Ignore.NoOne")));
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
		player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdScc.Ignore.Headline")
				.replace("%player%", name)));
		player.sendMessage(ChatApi.tl(String.join("", bclist)));
		return;
	}
}
