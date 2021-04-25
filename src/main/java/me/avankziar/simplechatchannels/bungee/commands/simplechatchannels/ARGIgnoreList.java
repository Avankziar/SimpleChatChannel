package main.java.me.avankziar.simplechatchannels.bungee.commands.simplechatchannels;

import java.util.ArrayList;
import java.util.UUID;

import main.java.me.avankziar.scc.bungee.SimpleChatChannels;
import main.java.me.avankziar.simplechatchannels.bungee.assistance.Utility;
import main.java.me.avankziar.simplechatchannels.bungee.commands.tree.ArgumentConstructor;
import main.java.me.avankziar.simplechatchannels.bungee.commands.tree.ArgumentModule;
import main.java.me.avankziar.simplechatchannels.bungee.database.MysqlHandler;
import main.java.me.avankziar.simplechatchannels.bungee.objects.KeyHandler;
import main.java.me.avankziar.simplechatchannels.bungee.objects.PluginSettings;
import main.java.me.avankziar.simplechatchannels.handlers.ConvertHandler;
import main.java.me.avankziar.simplechatchannels.objects.ChatApi;
import main.java.me.avankziar.simplechatchannels.objects.IgnoreObject;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class ARGIgnoreList extends ArgumentModule
{
	private SimpleChatChannels plugin;
	
	public ARGIgnoreList(SimpleChatChannels plugin, ArgumentConstructor argumentConstructor)
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
				player.sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("PlayerNotExist")));
				return;
			}
			name = otherplayer;
			iolist = ConvertHandler.convertListII(
					plugin.getMysqlHandler().getAllListAt(MysqlHandler.Type.IGNOREOBJECT,
							"`id`", false, "`player_uuid` = ?", player.getUniqueId().toString()));
		} else
		{
			iolist = ConvertHandler.convertListII(
					plugin.getMysqlHandler().getAllListAt(MysqlHandler.Type.IGNOREOBJECT,
							"`id`", false, "`player_uuid` = ?", player.getUniqueId().toString()));
		}
		if(iolist.isEmpty())
		{
			player.sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("CmdScc.Ignore.NoOne")));
			return;
		}
		ArrayList<BaseComponent> bclist = new ArrayList<>();
		for(IgnoreObject io : iolist)
		{
			bclist.add(ChatApi.apiChat(
					io.getIgnoreName()+" ",
					ClickEvent.Action.RUN_COMMAND,
					PluginSettings.settings.getCommands(KeyHandler.SCC_IGNORE)+io.getIgnoreName(),
					HoverEvent.Action.SHOW_TEXT,
					plugin.getYamlHandler().getLang().getString("CmdScc.Ignore.Hover")));
		}
		player.sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("CmdScc.Ignore.Headline")
				.replace("%player%", name)));
		TextComponent tc = ChatApi.tc("");
		tc.setExtra(bclist);
		player.sendMessage(tc);
		return;
	}
}
