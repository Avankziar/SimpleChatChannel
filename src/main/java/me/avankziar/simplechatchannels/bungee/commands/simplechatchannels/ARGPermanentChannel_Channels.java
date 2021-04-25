package main.java.me.avankziar.simplechatchannels.bungee.commands.simplechatchannels;

import java.util.ArrayList;
import java.util.List;

import main.java.me.avankziar.scc.bungee.SimpleChatChannels;
import main.java.me.avankziar.simplechatchannels.bungee.commands.tree.ArgumentConstructor;
import main.java.me.avankziar.simplechatchannels.bungee.commands.tree.ArgumentModule;
import main.java.me.avankziar.simplechatchannels.bungee.objects.KeyHandler;
import main.java.me.avankziar.simplechatchannels.bungee.objects.PluginSettings;
import main.java.me.avankziar.simplechatchannels.objects.ChatApi;
import main.java.me.avankziar.simplechatchannels.objects.PermanentChannel;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class ARGPermanentChannel_Channels extends ArgumentModule
{
	private SimpleChatChannels plugin;
	
	public ARGPermanentChannel_Channels(SimpleChatChannels plugin, ArgumentConstructor argumentConstructor)
	{
		super(argumentConstructor);
		this.plugin = plugin;
	}

	@Override
	public void run(CommandSender sender, String[] args)
	{
		ProxiedPlayer player = (ProxiedPlayer) sender;
		List<BaseComponent> list = new ArrayList<>();
		for(int i = 0; i < PermanentChannel.getPermanentChannel().size(); i++)
		{
			PermanentChannel pc = PermanentChannel.getPermanentChannel().get(i);
			String cmd = PluginSettings.settings.getCommands(KeyHandler.SCC_PC_INFO)+pc.getName();
			if(i+1 == PermanentChannel.getPermanentChannel().size())
			{
				list.add(ChatApi.clickEvent(pc.getNameColor()+pc.getName(),
						ClickEvent.Action.RUN_COMMAND, cmd));
			} else
			{
				list.add(ChatApi.clickEvent(pc.getNameColor()+pc.getName()+", &r",
						ClickEvent.Action.RUN_COMMAND, cmd));
			}
		}
		player.sendMessage(ChatApi.tctl(
				plugin.getYamlHandler().getLang().getString("CmdScc.PermanentChannel.Channels.Headline")));
		TextComponent msg = ChatApi.tc("");
		msg.setExtra(list);
		player.sendMessage(msg);
	}
}