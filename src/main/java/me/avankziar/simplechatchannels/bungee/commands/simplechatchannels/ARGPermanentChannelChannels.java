package main.java.me.avankziar.simplechatchannels.bungee.commands.simplechatchannels;

import java.util.ArrayList;
import java.util.List;

import main.java.me.avankziar.simplechatchannels.bungee.SimpleChatChannels;
import main.java.me.avankziar.simplechatchannels.bungee.commands.tree.ArgumentConstructor;
import main.java.me.avankziar.simplechatchannels.bungee.commands.tree.ArgumentModule;
import main.java.me.avankziar.simplechatchannels.objects.ChatApi;
import main.java.me.avankziar.simplechatchannels.objects.PermanentChannel;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class ARGPermanentChannelChannels extends ArgumentModule
{
	private SimpleChatChannels plugin;
	
	public ARGPermanentChannelChannels(SimpleChatChannels plugin, ArgumentConstructor argumentConstructor)
	{
		super(plugin, argumentConstructor);
		this.plugin = plugin;
	}

	@Override
	public void run(CommandSender sender, String[] args)
	{
		ProxiedPlayer player = (ProxiedPlayer) sender;
		String scc = "CmdScc.";
		List<BaseComponent> list = new ArrayList<>();
		for(int i = 0; i < PermanentChannel.getPermanentChannel().size(); i++)
		{
			PermanentChannel pc = PermanentChannel.getPermanentChannel().get(i);
			if(i+1 == PermanentChannel.getPermanentChannel().size())
			{
				list.add(ChatApi.clickEvent(pc.getNameColor()+pc.getName(),
						ClickEvent.Action.RUN_COMMAND, "/scc pcinfo "+pc.getName()));
			} else
			{
				list.add(ChatApi.clickEvent(pc.getNameColor()+pc.getName()+", &r",
						ClickEvent.Action.RUN_COMMAND, "/scc pcinfo "+pc.getName()));
			}
		}
		///&e=====&5[&5Perma&fnente &fChannels&5]&e=====
		player.sendMessage(ChatApi.tctl(
				plugin.getYamlHandler().getL().getString(scc+"PCChannels.Headline")));
		TextComponent msg = ChatApi.tc("");
		msg.setExtra(list);
		player.sendMessage(msg);
		return;
	}
}