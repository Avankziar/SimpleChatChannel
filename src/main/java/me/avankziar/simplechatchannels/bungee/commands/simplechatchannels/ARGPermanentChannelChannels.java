package main.java.me.avankziar.simplechatchannels.bungee.commands.simplechatchannels;

import java.util.ArrayList;
import java.util.List;

import main.java.me.avankziar.simplechatchannels.bungee.SimpleChatChannels;
import main.java.me.avankziar.simplechatchannels.bungee.Utility;
import main.java.me.avankziar.simplechatchannels.bungee.commands.CommandModule;
import main.java.me.avankziar.simplechatchannels.bungee.interfaces.PermanentChannel;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class ARGPermanentChannelChannels extends CommandModule
{
	private SimpleChatChannels plugin;
	
	public ARGPermanentChannelChannels(SimpleChatChannels plugin)
	{
		super("pcchannels","scc.cmd.pc.channels",SimpleChatChannels.sccarguments,1,1,"pckanäle");
		this.plugin = plugin;
	}

	@Override
	public void run(CommandSender sender, String[] args)
	{
		ProxiedPlayer player = (ProxiedPlayer) sender;
		Utility utility = plugin.getUtility();
		String language = utility.getLanguage();
		String scc = ".CmdScc.";
		List<BaseComponent> list = new ArrayList<>();
		for(int i = 0; i < PermanentChannel.getPermanentChannel().size(); i++)
		{
			PermanentChannel pc = PermanentChannel.getPermanentChannel().get(i);
			if(i+1 == PermanentChannel.getPermanentChannel().size())
			{
				list.add(utility.clickEvent(pc.getNameColor()+pc.getName(),
						ClickEvent.Action.RUN_COMMAND, "/scc pcinfo "+pc.getName(), false));
			} else
			{
				list.add(utility.clickEvent(pc.getNameColor()+pc.getName()+", &r",
						ClickEvent.Action.RUN_COMMAND, "/scc pcinfo "+pc.getName(), false));
			}
		}
		///&e=====&5[&5Perma&fnente &fChannels&5]&e=====
		player.sendMessage(utility.tctl(
				plugin.getYamlHandler().getL().getString(language+scc+"PCChannels.Headline")));
		TextComponent msg = utility.tc("");
		msg.setExtra(list);
		player.sendMessage(msg);
		return;
	}
}